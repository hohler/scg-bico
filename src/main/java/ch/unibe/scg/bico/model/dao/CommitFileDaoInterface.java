package ch.unibe.scg.bico.model.dao;

import java.util.List;

import ch.unibe.scg.bico.model.CommitFile;

public interface CommitFileDaoInterface {

	void persist(CommitFile commitFile);

	List<CommitFile> findAll();

	CommitFile findById(Long id);

	void delete(CommitFile commitFile);

	void update(CommitFile commitFile);

}
