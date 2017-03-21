package tool.bico.job;

import java.nio.file.Paths;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import ch.unibe.scg.metrics.changemetrics.ChangeMetrics;
import ch.unibe.scg.metrics.changemetrics.domain.CMFile;
import ch.unibe.scg.metrics.changemetrics.domain.CMRepository;
import tool.bico.model.Project;
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
		
		/*List result=new ArrayList();
		JdbcTemplate myJDBC=new JdbcTemplate(getDataSource());		
        result = myJDBC.query(sql, new PersonMapper());
        System.out.println("Number of records effected: "+ result);*/
		
		ChangeMetrics cm = new ChangeMetrics(Paths.get("C:/eclipse/target/repositories/flume"));
		CMRepository results = cm.analyze();
		
		for(CMFile f : results.all()) {
			System.out.println(f);
		}
		
		// Write to DB!
		
        return RepeatStatus.FINISHED;
		
	}
}