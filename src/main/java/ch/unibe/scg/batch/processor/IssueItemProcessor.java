package ch.unibe.scg.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import ch.unibe.scg.model.CommitIssue;
import ch.unibe.scg.parser.IssueTrackerParser;

public class IssueItemProcessor implements ItemProcessor<CommitIssue, CommitIssue> {
	
	private String urlPattern;
	
	public void setIssueTrackerUrlPattern(String urlPattern) throws NullPointerException {
		if(urlPattern == null) throw new NullPointerException("urlPattern is null");
		this.urlPattern = urlPattern;
	}
	
	@Override
	public CommitIssue process(final CommitIssue input) throws Exception {
		IssueTrackerParser itp;
		try {
			itp = new IssueTrackerParser(urlPattern);
			itp.setIssue(input);
			itp.parse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return input;
	}


}