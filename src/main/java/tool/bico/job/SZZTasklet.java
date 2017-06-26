package tool.bico.job;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import ch.unibe.scg.metrics.szz.SZZ;
import ch.unibe.scg.metrics.szz.domain.SZZBugRepository;
import ch.unibe.scg.metrics.szz.domain.SZZCommit;
import ch.unibe.scg.metrics.szz.domain.SZZFile;
import ch.unibe.scg.metrics.szz.domain.SZZRepository;
import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.model.SzzMetric;
import tool.bico.model.service.CommitIssueService;
import tool.bico.model.service.CommitService;
import tool.bico.model.service.SzzMetricService;
import tool.bico.repository.GitRepository;

public class SZZTasklet implements Tasklet {
	
	private CommitIssueService commitIssueService;
	private CommitService commitService;
	private SzzMetricService szzMetricService;
	private Project project;
	private String path;
	
	public SZZTasklet(Project project, CommitIssueService commitIssueService, CommitService commitService, SzzMetricService szzMetricService) {
		this.project = project;
		this.commitIssueService = commitIssueService;
		this.commitService = commitService;
		this.szzMetricService = szzMetricService;
	}

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		System.setProperty("git.maxdiff", "1000000"); // default was 100'000
		
		GitRepository repo = new GitRepository(project, false);
		path = repo.getRepositoryPath();
		if(path == null) System.err.println("Could not clone repository of project: "+project);
		
		//changeMetricsService.removeAllByProject(project);
		//changeMetricsService.flush();
		
		szzMetricService.removeAllByProject(project);
		szzMetricService.flush();
		
		//ChangeMetrics cm = new ChangeMetrics(Paths.get(path));
		SZZ szz = new SZZ(Paths.get(path));
		
		//szz.setThreads(20);
		
		//if(project.getChangeMetricEveryCommits() != 0) cm.setEveryNthCommit(project.getChangeMetricEveryCommits());
		
		/*if(project.getChangeMetricStartDate() != null && project.getChangeMetricEndDate() != null) {
			Calendar start = Calendar.getInstance();
			start.setTime(project.getChangeMetricStartDate());
			Calendar end = Calendar.getInstance();
			end.setTime(project.getChangeMetricEndDate());
			
			cm.setRange(start, end);
		}*/
		
		
		SZZBugRepository bugRepo = new SZZBugRepository();
        
		List<CommitIssue> issues = commitIssueService.findAllByProjectAndType(project, CommitIssue.Type.BUG);
		
		Set<String> commits = new HashSet<>();
		for(CommitIssue i : issues) {
			for(Commit c : i.getCommits()) {
				commits.add(c.getRef());
			}
		}
		
		
		if(commits.size() == 0) commits = null;
        /*String[] commits = {
	        "96a4c30f29e1e66f9a5351ec1130eda6789ea7c9",
	        //"a6726ddd15cd048cec1765500675e2aa9a5432d2",
	        "b2928f282707e5af03a91ff7cc237496223799ee",
	        //"34e52bafc4c91abf45b75f8c688058e23f956740"
        };*/
        
        //bugRepo.setBugCommits(new HashSet<String>(Arrays.asList(commits)));
		bugRepo.setBugCommits(commits);
        szz.setBugRepository(bugRepo);
        
        if(project.getSzzMetricsExcludeBigCommits()) szz.excludeCommits( project.getBigCommits().stream().map(b -> b.getCommit().getRef()).collect(Collectors.toList()) );
        
        szz.generateCommitList();
        SZZRepository results = szz.analyze(szz.getCommitList());
        
        for(SZZFile f : results.all()) {
        	
        	List<SzzMetric> toPersist = new ArrayList<>();
        	
        	contribution.incrementReadCount();
        	for(SZZCommit c : f.getCommits()) {
        		//System.out.println(c);
        		Commit commit = commitService.getCommitByProjectAndRef(project, c.getHash());
        		SzzMetric sz = new SzzMetric();
        		sz.setFile(f.getFile());
        		sz.setBugs(c.getBugs());
        		sz.setCommit(commit);
        		sz.setBugfix(c.isBugfix());
        		sz.setDeleted(c.isDeleted());
        		
        		toPersist.add(sz);
        		
        		contribution.incrementWriteCount(1);
        	}
        	
        	szzMetricService.addAll(toPersist);
        	
        	chunkContext.getStepContext().getStepExecution().incrementCommitCount();
        }
		  
        return RepeatStatus.FINISHED;
		
	}
}