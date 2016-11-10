package org.springframework.batch.admin.sample.model.service;

import java.util.Collection;
import java.util.List;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.dao.CommitDaoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CommitService {

	@Autowired
	private CommitDaoInterface commitDao;

	public CommitService() {
		System.err.println("CommitService bean created!");
	}
	

	@Transactional
	public void add(Commit commit) {
		commitDao.persist(commit);
	}

	@Transactional
	public void addAll(Collection<Commit> commits) {
		for (Commit commit : commits) {
			commitDao.persist(commit);
		}
	}

	@Transactional(readOnly = true)
	public List<Commit> listAll() {
		return commitDao.findAll();

	}
	
	@Transactional(readOnly = true)
	public Commit findById(Long id) {
		return commitDao.findById(id);
	}
	
	@Transactional
	public void delete(Commit commit) {
		commitDao.delete(commit);
	}
	
	@Transactional
	public void update(Commit commit) {
		commitDao.update(commit);
	}

}
