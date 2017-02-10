package tool.bico.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import tool.bico.model.CommitFile;

@Repository
public class CommitFileDao implements CommitFileDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(CommitFile commitFile) {
		em.persist(commitFile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommitFile> findAll() {
		return em.createQuery("SELECT c FROM CommitFile c").getResultList();
	}
	
	@Override
	public CommitFile findById(Long id) {
		return em.find(CommitFile.class, id);
	}
	
	@Override
	public void delete(CommitFile commitFile) {
		em.remove(commitFile);
	}
	
	@Override
	public void update(CommitFile commitFile) {
		em.merge(commitFile);
	}
}