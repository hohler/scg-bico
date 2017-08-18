package tool.bico.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import tool.bico.analysis.ResultsContainer;

@Entity
@Table(name="commitissueanalysis")
public class CommitIssueAnalysis {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Project project;
	
	private CommitIssue.Type type;
	
	private int numberOfResults;
	
	private int maxFilesChangedPerCommit;
	private int minFilesChangedPerCommit;
	private int medianFilesChangedPerCommit;
	
	private int maxAdditionsPerFile;
	private int minAdditionsPerFile;
	private int medianAdditionsPerFile;
	
	private int maxDeletionsPerFile;
	private int minDeletionsPerFile;
	private int medianDeletionsPerFile;
	
	private int maxAdditionsPerCommit;
	private int minAdditionsPerCommit;
	private int medianAdditionsPerCommit;
	
	private int maxDeletionsPerCommit;
	private int minDeletionsPerCommit;
	private int medianDeletionsPerCommit;
	
	
	private int firstQuartileFilesChanged;
	private int thirdQuartileFilesChanged;
	private int filesChangedThreshold;
	
	private int firstQuartileAdditions;
	private int thirdQuartileAdditions;
	private int additionsThreshold;
	
	private int[] additionsPerCommit;
	private int[] filesChangedPerCommit;
	
	public CommitIssueAnalysis() {}
	
	public CommitIssueAnalysis(Project project, CommitIssue.Type type, ResultsContainer r) {
		this.project = project;
		this.type = type;
		this.numberOfResults = r.getResultsSize();
		
		this.maxFilesChangedPerCommit = r.getHighestFilesChanged();
		this.minFilesChangedPerCommit = r.getLowestFilesChanged();
		this.medianFilesChangedPerCommit = r.getMedianFilesChanged();
		
		this.maxAdditionsPerFile = r.getHighestAdditionsPerFile();
		this.minAdditionsPerFile = r.getLowestAdditionsPerFile();
		this.medianAdditionsPerFile = r.getMedianAdditionsInFiles();
		
		this.maxAdditionsPerCommit = r.getHighestAdditionsPerCommit();
		this.minAdditionsPerCommit = r.getLowestAdditionsPerCommit();
		this.medianAdditionsPerCommit = r.getMedianAdditions();
		
		this.maxDeletionsPerFile = r.getHighestDeletionsPerFile();
		this.minDeletionsPerFile = r.getLowestDeletionsPerFile();
		this.medianDeletionsPerFile = r.getMedianDeletionsInFiles();
		
		this.maxDeletionsPerCommit = r.getHighestDeletionsPerCommit();
		this.minDeletionsPerCommit = r.getLowestDeletionsPerCommit();
		this.medianDeletionsPerCommit = r.getMedianDeletions();
		
		this.firstQuartileFilesChanged = r.getFirstQuartileFilesChanged();
		this.thirdQuartileFilesChanged = r.getThirdQuartileFilesChanged();
	
		this.filesChangedThreshold = r.getFilesChangedThreshold();
		this.additionsThreshold = r.getAdditionsThreshold();
		
		this.additionsPerCommit = r.getAdditionsPerCommit();
		this.filesChangedPerCommit = r.getFilesChangedPerCommit();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public CommitIssue.Type getType() {
		return type;
	}
	public void setType(CommitIssue.Type type) {
		this.type = type;
	}
	public int getNumberOfIssues() {
		return numberOfResults;
	}
	public void setNumberOfIssues(int numberOfIssues) {
		this.numberOfResults = numberOfIssues;
	}
	public int getMaxFilesChangedPerCommit() {
		return maxFilesChangedPerCommit;
	}
	public void setMaxFilesChangedPerCommit(int maxFilesChangedPerCommit) {
		this.maxFilesChangedPerCommit = maxFilesChangedPerCommit;
	}
	public int getMinFilesChangedPerCommit() {
		return minFilesChangedPerCommit;
	}
	public void setMinFilesChangedPerCommit(int minFilesChangedPerCommit) {
		this.minFilesChangedPerCommit = minFilesChangedPerCommit;
	}
	public int getMedianFilesChangedPerCommit() {
		return medianFilesChangedPerCommit;
	}
	public void setMedianFilesChangedPerCommit(int medianFilesChangedPerCommit) {
		this.medianFilesChangedPerCommit = medianFilesChangedPerCommit;
	}
	public int getMaxAdditionsPerFile() {
		return maxAdditionsPerFile;
	}
	public void setMaxAdditionsPerFile(int maxAdditionsPerFile) {
		this.maxAdditionsPerFile = maxAdditionsPerFile;
	}
	public int getMinAdditionsPerFile() {
		return minAdditionsPerFile;
	}
	public void setMinAdditionsPerFile(int minAdditionsPerFile) {
		this.minAdditionsPerFile = minAdditionsPerFile;
	}
	public int getMedianAdditionsPerFile() {
		return medianAdditionsPerFile;
	}
	public void setMedianAdditionsPerFile(int medianAdditionsPerFile) {
		this.medianAdditionsPerFile = medianAdditionsPerFile;
	}
	public int getMaxDeletionsPerFile() {
		return maxDeletionsPerFile;
	}
	public void setMaxDeletionsPerFile(int maxDeletionsPerFile) {
		this.maxDeletionsPerFile = maxDeletionsPerFile;
	}
	public int getMinDeletionsPerFile() {
		return minDeletionsPerFile;
	}
	public void setMinDeletionsPerFile(int minDeletionsPerFile) {
		this.minDeletionsPerFile = minDeletionsPerFile;
	}
	public int getMedianDeletionsPerFile() {
		return medianDeletionsPerFile;
	}
	public void setMedianDeletionsPerFile(int medianDeletionsPerFile) {
		this.medianDeletionsPerFile = medianDeletionsPerFile;
	}
	public int getMaxAdditionsPerCommit() {
		return maxAdditionsPerCommit;
	}
	public void setMaxAdditionsPerCommit(int maxAdditionsPerCommit) {
		this.maxAdditionsPerCommit = maxAdditionsPerCommit;
	}
	public int getMinAdditionsPerCommit() {
		return minAdditionsPerCommit;
	}
	public void setMinAdditionsPerCommit(int minAdditionsPerCommit) {
		this.minAdditionsPerCommit = minAdditionsPerCommit;
	}
	public int getMedianAdditionsPerCommit() {
		return medianAdditionsPerCommit;
	}
	public void setMedianAdditionsPerCommit(int medianAdditionsPerCommit) {
		this.medianAdditionsPerCommit = medianAdditionsPerCommit;
	}
	public int getMaxDeletionsPerCommit() {
		return maxDeletionsPerCommit;
	}
	public void setMaxDeletionsPerCommit(int maxDeletionsPerCommit) {
		this.maxDeletionsPerCommit = maxDeletionsPerCommit;
	}
	public int getMinDeletionsPerCommit() {
		return minDeletionsPerCommit;
	}
	public void setMinDeletionsPerCommit(int minDeletionsPerCommit) {
		this.minDeletionsPerCommit = minDeletionsPerCommit;
	}
	public int getMedianDeletionsPerCommit() {
		return medianDeletionsPerCommit;
	}
	public void setMedianDeletionsPerCommit(int medianDeletionsPerCommit) {
		this.medianDeletionsPerCommit = medianDeletionsPerCommit;
	}
	public int getFirstQuartileFilesChanged() {
		return firstQuartileFilesChanged;
	}
	public void setFirstQuartileFilesChanged(int firstQuartileFilesChanged) {
		this.firstQuartileFilesChanged = firstQuartileFilesChanged;
	}
	public int getThirdQuartileFilesChanged() {
		return thirdQuartileFilesChanged;
	}
	public void setThirdQuartileFilesChanged(int thirdQuartileFilesChanged) {
		this.thirdQuartileFilesChanged = thirdQuartileFilesChanged;
	}
	public int getFilesChangedThreshold() {
		return filesChangedThreshold;
	}
	public void setFilesChangedThreshold(int filesChangedThreshold) {
		this.filesChangedThreshold = filesChangedThreshold;
	}
	public int getFirstQuartileAdditions() {
		return firstQuartileAdditions;
	}
	public void setFirstQuartileAdditions(int firstQuartileAdditions) {
		this.firstQuartileAdditions = firstQuartileAdditions;
	}
	public int getThirdQuartileAdditions() {
		return thirdQuartileAdditions;
	}
	public void setThirdQuartileAdditions(int thirdQuartileAdditions) {
		this.thirdQuartileAdditions = thirdQuartileAdditions;
	}
	public int getAdditionsThreshold() {
		return additionsThreshold;
	}
	public void setAdditionsThreshold(int additionsThreshold) {
		this.additionsThreshold = additionsThreshold;
	}
	public int getNumberOfResults() {
		return numberOfResults;
	}
	public void setNumberOfResults(int numberOfResults) {
		this.numberOfResults = numberOfResults;
	}
	public int[] getAdditionsPerCommit() {
		return additionsPerCommit;
	}
	public void setAdditionsPerCommit(int[] additionsPerCommit) {
		this.additionsPerCommit = additionsPerCommit;
	}
	public int[] getFilesChangedPerCommit() {
		return filesChangedPerCommit;
	}
	public void setFilesChangedPerCommit(int[] filesChangedPerCommit) {
		this.filesChangedPerCommit = filesChangedPerCommit;
	}
		
}
