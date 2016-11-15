package org.springframework.batch.admin.sample.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.springframework.batch.admin.sample.model.CommitIssue;

public class JiraParser implements Parser {

	DocumentBuilder builder;
	
	public JiraParser() {
		
		DocumentBuilderFactory factory =
		DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String formatUrl(String url, String issue) {
		return String.format(url+"?field=type&field=priority",  issue, issue);
	}

	@Override
	public IssueInfoHolder parse(String content) {
		
	
		Document doc;
		try {
			
			//CommitIssue result = new CommitIssue(issue);
			//CommitIssue result = issue;
			IssueInfoHolder result = new IssueInfoHolder();
			
			StringBuilder xmlStringBuilder = new StringBuilder();
			xmlStringBuilder.append(content);
			ByteArrayInputStream input =  new ByteArrayInputStream(
			   xmlStringBuilder.toString().getBytes("UTF-8"));
			
			doc = builder.parse(input);
			
			doc.getDocumentElement().normalize();
			
			NodeList items = doc.getElementsByTagName("item");
			Node node = items.item(0);
			Element element = (Element) node;	
			
			String key = element.getElementsByTagName("key").item(0).getTextContent();
			String type = element.getElementsByTagName("type").item(0).getTextContent();
			String priority = element.getElementsByTagName("priority").item(0).getTextContent();
			
			//issue.setName(key);
			System.out.println("["+key+"] type: "+type + " priority: " + priority);
			
			switch(type) {
				case "Access": result.setType(CommitIssue.Type.ACCESS); break;
				case "Bug": result.setType(CommitIssue.Type.BUG); break;
				case "Dependency upgrade": result.setType(CommitIssue.Type.DEPENDENCY_UPGRADE); break; 
				case "Documentation": result.setType(CommitIssue.Type.DOCUMENTATION); break;
				case "Improvement": result.setType(CommitIssue.Type.IMPROVEMENT); break;
				case "Request": result.setType(CommitIssue.Type.REQUEST); break;
				case "Task": result.setType(CommitIssue.Type.TASK); break;
				case "Test": result.setType(CommitIssue.Type.TEST); break;
				case "Wish": result.setType(CommitIssue.Type.WISH); break;
				case "New Feature": result.setType(CommitIssue.Type.FEATURE); break;
				case "Sub-task": result.setType(CommitIssue.Type.SUBTASK); break;
				default: result.setType(CommitIssue.Type.OTHER); break;
			}
			
			
			switch(priority) {
				case "Blocker": result.setPriority(CommitIssue.Priority.BLOCKER); break;
				case "Critical": result.setPriority(CommitIssue.Priority.CRITICAL); break;
				case "Major": result.setPriority(CommitIssue.Priority.MAJOR); break;
				case "Minor": result.setPriority(CommitIssue.Priority.MINOR); break;
				case "Trivial": result.setPriority(CommitIssue.Priority.TRIVIAL); break;
				default: result.setPriority(CommitIssue.Priority.OTHER); break;
			}
			
			return result;
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
