package ch.unibe.scg.bico.job;

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

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.model.Project;
import ch.unibe.scg.bico.model.service.CommitIssueService;
import ch.unibe.scg.bico.model.service.CommitService;
import ch.unibe.scg.bico.model.service.ProjectService;
import ch.unibe.scg.bico.processor.CommitProcessor;
import ch.unibe.scg.bico.processor.RepositoryProcessor;
import ch.unibe.scg.bico.reader.CommitReader;
import ch.unibe.scg.bico.reader.RepositoryReader;
import ch.unibe.scg.bico.repository.GitHubAPI;
import ch.unibe.scg.bico.writer.CommitWriter;
import ch.unibe.scg.bico.writer.RepositoryWriter;

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
				.writer(new RepositoryWriter(commitService))
				//.listener(promotionListener())
				.build();
		
		
		CommitProcessor commitProcessor = new CommitProcessor(project.getType(), project.getIssueTrackerUrlPattern());
		if(project.getType() == Project.Type.GITHUB) {
			GitHubAPI github = new GitHubAPI(project.getIssueTrackerUrlPattern());
			commitProcessor.setGitHubApi(github);
		}
		
		Step step2 = stepBuilderFactory.get(jobName+"_getIssueInformationForEachCommit")
				.<Commit, CommitIssue> chunk(50)
				.reader(new CommitReader(project))
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
