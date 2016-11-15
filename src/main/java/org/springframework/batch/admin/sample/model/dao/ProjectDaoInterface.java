package org.springframework.batch.admin.sample.model.dao;

import java.util.List;

import org.springframework.batch.admin.sample.model.Project;

public interface ProjectDaoInterface {

	void persist(Project project);

	List<Project> findAll();
	
	Project findById(Long id);

	void delete(Project project);

	void update(Project project);

}