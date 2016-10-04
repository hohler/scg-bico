package ch.unibe.scg;

import java.util.ArrayList;

import ch.unibe.scg.model.Commit;
import ch.unibe.scg.model.Issue;
import ch.unibe.scg.model.Project;
import ch.unibe.scg.parser.IssueParser;
import ch.unibe.scg.parser.IssueTrackerParser;
import ch.unibe.scg.repository.GitLoader;
import ch.unibe.scg.repository.GithubRepository;

public class Playground {
	public static void main(String[] args) {
		
		/*GitLoader gitLoader = new GitLoader("https://github.com/apache/flume.git", "trunk");
		boolean result = gitLoader.init();*/
		
		/*GithubRepository g = new GithubRepository("https://github.com/apache/flume");
		ArrayList<Commit> result = g.getCommits();
		System.out.println("commits: "+result.size());
		*/
		
		Project project = new Project(Project.Type.GITHUB);
		project.setUrl("https://github.com/apache/flume");
		project.setName("flume");
		
		
		IssueParser issueParser = new IssueParser("^(\\w+\\-\\d+).");
		
		Commit testCommit = new Commit();
		testCommit.setMessage("FLUME-2979. Integrate checkstyle for test classes\n\nAlso make test code conform to style guidelines.\n\nAdditionally, this patch makes style violations fatal to the build.\n\nThis patch is whitespace-only from a code perspective. After stripping\nline numbers, the generated test bytecode before and after these changes\nis identical.\n\nCode review: https://reviews.apache.org/r/49830/\n\nReviewed by Hari.");
		issueParser.parse(testCommit);
		System.out.println("issue: "+testCommit.getIssue());
		
		try {
			IssueTrackerParser itp = new IssueTrackerParser("https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml");
			itp.setIssue(testCommit.getIssue());
			Issue issue = itp.parse();
			if(issue != null) {
				System.out.println(issue);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		project.addCommit(testCommit);
		
		//ProjectRepository repo = new ProjectRepository();
		
		
	}
}
