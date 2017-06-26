package tool.bico.model.dao;

import java.util.Collection;
import java.util.List;

import tool.bico.model.SourceMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;

public interface SourceMetricDaoInterface {

	void persist(SourceMetric SourceMetric);

	List<SourceMetric> findAll();
	
	SourceMetric findById(Long id);

	void delete(SourceMetric SourceMetric);

	void update(SourceMetric SourceMetric);

	List<SourceMetric> getProjectSourceMetrics(Project project);
	
	void removeAllByCommit(Commit commit);

	List<SourceMetric> getSourceMetricsByCommit(Commit commit);

	void removeAllByProject(Project project);

	void persistAll(Collection<SourceMetric> SourceMetrics);
	
	void flush();

	List<SourceMetric> findByFileAndProject(String file, Project project);
}