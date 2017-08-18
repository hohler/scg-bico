package tool.bico.model.dao;

import java.util.List;

import tool.bico.model.CommitIssue.Type;
import tool.bico.model.CommitIssueAnalysis;
import tool.bico.model.Project;

public interface CommitIssueAnalysisDaoInterface {

	void persist(CommitIssueAnalysis commitIssue);

	List<CommitIssueAnalysis> findAll();
	
	CommitIssueAnalysis findById(Long id);

	void delete(CommitIssueAnalysis commitIssue);

	void update(CommitIssueAnalysis commitIssue);
	
	void updateAll(List<? extends CommitIssueAnalysis> commitIssues);

	List<CommitIssueAnalysis> findAllByProject(Project project);

	List<CommitIssueAnalysis> findAllByProjectAndType(Project project, Type type);
	
	void removeAllByProject(Project project);

}