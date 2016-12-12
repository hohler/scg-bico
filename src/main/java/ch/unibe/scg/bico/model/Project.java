package ch.unibe.scg.bico.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="projects")
public class Project {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String url;
	private Type type;
	private String branch;
	private String issueTrackerUrlPattern;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="project", orphanRemoval = true)
	@OrderBy("id")
	private Set<Commit> commits;
	
	public enum Type {
		GITHUB("GitHub"), GIT("Git"), JIRA("Jira"), BUGZILLA("Bugzilla");
		
		private final String name;
		
		Type(String name) {
			this.name = name;
		}
		public String toString() {
			return name;
		}
	}
	
	protected Project() {
		this.commits = new HashSet<Commit>();
	}
	
	public Project(Type type) {
		this.type = type;
		this.commits = new HashSet<Commit>();
	}
	
	public Project(String name, Type type) {
		this.name = name;
		this.type = type;
		this.commits = new HashSet<Commit>();
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Set<Commit> getCommits() {
		return commits;
	}

	public void addCommit(Commit commit) {
		commit.setProject(this);
		this.commits.add(commit);
	}
	
	public void removeCommit(Commit commit) {
		this.commits.remove(commit);
		commit.setProject(null);
	}
	
	public void addCommits(ArrayList<Commit> list) {
		list.forEach(c -> c.setProject(this));
		this.commits.addAll(list);
	}
	
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String toString() {
		return String.format("Project[id=%d, name='%s', type='%s', url='%s']",
				id, name, type, url);
	}

	public void setIssueTrackerUrlPattern(String string) {
		issueTrackerUrlPattern = string;
	}
	
	public String getIssueTrackerUrlPattern() {
		return issueTrackerUrlPattern;
	}
	
	public Long getId() {
		return id;
	}
	
	public void cleanForProcessing() {
		commits.forEach(s -> s.setProject(null));
		commits.clear();
	}
	
	public int getNumberOfCategorizedCommits() {
		int count = 0;
		for(Commit c : commits) {
			if(c.commitIssues.size() > 0) count++;
		}
		return count;
	}
	
}
