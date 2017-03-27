package tool.bico.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import tool.bico.model.Commit;
import tool.bico.model.Project;

@Repository
public class CommitDao implements CommitDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(Commit commit) {
		em.persist(commit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Commit> findAll() {
		return em.createQuery("SELECT c FROM Commit c").getResultList();
	}
	
	@Override
	public Commit findById(Long id) {
		return em.find(Commit.class, id);
	}
	
	@Override
	public void delete(Commit commit) {
		em.remove(commit);
	}
	
	@Override
	public void update(Commit commit) {
		em.merge(commit);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Commit> getProjectCommits(Project project) {
		return em.createQuery("SELECT c from  Commit c WHERE c.project = :project_id")
		.setParameter("project_id", project).getResultList();
	}
	
	@Override
	public void flush() {
		em.flush();
	}

	@Override
	public Commit getCommitByRef(String ref) {
		return (Commit) em.createQuery("SELECT c FROM Commit c WHERE c.ref = :ref")
				.setParameter("ref", ref).getSingleResult();
	}
}