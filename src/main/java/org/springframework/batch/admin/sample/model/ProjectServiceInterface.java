package org.springframework.batch.admin.sample.model;

import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface ProjectServiceInterface {

	void add(Project project);

	void addAll(Collection<Project> projects);

	List<Project> listAll();

	Project findById(Long id);

	void delete(Project project);

	void update(Project project);

}