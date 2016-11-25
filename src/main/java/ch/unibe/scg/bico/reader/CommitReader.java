package ch.unibe.scg.bico.reader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.batch.item.ItemReader;

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.model.Project;
import ch.unibe.scg.bico.model.service.CommitIssueService;

public class CommitReader implements ItemReader<CommitIssue> {

	private List<CommitIssue> commitIssues;
	private Iterator<CommitIssue> iterator;
	private Project project;
	private CommitIssueService commitIssueService;

	public CommitReader(Project project, CommitIssueService commitIssueService) {
		this.project = project;
		this.commitIssueService = commitIssueService;
	}

	private synchronized void init() {
		if(commitIssues != null) return;
		//Hibernate.initialize(project.getCommits());
		//commits = project.getCommits();
		//commits = commitService.getProjectCommits(project);
		//commitIssues = new HashSet<>();
		/*for(Commit c : project.getCommits()) {
			commitIssues.addAll(c.getCommitIssues());
		}*/
		commitIssues = commitIssueService.findAllByProject(project);
		//commits = project.getCommits(); 
		iterator = commitIssues.iterator();
		
	}

	@Override
	public CommitIssue read() {
		if (commitIssues == null)
			init();
		if (commitIssues.isEmpty())
			throw new NullPointerException("commit list is empty");
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