package ch.unibe.scg.bico.job;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import ch.unibe.scg.bico.model.Project;
import ch.unibe.scg.bico.model.service.ProjectService;

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