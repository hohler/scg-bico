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
		
		public String toString() {
			return this.getName();
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
	    
	    public String toString() {
	    	return this.getName();
	    }
	}
	
	private Type type = Type.OTHER;
	private Priority priority = Priority.OTHER;
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
	
	public String toString() {
		return "Issue: "+name+" Type: "+type+" Priority: "+priority;
	}
}
