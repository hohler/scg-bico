package ch.unibe.scg.bico.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.model.service.CommitIssueService;

public class CommitWriter implements ItemWriter<CommitIssue> {

	private CommitIssueService commitIssueService;
	
	public CommitWriter(CommitIssueService commitIssueService) {
		this.commitIssueService = commitIssueService;
	}
	
	@Override
	public void write(List<? extends CommitIssue> items) throws Exception {
		for(CommitIssue commitIssue : items) {
			if(commitIssue.getName() == null) {
				commitIssueService.delete(commitIssue);
			} else {
				commitIssueService.update(commitIssue);
			}
		}
	}
}