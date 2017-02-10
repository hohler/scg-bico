package tool.bico.model.dao;

import java.util.List;

import tool.bico.model.CommitFile;

public interface CommitFileDaoInterface {

	void persist(CommitFile commitFile);

	List<CommitFile> findAll();

	CommitFile findById(Long id);

	void delete(CommitFile commitFile);

	void update(CommitFile commitFile);

}
