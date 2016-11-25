package ch.unibe.scg.bico.controller.analysis;

import ch.unibe.scg.bico.model.Project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.model.CommitFile;
import ch.unibe.scg.bico.model.CommitIssue;

public class CommitAnalyzer {

	private Set<Commit> commits;
	
	private Set<CommitIssue.Type> typeSet;
	
	private Set<CommitIssue> toAnalyze;
	
	private Map<CommitIssue.Type, ResultsContainer> typeResults;
	/**
	 * 
	 * @param project
	 * @param typeSet types to analyze
	 */
	public CommitAnalyzer(Project project, Set<CommitIssue.Type> typeSet) {
		commits = project.getCommits();
		this.typeSet = typeSet;
		typeResults = new HashMap<>();
		for(CommitIssue.Type type : typeSet) {
			typeResults.put(type, new ResultsContainer(type));
		}
	}
	
	
	public void load() {
		toAnalyze = new HashSet<>();
		
		for(Commit c : commits) {
			for(CommitIssue i : c.getCommitIssues()) {
				if(typeSet.contains(i.getType())) {
					toAnalyze.add(i);
				}
			}
		}		
	}
	
	public void analyze() {
		// per type:
			// how much files changed in a commit
			// how much additions / deletions per file
			//
		
		for(CommitIssue i : toAnalyze) {
			Commit c = i.getCommit();
			ResultsContainer rc = typeResults.get(i.getType());
			
			Set<CommitFile> files = c.getFiles();			
			Set<ResultsContainer.FileHolder> fileList = new HashSet<>();
			for(CommitFile f : files) {
				fileList.add(rc.new FileHolder(f.getChangeType(), f.getAdditions(), f.getDeletions()));
			}
			
			rc.addResult(c.getId(), c.getFiles().size(), c.getAdditions(), c.getDeletions(), fileList);
		}
	}
	
	public Map<CommitIssue.Type, ResultsContainer> getTypeResults() {
		return typeResults;
	}
}
