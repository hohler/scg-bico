package ch.unibe.scg.bico.controller.analysis;

import java.util.HashSet;
import java.util.Set;

import ch.unibe.scg.bico.model.CommitFile;
import ch.unibe.scg.bico.model.CommitIssue;

public class ResultsContainer {
	
	
	private Set<ResultHolder> results;
	
	private CommitIssue.Type type;
	public ResultsContainer(CommitIssue.Type type) {
		this.type = type;
		results = new HashSet<>();
	}

	public void addResult(Long commitId, int filesChanged, int additions, int deletions, Set<ResultsContainer.FileHolder> fileList) {
		results.add(new ResultHolder(commitId, filesChanged, additions, deletions, fileList));
	}
	
	public Set<ResultsContainer.ResultHolder> getResults() {
		return results;
	}
	
	public int getResultsSize() {
		return results.size();
	}
	
	public CommitIssue.Type getType() {
		return type;
	}
	
	public double getAvgFilesChanged() {
		double result = 0;
		for(ResultHolder r : results) {
			result += r.filesChanged;
		}
		if(result != 0) result /= (double) results.size();
		return result;
	}
	
	public int getHighestFilesChanged() {
		int result = 0;
		for(ResultHolder r : results) {
			if(result < r.filesChanged) result = r.filesChanged;
		}
		return result;
	}
	
	public double getAvgAdditionsPerFile() {
		double result = 0;
		for(ResultHolder r : results) {
			double tmp = 0;
			for(FileHolder f : r.fileList) {
				tmp += f.additions;
			}
			if(tmp != 0) tmp /= (double) r.fileList.size();
			result += tmp;
		}
		if(result != 0) result /= (double) results.size();
		return result;
	}
	
	public double getAvgDeletionsPerFile() {
		double result = 0;
		for(ResultHolder r : results) {
			double tmp = 0;
			for(FileHolder f : r.fileList) {
				tmp += f.deletions;
			}
			if(tmp != 0) tmp /= (double) r.fileList.size();
			result += tmp;
		}
		if(result != 0) result /= (double) results.size();
		return result;
	}
	
	public int getHighestDeletionsPerFile() {
		int result = 0;
		for(ResultHolder r : results) {
			for(FileHolder f : r.fileList) {
				if(result < f.deletions) result = f.deletions;
			}
		}
		return result;
	}
	
	public int getHighestAdditionsPerFile() {
		int result = 0;
		for(ResultHolder r : results) {
			for(FileHolder f : r.fileList) {
				if(result < f.additions) result = f.additions;
			}
		}
		return result;
	}
	
	
	public class ResultHolder {
		private Long commitId;
		private int filesChanged;
		private int additions;
		private int deletions;
		private Set<ResultsContainer.FileHolder> fileList;
		
		public ResultHolder(Long commitId, int filesChanged, int additions, int deletions, Set<ResultsContainer.FileHolder> fileList) {
			this.commitId = commitId;
			this.filesChanged = filesChanged;
			this.additions = additions;
			this.deletions = deletions;
			this.fileList = fileList;
		}
	}
	
	public class FileHolder {
		private CommitFile.ChangeType changeType;
		private int additions;
		private int deletions;
		
		public FileHolder(CommitFile.ChangeType changeType, int additions, int deletions) {
			this.changeType = changeType;
			this.additions = additions;
			this.deletions = deletions;
		}
	}

}
