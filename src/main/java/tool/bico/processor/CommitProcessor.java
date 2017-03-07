package tool.bico.processor;

import org.springframework.batch.item.ItemProcessor;

import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.parser.IssueInfoHolder;
import tool.bico.parser.WebIssueTrackerParser;
import tool.bico.repository.GitHubAPI;

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
		System.out.println("commit process: " + input.getName());

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