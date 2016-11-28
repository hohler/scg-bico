package ch.unibe.scg.bico;

import java.util.List;

import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.parser.IssueInfoHolder;
import ch.unibe.scg.bico.parser.IssueStringParser;
import ch.unibe.scg.bico.parser.SwitchSubstring;
import ch.unibe.scg.bico.repository.GitHubAPI;

public class Playground {

	public static void main(String[] args) {
		//IssueStringParser issueParser = new IssueStringParser("(\\w+-\\d+)");
		IssueStringParser issueParser = new IssueStringParser("\\[*(\\w+-\\d+)\\]*");
		List<String> identifiers;
		String input = "Remove transport-netty3-client mention (#21628)\r\n\r\nNow netty3 is gone, this mention must also be removed. This solves #12345";
		
		String input2 = "HSEARCH-190 add pom HSEARCH-191 make change build system to get deps from JBoss repo, make doc build independent of hibernate core structure add maven deploy task HSEARCH-192 Move to Hibernate Core 3.3, fix compilations HSEARCH-193 move to a small solr jar with only analyzers";
		identifiers = issueParser.parseAll(input2);
		System.out.println(identifiers);
		
		/*LUCENE-5371, LUCENE-5339: speed up range faceting from O(N) per hit to O(log(N)) using segment trees; simplify facet APIs
		 
		 git-svn-id: https://svn.apache.org/repos/asf/lucene/dev/trunk@1555338 13f79535-47bb-0310-9956-ffa450edef68
		 commit id	: 102273
		 
		 */
		
		
		 
		
		/*
		GitHubAPI gitHubApi = new GitHubAPI("https://github.com/elastic/elasticsearch");
		IssueInfoHolder result;
		result = gitHubApi.parseIssue(identifier);
		
		System.out.println(result);*/
		
		
		/*SwitchSubstring.of("nigga")
		.when("bli").then(() -> System.out.println("found bli"))
		.when("bla", "blubb").then(() -> System.out.println("found bla or blubb"))
		.orElse(() -> System.out.println("nothing found"));*/
	
	}
}
