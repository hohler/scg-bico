package tool.bico.processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.parser.IssueComment;
import tool.bico.parser.IssueInfoHolder;
import tool.bico.parser.WebIssueTrackerParser;
import tool.bico.repository.GitHubAPI;
import ch.unibe.scg.curtys.classifier.Classifier;
import ch.unibe.scg.curtys.classifier.MulticlassClassifier;
import ch.unibe.scg.curtys.classifier.Prediction;
import ch.unibe.scg.curtys.quality.QualityEstimator;
import ch.unibe.scg.curtys.quality.ScoreEstimator;
import ch.unibe.scg.curtys.vectorization.issue.Issue;
import ch.unibe.scg.curtys.vectorization.issue.Comment;

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
				
				if(issueTrackerType == Project.Type.JIRA) { // TODO implement GitHub
					// simons classifier
					Issue cIssue = new Issue();
					List<Comment> comments = new ArrayList<>();
					for(IssueComment ic : result.getComments()) {
						Comment c = new Comment();
						c.setAuthor(ic.getAuthor());
						c.setDate(ic.getDate());
						c.setBody(ic.getBody());
						c.setId(ic.getId());
						comments.add(c);
					}
					cIssue.setComments(comments);
					cIssue.setComponent(result.getComponent());
					cIssue.setDescription(result.getDescription());
					cIssue.setId(result.getName());
					cIssue.setProduct(result.getProject());
					cIssue.setSummary(result.getSummary());
					cIssue.setIssuetypeTracker("Jira");
					cIssue.setVersion(result.getVersion());
					cIssue.setHasPatch(result.hasPatch());
					cIssue.setHasScreenshot(result.hasScreenshot());
					
					// TODO cIssue 
					Classifier classifier = new MulticlassClassifier();
					Prediction prediction = classifier.query(cIssue);
					String type = prediction.getBestClassLabel();
					ScoreEstimator estimator = new QualityEstimator();
					int score = estimator.score(cIssue);
					// TODO check what type
					
					input.setTypeByClassifier(type);
					input.setClassifierScore(score);
				}
			} else {
				input.setProcessed(true);
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