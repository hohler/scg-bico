package org.springframework.batch.admin.sample.writer;

import java.util.List;

import org.springframework.batch.admin.sample.model.CommitIssue;
import org.springframework.batch.admin.sample.model.service.CommitIssueService;
import org.springframework.batch.item.ItemWriter;

public class CommitWriter implements ItemWriter<CommitIssue> {

	private CommitIssueService commitIssueService;
	
	public CommitWriter(CommitIssueService commitIssueService) {
		this.commitIssueService = commitIssueService;
	}
	
	@Override
	public void write(List<? extends CommitIssue> items) throws Exception {
		for(CommitIssue commitIssue : items) {
			commitIssueService.update(commitIssue);
		}
	}
}