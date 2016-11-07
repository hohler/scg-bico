package org.springframework.batch.admin.sample.reader;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.repository.Repository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class RepositoryReader implements ItemReader<Commit> {

	private ArrayList<Commit> commits;
	private Iterator<Commit> iterator;
	
	private Project project;
	
	public RepositoryReader(Project project) {
		this.project = project;
	}
	
	private void init() {
		Repository g = new Repository(project);
		commits = g.getCommits();
		iterator = commits.iterator();
	}
	
	@Override
	public Commit read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if(iterator == null) init();
		if(iterator.hasNext()) return iterator.next();
		return null;
	}
}
