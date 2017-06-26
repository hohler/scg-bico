package tool.bico.model.dao;

import java.util.Collection;
import java.util.List;

import tool.bico.model.SzzMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;

public interface SzzMetricDaoInterface {

	void persist(SzzMetric SzzMetric);

	List<SzzMetric> findAll();
	
	SzzMetric findById(Long id);

	void delete(SzzMetric SzzMetric);

	void update(SzzMetric SzzMetric);

	List<SzzMetric> getProjectSzzMetrics(Project project);
	
	void removeAllByCommit(Commit commit);

	List<SzzMetric> getSzzMetricsByCommit(Commit commit);

	void removeAllByProject(Project project);

	void persistAll(Collection<SzzMetric> SzzMetrics);
	
	void flush();

	List<SzzMetric> findByFileAndProject(String file, Project project);
}