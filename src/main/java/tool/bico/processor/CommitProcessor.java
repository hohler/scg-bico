package tool.bico.processor;

import org.springframework.batch.item.ItemProcessor;

import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.parser.IssueInfoHolder;
import tool.bico.parser.WebIssueTrackerParser;
import tool.bico.repository.GitHubAPI;
import ch.unibe.scg.curtys.classifier.Classifier;
import ch.unibe.scg.curtys.classifier.MulticlassClassifier;
import ch.unibe.scg.curtys.classifier.Prediction;
import ch.unibe.scg.curtys.vectorization.issue.Issue;

public class CommitProcessor implements ItemProcessor<CommitIssue, CommitIssue> {
	
	private String urlPattern;
	private Project.Type issueTrackerType;
	private GitHubAPI gitHubApi;
	
	public CommitProcessor(Project.Type type, String urlPattern) {
		this.issueTrackerType = type;
		this.urlPattern = urlPattern;
		if(urlPattern == null) throw new NullPointerException("urlPattern is null");
	}

	@Override
	public CommitIssue process(CommitIssue input) throws Exception {
		
		if(input.isProcessed()) {
			System.out.println("commit already processed: "+input.getName());
			return input;
		} else {
			System.out.println("commit process: " + input.getName());
		}

		try {
			IssueInfoHolder result = null;
			if(issueTrackerType == Project.Type.GITHUB && gitHubApi != null)	{
				result = gitHubApi.parseIssue(input.getName());
			} else
			if(issueTrackerType == Project.Type.JIRA || issueTrackerType == Project.Type.BUGZILLA) {
				WebIssueTrackerParser itp = new WebIssueTrackerParser(urlPattern, issueTrackerType);
				result = itp.parse(input);
			}
			if(result != null) {
				input.setName(result.getName());
				input.setPriority(result.getPriority());
				input.setType(result.getType());
				input.setLink(result.getLink());
				input.setDescription(result.getDescription());
				input.setProcessed(true);
				
				// classifier
				Issue cIssue = new Issue();
				// TODO cIssue 
				Classifier classifier = new MulticlassClassifier();
				Prediction prediction = classifier.query(cIssue);
				String type = prediction.getBestClassLabel();
				// check what type
				// input.setClassifierType(type);
			} else {
				input.setName(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}
	
	public void setGitHubApi(GitHubAPI api) {
		this.gitHubApi = api;
	}	
	
}