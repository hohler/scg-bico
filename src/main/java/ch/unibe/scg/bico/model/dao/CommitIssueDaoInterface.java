package ch.unibe.scg.bico.model.dao;

import java.util.List;

import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.model.Project;

public interface CommitIssueDaoInterface {

	void persist(CommitIssue commitIssue);

	List<CommitIssue> findAll();
	
	CommitIssue findById(Long id);

	void delete(CommitIssue commitIssue);

	void update(CommitIssue commitIssue);
	
	void updateAll(List<? extends CommitIssue> commitIssues);

	List<CommitIssue> findAllByProject(Project project);

}