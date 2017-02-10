package tool.bico.analysis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import tool.bico.Tools;
import tool.bico.model.CommitFile;
import tool.bico.model.CommitIssue;

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
	
	public int getMedianFilesChanged() {
		int[] data = results.stream().mapToInt(i -> i.filesChanged).toArray();
		Arrays.sort(data);
		return Tools.median(data);
	}
	
	public int getHighestFilesChanged() {
		int result = 0;
		for(ResultHolder r : results) {
			if(result < r.filesChanged) result = r.filesChanged;
		}
		return result;
	}
	
	public int getLowestFilesChanged() {
		int result = Integer.MAX_VALUE;
		for(ResultHolder r : results) {
			if(result > r.filesChanged) result = r.filesChanged;
		}
		if(result == Integer.MAX_VALUE) result = 0;
		return result;
	}
	
	/*
	 * splits off the lowest 25% of data from the highest 75%
	 */
	public int getFirstQuartileFilesChanged() {
		int[] data = results.stream().mapToInt(i -> i.filesChanged).toArray();
		if(data.length == 0) return 0;
		Arrays.sort(data); // ascending order
		int index = Math.round(data.length * (float)0.25);
		return data[index];
	}
	
	/*
	 * splits off the highest 25% of data from the lowest 75%
	 */
	public int getThirdQuartileFilesChanged() {
		int[] data = results.stream().mapToInt(i -> i.filesChanged).toArray();
		if(data.length == 0) return 0;
		Arrays.sort(data); // ascending order
		ArrayUtils.reverse(data); // reverse to descending order
		int index = Math.round(data.length * (float)0.25);
		return data[index];
	}
	
	public int getMedianAdditionsInFiles() {
		int[] result = new int[0];
		for(ResultHolder r : results) {
			int[] data = r.fileList.stream().mapToInt(i -> i.additions).toArray();
			Arrays.sort(data);
			result = (result.length > 0) ? Tools.merge(result, data) : data;
		}
		return Tools.median(result);
	}
	
	/*
	 * splits off the lowest 25% of data from the highest 75%
	 */
	public int getFirstQuartileAdditions() {
		int[] data = results.stream().mapToInt(i -> i.additions).toArray();
		if(data.length == 0) return 0;
		Arrays.sort(data); // ascending order
		int index = Math.round(data.length * (float)0.25);
		return data[index];
	}
	
	/*
	 * splits off the highest 25% of data from the lowest 75%
	 */
	public int getThirdQuartileAdditions() {
		int[] data = results.stream().mapToInt(i -> i.additions).toArray();
		if(data.length == 0) return 0;
		Arrays.sort(data); // ascending order
		ArrayUtils.reverse(data); // reverse to descending order
		int index = Math.round(data.length * (float)0.25);
		return data[index];
	}
	
	public int getMedianAdditions() {
		int[] data = results.stream().mapToInt(i -> i.additions).toArray();
		Arrays.sort(data);
		return Tools.median(data);
	}
	
	public int getMedianDeletionsInFiles() {
		int[] result = new int[0];
		for(ResultHolder r : results) {
			int[] data = r.fileList.stream().mapToInt(i -> i.deletions).toArray();
			Arrays.sort(data);
			result = (result.length > 0) ? Tools.merge(result, data) : data;
		}
		return Tools.median(result);
	}
	
	public int getMedianDeletions() {
		int[] data = results.stream().mapToInt(i -> i.deletions).toArray();
		Arrays.sort(data);
		return Tools.median(data);
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
	
	public int getLowestDeletionsPerFile() {
		int result = Integer.MAX_VALUE;
		for(ResultHolder r : results) {
			for(FileHolder f : r.fileList) {
				if(result > f.deletions) result = f.deletions;
			}
		}
		if(result == Integer.MAX_VALUE) result = 0;
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
	
	public int getLowestAdditionsPerFile() {
		int result = Integer.MAX_VALUE;
		for(ResultHolder r : results) {
			for(FileHolder f : r.fileList) {
				if(result > f.additions) result = f.additions;
			}
		}
		if(result == Integer.MAX_VALUE) result = 0;
		return result;
	}
	
	
	public int getHighestDeletionsPerCommit() {
		int result = 0;
		for(ResultHolder r : results) {
			if(result < r.deletions) result = r.deletions;
		}
		return result;
	}
	
	public int getLowestDeletionsPerCommit() {
		int result = Integer.MAX_VALUE;
		for(ResultHolder r : results) {
			if(result > r.deletions) result = r.deletions;
		}
		if(result == Integer.MAX_VALUE) result = 0;
		return result;
	}
	
	public int getHighestAdditionsPerCommit() {
		int result = 0;
		for(ResultHolder r : results) {
			if(result < r.additions) result = r.additions;
		}
		return result;
	}
	
	public int getLowestAdditionsPerCommit() {
		int result = Integer.MAX_VALUE;
		for(ResultHolder r : results) {
			if(result > r.additions) result = r.additions;
		}
		if(result == Integer.MAX_VALUE) result = 0;
		return result;
	}
	
	public int[] getAdditionsPerCommit() {
		int[] result = results.stream().mapToInt(i -> i.additions).toArray();
		Arrays.sort(result);
		return result;
	}
	
	public int[] getDeletionsPerCommit() {
		return results.stream().mapToInt(i -> i.deletions).toArray();
	}
	
	public int[] getFilesChangedPerCommit() {
		int result[] = results.stream().mapToInt(i -> i.filesChanged).toArray();
		Arrays.sort(result);
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

		public Long getCommitId() {
			return commitId;
		}

		public int getFilesChanged() {
			return filesChanged;
		}

		public int getAdditions() {
			return additions;
		}

		public int getDeletions() {
			return deletions;
		}

		public Set<ResultsContainer.FileHolder> getFileList() {
			return fileList;
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
