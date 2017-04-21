package tool.bico.model.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tool.bico.model.SzzMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;
import tool.bico.model.dao.SzzMetricDaoInterface;

@Service
@Transactional
public class SzzMetricService {
	
	@Autowired
	private SzzMetricDaoInterface szzDao;

	public SzzMetricService() {
		System.err.println("SzzService bean created!");
	}

	@Transactional
	public void add(SzzMetric SzzMetric) {
		szzDao.persist(SzzMetric);
	}

	@Transactional
	public void addAll(Collection<SzzMetric> SzzMetrics) {
		/*for (SzzMetric SzzMetric : SzzMetrics) {
			SzzMetricDao.persist(SzzMetric);
		}*/
		
		szzDao.persistAll(SzzMetrics);
	}

	@Transactional(readOnly = true)
	public List<SzzMetric> listAll() {
		return szzDao.findAll();

	}
	
	@Transactional(readOnly = true)
	public SzzMetric findById(Long id) {
		return szzDao.findById(id);
	}
	
	@Transactional
	public void delete(SzzMetric SzzMetric) {
		szzDao.delete(SzzMetric);
	}
	
	@Transactional
	public void update(SzzMetric SzzMetric) {
		szzDao.update(SzzMetric);
	}
	
	@Transactional(readOnly = true)
	public List<SzzMetric> getProjectSzzMetrics(Project project) {
		return szzDao.getProjectSzzMetrics(project);
	}
	
	@Transactional
	public void removeAllByCommit(Commit commit) {
		szzDao.removeAllByCommit(commit);
	}
	
	@Transactional
	public void flush() {
		szzDao.flush();
	}

	@Transactional(readOnly = true)
	public List<SzzMetric> getSzzMetricsByCommit(Commit commit) {
		return szzDao.getSzzMetricsByCommit(commit);
	}


	@Transactional
	public void removeAllByProject(Project project) {
		szzDao.removeAllByProject(project);		
	}

	@Transactional(readOnly = true)
	public List<SzzMetric> findByFileAndProject(String file, Project project) {
		return szzDao.findByFileAndProject(file, project);
	}
}
