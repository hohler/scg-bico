package org.springframework.batch.admin.sample.job;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.CommitIssue;
import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.model.service.CommitIssueService;
import org.springframework.batch.admin.sample.model.service.CommitService;
import org.springframework.batch.admin.sample.model.service.ProjectService;
import org.springframework.batch.admin.sample.processor.CommitProcessor;
import org.springframework.batch.admin.sample.processor.RepositoryProcessor;
import org.springframework.batch.admin.sample.reader.CommitReader;
import org.springframework.batch.admin.sample.reader.RepositoryReader;
import org.springframework.batch.admin.sample.writer.CommitWriter;
import org.springframework.batch.admin.sample.writer.RepositoryWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

public class JobCreator {
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private JobRegistry jobRegistry;
	
	@Autowired
	private CommitService commitService;
	
	@Autowired
	private CommitIssueService commitIssueService;
	
	@Autowired
	private ProjectService projectService;
	
	/*private ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
		listener.setKeys( new String[] { "issuedCommits" } );
		return listener;
	}*/
	
	private TaskExecutor issueTaskExecutor() {
		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		executor.setConcurrencyLimit(40);
		return executor;
	}
	
	public JobCreator() {
		System.err.println("JobCreator initialized!");
	}
	
	public void removeJob(Project project) {
		String jobName = project.getId().toString() + "_" + project.getName();
		jobRegistry.unregister(jobName);
	}
	
	public void createJob(Project project) {
		String jobName = project.getId().toString() + "_" + project.getName();
		
		Step step = stepBuilderFactory.get(jobName+"_repositoryToCollectionOfCommits")
				.<Commit, Commit> chunk(100)
				.reader(new RepositoryReader(project, projectService))
				.processor(new RepositoryProcessor())
				.writer(new RepositoryWriter(commitService))
				//.listener(promotionListener())
				.build();
		
		Step step2 = stepBuilderFactory.get(jobName+"_getIssueInformationForEachCommit")
				.<Commit, CommitIssue> chunk(50)
				.reader(new CommitReader(project))
				.processor(new CommitProcessor(project.getIssueTrackerUrlPattern()))
				.writer(new CommitWriter(commitIssueService))
				.taskExecutor(issueTaskExecutor())
				.build();
		
		Job builder = jobBuilderFactory.get(jobName)
				.incrementer(new RunIdIncrementer())
				.start(step)
				.next(step2)
				.build();
		
		
		try {
			jobRegistry.getJob(jobName);
		} catch (NoSuchJobException e) {
			// TODO Auto-generated catch block
			try {
				jobRegistry.register(new ReferenceJobFactory(builder));
			} catch (DuplicateJobException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}
	}
}
