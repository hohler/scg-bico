package tool.bico.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="commitissues")
public class CommitIssue {

	public enum Priority {
		BLOCKER,
		CRITICAL,
		MAJOR,
		MINOR,
		TRIVIAL,
		OTHER,
		NA;
		
		private String formatName() {
			String name = name();
			return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		}
		public String getName() {
			return formatName();
	    }
		
		public String toString() {
			return formatName();
		}
	}
	
	public enum Type {
		ACCESS,
		BUG,
		DEPENDENCY_UPGRADE,
		DOCUMENTATION,
		IMPROVEMENT,
		REQUEST,
		TASK,
		TEST,
		FEATURE,
		WISH,
		OTHER,
		SUBTASK,
		DEPRECATION,
		REFACTOR,
		NA;
		
		private String formatName() {
			String name = name();
			return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		}
	    public String getName() {
	    	return formatName();
	    }
	    
	    public String toString() {
	    	return formatName();
	    }
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToMany(targetEntity = Commit.class, fetch = FetchType.LAZY, cascade={CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "commitIssues")
	@OrderBy("id ASC")
	protected Set<Commit> commits;
	
	private Type type = Type.NA;
	
	private Priority priority = Priority.NA;
	
	@Column(nullable = true)
	private String name = "";
	
	public CommitIssue() {
		this.commits = new HashSet<Commit>();
	}
	
	public CommitIssue(String name) {
		this.name = name;
		this.commits = new HashSet<Commit>();
	}
	
	public CommitIssue(String name, Type type, Priority priority) {
		this.name = name;
		this.type = type;
		this.priority = priority;
		this.commits = new HashSet<Commit>();
	}
	
	public CommitIssue(CommitIssue issue) {
		type = issue.getType();
		priority = issue.getPriority();
		name = issue.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}
	
	public String getStringType() {
		return type.getName();
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Priority getPriority() {
		return priority;
	}
	
	public String getStringPriority() {
		return priority.getName();
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public Set<Commit> getCommits() {
		return commits;
	}

	/*public void setCommit(Commit commit) {
		this.commit = commit;
	}*/
	
	public void addCommit(Commit commit) {
		this.commits.add(commit);
	}
	
	public void removeCommit(Commit commit) {
		this.commits.remove(commit);
	}

	public Long getId() {
		return id;
	}

	public String toString() {
		return "Issue[id="+id+", name="+name+", type="+type+", priority="+priority+"]";
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof CommitIssue)) return false;
	    CommitIssue that = (CommitIssue) other;
	    if(this.name != null && this.name.length() > 0 && that.name != null && that.name.length() > 0) {
	    	if(this.name.equals(that.name)) return true;
	    }
	    return false;
	}
}