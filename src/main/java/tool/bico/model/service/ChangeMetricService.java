package tool.bico.model.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tool.bico.model.ChangeMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;
import tool.bico.model.dao.ChangeMetricDaoInterface;

@Service
@Transactional
public class ChangeMetricService {
	
	@Autowired
	private ChangeMetricDaoInterface changeMetricDao;

	public ChangeMetricService() {
		System.err.println("ChangeMetricService bean created!");
	}
	

	@Transactional
	public void add(ChangeMetric changeMetric) {
		changeMetricDao.persist(changeMetric);
	}

	@Transactional
	public void addAll(Collection<ChangeMetric> changeMetrics) {
		/*for (ChangeMetric changeMetric : changeMetrics) {
			changeMetricDao.persist(changeMetric);
		}*/
		
		changeMetricDao.persistAll(changeMetrics);
	}

	@Transactional(readOnly = true)
	public List<ChangeMetric> listAll() {
		return changeMetricDao.findAll();

	}
	
	@Transactional(readOnly = true)
	public ChangeMetric findById(Long id) {
		return changeMetricDao.findById(id);
	}
	
	@Transactional
	public void delete(ChangeMetric changeMetric) {
		changeMetricDao.delete(changeMetric);
	}
	
	@Transactional
	public void update(ChangeMetric changeMetric) {
		changeMetricDao.update(changeMetric);
	}
	
	@Transactional(readOnly = true)
	public List<ChangeMetric> getProjectChangeMetrics(Project project) {
		return changeMetricDao.getProjectChangeMetrics(project);
	}
	
	@Transactional
	public void removeAllByCommit(Commit commit) {
		changeMetricDao.removeAllByCommit(commit);
	}
	
	@Transactional
	public void flush() {
		changeMetricDao.flush();
	}

	@Transactional(readOnly = true)
	public List<ChangeMetric> getChangeMetricsByCommit(Commit commit) {
		return changeMetricDao.getChangeMetricsByCommit(commit);
	}


	@Transactional
	public void removeAllByProject(Project project) {
		changeMetricDao.removeAllByProject(project);		
	}
}
