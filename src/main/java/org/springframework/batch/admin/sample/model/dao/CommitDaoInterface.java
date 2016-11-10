package org.springframework.batch.admin.sample.model.dao;

import java.util.List;

import org.springframework.batch.admin.sample.model.Commit;

public interface CommitDaoInterface {

	void persist(Commit commit);

	List<Commit> findAll();
	
	Commit findById(Long id);

	void delete(Commit commit);

	void update(Commit commit);

}