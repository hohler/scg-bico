package tool.bico;

import java.util.List;

import tool.bico.analysis.CheckForSourceOrTestFile;
import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.parser.IssueInfoHolder;
import tool.bico.parser.IssueStringParser;
import tool.bico.parser.WebIssueTrackerParser;
import tool.bico.repository.GitHubAPI;
import tool.bico.utils.SwitchSubstring;

public class Playground {

	public static void main(String[] args) {
		
		String f = "src/Store.java";
		String[] parts = f.split("/");
		String[] ext = null;
		if(parts.length > 0) {
			ext = parts[parts.length-1].split("\\.");
		}
		System.out.println(parts[parts.length-1]);
		//IssueStringParser issueParser = new IssueStringParser("(\\w+-\\d+)");
		//IssueStringParser issueParser = new IssueStringParser("\\[?(\\w+-\\d+)\\]?");
		//IssueStringParser issueParser = new IssueStringParser("\\(?#(\\d+)\\)?");
		
		System.out.println(CheckForSourceOrTestFile.check("src/test/java/org/apache/commons/lang3/concurrent/TimedSemaphoreTest.java")); // TRUE
		System.out.println(CheckForSourceOrTestFile.check("src/test/org/apache/commons/lang/enums/Extended2Enum.java")); // TRUE
		System.out.println(CheckForSourceOrTestFile.check("LANG_2_2_RC1/src/java/org/apache/commons/lang/IncompleteArgumentException.java")); // FALSE
		System.out.println(CheckForSourceOrTestFile.check("src/main/java/org/apache/commons/lang3/text/translate/NumericEntityEscaper.java")); // FALSE

		System.out.println(CheckForSourceOrTestFile.check("src/main/java/org/apache/commons/lang3/text/translate/TestForNumericEntityEscaping.java")); // FALSE
		

		IssueStringParser issueParser = new IssueStringParser("bug\\s(\\d+)");
		List<String> identifiers;
		String input = "Remove transport-netty3-client mention (#21628)\r\n\r\nNow netty3 is gone, this mention must also be removed. This solves #12345";
		
		String input2 = "HSEARCH-190 add pom HSEARCH-191 make change build system to get deps from JBoss repo, make doc build independent of hibernate core structure add maven deploy task HSEARCH-192 Move to Hibernate Core 3.3, fix compilations HSEARCH-193 move to a small solr jar with only analyzers";
		
		String input3 = "include not work in 5.x anymore (#21815)\r\n\r\ninclude not work in 5.x anymore use includes instead Fixes #11111";
		
		String input4 = "[HSEARCH-190] Blah blah HSEARCH-192\r\ntritrallalala HSEARCH-000";
		
		String input5 = "Bug 123262 This is a bug \r\n hello Bug 4523423 or bug 00000";
		
		identifiers = issueParser.parseAll(input5, 2);
		System.out.println(identifiers);
		
		System.out.println(issueParser.parse(input5));
		
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
		testBugzilla();
	}
	
	private static void testBugzilla() {
		String urlPattern = "https://bugzilla.mozilla.org/show_bug.cgi?ctype=xml&id=%s";
		Project.Type issueTrackerType = Project.Type.BUGZILLA;
		WebIssueTrackerParser itp;
		try {
			itp = new WebIssueTrackerParser(urlPattern, issueTrackerType);
			String input = "1121879";
			
			IssueInfoHolder result = itp.parse(input);
			
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
