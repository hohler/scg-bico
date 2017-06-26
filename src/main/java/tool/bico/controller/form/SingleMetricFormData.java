package tool.bico.controller.form;

public class SingleMetricFormData {
	private String ref;
	private int timeWindow;
	private boolean excludeBigCommits;
	
	public SingleMetricFormData() {}
	public String getRef() { return ref; }
	public int getTimeWindow() { return timeWindow; }
	public boolean getExcludeBigCommits() { return excludeBigCommits; }
	
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public void setTimeWindow(int timeWindow) {
		this.timeWindow = timeWindow;
	}
	
	public void setExcludeBigCommits(boolean exclude) {
		excludeBigCommits = exclude;
	}
}