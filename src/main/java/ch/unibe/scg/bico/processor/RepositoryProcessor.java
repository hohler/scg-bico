package ch.unibe.scg.bico.processor;

import org.springframework.batch.item.ItemProcessor;

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.parser.IssueStringParser;

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