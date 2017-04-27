package tool.bico.job;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.repodriller.filter.range.CommitRange;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import ch.unibe.scg.metrics.changemetrics.ChangeMetrics;
import ch.unibe.scg.metrics.changemetrics.domain.CMBugRepository;
import ch.unibe.scg.metrics.changemetrics.domain.CMFile;
import ch.unibe.scg.metrics.changemetrics.domain.CMRepository;
import tool.bico.model.ChangeMetric;
import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.model.service.ChangeMetricService;
import tool.bico.model.service.CommitIssueService;
import tool.bico.model.service.CommitService;
import tool.bico.repository.GitRepository;

public class ChangeMetricsTasklet implements Tasklet {
	
	private ChangeMetricService changeMetricsService;
	private CommitService commitService;
	private CommitIssueService commitIssueService;
	private Project project;
	private String path;
	
	public ChangeMetricsTasklet(Project project, ChangeMetricService changeMetricsService, CommitService commitService, CommitIssueService commitIssueService) {
		this.project = project;
		this.changeMetricsService = changeMetricsService;
		this.commitIssueService = commitIssueService;
		this.commitService = commitService;
	}

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		GitRepository repo = new GitRepository(project);
		path = repo.getRepositoryPath();
		if(path == null) System.err.println("Could not clone repository of project: "+project);
		
		changeMetricsService.removeAllByProject(project);
		changeMetricsService.flush();
		
		ChangeMetrics cm = new ChangeMetrics(Paths.get(path));
		
		cm.setThreads(20);
		if(project.getChangeMetricEveryCommits() != 0) cm.setEveryNthCommit(project.getChangeMetricEveryCommits());
		
		/*if(project.getChangeMetricStartDate() != null && project.getChangeMetricEndDate() != null) {
			Calendar start = Calendar.getInstance();
			start.setTime(project.getChangeMetricStartDate());
			Calendar end = Calendar.getInstance();
			end.setTime(project.getChangeMetricEndDate());
			
			cm.setRange(start, end);
		}*/
		
		
		// get all "BUG"-FIX issues and add them to the bug repository
		CMBugRepository bugRepo = new CMBugRepository();
        
		List<CommitIssue> issues = commitIssueService.findAllByProjectAndType(project, CommitIssue.Type.BUG);
		
		Set<String> commits = new HashSet<>();
		for(CommitIssue i : issues) {
			for(Commit c : i.getCommits()) {
				commits.add(c.getRef());
			}
		}
		
		
		if(commits.size() == 0) commits = null;
		bugRepo.setBugCommits(commits);
        cm.setBugRepository(bugRepo);
		
		Map<String, CommitRange> list = cm.generateCommitListWithWeeks(project.getChangeMetricTimeWindow());
        // cm.generateCommitList();
        int i = 0;
        int w = 0;
        for(Entry<String, CommitRange> e : list.entrySet()) {
        	i++;
        	
        	contribution.incrementReadCount();
        	
        	//chunkContext.getStepContext().getStepExecution().setReadCount(i);
        	
        	
        	String ref = e.getKey();
        	
        	cm.setRange(e.getValue());
        	CMRepository results = cm.analyze();
        	
        	//logger.debug(repo.all());
        	
        	Commit commit = commitService.getCommitByProjectAndRef(project, ref);
        	
        	if(commit == null) {
        		System.err.println("commit is NULL: " + ref);
        		continue;
        	}
        	else System.err.println("cm-commit: "+commit.getRef());
		
        	List<ChangeMetric> toPersist = new ArrayList<>();
        	
			for(CMFile f : results.all()) {
				w++;
				ChangeMetric c = new ChangeMetric(f);
				c.setCommit(commit);
				toPersist.add(c);
				contribution.incrementWriteCount(1);
				//chunkContext.getStepContext().getStepExecution().setWriteCount(w);
			}
			
			changeMetricsService.addAll(toPersist);
			
			chunkContext.getStepContext().getStepExecution().incrementCommitCount();
        }
		
		//cm.setTimeWindow(project.getChangeMetricTimeWindow());
		
		// OLD ONE
		/*if(project.getChangeMetricEveryCommits() != 0) cm.setEveryNthCommit(project.getChangeMetricEveryCommits());
		
		cm.generateCommitList();
		String firstRef = cm.getFirstRef();
		
		List<String> commits = cm.getCommitList();
		
		for(String ref : commits) {
			
        	cm.setRange(firstRef, ref);
        	CMRepository results = cm.analyze();
        	
        	Commit commit = commitService.getCommitByRef(ref);
        	
        	if(commit == null) {
        		System.err.println("commit is NULL: " + ref);
        		continue;
        	}
        	else System.err.println("cm-commit: "+commit.getRef());
		
        	List<ChangeMetric> toPersist = new ArrayList<>();
        	
			for(CMFile f : results.all()) {
				ChangeMetric c = new ChangeMetric(f);
				c.setCommit(commit);
				toPersist.add(c);
			}
			
			changeMetricsService.addAll(toPersist);
		}*/
		
        return RepeatStatus.FINISHED;
		
	}
}