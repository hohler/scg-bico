package ch.unibe.scg.bico.reader;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.batch.item.ItemReader;

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.model.Project;

public class CommitReader implements ItemReader<Commit> {

	private Set<Commit> commits;
	private Iterator<Commit> iterator;
	private Project project;

	public CommitReader(Project project) {
		this.project = project;
	}

	private void init() {
		//Hibernate.initialize(project.getCommits());
		//commits = project.getCommits();
		//commits = commitService.getProjectCommits(project);
		commits = project.getCommits(); 
		iterator = commits.iterator();
		
	}

	@Override
	public Commit read() {
		if (commits == null)
			init();
		if (commits == null)
			throw new NullPointerException("commit list is null");
		if (iterator == null)
			throw new NullPointerException("commit iterator is null");
		if (iterator.hasNext())
			try {
				return iterator.next();
			} catch(NoSuchElementException e) {
				return null;
			}
		return null;
	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @BeforeStep public void retrieveInterstepData(StepExecution
	 * stepExecution) { JobExecution jobExecution =
	 * stepExecution.getJobExecution(); ExecutionContext jobContext =
	 * jobExecution.getExecutionContext(); Object o =
	 * jobContext.get("issuedCommits"); if(o instanceof List<?>) { issues =
	 * (List<IssuedCommit>) o; iterator = issues.iterator(); } else { issues =
	 * null; } }
	 */
}