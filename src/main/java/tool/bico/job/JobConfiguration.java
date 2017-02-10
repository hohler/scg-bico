package tool.bico.job;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import tool.bico.model.Project;
import tool.bico.model.service.ProjectService;

@Configuration
public class JobConfiguration {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private JobCreator jobCreator;
	
	
	public JobConfiguration() {
		System.err.println("Job2Configuration initialized!");
	}	

	@PostConstruct
	private void createJobs() {
		for(Project project : projectService.listAll()) {
			jobCreator.createJob(project);
		}
	}	
}