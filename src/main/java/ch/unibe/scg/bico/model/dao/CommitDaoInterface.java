package ch.unibe.scg.bico.model.dao;

import java.util.List;

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.model.Project;

public interface CommitDaoInterface {

	void persist(Commit commit);

	List<Commit> findAll();
	
	Commit findById(Long id);

	void delete(Commit commit);

	void update(Commit commit);

	List<Commit> getProjectCommits(Project project);

}