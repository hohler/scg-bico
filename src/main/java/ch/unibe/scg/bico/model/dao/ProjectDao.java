package ch.unibe.scg.bico.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ch.unibe.scg.bico.model.Project;

@Repository
public class ProjectDao implements ProjectDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(Project project) {
		em.persist(project);
		em.flush();
	}

	@SuppressWarnings("unchecked")
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

}