package tool.bico.model.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import tool.bico.model.SourceMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;

@Repository
public class SourceMetricDao implements SourceMetricDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(SourceMetric SourceMetric) {
		em.persist(SourceMetric);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SourceMetric> findAll() {
		return em.createQuery("SELECT c FROM Commit c").getResultList();
	}
	
	@Override
	public SourceMetric findById(Long id) {
		return em.find(SourceMetric.class, id);
	}
	
	@Override
	public void delete(SourceMetric SourceMetric) {
		em.remove(SourceMetric);
	}
	
	@Override
	public void update(SourceMetric SourceMetric) {
		em.merge(SourceMetric);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SourceMetric> getProjectSourceMetrics(Project project) {
		return em.createQuery("SELECT DISTINCT c from SourceMetric c LEFT JOIN c.commit i WHERE i.project = :project")
				.setParameter("project", project).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void removeAllByCommit(Commit commit) {
		List<SourceMetric> list = em.createQuery("SELECT DISTINCT c from SourceMetric c LEFT JOIN c.commit i WHERE i = :commit")
				.setParameter("commit",  commit).getResultList();
		
		for(SourceMetric c : list) {
			em.remove(c);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SourceMetric> getSourceMetricsByCommit(Commit commit) {
		return em.createQuery("SELECT c FROM SourceMetric c WHERE c.commit = :commit")
				.setParameter("commit", commit).getResultList();
	}

	@Override
	public void removeAllByProject(Project project) {
		List<SourceMetric> list = getProjectSourceMetrics(project);
		
		for(SourceMetric c : list) {
			em.remove(c);
		}
	}

	@Override
	public void persistAll(Collection<SourceMetric> SourceMetrics) {
		for(SourceMetric cm : SourceMetrics) {
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
	public List<SourceMetric> findByFileAndProject(String file, Project project) {
		return em.createQuery("SELECT DISTINCT c from SourceMetric c LEFT JOIN c.commit i WHERE i.project = :project AND c.file = :file")
				.setParameter("project", project)
				.setParameter("file", file).getResultList();
	}
}