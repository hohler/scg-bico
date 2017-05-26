package tool.bico.job;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.model.service.ChangeMetricService;
import tool.bico.model.service.CommitIssueService;
import tool.bico.model.service.CommitService;
import tool.bico.model.service.ProjectService;
import tool.bico.model.service.SourceMetricService;
import tool.bico.model.service.SzzMetricService;
import tool.bico.processor.CommitProcessor;
import tool.bico.processor.RepositoryProcessor;
import tool.bico.reader.CommitReader2;
import tool.bico.reader.RepositoryReader;
import tool.bico.repository.GitHubAPI;
import tool.bico.writer.CommitWriter;
import tool.bico.writer.RepositoryWriter;

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
	
	@Autowired
	private ChangeMetricService changeMetricsService;
	
	@Autowired
	private SzzMetricService szzMetricService;
	
	@Autowired
	private SourceMetricService sourceMetricService;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	
	private TaskExecutor issueTaskExecutor() {
		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		executor.setConcurrencyLimit(100);
		return executor;
	}
	
	public JobCreator() {
		
		System.setProperty("git.maxdiff", "100000");
		System.setProperty("git.maxcommit", "200");
		
		System.err.println("JobCreator initialized!");
	}
	
	
	public void removeJob(Project project) {
		String jobName = project.getId().toString() + "_" + project.getName();
		// String jobNameMetrics = jobName + "_metrics";
		jobRegistry.unregister(jobName);
		// jobRegistry.unregister(jobNameMetrics);
	}
	
	public void removeJob(Long id, String name) {
		String jobName = id.toString() + "_" + name;
		// String jobNameMetrics = jobName + "_metrics";
		jobRegistry.unregister(jobName);
		// jobRegistry.unregister(jobNameMetrics);
	}
	
	public void createJob(Project project) {
		String jobName = project.getId().toString() + "_" + project.getName();
		
		createMetricsJob(project);
		
		Step step = stepBuilderFactory.get(jobName+"_repositoryToCollectionOfCommits")
				.<Commit, Commit> chunk(100)
				.reader(new RepositoryReader(project, projectService))
				.processor(new RepositoryProcessor(project.getType()))
				.writer(new RepositoryWriter(commitService, commitIssueService))
				.build();
		
		
		CommitProcessor commitProcessor = new CommitProcessor(project.getType(), project.getIssueTrackerUrlPattern());
		if(project.getType() == Project.Type.GITHUB) {
			GitHubAPI github = new GitHubAPI(project.getIssueTrackerUrlPattern());
			commitProcessor.setGitHubApi(github);
		}
		
		Step step2 = stepBuilderFactory.get(jobName+"_getIssueInformationForEachCommit")
				.<CommitIssue, CommitIssue> chunk(50)
				.reader(new CommitReader2(project, entityManagerFactory))
				.processor(commitProcessor)
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
			try {
				jobRegistry.register(new ReferenceJobFactory(builder));
			} catch (DuplicateJobException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void removeMetricsJob(Project project) {
		String jobName = project.getId().toString() + "_" + project.getName() + "_changemetrics";
		jobRegistry.unregister(jobName);
		
		jobName = project.getId().toString() + "_" + project.getName() + "_szz";
		jobRegistry.unregister(jobName);
		
		jobName = project.getId().toString() + "_" + project.getName() + "_sourcemetrics";
		jobRegistry.unregister(jobName);
	}
	
	public void createMetricsJob(Project project) {
		createChangeMetricsJob(project);
		createSZZJob(project);
		createSourceMetricsJob(project);
	}
	
	private void createChangeMetricsJob(Project project) {
		String jobName = project.getId().toString() + "_" + project.getName() + "_changemetrics";
		
		Tasklet tasklet = new ChangeMetricsTasklet(project, changeMetricsService, commitService, commitIssueService);
		
		Step step = stepBuilderFactory.get(jobName+"_changeMetrics")
				.tasklet(tasklet)
				.build();

		Job builder = jobBuilderFactory.get(jobName)
				.incrementer(new RunIdIncrementer())
				.start(step)
				.build();
		
		try {
			jobRegistry.getJob(jobName);
		} catch (NoSuchJobException e) {
			try {
				jobRegistry.register(new ReferenceJobFactory(builder));
			} catch (DuplicateJobException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void createSZZJob(Project project) {
		String jobName = project.getId().toString() + "_" + project.getName() + "_szz";
		
		Tasklet tasklet = new SZZTasklet(project, commitIssueService, commitService, szzMetricService);
		
		Step step = stepBuilderFactory.get(jobName+"_szz")
				.tasklet(tasklet)
				.build();
		
		Job builder = jobBuilderFactory.get(jobName)
				.incrementer(new RunIdIncrementer())
				.start(step)
				.build();
		
		try {
			jobRegistry.getJob(jobName);
		} catch (NoSuchJobException e) {
			try {
				jobRegistry.register(new ReferenceJobFactory(builder));
			} catch (DuplicateJobException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private void createSourceMetricsJob(Project project) {
		String jobName = project.getId().toString() + "_" + project.getName() + "_sourcemetrics";
		
		Tasklet tasklet = new SourceMetricsTasklet(project, commitService, sourceMetricService);
		
		Step step = stepBuilderFactory.get(jobName+"_sourcemetrics")
				.tasklet(tasklet)
				.build();
		
		Job builder = jobBuilderFactory.get(jobName)
				.incrementer(new RunIdIncrementer())
				.start(step)
				.build();
		
		try {
			jobRegistry.getJob(jobName);
		} catch (NoSuchJobException e) {
			try {
				jobRegistry.register(new ReferenceJobFactory(builder));
			} catch (DuplicateJobException e1) {
				e1.printStackTrace();
			}
		}
	}
}
