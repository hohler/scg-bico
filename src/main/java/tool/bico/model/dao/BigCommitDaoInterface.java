package tool.bico.model.dao;

import java.util.Collection;
import java.util.List;

import tool.bico.model.BigCommit;
import tool.bico.model.Project;

public interface BigCommitDaoInterface {

	void persist(BigCommit bigCommit);

	List<BigCommit> findAll();
	
	BigCommit findById(Long id);

	void delete(BigCommit bigCommit);

	void update(BigCommit bigCommit);

	List<BigCommit> getProjectBigCommits(Project project);

	void removeAllByProject(Project project);

	void persistAll(Collection<BigCommit> bigCommits);
	
	void flush();

	BigCommit findByRefAndProject(String ref, Project project);
}