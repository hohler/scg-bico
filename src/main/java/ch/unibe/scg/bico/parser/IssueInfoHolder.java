package ch.unibe.scg.bico.parser;

import ch.unibe.scg.bico.model.CommitIssue;

public class IssueInfoHolder {
	CommitIssue.Type type;
	CommitIssue.Priority priority;
	
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
}
