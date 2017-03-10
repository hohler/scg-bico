package tool.bico.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.service.CommitIssueService;
import tool.bico.model.service.CommitService;

public class CommitWriter implements ItemWriter<CommitIssue> {

	private CommitService commitService;
	private CommitIssueService commitIssueService;
	
	public CommitWriter(CommitService commitService, CommitIssueService commitIssueService) {
		this.commitService = commitService;
		this.commitIssueService = commitIssueService;
	}
	
	@Override
	public void write(List<? extends CommitIssue> items) throws Exception {
		for(CommitIssue commitIssue : items) {	
			if(commitIssue.getName() == null) {
				for(Commit c : commitIssue.getCommits()) {
					c.removeCommitIssue(commitIssue);
					commitService.update(c);
				}
				
				commitIssueService.delete(commitIssue);
			} else {
				commitIssueService.update(commitIssue);
			}
		}
	}
}