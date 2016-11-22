package ch.unibe.scg.bico.model;

import java.util.HashSet;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "commits")
public class Commit {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "commit", orphanRemoval = true)
	@OrderBy("id")
	protected Set<CommitFile> files;
	
	@OneToOne(cascade = CascadeType.DETACH, optional=true, fetch = FetchType.LAZY)
	protected Commit parentCommit;
	
	/*@OneToOne(mappedBy="parentCommit", fetch = FetchType.LAZY)
	protected Commit childCommit;*/
	
	@OneToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	protected CommitIssue commitIssue;
	
	@ManyToOne(fetch = FetchType.LAZY)
	protected Project project;
	
	protected int additions;
	
	protected int deletions;
	
	protected String ref;
	
	@Column(columnDefinition = "TEXT")
	protected String message;
	
	public Commit() {
		files = new HashSet<CommitFile>();
	
	}

	public void addFile(CommitFile file) {
		file.setCommit(this);
		files.add(file);
	}
	
	public void removeFile(CommitFile file) {
		files.remove(file);
		file.setCommit(null);
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
	
	public Set<CommitFile> getFiles() {
		return files;
	}
	
	
	public Commit getParentCommit() {
		return parentCommit;
	}

	public void setParentCommit(Commit parentCommit) {
		this.parentCommit = parentCommit;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*public Commit getChildCommit() {
		return childCommit;
	}

	public void setChildCommit(Commit childCommit) {
		this.childCommit = childCommit;
	}*/
	
	public CommitIssue getCommitIssue() {
		return commitIssue;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public void setCommitIssue(CommitIssue commitIssue) {
		this.commitIssue = commitIssue;
	}
	public void initIssue(String issue) {
		commitIssue = new CommitIssue(issue);
	}

	public String firstLineOfMessage() {
		return message.split("\\r?\\n")[0];
	}
	
	public int getChanges() {
		return additions + deletions;
	}
	
	public String toString() {
		return String.format("Commit[id=%d, name='%s', additions='%d', deletions='%d']",
				id, firstLineOfMessage(), additions, deletions);
	}
}
