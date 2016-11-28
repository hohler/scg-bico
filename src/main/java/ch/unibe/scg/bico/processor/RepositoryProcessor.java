package ch.unibe.scg.bico.processor;

import java.util.ArrayList;
import java.util.List;

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
		
		List<String> identifiers = new ArrayList<>();
		
		//if type == Jira
		if(type == Project.Type.JIRA) {
			//String identifier;
			IssueStringParser issueParser = new IssueStringParser("\\[*(\\w+-\\d+)\\]*"); //^\\[*(\\w+(\\-| )?\\d+)\\]*
			identifiers = issueParser.parseAll(input.getMessage());
		} else
		if(type == Project.Type.GITHUB) {
			IssueStringParser issueParser = new IssueStringParser("\\(?#(\\d+)\\)?");
			identifiers = issueParser.parseAll(input.getMessage());
		} else
		if(type == Project.Type.BUGZILLA) {
			IssueStringParser issueParser = new IssueStringParser("bug\\s(\\d+)");
			identifiers = issueParser.parseAll(input.getMessage());
		}
		
		for(String i : identifiers) {
			input.initIssue(i);
		}
		
		return input;
	}
}