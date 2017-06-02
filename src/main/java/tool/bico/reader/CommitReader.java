package tool.bico.reader;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.batch.item.ItemReader;

import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.model.service.CommitIssueService;

@Deprecated
public class CommitReader implements ItemReader<CommitIssue> {

	private List<CommitIssue> commitIssues;
	private Iterator<CommitIssue> iterator;
	private Project project;
	private CommitIssueService commitIssueService;
	private int inits = 0;

	public CommitReader(Project project, CommitIssueService commitIssueService) {
		this.project = project;
		this.commitIssueService = commitIssueService;
		inits++;
	}

	private synchronized void init() {
		if(commitIssues != null) return;

		commitIssues = commitIssueService.findAllByProject(project);
		iterator = commitIssues.iterator();
		
	}

	@Override
	public synchronized CommitIssue read() {
		if (commitIssues == null && inits == 0)
			init();
		else if(commitIssues == null && inits != 0) {
			System.err.println("YOU SHOULD NOT HAVE DONE THIS!");
			return null;
		}
		if (commitIssues.isEmpty())
			throw new NullPointerException("commit list is empty");
		if (iterator == null)
			throw new NullPointerException("commit iterator is null");
		if (iterator.hasNext()) {
			try {
				return iterator.next();
			} catch(NoSuchElementException e) {
				return null;
			}
		} else {
			iterator = null;
			commitIssues = null;
			return null;
		}
	}
}