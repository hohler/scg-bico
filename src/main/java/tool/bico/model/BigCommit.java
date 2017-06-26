package tool.bico.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bigcommits")
public class BigCommit {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Project project;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Commit commit;
	
	private CommitIssue.Type issueType;
	
	public BigCommit() {}
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Commit getCommit() {
		return commit;
	}
	
	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}


	public CommitIssue.Type getIssueType() {
		return issueType;
	}


	public void setIssueType(CommitIssue.Type issueType) {
		this.issueType = issueType;
	}
	
	public String toString() {
		return "BigCommit ["+id+", type: "+issueType+"]";
	}
}
