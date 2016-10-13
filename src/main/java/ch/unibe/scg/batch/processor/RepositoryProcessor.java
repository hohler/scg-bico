package ch.unibe.scg.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import ch.unibe.scg.model.Commit;
import ch.unibe.scg.model.IssuedCommit;
import ch.unibe.scg.parser.IssueStringParser;

public class RepositoryProcessor implements ItemProcessor<Commit, IssuedCommit> {
		
	@Override
	public IssuedCommit process(final Commit input) throws Exception {
		
		if(input == null) throw new NullPointerException("Commit is null");
		IssueStringParser issueParser = new IssueStringParser("^(\\w+\\-\\d+).");
		String issueIdentifier = issueParser.parse(input.getMessage());
		
		IssuedCommit commit = new IssuedCommit(input);
		commit.initIssue(issueIdentifier);
		return commit;
	}
}