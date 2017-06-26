package tool.bico.parser;

import tool.bico.model.CommitIssue;

public class IssueInfoHolder {
	CommitIssue.Type type = CommitIssue.Type.NA;
	CommitIssue.Priority priority = CommitIssue.Priority.NA;
	String name;
	String link;
	String description;
	
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

	public String toString() {
		return String.format("IssueInfoHolder[name='%s', priority='%s', type='%s']",
				name, priority, type);
	}
}
