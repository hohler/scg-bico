package tool.bico.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.LabelService;
import org.eclipse.egit.github.core.service.RepositoryService;

import tool.bico.model.CommitIssue;
import tool.bico.parser.IssueInfoHolder;
import tool.bico.utils.SwitchSubstring;

public class GitHubAPI {

	private Repository repository;
	private String name;
	private String owner;
	private GitHubClient client;

	private Map<String, CommitIssue.Type> types = new HashMap<>();
	private Map<String, CommitIssue.Priority> priorities = new HashMap<>();

	public GitHubAPI(String gitHubUrl) {
		client = new GitHubClient();
		client.setOAuth2Token("df5f225320a07d1de361bb3493d1f8aedb43fbfc");

		RepositoryService service = new RepositoryService(client);

		String[] splitted = gitHubUrl.split("/");
		name = splitted[splitted.length - 1];
		owner = splitted[splitted.length - 2];

		try {
			repository = service.getRepository(owner, name);
			loadLabels();
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("GitHub Repository error: API limit reached");
		}
	}

	public void loadLabels() {
		if(repository == null) return;
		LabelService service = new LabelService(client);
		try {
			List<Label> labels = service.getLabels(repository);
			for (Label l : labels) {
				String name = l.getName().toLowerCase();
				if (name.startsWith(":"))
					continue;
				SwitchSubstring.of(name)
						// Priorities
						.when("blocker").then(() -> priorities.put(name, CommitIssue.Priority.BLOCKER))
						.when("breaking", "breaking-java", "pretty blooding important", "critical").then(() -> priorities.put(name, CommitIssue.Priority.CRITICAL))
						.when("major", "severity").then(() -> priorities.put(name, CommitIssue.Priority.MAJOR))
						.when("minor").then(() -> priorities.put(name, CommitIssue.Priority.MINOR))
						.when("trivial").then(() -> priorities.put(name, CommitIssue.Priority.TRIVIAL))
						// Types
						.when("bug").then(() -> types.put(name, CommitIssue.Type.BUG))
						.when("deprecation").then(() -> types.put(name, CommitIssue.Type.DEPRECATION))
						.when("refactor", "change").then(() -> types.put(name, CommitIssue.Type.REFACTOR))
						.when("docs", "documentation", "doc").then(() -> types.put(name, CommitIssue.Type.DOCUMENTATION))
						.when("feature").then(() -> types.put(name, CommitIssue.Type.FEATURE))
						.when("enhancement", "optimization", "improvement").then(() -> types.put(name, CommitIssue.Type.IMPROVEMENT))
						.when("test").then(() -> types.put(name, CommitIssue.Type.TEST))
						.when("access").then(() -> types.put(name, CommitIssue.Type.ACCESS))
						.when("dependency").then(() -> types.put(name, CommitIssue.Type.DEPENDENCY_UPGRADE))
						.when("request").then(() -> types.put(name, CommitIssue.Type.REQUEST))
						.when("subtask").then(() -> types.put(name, CommitIssue.Type.SUBTASK))
						.when("task").then(() -> types.put(name, CommitIssue.Type.TASK))
						.when("wish").then(() -> types.put(name, CommitIssue.Type.WISH));
			}
		} catch (IOException e) {
			// e.printStackTrace();
			System.err.println("GitHub Labels error: API limit reached");
		}
	}

	public Issue getIssue(int issueNumber) {
		int maxTryCount = 3;
		int tryCount = 0;
		IssueService service = new IssueService(client);
		while (true) {
			try {
				tryCount++;
				Issue issue = service.getIssue(repository, issueNumber);
				return issue;
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("GitHub API error: some sleep time");
				try {
					Thread.sleep(120000);//wait for 2 minutes then try again
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				if (tryCount == maxTryCount)
					return null;
			}
		}
	}

	public IssueInfoHolder parseIssue(String issueNumber) {
		if (issueNumber == null)
			return null;
		Issue issue = getIssue(Integer.parseInt(issueNumber));

		IssueInfoHolder holder = new IssueInfoHolder();
		holder.setName(Integer.toString(issue.getNumber()));

		holder.setLink(issue.getHtmlUrl());
		holder.setDescription(issue.getTitle());

		for (Label l : issue.getLabels()) {
			if (l.getName().startsWith(":"))
				continue;
			if (holder.getType() == CommitIssue.Type.NA) {
				CommitIssue.Type type = types.get(l.getName().toLowerCase());
				if (type != null) {
					holder.setType(type);
				}
			}
			if (holder.getPriority() == CommitIssue.Priority.NA) {
				CommitIssue.Priority priority = priorities.get(l.getName().toLowerCase());
				if (priority != null) {
					holder.setPriority(priority);
				}
			}
		}
		if (holder.getType() == CommitIssue.Type.NA)
			holder.setType(CommitIssue.Type.OTHER);

		return holder;
	}

	/*
	 * public void loadPullRequests() { pullRequests = new HashMap<>();
	 * PullRequestService service = new PullRequestService(client);
	 * 
	 * try { List<PullRequest> result = service.getPullRequests(repository,
	 * "closed"); for(PullRequest p : result) {
	 * System.out.println("PullRequest:"+p.getNumber()); pullRequests.put(new
	 * Integer(p.getNumber()), p); } } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); pullRequests = null; } }
	 */

	/*
	 * public PullRequest loadSinglePullRequest(int id) { PullRequestService
	 * service = new PullRequestService(client);
	 * 
	 * try { PullRequest result = service.getPullRequest(repository, id);
	 * System.out.println("PullRequest:"+result.getNumber());
	 * pullRequests.put(new Integer(result.getNumber()), result); return result;
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return null; }
	 */

	/*
	 * public PullRequest getPullRequest(int id) { if(pullRequests == null) {
	 * System.err.println("GitHub pull requests are not loaded!"); return null;
	 * } return pullRequests.get(new Long(id)); }
	 */

	/*
	 * public Issue getIssue(int pullRequestNumber) { return
	 * getIssue(getPullRequest(pullRequestNumber)); }
	 * 
	 * public Issue getIssue(PullRequest p) { IssueService service = new
	 * IssueService(client); int issueNumber = getIssueNumberFromPullRequest(p);
	 * try { Issue issue = service.getIssue(repository, issueNumber); return
	 * issue; } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); return null; } }
	 */

	/*
	 * private int getIssueNumberFromPullRequest(PullRequest p) { String[] tmp =
	 * p.getIssueUrl().split("/"); return Integer.parseInt(tmp[tmp.length-1]); }
	 */

	/*
	 * public IssueInfoHolder parsePullRequestToIssue(String id) { PullRequest p
	 * = loadSinglePullRequest(Integer.parseInt(id)); Issue issue = getIssue(p);
	 * IssueInfoHolder holder = new IssueInfoHolder();
	 * holder.setName(Integer.toString(issue.getNumber()));
	 * 
	 * for(Label l : issue.getLabels()) { l.getName(); } //parse labels
	 * //holder.set return holder; }
	 */
}
