package ch.unibe.scg.bico.parser;

import ch.unibe.scg.bico.model.CommitIssue;

public class IssueInfoHolder {
	CommitIssue.Type type = CommitIssue.Type.NA;
	CommitIssue.Priority priority = CommitIssue.Priority.NA;
	String name;
	
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
	
	public String toString() {
		return String.format("IssueInfoHolder[name='%s', priority='%s', type='%s']",
				name, priority, type);
	}
}
