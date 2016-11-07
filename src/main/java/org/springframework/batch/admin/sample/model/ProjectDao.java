package org.springframework.batch.admin.sample.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
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
	
	/*public void setEntityManager(EntityManager em) {
		this.em = em;
	}*/

}