package tool.bico.model.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import tool.bico.model.BigCommit;
import tool.bico.model.Project;

@Repository
public class BigCommitDao implements BigCommitDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(BigCommit bigCommit) {
		em.persist(bigCommit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BigCommit> findAll() {
		return em.createQuery("SELECT b FROM BigCommit b").getResultList();
	}
	
	@Override
	public BigCommit findById(Long id) {
		return em.find(BigCommit.class, id);
	}
	
	@Override
	public void delete(BigCommit bigCommit) {
		em.remove(bigCommit);
	}
	
	@Override
	public void update(BigCommit bigCommit) {
		em.merge(bigCommit);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BigCommit> getProjectBigCommits(Project project) {
		return em.createQuery("SELECT b from BigCommit b WHERE b.project = :project")
				.setParameter("project", project).getResultList();
	}

	@Override
	public void removeAllByProject(Project project) {
		List<BigCommit> list = getProjectBigCommits(project);
		
		for(BigCommit b : list) {
			em.remove(b);
		}
	}

	@Override
	public void persistAll(Collection<BigCommit> bigCommits) {
		for(BigCommit b : bigCommits) {
			em.persist(b);
		}
		em.flush();
	}

	@Override
	public void flush() {
		em.flush();
	}

	@Override
	public BigCommit findByRefAndProject(String ref, Project project) {
		return (BigCommit) em.createQuery("SELECT b from BigCommit b LEFT JOIN b.commit i WHERE i.project = :project AND b.ref = :ref")
				.setParameter("project", project)
				.setParameter("ref", ref).getSingleResult();
	}
}