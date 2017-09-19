package tool.bico.job;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import tool.bico.analysis.CommitThreadedAnalyzer;
import tool.bico.analysis.ResultsContainer;
import tool.bico.model.BigCommit;
import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.CommitIssue.Type;
import tool.bico.model.CommitIssueAnalysis;
import tool.bico.model.Project;
import tool.bico.model.service.BigCommitService;
import tool.bico.model.service.CommitIssueAnalysisService;
import tool.bico.model.service.CommitService;

public class AnalysisTasklet implements Tasklet {
	
	private CommitIssueAnalysisService ciaService;
	private CommitService commitService;
	private BigCommitService bigCommitService;
	private Project project;
	
	private List<CommitIssue.Type> typeSet = new ArrayList<>();
	
	public AnalysisTasklet(Project project, CommitService commitService, BigCommitService bigCommitService, CommitIssueAnalysisService ciaService) {
		this.project = project;
		this.commitService = commitService;
		this.ciaService = ciaService;
		this.bigCommitService = bigCommitService;
		
		// init type set to analyze
		typeSet.add(CommitIssue.Type.BUG);
		typeSet.add(CommitIssue.Type.FEATURE);
		typeSet.add(CommitIssue.Type.IMPROVEMENT);
		typeSet.add(CommitIssue.Type.REFACTOR);
		typeSet.add(CommitIssue.Type.DOCUMENTATION);
		typeSet.add(CommitIssue.Type.NA);
	}

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		// delete all big commits
		// delete all analysis
		bigCommitService.removeAllByProject(project);
		project.removeAllBigCommits();
		
		ciaService.removeAllByProject(project);
		
		CommitThreadedAnalyzer ca = new CommitThreadedAnalyzer(project, commitService, new HashSet<CommitIssue.Type>(typeSet));
		ca.load();
		ca.analyzeThreaded();
		
		
		Map<CommitIssue.Type, ResultsContainer> results = ca.getTypeResults();
		
		for(Entry<Type, ResultsContainer> e : results.entrySet()) {
			CommitIssueAnalysis cia = new CommitIssueAnalysis(project, e.getKey(), e.getValue());
			ciaService.add(cia);
		}
		
		List<BigCommit> toAdd = new ArrayList<>();
		
		for(Entry<CommitIssue.Type, List<Commit>> e : ca.getPossibleBigCommits().entrySet()) {
			for(Commit c : e.getValue()) {
				BigCommit b = new BigCommit();
				b.setCommit(c);
				b.setIssueType(e.getKey());
				toAdd.add(b);
			}
		}
		
		project.addBigCommits(toAdd);
		bigCommitService.addAll(toAdd);
		
		  
        return RepeatStatus.FINISHED;
		
	}
}