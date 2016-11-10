package org.springframework.batch.admin.sample.model.service;

import java.util.Collection;
import java.util.List;

import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.model.dao.ProjectDaoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
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
	
	@Transactional(readOnly = true)
	public Project findById(Long id) {
		return projectDao.findById(id);
	}
	
	@Transactional
	public void delete(Project project) {
		projectDao.delete(project);
	}
	
	@Transactional
	public void update(Project project) {
		projectDao.update(project);
	}
	
	/*public void setProjectDao(ProjectDao dao) {
		this.projectDao = dao;
	}*/

}
