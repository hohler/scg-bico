package ch.unibe.scg.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
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
			System.out.println(content);
			InputSource is = new InputSource(new StringReader(content));
			doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			

			NodeList list = root.getElementsByTagName("item");
			Element element = (Element) list.item(0);
			String type = element.getAttribute("type");
			String priority = element.getAttribute("priority");
			
			Issue issue = new Issue();
			if(type == "Bug") issue.setType(Issue.TYPE_BUG);
			if(type == "Feature") issue.setType(Issue.TYPE_FEATURE);
			else issue.setType(Issue.TYPE_OTHER);
			
			if(priority == "Major") issue.setType(Issue.PRIORITY_HIGH);
			else issue.setType(Issue.PRIORITY_LOW);
			
			return issue;
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return null;
	}
}
