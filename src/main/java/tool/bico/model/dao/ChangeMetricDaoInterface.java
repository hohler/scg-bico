package tool.bico.model.dao;

import java.util.List;

import tool.bico.model.ChangeMetric;
import tool.bico.model.Commit;
import tool.bico.model.Project;

public interface ChangeMetricDaoInterface {

	void persist(ChangeMetric changeMetric);

	List<ChangeMetric> findAll();
	
	ChangeMetric findById(Long id);

	void delete(ChangeMetric changeMetric);

	void update(ChangeMetric changeMetric);

	List<ChangeMetric> getProjectChangeMetrics(Project project);
	
	void removeAllByCommit(Commit commit);
}