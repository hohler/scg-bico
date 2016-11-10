package org.springframework.batch.admin.sample.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.admin.sample.model.IssuedCommit;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class IssuedCommitReader implements ItemReader<IssuedCommit> {

	private List<IssuedCommit> issues;
	private Iterator<IssuedCommit> iterator;
	
	@Override
	public IssuedCommit read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
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