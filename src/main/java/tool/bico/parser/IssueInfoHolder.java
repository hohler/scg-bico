package tool.bico.parser;

import java.util.List;

import tool.bico.model.CommitIssue;

public class IssueInfoHolder {
	CommitIssue.Type type = CommitIssue.Type.NA;
	CommitIssue.Priority priority = CommitIssue.Priority.NA;
	String name;
	String link;
	String description;
	String project;
	String summary;
	List<IssueComment> comments;
	String version;
	String component;
	boolean hasPatch;
	boolean hasScreenshot;
	
	public void setType(CommitIssue.Type type) {
		this.type = type;
	}
	
	public CommitIssue.Type getType() {
		return type;
	}
	
	public void setPriority(CommitIssue.Priority priority) {
		this.priority = priority;
	}
	
	public CommitIssue.Priority getPriority() {
		return priority;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
		
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<IssueComment> getComments() {
		return comments;
	}

	public void setComments(List<IssueComment> comments) {
		this.comments = comments;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}
	

	public boolean hasPatch() {
		return hasPatch;
	}

	public void setHasPatch(boolean hasPatch) {
		this.hasPatch = hasPatch;
	}

	public boolean hasScreenshot() {
		return hasScreenshot;
	}

	public void setHasScreenshot(boolean hasScreenshot) {
		this.hasScreenshot = hasScreenshot;
	}

	public String toString() {
		return String.format("IssueInfoHolder[name='%s', priority='%s', type='%s']",
				name, priority, type);
	}
}
