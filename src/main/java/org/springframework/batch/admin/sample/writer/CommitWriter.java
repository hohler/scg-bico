package org.springframework.batch.admin.sample.writer;

import java.util.List;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.service.CommitService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

public class CommitWriter implements ItemWriter<Commit> {

	@Autowired
	private CommitService commitService;
	
	@Override
	public void write(List<? extends Commit> items) throws Exception {
		/*for(Commit c : items) {
			System.out.println("writing commit issue " + c.getCommitIssue());
		}*/
		for(Commit commit : items) {
			commitService.update(commit);
		}
	}
}