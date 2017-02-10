package tool.bico.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tool.bico.model.CommitIssue;

/**
 * Not yet working correctly, because bugzilla (mozilla) does not classify most bugs.
 * assume that everything is a bug, if nothing other is specified?
 * Currently: Scan short description for keywords like test, refactor(ing), etc. to classify
 *
 */
public class BugzillaParser implements Parser {

	DocumentBuilder builder;
	
	public BugzillaParser() {
		
		DocumentBuilderFactory factory =
		DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}

	@Override // https://bugzilla.mozilla.org/show_bug.cgi?ctype=xml&id=%s
	public String formatUrl(String url, String issue) {
		return String.format(url, issue);
	}

	@Override
	public IssueInfoHolder parse(String content) {
		
	
		Document doc;
		try {
			
			IssueInfoHolder result = new IssueInfoHolder();
			
			StringBuilder xmlStringBuilder = new StringBuilder();
			xmlStringBuilder.append(content);
			ByteArrayInputStream input =  new ByteArrayInputStream(
			   xmlStringBuilder.toString().getBytes("UTF-8"));
			
			doc = builder.parse(input);
			
			doc.getDocumentElement().normalize();
			
			NodeList items = doc.getElementsByTagName("bug");
			Node node = items.item(0);
			Element element = (Element) node;	
			
			String key = element.getElementsByTagName("bug_id").item(0).getTextContent();
			
			String type = null;
			Node typeNode = element.getElementsByTagName("whiteboard").item(0);
			if(typeNode != null) type = element.getElementsByTagName("whiteboard").item(0).getTextContent();
		
			String priority = null;
			Node priorityNode = element.getElementsByTagName("bug_severity").item(0);
			if(priorityNode != null) priority = priorityNode.getTextContent();
			
			String desc = element.getElementsByTagName("short_desc").item(0).getTextContent();
			
			result.setName(key);
			
			//issue.setName(key);
			System.out.println("["+key+"] type: "+type + " priority: " + priority + " desc: " + desc);
			
			SwitchSubstring.of(desc.toLowerCase())
				//Priorities
				/*.when("blocker").then(() -> result.setPriority(CommitIssue.Priority.BLOCKER))
				.when("breaking", "breaking-java", "pretty blooding important", "critical").then(() -> priorities.put(name,  CommitIssue.Priority.CRITICAL))
				.when("major", "severity").then(() -> priorities.put(name, CommitIssue.Priority.MAJOR))
				.when("minor").then(() -> priorities.put(name, CommitIssue.Priority.MINOR))
				.when("trivial").then(() -> priorities.put(name, CommitIssue.Priority.TRIVIAL))*/
				//Types
				.when("bug", "bugfix", "fix").then(() -> result.setType(CommitIssue.Type.BUG))
				.when("deprecation").then(() -> result.setType(CommitIssue.Type.DEPRECATION))
				.when("refactor", "refactoring", "change").then(() -> result.setType(CommitIssue.Type.REFACTOR))
				.when("docs", "documentation", "doc").then(() -> result.setType(CommitIssue.Type.DOCUMENTATION))
				.when("feature").then(() -> result.setType(CommitIssue.Type.FEATURE))
				.when("enhancement", "optimization", "improvement").then(() -> result.setType(CommitIssue.Type.IMPROVEMENT))
				.when("test").then(() -> result.setType(CommitIssue.Type.TEST))
				.when("access").then(() -> result.setType(CommitIssue.Type.ACCESS))
				.when("dependency").then(() -> result.setType(CommitIssue.Type.DEPENDENCY_UPGRADE))
				.when("request").then(() -> result.setType(CommitIssue.Type.REQUEST))
				.when("subtask").then(() -> result.setType(CommitIssue.Type.SUBTASK))
				.when("task").then(() -> result.setType(CommitIssue.Type.TASK))
				.when("wish").then(() -> result.setType(CommitIssue.Type.WISH))
				.orElse(() -> result.setType(CommitIssue.Type.NA));
			
			
			switch(priority.toLowerCase()) {
				case "blocker": result.setPriority(CommitIssue.Priority.BLOCKER); break;
				case "critical": result.setPriority(CommitIssue.Priority.CRITICAL); break;
				case "major": result.setPriority(CommitIssue.Priority.MAJOR); break;
				case "minor":
				case "normal": result.setPriority(CommitIssue.Priority.MINOR); break;
				case "trivial": result.setPriority(CommitIssue.Priority.TRIVIAL); break;
				case "enhancement": result.setType(CommitIssue.Type.IMPROVEMENT); result.setPriority(CommitIssue.Priority.MINOR); break; // enhancement actually a type, not a priority!
				default: result.setPriority(CommitIssue.Priority.NA); break;
			}
			
			return result;
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
