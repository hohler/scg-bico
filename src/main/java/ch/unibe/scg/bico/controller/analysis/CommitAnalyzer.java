package ch.unibe.scg.bico.controller.analysis;

import ch.unibe.scg.bico.model.Project;
import ch.unibe.scg.bico.model.service.CommitService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.model.CommitFile;
import ch.unibe.scg.bico.model.CommitIssue;

public class CommitAnalyzer {
	
	@Autowired
	private CommitService commitService;

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
			
			rc.addResult(new Long(c.getId()), c.getFiles().size(), c.getAdditions(), c.getDeletions(), fileList);
		}
	}
	
	public Map<CommitIssue.Type, ResultsContainer> getTypeResults() {
		return typeResults;
	}
	
	public Map<CommitIssue.Type, List<Commit>> getPossibleBigCommits() {
		// init results
		HashMap<CommitIssue.Type, List<Commit>> results = new HashMap<>();
		for(CommitIssue.Type type : typeSet) {
			results.put(type, new ArrayList<>());
		}
		
		
		for(Map.Entry<CommitIssue.Type, ResultsContainer> e : typeResults.entrySet()) {
			
			List<Commit> resultList = results.get(e.getKey()); 
			
			ResultsContainer r = e.getValue();
			int medianFilesChanged = r.getMedianFilesChanged();
			int medianFilesChangedThreshold = medianFilesChanged * 5;
			
			int medianAdditions = r.getMedianAdditions();
			int medianAdditionsThreshold = medianAdditions * 5;
			
			for(ResultsContainer.ResultHolder h : r.getResults()) {
				if(h.getFilesChanged() > medianFilesChangedThreshold || h.getAdditions() > medianAdditionsThreshold) {
					Commit c = commitService.findById(h.getCommitId());
					resultList.add(c);
				}
			}
		}
		
		return results;
	}
}
