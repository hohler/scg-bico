package org.springframework.batch.admin.sample.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDao implements ProjectDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(Project project) {
		em.persist(project);
	}

	@Override
	public List<Project> findAll() {
		return em.createQuery("SELECT p FROM Project p").getResultList();
	}
	
	@Override
	public Project findById(Long id) {
		return em.find(Project.class, id);
	}
	
	@Override
	public void delete(Project project) {
		em.remove(project);
	}
	
	@Override
	public void update(Project project) {
		em.merge(project);
	}
	/*public void setEntityManager(EntityManager em) {
		this.em = em;
	}*/

}