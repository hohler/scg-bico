package ch.unibe.scg.model;

import ch.unibe.scg.model.CommitFile;

public class CommitFile {
	public int additions;
	public int deletions;
	public int changes;
	public String fileName;
	public String patch;

	public CommitFile() {
	}

	public void setAdditions(int additions) {
		this.additions = additions;
	}
	
	public int getAdditions() {
		return additions;
	}

	public int getDeletions() {
		return deletions;
	}

	public void setDeletions(int deletions) {
		this.deletions = deletions;
	}

	public void setFilename(String filename) {
		this.fileName = filename;
	}
	
	public String getFilename() {
		return fileName;
	}

	public String getPatch() {
		return patch;
	}

	public void setPatch(String patch) {
		this.patch = patch;
	}

	public void setChanges(int changes) {
		this.changes = changes;			
	}
	
	public int getChanges() {
		return changes;
	}
}