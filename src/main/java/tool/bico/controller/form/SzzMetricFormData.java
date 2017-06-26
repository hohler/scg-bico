package tool.bico.controller.form;

public class SzzMetricFormData {
	
	private boolean excludeBigCommits = false;
	
	public SzzMetricFormData() {
	}
	
	public boolean getExcludeBigCommits() {
		return excludeBigCommits;
	}

	public void setExcludeBigCommits(boolean excludeBigCommits) {
		this.excludeBigCommits = excludeBigCommits;
	}
}
