package org.springframework.batch.admin.sample.reader;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.model.service.ProjectService;
import org.springframework.batch.admin.sample.repository.Repository;
import org.springframework.batch.item.ItemReader;

public class RepositoryReader implements ItemReader<Commit> {

	private ArrayList<Commit> commits;
	private Iterator<Commit> iterator;
	
	private Project project;
	
	private ProjectService projectService;
	
	public RepositoryReader(Project project, ProjectService projectService) {
		this.project = project;
		this.projectService = projectService;
	}
	
	private void init() {
		project.cleanForProcessing();
		projectService.update(project);
		Repository g = new Repository(project);
		commits = g.getCommits();
		iterator = commits.iterator();
	}
	
	@Override
	public Commit read() {
		if(iterator == null) init();
		//if(project.getCommits().size() == commits.size()) return null; // if no new commits are available
		if(iterator.hasNext()) return iterator.next();
		return null;
	}
}
