package tool.bico.controller.form;

import java.util.ArrayList;
import java.util.List;

public class SourceMetricFormData {

	private List<Integer> commitRanges;
	
	private int everyCommits = 0;
	
	public SourceMetricFormData() {
		commitRanges = new ArrayList<>();
		populateCommitRanges();
	}
	
	private void populateCommitRanges() {
		commitRanges.add(0);
		commitRanges.add(1);
		commitRanges.add(10);
		commitRanges.add(25);
		commitRanges.add(50);
		commitRanges.add(75);
		commitRanges.add(100);
		commitRanges.add(200);
		commitRanges.add(500);
		commitRanges.add(1000);
	}
	
	public List<Integer> getCommitRanges() {
		return commitRanges;
	}

	public void setCommitRanges(List<Integer> commitRanges) {
		this.commitRanges = commitRanges;
	}
	
	public int getEveryCommits() {
		return everyCommits;
	}

	public void setEveryCommits(int everyCommits) {
		this.everyCommits = everyCommits;
	}
}
