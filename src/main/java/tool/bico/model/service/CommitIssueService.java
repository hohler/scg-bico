package tool.bico.model.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tool.bico.model.CommitIssue;
import tool.bico.model.Project;
import tool.bico.model.dao.CommitIssueDaoInterface;


@Service
@Transactional
public class CommitIssueService {

	@Autowired
	private CommitIssueDaoInterface commitIssueDao;

	public CommitIssueService() {
		System.err.println("CommitIssueService bean created!");
	}
	

	@Transactional
	public void add(CommitIssue commitIssue) {
		commitIssueDao.persist(commitIssue);
	}

	@Transactional
	public void addAll(Collection<CommitIssue> commits) {
		for (CommitIssue commitIssue : commits) {
			commitIssueDao.persist(commitIssue);
		}
	}

	@Transactional(readOnly = true)
	public List<CommitIssue> listAll() {
		return commitIssueDao.findAll();

	}
	
	@Transactional(readOnly = true)
	public CommitIssue findById(Long id) {
		return commitIssueDao.findById(id);
	}
	
	@Transactional
	public void delete(CommitIssue commitIssue) {
		commitIssueDao.delete(commitIssue);
	}
	
	@Transactional
	public void update(CommitIssue commitIssue) {
		commitIssueDao.update(commitIssue);
	}
	
	@Transactional
	public void updateAll(List<? extends CommitIssue> commitIssues) {
		commitIssueDao.updateAll(commitIssues);
	}
	
	@Transactional
	public List<CommitIssue> findAllByProject(Project project) {
		return commitIssueDao.findAllByProject(project);
	}
	
	@Transactional
	public CommitIssue findByProjectAndIssueName(Project project, String name) {
		return commitIssueDao.findByProjectAndIssueName(project, name);
	}
	
	@Transactional
	public List<CommitIssue> findAllByProjectAndType(Project project, CommitIssue.Type type) {
		return commitIssueDao.findAllByProjectAndType(project, type);
	}
}
