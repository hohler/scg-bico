package tool.bico.model.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tool.bico.model.CommitIssue.Type;
import tool.bico.model.CommitIssueAnalysis;
import tool.bico.model.Project;
import tool.bico.model.dao.CommitIssueAnalysisDaoInterface;


@Service
@Transactional
public class CommitIssueAnalysisService {

	@Autowired
	private CommitIssueAnalysisDaoInterface commitIssueAnalysisDao;

	public CommitIssueAnalysisService() {
		System.err.println("CommitIssueAnalysisService bean created!");
	}
	

	@Transactional
	public void add(CommitIssueAnalysis commitIssueAnalysis) {
		commitIssueAnalysisDao.persist(commitIssueAnalysis);
	}

	@Transactional
	public void addAll(Collection<CommitIssueAnalysis> commits) {
		for (CommitIssueAnalysis commitIssueAnalysis : commits) {
			commitIssueAnalysisDao.persist(commitIssueAnalysis);
		}
	}

	@Transactional(readOnly = true)
	public List<CommitIssueAnalysis> listAll() {
		return commitIssueAnalysisDao.findAll();

	}
	
	@Transactional(readOnly = true)
	public CommitIssueAnalysis findById(Long id) {
		return commitIssueAnalysisDao.findById(id);
	}
	
	@Transactional
	public void delete(CommitIssueAnalysis commitIssueAnalysis) {
		commitIssueAnalysisDao.delete(commitIssueAnalysis);
	}
	
	@Transactional
	public void update(CommitIssueAnalysis commitIssueAnalysis) {
		commitIssueAnalysisDao.update(commitIssueAnalysis);
	}
	
	@Transactional
	public void updateAll(List<? extends CommitIssueAnalysis> commitIssueAnalysiss) {
		commitIssueAnalysisDao.updateAll(commitIssueAnalysiss);
	}
	
	@Transactional
	public List<CommitIssueAnalysis> findAllByProject(Project project) {
		return commitIssueAnalysisDao.findAllByProject(project);
	}
	
	@Transactional
	public List<CommitIssueAnalysis> findAllByProjectAndType(Project project, Type type) {
		return commitIssueAnalysisDao.findAllByProjectAndType(project, type);
	}
	
	@Transactional
	public void removeAllByProject(Project project) {
		commitIssueAnalysisDao.removeAllByProject(project);
	}
}
