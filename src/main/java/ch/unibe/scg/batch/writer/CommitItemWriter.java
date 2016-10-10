package ch.unibe.scg.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import ch.unibe.scg.model.Commit;

public class CommitItemWriter implements ItemWriter<Commit> {

	@Override
	public void write(List<? extends Commit> items) throws Exception {
		// TODO Auto-generated method stub
		for(Commit c : items) {
			System.out.println("writing commit " + c);
		}
	}	
}