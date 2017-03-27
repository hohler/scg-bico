package tool.bico.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import tool.bico.model.ChangeMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;

@Repository
public class ChangeMetricDao implements ChangeMetricDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(ChangeMetric changeMetric) {
		em.persist(changeMetric);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChangeMetric> findAll() {
		return em.createQuery("SELECT c FROM Commit c").getResultList();
	}
	
	@Override
	public ChangeMetric findById(Long id) {
		return em.find(ChangeMetric.class, id);
	}
	
	@Override
	public void delete(ChangeMetric changeMetric) {
		em.remove(changeMetric);
	}
	
	@Override
	public void update(ChangeMetric changeMetric) {
		em.merge(changeMetric);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ChangeMetric> getProjectChangeMetrics(Project project) {
		return em.createQuery("SELECT DISTINCT c from ChangeMetric c LEFT JOIN c.commit i WHERE i.project = :project")
				.setParameter("project", project).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void removeAllByCommit(Commit commit) {
		List<ChangeMetric> list = em.createQuery("SELECT DISTINCT c from ChangeMetric c LEFT JOIN c.commit i WHERE i = :commit")
				.setParameter("commit",  commit).getResultList();
		
		for(ChangeMetric c : list) {
			em.remove(c);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChangeMetric> getChangeMetricsByCommit(Commit commit) {
		return em.createQuery("SELECT c FROM ChangeMetric c WHERE c.commit = :commit")
				.setParameter("commit", commit).getResultList();
	}

	@Override
	public void removeAllByProject(Project project) {
		List<ChangeMetric> list = getProjectChangeMetrics(project);
		
		for(ChangeMetric c : list) {
			em.remove(c);
		}
	}
}