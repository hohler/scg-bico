package org.springframework.batch.admin.sample.processor;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.parser.IssueStringParser;
import org.springframework.batch.item.ItemProcessor;

public class RepositoryProcessor implements ItemProcessor<Commit, Commit> {
		
	@Override
	public Commit process(final Commit input) throws Exception {
		
		if(input == null) throw new NullPointerException("Commit is null");
		IssueStringParser issueParser = new IssueStringParser("^(\\w+(\\-| )?\\d+).");
		String issueIdentifier = issueParser.parse(input.getMessage());
		
		input.initIssue(issueIdentifier);
		return input;
	}
}