package org.springframework.batch.admin.sample.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
public class ProjectDao {

	@PersistenceContext
	private EntityManager em;

	public void persist(Project project) {
		em.persist(project);
	}

	public List<Project> findAll() {
		return em.createQuery("SELECT p FROM Project p").getResultList();
	}
	
	/*public void setEntityManager(EntityManager em) {
		this.em = em;
	}*/

}