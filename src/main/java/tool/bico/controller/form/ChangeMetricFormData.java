package tool.bico.controller.form;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChangeMetricFormData {

	private List<Integer> commitRanges;
	private Map<Integer, String> timeWindows;
	
	private int everyCommits = 75;
	private int timeWindow = 12;
	
	public ChangeMetricFormData() {
		commitRanges = new ArrayList<>();
		timeWindows = new LinkedHashMap<>();
		populateCommitRanges();
		populateTimeWindows();
	}
	
	private void populateCommitRanges() {
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
	
	private void populateTimeWindows() {
		timeWindows.put(1, "1 week");
		timeWindows.put(4, "1 month");
		timeWindows.put(12, "3 months");
		timeWindows.put(24, "6 months");
		timeWindows.put(52, "1 year");
		timeWindows.put(104, "2 years");
		timeWindows.put(156, "3 years");
		timeWindows.put(999999, "all");
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

	public int getTimeWindow() {
		return timeWindow;
	}

	public void setTimeWindow(int timeWindow) {
		this.timeWindow = timeWindow;
	}

	public Map<Integer, String> getTimeWindows() {
		return timeWindows;
	}

	public void setTimeWindows(Map<Integer, String> timeWindows) {
		this.timeWindows = timeWindows;
	}
}
