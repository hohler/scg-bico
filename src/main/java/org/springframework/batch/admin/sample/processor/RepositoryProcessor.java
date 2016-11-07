package org.springframework.batch.admin.sample.processor;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.IssuedCommit;
import org.springframework.batch.admin.sample.parser.IssueStringParser;
import org.springframework.batch.item.ItemProcessor;

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