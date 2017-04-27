package tool.bico.model.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tool.bico.model.SourceMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;
import tool.bico.model.dao.SourceMetricDaoInterface;

@Service
@Transactional
public class SourceMetricService {
	
	@Autowired
	private SourceMetricDaoInterface sourceMetricDao;

	public SourceMetricService() {
		System.err.println("SzzService bean created!");
	}

	@Transactional
	public void add(SourceMetric SourceMetric) {
		sourceMetricDao.persist(SourceMetric);
	}

	@Transactional
	public void addAll(Collection<SourceMetric> SourceMetrics) {
		/*for (SourceMetric SourceMetric : SourceMetrics) {
			SourceMetricDao.persist(SourceMetric);
		}*/
		
		sourceMetricDao.persistAll(SourceMetrics);
	}

	@Transactional(readOnly = true)
	public List<SourceMetric> listAll() {
		return sourceMetricDao.findAll();

	}
	
	@Transactional(readOnly = true)
	public SourceMetric findById(Long id) {
		return sourceMetricDao.findById(id);
	}
	
	@Transactional
	public void delete(SourceMetric SourceMetric) {
		sourceMetricDao.delete(SourceMetric);
	}
	
	@Transactional
	public void update(SourceMetric SourceMetric) {
		sourceMetricDao.update(SourceMetric);
	}
	
	@Transactional(readOnly = true)
	public List<SourceMetric> getProjectSourceMetrics(Project project) {
		return sourceMetricDao.getProjectSourceMetrics(project);
	}
	
	@Transactional
	public void removeAllByCommit(Commit commit) {
		sourceMetricDao.removeAllByCommit(commit);
	}
	
	@Transactional
	public void flush() {
		sourceMetricDao.flush();
	}

	@Transactional(readOnly = true)
	public List<SourceMetric> getSourceMetricsByCommit(Commit commit) {
		return sourceMetricDao.getSourceMetricsByCommit(commit);
	}


	@Transactional
	public void removeAllByProject(Project project) {
		sourceMetricDao.removeAllByProject(project);		
	}

	@Transactional(readOnly = true)
	public List<SourceMetric> findByFileAndProject(String file, Project project) {
		return sourceMetricDao.findByFileAndProject(file, project);
	}
}
