package org.springframework.batch.admin.sample.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "commits")
public class Commit {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "commit")
	protected List<CommitFile> files;
	
	@OneToOne(cascade = CascadeType.DETACH, optional=true)
	protected Commit parentCommit;
	
	@OneToOne(mappedBy="parentCommit")
	protected Commit childCommit;
	
	@OneToOne(cascade = CascadeType.ALL, optional=true)
	protected CommitIssue commitIssue;
	
	@ManyToOne
	protected Project project;
	
	protected int additions;
	
	protected int deletions;
	
	@Column(columnDefinition = "TEXT")
	protected String message;
	
	public Commit() {
		files = new ArrayList<CommitFile>();
	
	}

	public void addFile(CommitFile file) {
		file.setCommit(this);
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
	
	public List<CommitFile> getFiles() {
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

	public Commit getChildCommit() {
		return childCommit;
	}

	public void setChildCommit(Commit childCommit) {
		this.childCommit = childCommit;
	}
	
	public CommitIssue getCommitIssue() {
		return commitIssue;
	}

	public void setCommitIssue(CommitIssue commitIssue) {
		this.commitIssue = commitIssue;
	}
	public void initIssue(String issue) {
		commitIssue = new CommitIssue(issue);
	}

	public String toString() {
		return message.split("\\r?\\n")[0];
	}
}
