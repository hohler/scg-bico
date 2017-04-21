package tool.bico.model.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import tool.bico.model.SzzMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;

@Repository
public class SzzMetricDao implements SzzMetricDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(SzzMetric SzzMetric) {
		em.persist(SzzMetric);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SzzMetric> findAll() {
		return em.createQuery("SELECT c FROM Commit c").getResultList();
	}
	
	@Override
	public SzzMetric findById(Long id) {
		return em.find(SzzMetric.class, id);
	}
	
	@Override
	public void delete(SzzMetric SzzMetric) {
		em.remove(SzzMetric);
	}
	
	@Override
	public void update(SzzMetric SzzMetric) {
		em.merge(SzzMetric);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SzzMetric> getProjectSzzMetrics(Project project) {
		return em.createQuery("SELECT DISTINCT c from SzzMetric c LEFT JOIN c.commit i WHERE i.project = :project")
				.setParameter("project", project).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void removeAllByCommit(Commit commit) {
		List<SzzMetric> list = em.createQuery("SELECT DISTINCT c from SzzMetric c LEFT JOIN c.commit i WHERE i = :commit")
				.setParameter("commit",  commit).getResultList();
		
		for(SzzMetric c : list) {
			em.remove(c);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SzzMetric> getSzzMetricsByCommit(Commit commit) {
		return em.createQuery("SELECT c FROM SzzMetric c WHERE c.commit = :commit")
				.setParameter("commit", commit).getResultList();
	}

	@Override
	public void removeAllByProject(Project project) {
		List<SzzMetric> list = getProjectSzzMetrics(project);
		
		for(SzzMetric c : list) {
			em.remove(c);
		}
	}

	@Override
	public void persistAll(Collection<SzzMetric> SzzMetrics) {
		for(SzzMetric cm : SzzMetrics) {
			em.persist(cm);
		}
		em.flush();
	}

	@Override
	public void flush() {
		em.flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SzzMetric> findByFileAndProject(String file, Project project) {
		return em.createQuery("SELECT DISTINCT c from SzzMetric c LEFT JOIN c.commit i WHERE i.project = :project AND c.file = :file")
				.setParameter("project", project)
				.setParameter("file", file).getResultList();
	}
}