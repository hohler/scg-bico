package ch.unibe.scg.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import ch.unibe.scg.model.CommitIssue;
import ch.unibe.scg.model.IssuedCommit;
import ch.unibe.scg.model.Project;
import ch.unibe.scg.parser.IssueTrackerParser;

public class IssuedCommitProcessor implements ItemProcessor<IssuedCommit, IssuedCommit> {
	
	private String urlPattern;
	
	public IssuedCommitProcessor(Project project) {
		urlPattern = project.getIssueTrackerUrlPattern();
		if(urlPattern == null) throw new NullPointerException("urlPattern is null");
	}

	@Override
	public IssuedCommit process(final IssuedCommit input) throws Exception {
		IssueTrackerParser itp;
		IssuedCommit output = new IssuedCommit(input);
		try {
			itp = new IssueTrackerParser(urlPattern);
			itp.setIssue(input.getCommitIssue());
			CommitIssue result = itp.parse();
			if(result != null) output.setCommitIssue(result);
			else output.setCommitIssue(input.getCommitIssue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
}