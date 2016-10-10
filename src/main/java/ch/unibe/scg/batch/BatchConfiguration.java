package ch.unibe.scg.batch;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import ch.unibe.scg.batch.processor.CommitItemProcessor;
import ch.unibe.scg.batch.processor.IssueItemProcessor;
import ch.unibe.scg.batch.reader.CommitItemReader;
import ch.unibe.scg.batch.reader.IssueItemReader;
import ch.unibe.scg.batch.writer.CommitItemWriter;
import ch.unibe.scg.batch.writer.IssueItemWriter;
import ch.unibe.scg.model.Commit;
import ch.unibe.scg.model.CommitIssue;
import ch.unibe.scg.model.Project;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private Project project;

	public BatchConfiguration() {
		Project project = new Project(Project.Type.GIT);
		project.setUrl("https://github.com/apache/flume");
		project.setName("flume");
		project.setBranch("trunk");
		project.setIssueTrackerUrlPattern("https://issues.apache.org/jira/si/jira.issueviews:issue-xml/%s/%s.xml");
		System.err.println(project);
	}
	
	@Bean
	public ItemReader<Commit> commitReader() {
		CommitItemReader reader = new CommitItemReader();
		reader.setProject(project);
		return reader;
	}

	@Bean
	public ItemProcessor<Commit, Commit> commitProcessor() {
		CommitItemProcessor processor = new CommitItemProcessor();
		return processor;
	}

	@Bean
	public ItemWriter<Commit> commitWriter() {
		return new CommitItemWriter();
	}
	
	@Bean
	public ItemReader<CommitIssue> issueReader() {
		IssueItemReader reader = new IssueItemReader();
		reader.setProject(project);
		return reader;
	}

	@Bean
	public ItemProcessor<CommitIssue, CommitIssue> issueProcessor() {
		IssueItemProcessor processor = new IssueItemProcessor();
		processor.setIssueTrackerUrlPattern(project.getIssueTrackerUrlPattern());
		return processor;
	}

	@Bean
	public ItemWriter<CommitIssue> issueWriter() {
		return new IssueItemWriter();
	}
	
	@Bean
	public TaskExecutor issueTaskExecutor() {
		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		executor.setConcurrencyLimit(20);
		return executor;
	}

	@Bean
	public Job job() throws Exception {
		return jobBuilderFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.start(processCommits())
				.next(processIssues())
				.build();
	}

	@Bean
	public Step processCommits() {
		return stepBuilderFactory.get("processCommits")
				.<Commit, Commit> chunk(20)
				.reader(commitReader())
				.processor(commitProcessor())
				.writer(commitWriter())
				.build();
	}
	
	
	@Bean
	public Step processIssues() {
		return stepBuilderFactory.get("processIssues")
				.<CommitIssue, CommitIssue> chunk(20)
				.reader(issueReader())
				.processor(issueProcessor())
				.writer(issueWriter())
				.taskExecutor(issueTaskExecutor())
				.build();
	}
	
}