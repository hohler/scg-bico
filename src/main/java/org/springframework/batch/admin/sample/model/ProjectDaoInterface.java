package org.springframework.batch.admin.sample.model;

import java.util.List;

public interface ProjectDaoInterface {

	void persist(Project project);

	List<Project> findAll();

}