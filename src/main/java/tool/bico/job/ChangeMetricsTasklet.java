package tool.bico.job;

import java.nio.file.Paths;
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
import tool.bico.repository.GitRepository;

public class ChangeMetricsTasklet implements Tasklet {
	
	private ChangeMetricService changeMetricsService;
	private Project project;
	private String path;
	
	public ChangeMetricsTasklet(Project project, ChangeMetricService changeMetricsService) {
		this.project = project;
		this.changeMetricsService = changeMetricsService;
		
		GitRepository repo = new GitRepository(project);
		path = repo.getRepositoryPath();
		if(path == null) System.err.println("Could not clone repository of project: "+project);
	}

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		ChangeMetrics cm = new ChangeMetrics(Paths.get("C:/eclipse/target/repositories/flume"));
		CMRepository results = cm.analyze();
		Commit commit = project.getCommits().stream().findFirst().get(); 
		
		changeMetricsService.removeAllByCommit(commit);
		
		for(CMFile f : results.all()) {
			System.out.println(f);
			
			ChangeMetric c = new ChangeMetric(f);
			try {
				c.setCommit(commit);
			} catch(NoSuchElementException e) {
				// failed to get first commit of project
			}
			changeMetricsService.add(c);
		}
		
        return RepeatStatus.FINISHED;
		
	}
}