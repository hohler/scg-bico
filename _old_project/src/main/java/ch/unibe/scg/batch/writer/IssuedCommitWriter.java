package ch.unibe.scg.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import ch.unibe.scg.model.IssuedCommit;

public class IssuedCommitWriter implements ItemWriter<IssuedCommit> {

	@Override
	public void write(List<? extends IssuedCommit> items) throws Exception {
		for(IssuedCommit c : items) {
			System.out.println("writing commit issue " + c.getCommitIssue());
		}
	}
	
}
