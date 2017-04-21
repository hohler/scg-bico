package tool.bico.job;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.repodriller.filter.range.CommitRange;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import ch.unibe.scg.metrics.changemetrics.ChangeMetrics;
import ch.unibe.scg.metrics.changemetrics.domain.CMFile;
import ch.unibe.scg.metrics.changemetrics.domain.CMRepository;
import ch.unibe.scg.metrics.szz.SZZ;
import ch.unibe.scg.metrics.szz.domain.SZZBugRepository;
import ch.unibe.scg.metrics.szz.domain.SZZCommit;
import ch.unibe.scg.metrics.szz.domain.SZZFile;
import ch.unibe.scg.metrics.szz.domain.SZZRepository;
import tool.bico.model.ChangeMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;
import tool.bico.model.service.ChangeMetricService;
import tool.bico.model.service.CommitService;
import tool.bico.repository.GitRepository;

public class SZZTasklet implements Tasklet {
	
	//private ChangeMetricService changeMetricsService;
	private CommitService commitService;
	private Project project;
	private String path;
	
	public SZZTasklet(Project project, ChangeMetricService changeMetricsService, CommitService commitService) {
		this.project = project;
		//this.changeMetricsService = changeMetricsService;
		this.commitService = commitService;
	}

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		GitRepository repo = new GitRepository(project);
		path = repo.getRepositoryPath();
		if(path == null) System.err.println("Could not clone repository of project: "+project);
		
		//changeMetricsService.removeAllByProject(project);
		//changeMetricsService.flush();
		
		//ChangeMetrics cm = new ChangeMetrics(Paths.get(path));
		SZZ szz = new SZZ(Paths.get(path));
		
		szz.setThreads(20);
		
		//if(project.getChangeMetricEveryCommits() != 0) cm.setEveryNthCommit(project.getChangeMetricEveryCommits());
		
		/*if(project.getChangeMetricStartDate() != null && project.getChangeMetricEndDate() != null) {
			Calendar start = Calendar.getInstance();
			start.setTime(project.getChangeMetricStartDate());
			Calendar end = Calendar.getInstance();
			end.setTime(project.getChangeMetricEndDate());
			
			cm.setRange(start, end);
		}*/
		
		SZZBugRepository bugRepo = new SZZBugRepository();
        
        String[] commits = {
	        "96a4c30f29e1e66f9a5351ec1130eda6789ea7c9",
	        //"a6726ddd15cd048cec1765500675e2aa9a5432d2",
	        "b2928f282707e5af03a91ff7cc237496223799ee",
	        //"34e52bafc4c91abf45b75f8c688058e23f956740"
        };
        
        //bugRepo.setBugCommits(new HashSet<String>(Arrays.asList(commits)));
        
        //szz.setBugRepository(bugRepo);
        
        SZZRepository results = szz.analyze();
        
        for(SZZFile f : results.all()) {
        	contribution.incrementReadCount();
        	for(SZZCommit c : f.getCommits()) {
        		//System.out.println(c);	
        		contribution.incrementWriteCount(1);
        	}
        	
        	chunkContext.getStepContext().getStepExecution().incrementCommitCount();
        }
		  
        return RepeatStatus.FINISHED;
		
	}
}