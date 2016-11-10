package org.springframework.batch.admin.sample.job;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.IssuedCommit;
import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.processor.IssuedCommitProcessor;
import org.springframework.batch.admin.sample.processor.RepositoryProcessor;
import org.springframework.batch.admin.sample.reader.IssuedCommitReader;
import org.springframework.batch.admin.sample.reader.RepositoryReader;
import org.springframework.batch.admin.sample.writer.IssuedCommitWriter;
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
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
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
	
	private ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
		listener.setKeys( new String[] { "issuedCommits" } );
		return listener;
	}
	
	private TaskExecutor issueTaskExecutor() {
		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		executor.setConcurrencyLimit(20);
		return executor;
	}
	
	public JobCreator() {
		System.err.println("JobCreator initialized!");
	}
	
	public void createJob(Project project) {
		System.out.println(project);
		Step step = stepBuilderFactory.get(project.getId().toString()+"_repositoryToCollectionOfCommits")
				.<Commit, IssuedCommit> chunk(100)
				.reader(new RepositoryReader(project))
				.processor(new RepositoryProcessor())
				.writer(new RepositoryWriter())
				.listener(promotionListener())
				.build();
		
		Step step2 = stepBuilderFactory.get(project.getId().toString()+"_getIssueInformationForEachCommit")
				.<IssuedCommit, IssuedCommit> chunk(10)
				.reader(new IssuedCommitReader())
				.processor(new IssuedCommitProcessor(project))
				.writer(new IssuedCommitWriter())
				.taskExecutor(issueTaskExecutor())
				.build();
		
		String jobName = project.getId().toString() + "_" + project.getName();
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
