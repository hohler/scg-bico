package org.springframework.batch.admin.sample.writer;

import java.util.List;

import org.springframework.batch.admin.sample.model.IssuedCommit;
import org.springframework.batch.item.ItemWriter;

public class IssuedCommitWriter implements ItemWriter<IssuedCommit> {

	@Override
	public void write(List<? extends IssuedCommit> items) throws Exception {
		for(IssuedCommit c : items) {
			System.out.println("writing commit issue " + c.getCommitIssue());
		}
	}
	
}
