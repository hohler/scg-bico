package tool.bico.controller.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChangeMetricFormData {

	private List<Integer> commitRanges;
	
	private int everyCommits = 75;
	
	private Date startRange;
	private Date endRange;
	
	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
	
	public ChangeMetricFormData() {
		commitRanges = new ArrayList<>();
		populateCommitRanges();
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

	public Date getStartRange() {
		return startRange;
	}

	public void setStartRange(Date startRange) {
		this.startRange = startRange;
	}
	
	public void setStartRange(String startRange) {
		try {
			this.startRange = dateFormatter.parse(startRange);
		} catch (ParseException e) {
			this.startRange = null;
		}
	}

	public Date getEndRange() {
		return endRange;
	}

	public void setEndRange(Date endRange) {
		this.endRange = endRange;
	}
	
	public void setEndRange(String endRange) {
		try {
			this.endRange = dateFormatter.parse(endRange);
		} catch (ParseException e) {
			this.endRange = null;
		}
	}
	
	public String getEndRangeString() {
		if(endRange == null) return "";
		return dateFormatter.format(endRange);
	}
	
	public String getStartRangeString() {
		if(startRange == null) return "";
		return dateFormatter.format(startRange);
	}
}
