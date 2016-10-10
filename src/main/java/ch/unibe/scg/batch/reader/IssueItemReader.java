package ch.unibe.scg.batch.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import ch.unibe.scg.model.CommitIssue;
import ch.unibe.scg.model.Project;

public class IssueItemReader implements ItemReader<CommitIssue> {

	private Project project;
	private List<CommitIssue> issues;
	private Iterator<CommitIssue> iterator;
	
	public void setProject(Project project) throws NullPointerException {
		if(project == null) throw new NullPointerException("Project is null");
		this.project = project;
	}
	
	public void init() {
		issues = project.getAllIssues();
		iterator = issues.iterator();
	}
	
	@Override
	public CommitIssue read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if(iterator.hasNext()) return iterator.next();
		return null;
	}
}
