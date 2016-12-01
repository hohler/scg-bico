package ch.unibe.scg.bico.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
	    	//String name = toString();
	    	//return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	    	return formatName();
	    }
	    
	    public String toString() {
	    	//return this.getName();
	    	return formatName();
	    }
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=javax.persistence.CascadeType.REFRESH)
	private Commit commit;
	
	private Type type = Type.NA;
	
	private Priority priority = Priority.NA;
	
	private String name = "";
	
	public CommitIssue() {
	}
	
	public CommitIssue(String name) {
		this.name = name;
	}
	
	public CommitIssue(String name, Type type, Priority priority) {
		this.name = name;
		this.type = type;
		this.priority = priority;
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
	
	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public Long getId() {
		return id;
	}

	public String toString() {
		return "Issue[id="+id+", name="+name+", type="+type+", priority="+priority+"]";
	}
}