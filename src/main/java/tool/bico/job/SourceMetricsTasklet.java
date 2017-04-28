package tool.bico.job;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import ch.unibe.scg.metrics.sourcemetrics.SourceMetrics;
import ch.unibe.scg.metrics.sourcemetrics.domain.SMCommit;
import ch.unibe.scg.metrics.sourcemetrics.domain.SMFile;
import ch.unibe.scg.metrics.sourcemetrics.domain.SMRepository;
import tool.bico.model.Commit;
import tool.bico.model.Project;
import tool.bico.model.SourceMetric;
import tool.bico.model.service.CommitIssueService;
import tool.bico.model.service.CommitService;
import tool.bico.model.service.SourceMetricService;
import tool.bico.repository.GitRepository;

public class SourceMetricsTasklet implements Tasklet {
	
	private CommitService commitService;
	private SourceMetricService sourceMetricService;
	private Project project;
	private String path;
	
	public SourceMetricsTasklet(Project project, CommitService commitService, SourceMetricService sourceMetricService) {
		this.project = project;
		this.commitService = commitService;
		this.sourceMetricService = sourceMetricService;
	}

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		GitRepository repo = new GitRepository(project, false);
		path = repo.getRepositoryPath().replace("\\.git",  "").replace("/.git", "");
		if(path == null) System.err.println("Could not clone repository of project: "+project);
		
		sourceMetricService.removeAllByProject(project);
		sourceMetricService.flush();
		
		SourceMetrics sourceMetrics = new SourceMetrics(Paths.get(path));
		
		sourceMetrics.setEveryNthCommit(project.getSourceMetricEveryCommits());
		sourceMetrics.generateCommitList();
        
        SMRepository results = sourceMetrics.analyze(sourceMetrics.getCommitList());
        
        for(SMCommit c : results.all()) {
        	
        	List<SourceMetric> toPersist = new ArrayList<>();
        	
        	contribution.incrementReadCount();
        	
        	Commit commit = commitService.getCommitByProjectAndRef(project, c.getHash());
        	
        	for(SMFile f : c.getFiles().values()) {

        		SourceMetric sm = new SourceMetric(f);
        		sm.setCommit(commit);
        		        		
        		toPersist.add(sm);
        		
        		contribution.incrementWriteCount(1);
        	}
        	
        	sourceMetricService.addAll(toPersist);
        	
        	chunkContext.getStepContext().getStepExecution().incrementCommitCount();
        }
		  
        return RepeatStatus.FINISHED;
		
	}
}