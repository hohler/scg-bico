package tool.bico;

import java.util.List;

import tool.bico.analysis.CheckForSourceOrTestFile;
import tool.bico.analysis.CommitMessageClassifier;
import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.parser.IssueInfoHolder;
import tool.bico.parser.IssueStringParser;
import tool.bico.parser.WebIssueTrackerParser;
import tool.bico.repository.GitHubAPI;
import tool.bico.utils.SwitchSubstring;

public class Playground2 {

	public static void main(String[] args) {
		
		
		String input = "Remove transport-netty3-client mention (#21628)\r\n\r\nNow netty3 is gone, this mention must also be removed. This solves #12345";
		
		String input2 = "HSEARCH-190 add pom HSEARCH-191 make change build system to get deps from JBoss repo, make doc build independent of hibernate core structure add maven deploy task HSEARCH-192 Move to Hibernate Core 3.3, fix compilations HSEARCH-193 move to a small solr jar with only analyzers";
		
		String input3 = "include not work in 5.x anymore (#21815)\r\n\r\ninclude not work in 5.x anymore use includes instead Fixes #11111";
		
		String input4 = "[HSEARCH-190] Blah blah HSEARCH-192\r\ntritrallalala HSEARCH-000";
		
		String input5 = "Bug 123262 This is a bug \r\n hello Bug 4523423 or bug 00000";
		
		CommitMessageClassifier c = new CommitMessageClassifier();
		//System.out.println(c.classify(input));
		
		//System.out.println(c.classify(input2));
		
		//System.out.println(c.classify(input3));
		
		//System.out.println(c.classify(input4));
		
		System.out.println(c.classify(input5));
		
	}
	
}
