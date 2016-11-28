package ch.unibe.scg.bico.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.model.Project;

public class WebIssueTrackerParser {

	private Parser parser;
	//private CommitIssue issue;
	private String urlPattern;
	
	public WebIssueTrackerParser(String urlPattern, Project.Type trackerType) throws Exception {
		this.urlPattern = urlPattern;
		
		if(trackerType == Project.Type.JIRA && urlPattern.contains("jira")) {
			
			if(!urlPattern.contains(".xml")) {
				throw new Exception("The Jira parser only works with xml format");
			}
			
			parser = new JiraParser();
		} else
		if(trackerType == Project.Type.BUGZILLA && urlPattern.contains("bugzilla")) {
			if(!urlPattern.contains("xml")) {
				throw new Exception("The bugzilla parser only works with xml format");
			}
			
			parser = new BugzillaParser();
		} else {
			throw new Exception("This issue tracker is not known!");
		}
	}
	
	/*public void setIssue(CommitIssue issue) {
		this.issue = issue;
	}
	
	public CommitIssue getIssue() {
		return issue;
	}*/
	
	public IssueInfoHolder parse(CommitIssue issue) {
		if(issue == null) return null;
		if(issue.getName() == null) return null;
		String url = parser.formatUrl(urlPattern, issue.getName());
		String content = retrieveContent(url);
		if(content == null) return null;
		
		return parser.parse(content);
	}
	
	public IssueInfoHolder parse(String issue) {
		if(issue == null) return null;
		String url = parser.formatUrl(urlPattern, issue);
		String content = retrieveContent(url);
		if(content == null) return null;
		
		return parser.parse(content);
	}
	
	private String retrieveContent(String url) {
		URL urlObj;
		try {
			urlObj = new URL(url);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlObj.openStream()));
			
			String inputLine;
			StringBuffer stringBuffer = new StringBuffer();
	        while ((inputLine = reader.readLine()) != null) {
	            stringBuffer.append(inputLine);
	        }
	        reader.close();
	        return stringBuffer.toString();	   
	        
		} catch (MalformedURLException e1) {
			System.err.println("Wrong url: "+url);
			e1.printStackTrace();
		} catch (FileNotFoundException e2) {
			System.err.println("404 Error: "+url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
