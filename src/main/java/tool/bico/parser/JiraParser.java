package tool.bico.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tool.bico.model.CommitIssue;
import tool.bico.utils.SwitchSubstring;

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
		return String.format(url/*+"?field=type&field=priority&field=title&field=link&field=description"*/,  issue, issue);
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
			
			NodeList items = doc.getElementsByTagName("item");
			Node node = items.item(0);
			Element element = (Element) node;	
			
			String link = element.getElementsByTagName("link").item(0).getTextContent();
			String description = element.getElementsByTagName("description").item(0).getTextContent();
			
			String key = element.getElementsByTagName("key").item(0).getTextContent();
			String type = element.getElementsByTagName("type").item(0).getTextContent();
			String priority = element.getElementsByTagName("priority").item(0).getTextContent();
			
			String title = element.getElementsByTagName("title").item(0).getTextContent();
			
			// for classifier of simon
			String summary = element.getElementsByTagName("summary").item(0) != null ? element.getElementsByTagName("summary").item(0).getTextContent() : "";
			String component = element.getElementsByTagName("component").item(0) != null ? element.getElementsByTagName("component").item(0).getTextContent() : "";
			String project = element.getElementsByTagName("project").item(0) != null ? element.getElementsByTagName("project").item(0).getTextContent() : "";
			String version = element.getElementsByTagName("version").item(0) != null ? element.getElementsByTagName("version").item(0).getTextContent() : "";
			List<IssueComment> comments = new ArrayList<>();
			
			NodeList commentsList = element.getElementsByTagName("comments");
			for(int i = 0; i<commentsList.getLength(); i++) {
				Element e = (Element) commentsList.item(i);
				if(e == null) continue;
				IssueComment ic = new IssueComment();
				ic.setId(e.getAttribute("id"));
				ic.setAuthor(e.getAttribute("author"));
				ic.setDate(e.getAttribute("created"));
				ic.setBody(e.getTextContent());
				comments.add(ic);
			}
			
			boolean patch = false;
			boolean screenshot = false;
			
			NodeList attachmentsList = element.getElementsByTagName("attachments");
			for(int i = 0; i<attachmentsList.getLength(); i++) {
				Element e = (Element) attachmentsList.item(i);
				if(e == null) continue;	
				if(e.getAttribute("name").contains("patch") || e.getAttribute("name").contains("diff")) {
					patch = true;
				}
				if(e.getAttribute("name").contains("screenshot")) { // todo right word?
					screenshot = true;
				}
			}
			
			
			result.setName(key);
			result.setLink(link);
			result.setDescription(description);
			
			result.setSummary(summary);
			result.setComponent(component);
			result.setProject(project);
			result.setVersion(version);
			result.setComments(comments);
			result.setHasPatch(patch);
			result.setHasScreenshot(screenshot);

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
			
			// special case for refactoring
			if(result.getType() != CommitIssue.Type.REFACTOR) {
				SwitchSubstring.of(title.toLowerCase())
				.when("refactor", "refactoring", "refactored").then(() -> result.setType(CommitIssue.Type.REFACTOR));
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
			e.printStackTrace();
		}
		return null;
	}
	
	public class IssueComment {
		private String body;
		private String date;
		private String author;
		private String id;
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	}
}
