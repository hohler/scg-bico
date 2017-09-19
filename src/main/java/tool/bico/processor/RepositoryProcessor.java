package tool.bico.processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import tool.bico.analysis.CommitMessageClassifier;
import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.parser.IssueStringParser;

public class RepositoryProcessor implements ItemProcessor<Commit, Commit> {
	
	private Project.Type type;
	public RepositoryProcessor(Project.Type type) {
		this.type = type;
	}
	
	@Override
	public Commit process(final Commit input) throws Exception {
		
		if(input == null) throw new NullPointerException("Commit is null");
		
		List<String> identifiers = new ArrayList<>();
		
		if(type == Project.Type.JIRA) {
			IssueStringParser issueParser = new IssueStringParser("(^|\\s+|#)\\[?(\\w+-\\d+)\\]?(\\s|\\.|:|;|,)+");
			identifiers = issueParser.parseAll(input.getMessage(), 2);
		} else
		if(type == Project.Type.GITHUB) {
			IssueStringParser issueParser = new IssueStringParser("\\(?#(\\d+)\\)?");
			identifiers = issueParser.parseAll(input.getMessage(), 1);
		} else
		if(type == Project.Type.BUGZILLA) {
			IssueStringParser issueParser = new IssueStringParser("bug\\s(\\d+)");
			identifiers = issueParser.parseAll(input.getMessage(), 1);
		}
		
		// commitmessage based classifier
		CommitIssue.Type commitMessageType = CommitMessageClassifier.classify(input.getMessage());
		input.setCommitMessageBasedType(commitMessageType);
		
		for(String i : identifiers) {
			if(i != null && !i.isEmpty()) input.initIssue(i.toUpperCase());
		}
		
		return input;
	}
}