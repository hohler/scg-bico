package tool.bico.controller.form;

public class SingleMetricFormData {
	private String ref;
	private int timeWindow;
	
	public SingleMetricFormData() {}
	public String getRef() { return ref; }
	public int getTimeWindow() { return timeWindow; }
	
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public void setTimeWindow(int timeWindow) {
		this.timeWindow = timeWindow;
	}
}