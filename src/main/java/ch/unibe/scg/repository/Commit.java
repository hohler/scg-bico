package ch.unibe.scg.repository;

import java.util.ArrayList;

public class Commit {

	private ArrayList<CommitFile> files;
	private int additions;
	private int deletions;
	private String message;
	
	public Commit() {
		files = new ArrayList<CommitFile>();
	
	}

	public void addFile(Commit.CommitFile file) {
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

	public class CommitFile {
		
		private int additions;
		private int deletions;
		private int changes;
		private String fileName;
		private String patch;
		
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
}
