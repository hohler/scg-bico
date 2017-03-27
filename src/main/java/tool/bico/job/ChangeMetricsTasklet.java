package tool.bico.job;

import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import ch.unibe.scg.metrics.changemetrics.ChangeMetrics;
import ch.unibe.scg.metrics.changemetrics.domain.CMFile;
import ch.unibe.scg.metrics.changemetrics.domain.CMRepository;
import tool.bico.model.ChangeMetric;
import tool.bico.model.Project;
import tool.bico.model.Commit;
import tool.bico.model.service.ChangeMetricService;
import tool.bico.model.service.CommitService;
import tool.bico.repository.GitRepository;

public class ChangeMetricsTasklet implements Tasklet {
	
	private ChangeMetricService changeMetricsService;
	private CommitService commitService;
	private Project project;
	private String path;
	
	public ChangeMetricsTasklet(Project project, ChangeMetricService changeMetricsService, CommitService commitService) {
		this.project = project;
		this.changeMetricsService = changeMetricsService;
		this.commitService = commitService;
	}

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		GitRepository repo = new GitRepository(project);
		path = repo.getRepositoryPath();
		if(path == null) System.err.println("Could not clone repository of project: "+project);
		
		changeMetricsService.removeAllByProject(project);
		commitService.flush();
		
		ChangeMetrics cm = new ChangeMetrics(Paths.get(path));
		if(project.getChangeMetricStartDate() != null && project.getChangeMetricEndDate() != null) {
			Calendar start = Calendar.getInstance();
			start.setTime(project.getChangeMetricStartDate());
			Calendar end = Calendar.getInstance();
			end.setTime(project.getChangeMetricEndDate());
			
			cm.setRange(start, end);
		}
		
		if(project.getChangeMetricEveryCommits() != 0) cm.setEveryNthCommit(project.getChangeMetricEveryCommits());
		
		cm.generateCommitList();
		String firstRef = cm.getFirstRef();
		
		List<String> commits = cm.getCommitList();
		
		for(String ref : commits) {
        	cm.setRange(firstRef, ref);
        	CMRepository results = cm.analyze();
        	
        	Commit commit = commitService.getCommitByRef(ref);
        	
        	if(commit == null) System.err.println("commit is NULL");
		
		//CMRepository results = cm.analyze();
		//Commit commit = project.getCommits().stream().findFirst().get();
		
		
			for(CMFile f : results.all()) {
				//System.out.println(f);
				
				ChangeMetric c = new ChangeMetric(f);
				c.setCommit(commit);
				changeMetricsService.add(c);
				System.out.println(c);
			}
		}
		
        return RepeatStatus.FINISHED;
		
	}
}