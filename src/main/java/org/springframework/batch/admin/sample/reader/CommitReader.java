package org.springframework.batch.admin.sample.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class CommitReader implements ItemReader<Commit> {

	private List<Commit> issues;
	private Iterator<Commit> iterator;
	private Project project;
	
	public CommitReader(Project project) {
		this.project = project;
	}
	
	private void init() {
		issues = project.getCommits();
		iterator = issues.iterator();
	}
	
	@Override
	public Commit read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if(issues == null) init();
		if(issues == null) throw new NullPointerException("issues list is null");
		if(iterator.hasNext()) return iterator.next();
		return null;
	}
	
	/*@SuppressWarnings("unchecked")
	@BeforeStep
    public void retrieveInterstepData(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        Object o = jobContext.get("issuedCommits");
        if(o instanceof List<?>) {
        	issues = (List<IssuedCommit>) o;
        	iterator = issues.iterator();
        } else {
        	issues = null;
        }
    }*/
}