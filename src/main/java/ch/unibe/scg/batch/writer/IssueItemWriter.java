package ch.unibe.scg.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import ch.unibe.scg.model.CommitIssue;

public class IssueItemWriter implements ItemWriter<CommitIssue> {

	@Override
	public void write(List<? extends CommitIssue> items) throws Exception {
		// TODO Auto-generated method stub
		for(CommitIssue c : items) {
			System.out.println("writing commit issue " + c);
		}
	}
	
}
