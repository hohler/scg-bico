package ch.unibe.scg.model;

public class CommitIssue {

	public enum Priority {
		BLOCKER,
		CRITICAL,
		MAJOR,
		MINOR,
		TRIVIAL,
		OTHER;
		
		public String getName() {
			String name = toString();
			return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
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
		OTHER;
		
	    public String getName() {
	    	String name = toString();
	    	return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	    }
	}
	
	private Type type;
	private Priority priority;
	private String name;
	private Commit commit;
	
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

	public String toString() {
		return "Issue: "+name+" Type: "+type.getName()+" Priority: "+priority.getName();
	}
}
