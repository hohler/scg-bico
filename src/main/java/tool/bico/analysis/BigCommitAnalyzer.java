package tool.bico.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import tool.bico.model.BigCommit;
import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.CommitIssue.Type;
import tool.bico.model.Project;
import tool.bico.model.service.CommitService;
import tool.bico.model.service.ProjectService;

@Deprecated
public class BigCommitAnalyzer {

	
	public static Map<Type, Map<String, Integer>> analyzeBigCommits(Project project, ProjectService projectService, CommitService commitService) {
		
		List<CommitIssue.Type> typeSet = new ArrayList<>();
	
		// init type set to analyze
		typeSet.add(CommitIssue.Type.BUG);
		typeSet.add(CommitIssue.Type.FEATURE);
		typeSet.add(CommitIssue.Type.IMPROVEMENT);
		typeSet.add(CommitIssue.Type.REFACTOR);
		typeSet.add(CommitIssue.Type.DOCUMENTATION);
	
		CommitAnalyzer ca = new CommitAnalyzer(project, commitService, new HashSet<CommitIssue.Type>(typeSet));
		ca.setCommitService(commitService);
		ca.load();
		ca.analyze();
		
		List<String> commitRefs = new ArrayList<>();
		for(BigCommit b : project.getBigCommits()) {
			commitRefs.add(b.getCommit().getRef());
		}
		
		List<BigCommit> toAdd = new ArrayList<>();
		
		for(Entry<CommitIssue.Type, List<Commit>> e : ca.getPossibleBigCommits().entrySet()) {
			for(Commit c : e.getValue()) {
				if(!commitRefs.contains(c.getRef())) {
					BigCommit b = new BigCommit();
					b.setCommit(c);
					b.setIssueType(e.getKey());
					toAdd.add(b);
				}
			}
		}
		
		project.addBigCommits(toAdd);
		projectService.update(project);
		
		return ca.getThresholds();
	}
}
