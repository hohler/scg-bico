package ch.unibe.scg.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ch.unibe.scg.model.CommitIssue;

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
	public CommitIssue parse(String content) {
		
	
		Document doc;
		try {
			
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
			
			CommitIssue issue = new CommitIssue();
			issue.setName(key);
			
			
			switch(type) {
			case "Access": issue.setType(CommitIssue.Type.ACCESS); break;
			case "Bug": issue.setType(CommitIssue.Type.BUG); break;
			case "Dependency upgrade": issue.setType(CommitIssue.Type.DEPENDENCY_UPGRADE); break; 
			case "Documentation": issue.setType(CommitIssue.Type.DOCUMENTATION); break;
			case "Improvement": issue.setType(CommitIssue.Type.IMPROVEMENT); break;
			case "Request": issue.setType(CommitIssue.Type.REQUEST); break;
			case "Task": issue.setType(CommitIssue.Type.TASK); break;
			case "Test": issue.setType(CommitIssue.Type.TEST); break;
			case "Wish": issue.setType(CommitIssue.Type.WISH); break;
			default: issue.setType(CommitIssue.Type.OTHER); break;
			}
			
			
			switch(priority) {
			case "Blocker": issue.setPriority(CommitIssue.Priority.BLOCKER); break;
			case "Critical": issue.setPriority(CommitIssue.Priority.CRITICAL); break;
			case "Major": issue.setPriority(CommitIssue.Priority.MAJOR); break;
			case "Minor": issue.setPriority(CommitIssue.Priority.MINOR); break;
			case "Trivial": issue.setPriority(CommitIssue.Priority.TRIVIAL); break;
			default: issue.setPriority(CommitIssue.Priority.OTHER); break;
			}
			
			return issue;
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
