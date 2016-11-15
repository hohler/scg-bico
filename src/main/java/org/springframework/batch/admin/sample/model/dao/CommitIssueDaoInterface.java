package org.springframework.batch.admin.sample.model.dao;

import java.util.List;

import org.springframework.batch.admin.sample.model.CommitIssue;

public interface CommitIssueDaoInterface {

	void persist(CommitIssue commitIssue);

	List<CommitIssue> findAll();
	
	CommitIssue findById(Long id);

	void delete(CommitIssue commitIssue);

	void update(CommitIssue commitIssue);
	
	void updateAll(List<? extends CommitIssue> commitIssues);

}