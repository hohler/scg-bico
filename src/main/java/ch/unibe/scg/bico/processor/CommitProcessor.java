package ch.unibe.scg.bico.processor;

import org.springframework.batch.item.ItemProcessor;

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.model.Project;
import ch.unibe.scg.bico.parser.IssueInfoHolder;
import ch.unibe.scg.bico.parser.WebIssueTrackerParser;
import ch.unibe.scg.bico.repository.GitHubAPI;

public class CommitProcessor implements ItemProcessor<Commit, CommitIssue> {
	
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
	public CommitIssue process(Commit input) throws Exception {
		//if(input.getCommitIssue() != null) return input;
		CommitIssue issue = input.getCommitIssue();
		System.out.println("commit process: " + issue.getName());
		//System.out.println(input.toString());
		try {
			IssueInfoHolder result = null;
			if(issueTrackerType == Project.Type.GITHUB)	{
				result = gitHubApi.parseIssue(issue.getName());
			} else
			if(issueTrackerType == Project.Type.JIRA) {
				WebIssueTrackerParser itp = new WebIssueTrackerParser(urlPattern);
				result = itp.parse(issue);
			}
			if(result != null) {
				issue.setName(result.getName());
				issue.setPriority(result.getPriority());
				issue.setType(result.getType());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return issue;
	}
	
	public void setGitHubApi(GitHubAPI api) {
		this.gitHubApi = api;
	}

	/*public void setProject(Project project) {
		this.project = project;
	}*/
	
	
}