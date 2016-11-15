package ch.unibe.scg.bico.model.dao;

import java.util.List;

import ch.unibe.scg.bico.model.CommitIssue;

public interface CommitIssueDaoInterface {

	void persist(CommitIssue commitIssue);

	List<CommitIssue> findAll();
	
	CommitIssue findById(Long id);

	void delete(CommitIssue commitIssue);

	void update(CommitIssue commitIssue);
	
	void updateAll(List<? extends CommitIssue> commitIssues);

}