package tool.bico.reader;

import java.util.Iterator;

import org.springframework.batch.item.ItemReader;

import tool.bico.model.Commit;
import tool.bico.model.Project;
import tool.bico.model.service.ProjectService;
import tool.bico.repository.Repository;

public class RepositoryReader implements ItemReader<Commit> {

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
		iterator = g.getCommitIterator();
	}
	
	@Override
	public Commit read() {
		if(iterator == null) init();
		if(iterator.hasNext()) {
			Commit next = iterator.next();
			project.addCommit(next);
			return next;
		}
		iterator = null;
		return null;
	}
}
