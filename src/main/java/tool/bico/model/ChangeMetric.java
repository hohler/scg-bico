package tool.bico.model;

import java.util.Calendar;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "changemetrics")
public class ChangeMetric {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	protected Commit commit;
	
	String file;
	private int revisions;
	private int refactorings;
	private int bugfixes;
	private ArrayList<String> authors;
	private long locAdded;
	private long locRemoved;
	private long maxLocAdded;
	private long maxLocRemoved;
	private long codeChurn;
	private int maxChangeset;
	private long totalChangeset;
	private Calendar firstCommit;
	private Calendar lastCommit;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Commit getCommit() {
		return commit;
	}
	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public int getRevisions() {
		return revisions;
	}
	public void setRevisions(int revisions) {
		this.revisions = revisions;
	}
	public int getRefactorings() {
		return refactorings;
	}
	public void setRefactorings(int refactorings) {
		this.refactorings = refactorings;
	}
	public int getBugfixes() {
		return bugfixes;
	}
	public void setBugfixes(int bugfixes) {
		this.bugfixes = bugfixes;
	}
	public ArrayList<String> getAuthors() {
		return authors;
	}
	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}
	public long getLocAdded() {
		return locAdded;
	}
	public void setLocAdded(long locAdded) {
		this.locAdded = locAdded;
	}
	public long getLocRemoved() {
		return locRemoved;
	}
	public void setLocRemoved(long locRemoved) {
		this.locRemoved = locRemoved;
	}
	public long getMaxLocAdded() {
		return maxLocAdded;
	}
	public void setMaxLocAdded(long maxLocAdded) {
		this.maxLocAdded = maxLocAdded;
	}
	public long getMaxLocRemoved() {
		return maxLocRemoved;
	}
	public void setMaxLocRemoved(long maxLocRemoved) {
		this.maxLocRemoved = maxLocRemoved;
	}
	public long getCodeChurn() {
		return codeChurn;
	}
	public void setCodeChurn(long codeChurn) {
		this.codeChurn = codeChurn;
	}
	public int getMaxChangeset() {
		return maxChangeset;
	}
	public void setMaxChangeset(int maxChangeset) {
		this.maxChangeset = maxChangeset;
	}
	public long getTotalChangeset() {
		return totalChangeset;
	}
	public void setTotalChangeset(long totalChangeset) {
		this.totalChangeset = totalChangeset;
	}
	public Calendar getFirstCommit() {
		return firstCommit;
	}
	public void setFirstCommit(Calendar firstCommit) {
		this.firstCommit = firstCommit;
	}
	public Calendar getLastCommit() {
		return lastCommit;
	}
	public void setLastCommit(Calendar lastCommit) {
		this.lastCommit = lastCommit;
	}
}
