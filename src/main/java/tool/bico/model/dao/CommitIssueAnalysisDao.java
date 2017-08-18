package tool.bico.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import tool.bico.model.CommitIssue.Type;
import tool.bico.model.CommitIssueAnalysis;
import tool.bico.model.Project;

@Repository
public class CommitIssueAnalysisDao implements CommitIssueAnalysisDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(CommitIssueAnalysis commitIssueAnalysis) {
		em.persist(commitIssueAnalysis);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommitIssueAnalysis> findAll() {
		return em.createQuery("SELECT c FROM CommitIssueAnalysis c").getResultList();
	}
	
	@Override
	public CommitIssueAnalysis findById(Long id) {
		return em.find(CommitIssueAnalysis.class, id);
	}
	
	@Override
	public void delete(CommitIssueAnalysis commitIssueAnalysis) {
		if(!em.contains(commitIssueAnalysis)) {
			CommitIssueAnalysis c  = em.merge(commitIssueAnalysis);
			em.remove(c);
		} else {
			em.remove(commitIssueAnalysis);
		}
	}
	
	@Override
	public void update(CommitIssueAnalysis commitIssueAnalysis) {
		em.merge(commitIssueAnalysis);
	}

	@Override
	public void updateAll(List<? extends CommitIssueAnalysis> commitIssueAnalysiss) {
		for(CommitIssueAnalysis ci : commitIssueAnalysiss) {
			em.merge(ci);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CommitIssueAnalysis> findAllByProject(Project project) {
		return em.createQuery("SELECT c from CommitIssueAnalysis c WHERE c.project = :project")
				.setParameter("project", project).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommitIssueAnalysis> findAllByProjectAndType(Project project, Type type) {
		return em.createQuery("SELECT c from CommitIssueAnalysis c WHERE c.project = :project AND c.type = :type")
				.setParameter("project", project)
				.setParameter("type", type).getResultList();
	}

	@Override
	public void removeAllByProject(Project project) {
		em.createQuery("DELETE from CommitIssueAnalysis WHERE project = :project")
		.setParameter("project", project);
		
	}
}