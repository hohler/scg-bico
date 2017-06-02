package tool.bico.reader;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemReader;

import tool.bico.model.Commit;
import tool.bico.model.Project;
import tool.bico.model.service.ProjectService;
import tool.bico.repository.GitRepository;

public class RepositoryReader implements ItemReader<Commit> {

	private Iterator<Commit> iterator;
	
	private Project project;
	
	// unused
	private ProjectService projectService;
	
	private List<String> commits;
	
	public RepositoryReader(Project project, ProjectService projectService) {
		this.project = project;
		this.projectService = projectService;
		commits = project.getCommits().stream().map(c -> c.getRef()).collect(Collectors.toList());
	}
	
	private void init() {
		// project.cleanForProcessing();
		// projectService.update(project);
		GitRepository g = new GitRepository(project, true);
		iterator = g.getCommitIterator();
	}
	
	@Override
	public Commit read() {
		if(iterator == null) init();
		if(iterator.hasNext()) {
			Commit next = iterator.next();
			
			// check if commit does already exist
			if(commits.contains(next.getRef())) return read();
			
			project.addCommit(next);
			return next;
		}
		iterator = null;
		return null;
	}
}
