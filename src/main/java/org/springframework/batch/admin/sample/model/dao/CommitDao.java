package org.springframework.batch.admin.sample.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.Project;
import org.springframework.stereotype.Repository;

@Repository
public class CommitDao implements CommitDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(Commit commit) {
		em.persist(commit);
		em.flush();
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
}