package org.springframework.batch.admin.sample.job;

import javax.annotation.PostConstruct;

import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.model.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

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