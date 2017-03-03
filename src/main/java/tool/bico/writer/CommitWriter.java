package tool.bico.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.service.CommitIssueService;

public class CommitWriter implements ItemWriter<CommitIssue> {

	private CommitIssueService commitIssueService;
	
	public CommitWriter(CommitIssueService commitIssueService) {
		this.commitIssueService = commitIssueService;
	}
	
	@Override
	public void write(List<? extends CommitIssue> items) throws Exception {
		for(CommitIssue commitIssue : items) {
			/*if(commitIssue.getName() == null) {
				//commitIssue.getCommits().clear();
				System.err.println("trying to delete); remove first issue from commits");
				for(Commit c : commitIssue.getCommits()) {
					c.removeCommitIssue(commitIssue);
				}
				System.err.println("trying to clear commits list in issue");
				commitIssue.getCommits().clear();
				System.err.println("start to delete");
				commitIssueService.delete(commitIssue);
				System.err.println("DELETE SUCCESSFULL");
			} else {
				commitIssueService.update(commitIssue);
			}*/
			commitIssueService.update(commitIssue);
		}
	}
}