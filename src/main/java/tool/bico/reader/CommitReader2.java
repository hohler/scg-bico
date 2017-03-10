package tool.bico.reader;

import javax.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;

import tool.bico.model.CommitIssue;
import tool.bico.model.Project;

public class CommitReader2 extends JpaPagingItemReader<CommitIssue> {


	public CommitReader2(Project project, EntityManagerFactory entityManagerFactory) {

		this.setEntityManagerFactory(entityManagerFactory);
	
		CommitQueryProvider h = new CommitQueryProvider(project);
		h.setEntityManager(entityManagerFactory.createEntityManager());
		this.setQueryProvider(h);
		this.setSaveState(false);
	}
	
}