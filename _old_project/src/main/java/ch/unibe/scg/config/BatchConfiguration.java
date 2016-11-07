package ch.unibe.scg.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import ch.unibe.scg.batch.processor.RepositoryProcessor;
import ch.unibe.scg.batch.processor.IssuedCommitProcessor;
import ch.unibe.scg.batch.reader.RepositoryReader;
import ch.unibe.scg.batch.reader.IssuedCommitReader;
import ch.unibe.scg.batch.writer.RepositoryWriter;
import ch.unibe.scg.batch.writer.IssuedCommitWriter;
import ch.unibe.scg.model.Commit;
import ch.unibe.scg.model.IssuedCommit;
import ch.unibe.scg.model.Project;

//@Configuration
//@EnableBatchProcessing
//@EnableAutoConfiguration
public class BatchConfiguration {
	
	//private final String URL = "https://github.com/apache/flume";
	//private final String BRANCH = "trunk";

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Project project() {
		Project project = new Project(Project.Type.GIT);
		project.setUrl("https://github.com/apache/flume");
		project.setName("flume");
		project.setBranch("trunk");
		project.setIssueTrackerUrlPattern("https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml");
		System.err.println(project);
		return project;
	}
	
	@Bean("promotionListener")
	public ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
		listener.setKeys( new String[] { "issuedCommits" } );
		return listener;
	}
	
	@Bean("repositoryReader")
	public ItemReader<Commit> repositoryReader(Project project) {
		return new RepositoryReader(project);
	}

	@Bean("repositoryProcessor")
	public ItemProcessor<Commit, IssuedCommit> repositoryProcessor() {
		return new RepositoryProcessor();
	}

	@Bean("repositoryWriter")
	public ItemWriter<IssuedCommit> repositoryWriter() {
		return new RepositoryWriter();
	}
	
	@Bean("issuedCommitReader")
	public ItemReader<IssuedCommit> issuedCommitReader() {
		return new IssuedCommitReader();
	}

	@Bean("issuedCommitProcessor")
	public ItemProcessor<IssuedCommit, IssuedCommit> issuedCommitProcessor(Project project) {
		return new IssuedCommitProcessor(project);
	}

	@Bean("issuedCommitWriter")
	public ItemWriter<IssuedCommit> issuedCommitWriter() {
		return new IssuedCommitWriter();
	}
	
	@Bean("issuedCommitTaskExecutor")
	public TaskExecutor issueTaskExecutor() {
		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		executor.setConcurrencyLimit(20);
		return executor;
	}

	@Bean
	public Job job(@Qualifier("repositoryToCollectionOfCommits") Step step1, @Qualifier("getIssueInformationForEachCommit") Step step2) throws Exception {
		return jobBuilderFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.start(step1)
				.next(step2)
				.build();
	}

	@Bean
	public Step repositoryToCollectionOfCommits(@Qualifier("repositoryReader") ItemReader<Commit> reader,
			@Qualifier("repositoryWriter") ItemWriter<IssuedCommit> writer,
			@Qualifier("repositoryProcessor") ItemProcessor<Commit, IssuedCommit> processor,
			@Qualifier("promotionListener") ExecutionContextPromotionListener promotionListener) {
		return stepBuilderFactory.get("repositoryToCollectionOfCommits")
				.<Commit, IssuedCommit> chunk(100)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.listener(promotionListener)
				.build();
	}
	
	
	@Bean
	public Step getIssueInformationForEachCommit(@Qualifier("issuedCommitReader") ItemReader<IssuedCommit> reader,
			@Qualifier("issuedCommitWriter") ItemWriter<IssuedCommit> writer,
			@Qualifier("issuedCommitProcessor") ItemProcessor<IssuedCommit, IssuedCommit> processor,
			@Qualifier("issuedCommitTaskExecutor") TaskExecutor executor) {
		return stepBuilderFactory.get("getIssueInformationForEachCommit")
				.<IssuedCommit, IssuedCommit> chunk(10)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.taskExecutor(executor)
				.build();
	}
	
}