package ch.unibe.scg.bico.model.dao;

import java.util.List;

import ch.unibe.scg.bico.model.Project;

public interface ProjectDaoInterface {

	void persist(Project project);

	List<Project> findAll();
	
	Project findById(Long id);

	void delete(Project project);

	void update(Project project);

}