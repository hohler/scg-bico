package tool.bico.analysis;

import tool.bico.model.Commit;
import tool.bico.model.CommitFile;
import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.model.service.CommitService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

public class CommitAnalyzer {
	
	//@Autowired
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
	public CommitAnalyzer(Project project, CommitService commitService, Set<CommitIssue.Type> typeSet) {
		commits = project.getCommits();
		this.commitService = commitService; 
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
		for(CommitIssue i : toAnalyze) {
			for(Commit c : i.getCommits()) {
				//System.out.println(""+count+": "+c.getRef());
				ResultsContainer rc = typeResults.get(i.getType());
				
				Set<CommitFile> files = c.getFiles();			
				Set<ResultsContainer.FileHolder> fileList = new HashSet<>();
				for(CommitFile f : files) {
					if(f.isSrc()) {
						fileList.add(rc.new FileHolder(f.getChangeType(), f.getAdditions(), f.getDeletions()));
					}
				}
				
				rc.addResult(new Long(c.getId()), c.getFiles().size(), c.getAdditions(), c.getDeletions(), fileList);
			}
		}
	}
	
	public Map<CommitIssue.Type, ResultsContainer> getTypeResults() {
		return typeResults;
	}
	
	public Map<CommitIssue.Type, Map<String, Integer>> getThresholds() {
		// init results
		Map<CommitIssue.Type, Map<String, Integer>> results = new HashMap<>();
		for(CommitIssue.Type type : typeSet) {
			results.put(type, new HashMap<>());
		}
		
		for(Map.Entry<CommitIssue.Type, ResultsContainer> e : typeResults.entrySet()) {
			
			
			Map<String, Integer> thresholds = results.get(e.getKey());
			
			ResultsContainer r = e.getValue();
			
			int firstQuartileFilesChanged = r.getFirstQuartileFilesChanged();
			int thirdQuartileFilesChanged = r.getThirdQuartileFilesChanged();
			int filesChangedThreshold = thirdQuartileFilesChanged + (3*(thirdQuartileFilesChanged - firstQuartileFilesChanged));
			
			int firstQuartileAdditions = r.getFirstQuartileAdditions();
			int thirdQuartileAdditions = r.getThirdQuartileAdditions();
			int additionsThreshold = thirdQuartileAdditions + (3*(thirdQuartileAdditions - firstQuartileAdditions));
			
			thresholds.put("firstQuartileFilesChanged", firstQuartileFilesChanged);
			thresholds.put("thirdQuartileFilesChanged", thirdQuartileFilesChanged);
			thresholds.put("filesChangedThreshold", filesChangedThreshold);
			
			thresholds.put("firstQuartileAdditions",  firstQuartileAdditions);
			thresholds.put("thirdQuartileAdditions", thirdQuartileAdditions);
			thresholds.put("additionsThreshold", additionsThreshold);
			
		}
		return results;
	
	}

	public Map<CommitIssue.Type, List<Commit>> getPossibleBigCommits() {
		// init results
		HashMap<CommitIssue.Type, List<Commit>> results = new HashMap<>();
		for(CommitIssue.Type type : typeSet) {
			results.put(type, new ArrayList<>());
		}
		
		Map<CommitIssue.Type, Map<String, Integer>> thresholds = this.getThresholds(); 
		
		for(Map.Entry<CommitIssue.Type, ResultsContainer> e : typeResults.entrySet()) {
			
			List<Commit> resultList = results.get(e.getKey());
			Map<String, Integer> threshold = thresholds.get(e.getKey());
			
			ResultsContainer r = e.getValue();
			
			if(r == null) continue;
			
			int filesChangedThreshold = threshold.get("filesChangedThreshold") == null ? 0 : threshold.get("filesChangedThreshold");
			int additionsThreshold = threshold.get("additionsThreshold") == null ? 0 : threshold.get("additionsThreshold");

			for(ResultsContainer.ResultHolder h : r.getResults()) {
				if(h == null) continue;
				if(h.getFilesChanged() > filesChangedThreshold || h.getAdditions() > additionsThreshold) {
					Commit c = commitService.findById(h.getCommitId());
					resultList.add(c);
				}
			}
		}
		
		return results;
	}	
	
	public void setCommitService(CommitService commitService) {
		this.commitService = commitService;
	}
}
