package ch.unibe.scg.bico.processor;

import org.springframework.batch.item.ItemProcessor;

import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.model.Project;
import ch.unibe.scg.bico.parser.IssueInfoHolder;
import ch.unibe.scg.bico.parser.WebIssueTrackerParser;
import ch.unibe.scg.bico.repository.GitHubAPI;

public class CommitProcessor implements ItemProcessor<CommitIssue, CommitIssue> {
	
	private String urlPattern;
	private Project.Type issueTrackerType;
	private GitHubAPI gitHubApi;
	
	public CommitProcessor(Project.Type type, String urlPattern) {
		//if(project == null) throw new NullPointerException("project is null!");
		//urlPattern = project.getIssueTrackerUrlPattern();
		this.issueTrackerType = type;
		this.urlPattern = urlPattern;
		if(urlPattern == null) throw new NullPointerException("urlPattern is null");
	}

	@Override
	public CommitIssue process(CommitIssue input) throws Exception {
		//if(input.getCommitIssue() != null) return input;
		//CommitIssue issue = input.getCommitIssue();
		System.out.println("commit process: " + input.getName());
		//System.out.println(input.toString());

		try {
			IssueInfoHolder result = null;
			if(issueTrackerType == Project.Type.GITHUB && gitHubApi != null)	{
				result = gitHubApi.parseIssue(input.getName());
			} else
			if(issueTrackerType == Project.Type.JIRA) {
				WebIssueTrackerParser itp = new WebIssueTrackerParser(urlPattern);
				result = itp.parse(input);
			}
			if(result != null) {
				input.setName(result.getName());
				input.setPriority(result.getPriority());
				input.setType(result.getType());
			} else {
				input.setName(null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
	
	public void setGitHubApi(GitHubAPI api) {
		this.gitHubApi = api;
	}	
	
}