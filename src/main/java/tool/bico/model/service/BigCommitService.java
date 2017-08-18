package tool.bico.model.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tool.bico.model.BigCommit;
import tool.bico.model.Project;
import tool.bico.model.dao.BigCommitDaoInterface;

@Service
@Transactional
public class BigCommitService {
	
	@Autowired
	private BigCommitDaoInterface bcDao;

	public BigCommitService() {
		System.err.println("SzzService bean created!");
	}

	@Transactional
	public void add(BigCommit bigCommit) {
		bcDao.persist(bigCommit);
	}

	@Transactional
	public void addAll(Collection<BigCommit> bigCommits) {
		/*for (SzzMetric SzzMetric : SzzMetrics) {
			SzzMetricDao.persist(SzzMetric);
		}*/
		
		bcDao.persistAll(bigCommits);
	}

	@Transactional(readOnly = true)
	public List<BigCommit> listAll() {
		return bcDao.findAll();

	}
	
	@Transactional(readOnly = true)
	public BigCommit findById(Long id) {
		return bcDao.findById(id);
	}
	
	@Transactional
	public void delete(BigCommit bigCommit) {
		bcDao.delete(bigCommit);
	}
	
	@Transactional
	public void update(BigCommit bigCommit) {
		bcDao.update(bigCommit);
	}
	
	@Transactional(readOnly = true)
	public List<BigCommit> getProjectBigCommits(Project project) {
		return bcDao.getProjectBigCommits(project);
	}
	
	@Transactional
	public void flush() {
		bcDao.flush();
	}

	@Transactional
	public void removeAllByProject(Project project) {
		bcDao.removeAllByProject(project);		
	}

	@Transactional(readOnly = true)
	public BigCommit findByRefAndProject(String ref, Project project) {
		return bcDao.findByRefAndProject(ref, project);
	}
}
