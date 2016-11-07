package ch.unibe.scg.controller.service;

import ch.unibe.scg.model.Project;

public interface ProjectService {

	public Project saveProject(Project project);
	
	public Iterable<Project> listAllProjects();

	public Project getProjectById(Long id);

	public void deleteProject(Long id);
}
