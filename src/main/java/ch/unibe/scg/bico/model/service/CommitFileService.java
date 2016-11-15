package ch.unibe.scg.bico.model.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.scg.bico.model.CommitFile;
import ch.unibe.scg.bico.model.dao.CommitFileDaoInterface;


@Service
@Transactional
public class CommitFileService {

	@Autowired
	private CommitFileDaoInterface commitFileDao;

	public CommitFileService() {
		System.err.println("CommitFileService bean created!");
	}
	

	@Transactional
	public void add(CommitFile commitFile) {
		commitFileDao.persist(commitFile);
	}

	@Transactional
	public void addAll(Collection<CommitFile> commitFiles) {
		for (CommitFile commitFile : commitFiles) {
			commitFileDao.persist(commitFile);
		}
	}

	@Transactional(readOnly = true)
	public List<CommitFile> listAll() {
		return commitFileDao.findAll();

	}
	
	@Transactional(readOnly = true)
	public CommitFile findById(Long id) {
		return commitFileDao.findById(id);
	}
	
	@Transactional
	public void delete(CommitFile commitFile) {
		commitFileDao.delete(commitFile);
	}
	
	@Transactional
	public void update(CommitFile commitFile) {
		commitFileDao.update(commitFile);
	}
}
