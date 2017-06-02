package tool.bico.controller.form;

public class SingleMetricFormData {
	private String ref;
	private int timeWindow;
	private boolean includeBigCommits;
	
	public SingleMetricFormData() {}
	public String getRef() { return ref; }
	public int getTimeWindow() { return timeWindow; }
	public boolean getIncludeBigCommits() { return includeBigCommits; }
	
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public void setTimeWindow(int timeWindow) {
		this.timeWindow = timeWindow;
	}
	
	public void setIncludeBigCommits(boolean include) {
		includeBigCommits = include;
	}
}