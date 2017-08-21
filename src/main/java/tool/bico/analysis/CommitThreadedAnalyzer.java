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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class CommitThreadedAnalyzer {
	
	private static final int NUMBER_OF_THREADS = 25;

	//@Autowired
	private CommitService commitService;

	private Set<Commit> commits;
	
	private Set<CommitIssue.Type> typeSet;
	
	private Set<Commit> commitsToAnalyze;
	
	private Map<CommitIssue.Type, ResultsContainer> typeResults;
	
	/**
	 * 
	 * @param project
	 * @param typeSet types to analyze
	 */
	public CommitThreadedAnalyzer(Project project, CommitService commitService, Set<CommitIssue.Type> typeSet) {
		commits = project.getCommits();
		this.commitService = commitService; 
		this.typeSet = typeSet;
		typeResults = new HashMap<>();
		for(CommitIssue.Type type : typeSet) {
			typeResults.put(type, new ResultsContainer(type));
		}
		
	}
	
	
	public void load() {
		commitsToAnalyze = new HashSet<>();
		for(Commit c : commits) {
			for(CommitIssue i : c.getCommitIssues()) {
				if(typeSet.contains(i.getType())) {
					commitsToAnalyze.add(c);
				}
			}
		}		
	}
	
	public void analyzeThreaded() {
		ExecutorService exec = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		List<FutureTask<List<Result>>> taskList = new ArrayList<FutureTask<List<Result>>>();
		
		List<Commit> list = new ArrayList<>(commitsToAnalyze);
		
		int numberOfItems = list.size();
		
		int minItemsPerThread = numberOfItems / NUMBER_OF_THREADS;
	    int maxItemsPerThread = minItemsPerThread + 1;
	    int threadsWithMaxItems = numberOfItems - NUMBER_OF_THREADS * minItemsPerThread;
	    
	    
	    int start = 0;
	    
	    for (int i = 0; i < NUMBER_OF_THREADS; i++) {
	        int itemsCount = (i < threadsWithMaxItems ? maxItemsPerThread : minItemsPerThread);
	        int end = start + itemsCount;
	        
	        FutureTask<List<Result>> futureTask = new FutureTask<List<Result>>(new CallableAnalyzer(list.subList(start, end), typeSet));
			taskList.add(futureTask);
			exec.execute(futureTask);
			
	        start = end;
	    }
		
		// Wait until all results are available and combine them at the same time
        for (FutureTask<List<Result>> futureTask : taskList) {
        	try {
				List<Result> results = futureTask.get();
				for(Result r : results) {
					
					ResultsContainer rc = typeResults.get(r.commitIssueType);
					if(rc != null) rc.addResult(r.id, r.files, r.additions, r.deletions, r.fileList);
				}
			} catch (InterruptedException e) {
				//e.printStackTrace();
			} catch (ExecutionException e) {
				//e.printStackTrace();
			}
		}
        
        // Shutdown the ExecutorService 
        exec.shutdown();
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
	
	private class CallableAnalyzer implements Callable<List<Result>> {

		private List<Commit> commits;
		private Set<CommitIssue.Type> typeSet;
		
		public CallableAnalyzer(List<Commit> commits, Set<CommitIssue.Type> typeSet) {
			this.commits = commits;
			this.typeSet = typeSet;
		}


		@Override
		public List<Result> call() throws Exception {

			List<Result> results = new ArrayList<>();
			
			for(Commit c : commits) {
				for(CommitIssue i : c.getCommitIssues()) {
					if(!typeSet.contains(i.getType())) continue;
					ResultsContainer rc = typeResults.get(i.getType());
					
					Set<CommitFile> files = c.getFiles();			
					Set<ResultsContainer.FileHolder> fileList = new HashSet<>();
					for(CommitFile f : files) {
						if(f.isSrc()) {
							fileList.add(rc.new FileHolder(f.getChangeType(), f.getAdditions(), f.getDeletions()));
						}
					}
					
					Result r = new Result();
					r.id = new Long(c.getId());
					r.files = c.getFiles().size();
					r.additions = c.getAdditions();
					r.deletions = c.getDeletions();
					r.fileList = fileList;
					r.commitIssueType = i.getType();
					
					results.add(r);
				}
			}
			
			return results;
		}

	}
	
	private class Result {
		private Long id;
		int files;
		int additions;
		int deletions;
		Set<ResultsContainer.FileHolder> fileList;
		CommitIssue.Type commitIssueType;
	}
}
