package ch.unibe.scg.bico;

import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.parser.IssueInfoHolder;
import ch.unibe.scg.bico.parser.IssueStringParser;
import ch.unibe.scg.bico.parser.SwitchSubstring;
import ch.unibe.scg.bico.repository.GitHubAPI;

public class Playground {

	public static void main(String[] args) {
		IssueStringParser issueParser = new IssueStringParser("\\(#(\\d+)\\)");
		String identifier;
		String input = "Remove transport-netty3-client mention (#21628)\r\n\r\nNow netty3 is gone, this mention must also be removed.";
		identifier = issueParser.parse(input);
		System.out.println(identifier);
		
		
		GitHubAPI gitHubApi = new GitHubAPI("https://github.com/elastic/elasticsearch");
		IssueInfoHolder result;
		result = gitHubApi.parseIssue(identifier);
		
		System.out.println(result);
		
		
		/*SwitchSubstring.of("nigga")
		.when("bli").then(() -> System.out.println("found bli"))
		.when("bla", "blubb").then(() -> System.out.println("found bla or blubb"))
		.orElse(() -> System.out.println("nothing found"));*/
	
	}
}
