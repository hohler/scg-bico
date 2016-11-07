package ch.unibe.scg.model;

import java.util.ArrayList;

public class Commit {
	
	protected ArrayList<CommitFile> files;
	protected int additions;
	protected int deletions;
	protected String message;
	
	private int type;
	
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
	
	public ArrayList<CommitFile> getFiles() {
		return files;
	}
	
	public String toString() {
		return message.split("\\r?\\n")[0];
	}
}
