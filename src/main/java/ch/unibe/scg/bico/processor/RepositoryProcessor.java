package ch.unibe.scg.bico.processor;

import org.springframework.batch.item.ItemProcessor;

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.model.Project;
import ch.unibe.scg.bico.parser.IssueStringParser;

public class RepositoryProcessor implements ItemProcessor<Commit, Commit> {
	
	private Project.Type type;
	public RepositoryProcessor(Project.Type type) {
		this.type = type;
	}
	
	@Override
	public Commit process(final Commit input) throws Exception {
		
		if(input == null) throw new NullPointerException("Commit is null");
		
		//if type == Jira
		String identifier = null;
		if(type == Project.Type.JIRA) {
			IssueStringParser issueParser = new IssueStringParser("^(\\w+(\\-| )?\\d+).");
			identifier = issueParser.parse(input.getMessage());

		} else
		if(type == Project.Type.GITHUB) {
			IssueStringParser issueParser = new IssueStringParser("\\(#(\\d+)\\)");
			identifier = issueParser.parse(input.getMessage());
		}
		
		input.initIssue(identifier);
		
		return input;
	}
}