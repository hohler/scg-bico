package tool.bico.model.dao;

import java.util.List;

import tool.bico.model.CommitIssue;
import tool.bico.model.CommitIssue.Type;
import tool.bico.model.Project;

public interface CommitIssueDaoInterface {

	void persist(CommitIssue commitIssue);

	List<CommitIssue> findAll();
	
	CommitIssue findById(Long id);

	void delete(CommitIssue commitIssue);

	void update(CommitIssue commitIssue);
	
	void updateAll(List<? extends CommitIssue> commitIssues);

	List<CommitIssue> findAllByProject(Project project);

	CommitIssue findByProjectAndIssueName(Project project, String name);

	List<CommitIssue> findAllByProjectAndType(Project project, Type type);

}