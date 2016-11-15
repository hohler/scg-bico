package ch.unibe.scg.bico.reader;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.batch.item.ItemReader;

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.model.Project;
import ch.unibe.scg.bico.model.service.ProjectService;
import ch.unibe.scg.bico.repository.Repository;

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
