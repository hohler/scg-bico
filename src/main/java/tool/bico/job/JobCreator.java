package tool.bico.job;

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

import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.model.service.CommitIssueService;
import tool.bico.model.service.CommitService;
import tool.bico.model.service.ProjectService;
import tool.bico.processor.CommitProcessor;
import tool.bico.processor.RepositoryProcessor;
import tool.bico.reader.CommitReader;
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
	
	private TaskExecutor issueTaskExecutor() {
		SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
		executor.setConcurrencyLimit(100);
		return executor;
	}
	
	public JobCreator() {
		System.err.println("JobCreator initialized!");
	}
	
	public void removeJob(Project project) {
		String jobName = project.getId().toString() + "_" + project.getName();
		jobRegistry.unregister(jobName);
	}
	
	public void removeJob(Long id, String name) {
		String jobName = id.toString() + "_" + name;
		jobRegistry.unregister(jobName);
	}
	
	public void createJob(Project project) {
		String jobName = project.getId().toString() + "_" + project.getName();
		
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
				.reader(new CommitReader(project, commitIssueService))
				.processor(commitProcessor)
				.writer(new CommitWriter(commitService, commitIssueService))
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
}
