package tool.bico.reader;

import javax.persistence.EntityManager;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import tool.bico.model.Project;

public class CommitQueryProvider implements JpaQueryProvider {

	private EntityManager entityManager;
	private Project project;
	
	public CommitQueryProvider(Project project) {
		this.project = project;
	}

	@Override
	public javax.persistence.Query createQuery() {
		return entityManager.createQuery("SELECT c from CommitIssue c LEFT JOIN c.commits i WHERE i.project = :project AND c.processed = false")
				.setParameter("project", this.project);
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		
	}

}
