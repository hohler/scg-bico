package ch.unibe.scg.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	public Issue parse(String content) {
		
	
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
			
			Issue issue = new Issue();
			issue.setName(key);
			if(type.equals("Bug")) issue.setType(Issue.Type.BUG);
			else if(type.equals("Feature")) issue.setType(Issue.Type.FEATURE);
			else issue.setType(Issue.Type.OTHER);
			
			if(priority.equals("Major")) issue.setPriority(Issue.Priority.HIGH);
			else issue.setPriority(Issue.Priority.LOW);
			
			return issue;
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return null;
	}
}
