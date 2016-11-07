package ch.unibe.scg.batch.reader;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import ch.unibe.scg.model.Commit;
import ch.unibe.scg.model.Project;
import ch.unibe.scg.repository.Repository;

public class RepositoryReader implements ItemReader<Commit> {

	private ArrayList<Commit> commits;
	private Iterator<Commit> iterator;
	
	public RepositoryReader(Project project) {
		Repository g = new Repository(project);
		commits = g.getCommits();
		iterator = commits.iterator();
	}
	
	@Override
	public Commit read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if(iterator.hasNext()) return iterator.next();
		return null;
	}
}
