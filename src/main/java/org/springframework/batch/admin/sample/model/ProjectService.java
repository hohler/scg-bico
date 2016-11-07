package org.springframework.batch.admin.sample.model;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class ProjectService {

	@Autowired
	private ProjectDaoInterface projectDao;

	public ProjectService() {
		System.err.println("ProjectService bean created!");
	}
	@Transactional
	public void add(Project project) {
		projectDao.persist(project);
	}
	
	@Transactional
	public void addAll(Collection<Project> projects) {
		for (Project project : projects) {
			projectDao.persist(project);
		}
	}

	@Transactional(readOnly = true)
	public List<Project> listAll() {
		return projectDao.findAll();

	}
	
	/*public void setProjectDao(ProjectDao dao) {
		this.projectDao = dao;
	}*/

}
