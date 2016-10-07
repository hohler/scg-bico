package ch.unibe.scg.model;

import java.util.ArrayList;

public class Commit {
	
	private ArrayList<CommitFile> files;
	private int additions;
	private int deletions;
	private String message;
	private CommitIssue commitIssue;
	
	private int type;
	private String issue;
	
	public Commit() {
		files = new ArrayList<CommitFile>();
	
	}

	public void addFile(CommitFile file) {
		files.add(file);
		
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getAdditions() {
		return additions;
	}

	public void setAdditions(int additions) {
		this.additions = additions;
	}

	public int getDeletions() {
		return deletions;
	}

	public void setDeletions(int deletions) {
		this.deletions = deletions;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public CommitIssue getCommitIssue() {
		return commitIssue;
	}

	public void setCommitIssue(CommitIssue commitIssue) {
		this.commitIssue = commitIssue;
	}

}
