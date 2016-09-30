package ch.unibe.scg.parser;

public class Issue {

	public enum Priority {
		LOW (0, "Low"),
		HIGH (1, "High"),
		OTHER (10, "Other");
		
		private final int i;
	    private final String description;
	    Priority(int i, String description) {
	        this.i = i;
	        this.description = description;
	    }
	    
	    public int getNumber() {
	    	return i;
	    }
	    
	    public String getDescription() {
	    	return description;
	    }
	}
	
	public enum Type {
		BUG (0, "Bug"),
		FEATURE (1, "Feature"),
		OTHER (10, "Other");
		
		private final int i;
	    private final String description;
	    Type(int i, String description) {
	        this.i = i;
	        this.description = description;
	    }
	    
	    public int getNumber() {
	    	return i;
	    }
	    
	    public String getDescription() {
	    	return description;
	    }
	}
	
	private Type type;
	private Priority priority;
	private String name;
	
	public Issue() {
	}
	
	public Issue(String name, Type type, Priority priority) {
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
		return type.getDescription();
	}

	public void setType(Issue.Type type) {
		this.type = type;
	}

	public Priority getPriority() {
		return priority;
	}
	
	public String getStringPriority() {
		return priority.getDescription();
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public String toString() {
		return "Issue: "+name+" Type: "+type.getDescription()+" Priority: "+priority.getDescription();
	}
}
