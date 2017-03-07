package tool.bico.model;

import java.util.HashSet;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
	
	@OneToOne(cascade = {CascadeType.DETACH}, optional=true, fetch = FetchType.LAZY)
	protected Commit parentCommit;
	
	@ManyToMany(targetEntity = CommitIssue.class, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	//@JoinTable(name = "commitissues_commits", joinColumns = @JoinColumn(name = "commits_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "commitissues_id", referencedColumnName = "id"))
	protected Set<CommitIssue> commitIssues;
	
	@ManyToOne(fetch = FetchType.LAZY)
	protected Project project;
	
	protected int additions;
	
	protected int deletions;
	
	protected String ref;
	
	@Column(columnDefinition = "TEXT")
	protected String message;
	
	public Commit() {
		files = new HashSet<CommitFile>();
		commitIssues = new HashSet<CommitIssue>();
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

	public Set<CommitIssue> getCommitIssues() {
		return commitIssues;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public void initIssue(String issue) {
		if(issue == null) return;
		CommitIssue i = new CommitIssue(issue);
		//i.setCommit(this);
		this.addCommitIssue(i);
	}
	
	public void addCommitIssue(CommitIssue commitIssue) {
		//commitIssue.setCommit(this);
		commitIssue.addCommit(this);
		this.commitIssues.add(commitIssue);
	}
	
	public void removeCommitIssue(CommitIssue commitIssue) {
		//commitIssue.setCommit(null);
		commitIssue.removeCommit(this);
		this.commitIssues.remove(commitIssue);
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
