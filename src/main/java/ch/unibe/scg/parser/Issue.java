package ch.unibe.scg.parser;

public class Issue {

	public static final int TYPE_BUG = 0;
	public static final int TYPE_FEATURE = 1;
	public static final int TYPE_OTHER = 10;
	
	public static final int PRIORITY_LOW = 0;
	public static final int PRIORITY_HIGH = 1;
	
	private int type;
	private int priority;
	
	public Issue() {
	}
	
	public Issue(int type, int priority) {
		this.type = type;
		this.priority = priority;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
