package tool.bico.writer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.service.CommitIssueService;
import tool.bico.model.service.CommitService;

public class RepositoryWriter implements ItemWriter<Commit> {

	private CommitService commitService;
	
	private List<CommitIssue> tempCommitIssues;

	public RepositoryWriter(CommitService commitService) {
		this.commitService = commitService;
		this.tempCommitIssues = new ArrayList<CommitIssue>();
	}
	
	@Override
	public void write(List<? extends Commit> items) throws Exception {
		for(Commit commit : items) {
			// Check if there already exists and issue in this project with the same name
			for(CommitIssue c : commit.getCommitIssues()) {
				//CommitIssue i = commitIssueService.findByProjectAndIssueName(commit.getProject(), c.getName());
				if(tempCommitIssues.contains(c)) {
					CommitIssue i = tempCommitIssues.get(tempCommitIssues.indexOf(c));
					if(i != null) {
						commit.removeCommitIssue(c);
						commit.addCommitIssue(i);
					}
				} else {
					tempCommitIssues.add(c);
				}
			}
			commitService.add(commit);
		}
	}
}