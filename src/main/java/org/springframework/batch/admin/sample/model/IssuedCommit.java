package org.springframework.batch.admin.sample.model;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class IssuedCommit extends Commit {
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "commit")
	protected CommitIssue commitIssue;
	
	public IssuedCommit() {
		super();
	}
	
	public IssuedCommit(Commit commit) {
		super();
		id = commit.getId();
		project = commit.getProject();
		files = new ArrayList<CommitFile>(commit.getFiles());
		additions = commit.getAdditions();
		deletions = commit.getDeletions();
		message = commit.getMessage();
		parentCommit = commit.getParentCommit();
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

}
