package tool.bico.writer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.service.CommitIssueService;
import tool.bico.model.service.CommitService;

public class RepositoryWriter implements ItemWriter<Commit> {

	private CommitService commitService;
	private CommitIssueService commitIssueService;
	
	private List<CommitIssue> tempCommitIssues;
	private List<CommitIssue> tempNewCommitIssues;

	public RepositoryWriter(CommitService commitService, CommitIssueService commitIssueService) {
		this.commitService = commitService;
		this.commitIssueService = commitIssueService;
		this.tempCommitIssues = new ArrayList<CommitIssue>();
		this.tempNewCommitIssues = new ArrayList<CommitIssue>();
	}
	
	@Override
	public void write(List<? extends Commit> items) throws Exception {
		for(Commit commit : items) {
			tempNewCommitIssues = new ArrayList<>();
			// Check if there already exists and issue in this project with the same name
			Iterator<CommitIssue> commitIssuesIterator = commit.getCommitIssues().iterator();
			while(commitIssuesIterator.hasNext()) {
				CommitIssue c = commitIssuesIterator.next();

				if(tempCommitIssues.contains(c)) {
					CommitIssue i = tempCommitIssues.get(tempCommitIssues.indexOf(c));
					if(i != null) {
						commitIssuesIterator.remove();
						tempNewCommitIssues.add(i);
					}
				} else {
					commitIssueService.add(c);
					tempCommitIssues.add(c);
				}
			}
			
			commitService.flush();
			for(CommitIssue t : tempNewCommitIssues) {
				commit.addCommitIssueSilently(t);
			}
			commitService.add(commit);
		}
	}
}