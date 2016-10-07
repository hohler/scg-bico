package ch.unibe.scg.model;

import org.eclipse.jgit.diff.DiffEntry;

import ch.unibe.scg.model.CommitFile;

public class CommitFile {
	public int additions;
	public int deletions;
	public int changes;
	public String fileName;
	
	public String oldPath;
	public String newPath;
	public String patch;
	public ChangeType changeType;
	
	public static enum ChangeType {
		/** Add a new file to the project */
		ADD,
		/** Modify an existing file in the project (content and/or mode) */
		MODIFY,
		/** Delete an existing file from the project */
		DELETE,
		/** Rename an existing file to a new location */
		RENAME,
		/** Copy an existing file to a new location, keeping the original */
		COPY;
	}

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

	/**
	 * Get the old name associated with this file.
	 * <p>
	 * The meaning of the old name can differ depending on the semantic meaning
	 * of this patch:
	 * <ul>
	 * <li><i>file add</i>: always <code>/dev/null</code></li>
	 * <li><i>file modify</i>: always {@link #getNewPath()}</li>
	 * <li><i>file delete</i>: always the file being deleted</li>
	 * <li><i>file copy</i>: source file the copy originates from</li>
	 * <li><i>file rename</i>: source file the rename originates from</li>
	 * </ul>
	 *
	 * @return old name for this file.
	 */
	public String getOldPath() {
		return oldPath;
	}

	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}

	/**
	 * Get the new name associated with this file.
	 * <p>
	 * The meaning of the new name can differ depending on the semantic meaning
	 * of this patch:
	 * <ul>
	 * <li><i>file add</i>: always the file being created</li>
	 * <li><i>file modify</i>: always {@link #getOldPath()}</li>
	 * <li><i>file delete</i>: always <code>/dev/null</code></li>
	 * <li><i>file copy</i>: destination file the copy ends up at</li>
	 * <li><i>file rename</i>: destination file the rename ends up at</li>
	 * </ul>
	 *
	 * @return new name for this file.
	 */
	public String getNewPath() {
		return newPath;
	}

	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}
	
	public void setChangeType(DiffEntry.ChangeType changeType) {
		switch(changeType) {
			case ADD: setChangeType(ChangeType.ADD); break;
			case COPY: setChangeType(ChangeType.COPY); break;
			case DELETE: setChangeType(ChangeType.DELETE); break;
			case MODIFY: setChangeType(ChangeType.MODIFY); break;
			case RENAME: setChangeType(ChangeType.RENAME); break;
			default: setChangeType(ChangeType.MODIFY); break;
		}
	}
}