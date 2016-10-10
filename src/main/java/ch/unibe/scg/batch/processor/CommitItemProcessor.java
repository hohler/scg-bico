package ch.unibe.scg.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import ch.unibe.scg.model.Commit;
import ch.unibe.scg.parser.IssueStringParser;

public class CommitItemProcessor implements ItemProcessor<Commit, Commit> {
		
	@Override
	public Commit process(final Commit input) throws Exception {
		
		if(input == null) throw new NullPointerException("Commit is null");
		IssueStringParser issueParser = new IssueStringParser("^(\\w+\\-\\d+).");
		issueParser.parse(input);
		
		return input;
	}
}