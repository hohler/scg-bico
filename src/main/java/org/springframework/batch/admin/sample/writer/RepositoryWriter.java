package org.springframework.batch.admin.sample.writer;

import java.util.List;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.service.CommitService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public class RepositoryWriter implements ItemWriter<Commit> {

	//private StepExecution stepExecution;
	
	@Autowired
	@Lazy
	private CommitService commitService;

	@Override
	public void write(List<? extends Commit> items) throws Exception {
		
		/*ExecutionContext stepContext = this.stepExecution.getExecutionContext();
        
		Object obj = stepContext.get("issuedCommits");
		if(obj == null) stepContext.put("issuedCommits",  items);
		else {
			List<IssuedCommit> list = new ArrayList<IssuedCommit>((List<IssuedCommit>) obj);
			list.addAll(items);
			stepContext.put("issuedCommits",  list);
		}*/
		
		System.out.println("commitservice:" + commitService);
		for(Commit commit : items) {
			commitService.add(commit);
		}
		
		//commitService.addAll((List<Commit>)items);
		
		//stepContext.put("issuedCommits", items);
        
		// TODO Auto-generated method stub
		/*for(IssuedCommit c : items) {
			System.out.println("writing commit " + c);
		}*/
		
		/*System.err.println("OH:");
		System.out.println(stepContext.get("issuedCommits"));
		System.err.println("OH:");
		System.out.println(((List<IssuedCommit>)stepContext.get("issuedCommits")).size());*/
	}
	
	/*@BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }*/
}