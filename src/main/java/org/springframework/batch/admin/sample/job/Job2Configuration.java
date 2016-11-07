package org.springframework.batch.admin.sample.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import org.springframework.batch.admin.sample.processor.IssuedCommitProcessor;
import org.springframework.batch.admin.sample.processor.RepositoryProcessor;
import org.springframework.batch.admin.sample.reader.IssuedCommitReader;
import org.springframework.batch.admin.sample.reader.RepositoryReader;
import org.springframework.batch.admin.sample.writer.IssuedCommitWriter;
import org.springframework.batch.admin.sample.writer.RepositoryWriter;
import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.IssuedCommit;
import org.springframework.batch.admin.sample.model.Project;

@Configuration
public class Job2Configuration {
	
	//private final String URL = "https://github.com/apache/flume";
	//private final String BRANCH = "trunk";

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	@JobScope
	public Project project() {
		Project project = new Project(Project.Type.GIT);
		project.setUrl("https://github.com/apache/flume");
		project.setName("flume");
		project.setBranch("trunk");
		project.setIssueTrackerUrlPattern("https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml");
		System.err.println(project);
		return project;
	}
	
	@Bean(name="promotionListener")
	@JobScope
	public ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
		listener.setKeys( new String[] { "issuedCommits" } );
		return listener;
	}
	
	@Bean(name="repositoryReader")
	@JobScope
	public ItemReader<Commit> repositoryReader(Project project) {
		return new RepositoryReader(project);
	}

	@Bean(name="repositoryProcessor")
	@StepScope
	public ItemProcessor<Commit, IssuedCommit> repositoryProcessor() {
		return new RepositoryProcessor();
	}

	@Bean(name="repositoryWriter")
	@StepScope
	public ItemWriter<IssuedCommit> repositoryWriter() {
		return new RepositoryWriter();
	}
	
	@Bean(name="issuedCommitReader")
	@JobScope
	public ItemReader<IssuedCommit> issuedCommitReader() {
		return new IssuedCommitReader();
	}

	@Bean(name="issuedCommitProcessor")
	@StepScope
	public ItemProcessor<IssuedCommit, IssuedCommit> issuedCommitProcessor(Project project) {
		return new IssuedCommitProcessor(project);
	}

	@Bean(name="issuedCommitWriter")
	@StepScope
	public ItemWriter<IssuedCommit> issuedCommitWriter() {
		return new IssuedCommitWriter();
	}
	
	@Bean(name="issueTaskExecutor")
	@JobScope
	public TaskExecutor issueTaskExecutor() {
		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		executor.setConcurrencyLimit(20);
		return executor;
	}

	@Bean
	public Job job(Step repositoryToCollectionOfCommits, Step getIssueInformationForEachCommit) throws Exception {
		return jobBuilderFactory.get("batchJob")
				.incrementer(new RunIdIncrementer())
				.start(repositoryToCollectionOfCommits)
				.next(getIssueInformationForEachCommit)
				.build();
	}

	@Bean
	public Step repositoryToCollectionOfCommits() {
		return stepBuilderFactory.get("repositoryToCollectionOfCommits")
				.<Commit, IssuedCommit> chunk(100)
				.reader(repositoryReader(project()))
				.processor(repositoryProcessor())
				.writer(repositoryWriter())
				.listener(promotionListener())
				.build();
	}
	
	
	@Bean
	public Step getIssueInformationForEachCommit() {
		return stepBuilderFactory.get("getIssueInformationForEachCommit")
				.<IssuedCommit, IssuedCommit> chunk(10)
				.reader(issuedCommitReader())
				.processor(issuedCommitProcessor(project()))
				.writer(issuedCommitWriter())
				.taskExecutor(issueTaskExecutor())
				.build();
	}
	
}