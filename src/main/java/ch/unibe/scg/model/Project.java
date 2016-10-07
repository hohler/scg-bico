package ch.unibe.scg.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.ArrayList;

@Entity
public class Project {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String url;
	private Type type;
	private String branch;
	
	private ArrayList<Commit> commits;
	
	public enum Type {
		GITHUB("Github"), GIT("Git");
		
		private final String name;
		
		Type(String name) {
			this.name = name;
		}
		public String toString() {
			return name;
		}
	}
	
	protected Project() {}
	
	public Project(Type type) {
		this.type = type;
		this.commits = new ArrayList<Commit>();
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

	public ArrayList<Commit> getCommits() {
		return commits;
	}

	public void addCommit(Commit commit) {
		this.commits.add(commit);
	}
	
	public void removeCommit(Commit commit) {
		this.commits.remove(commit);
	}
	
	public void addCommits(ArrayList<Commit> list) {
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
	
	
}
