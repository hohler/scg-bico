package org.springframework.batch.admin.sample.model;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class IssuedCommit extends Commit {
	
	@OneToOne(cascade = CascadeType.ALL)
	protected CommitIssue commitIssue;
	
	public IssuedCommit() {
		super();
	}
	
	public IssuedCommit(Commit commit) {
		super();
		files = new ArrayList<CommitFile>(commit.getFiles());
		additions = commit.getAdditions();
		deletions = commit.getDeletions();
		message = commit.getMessage();
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