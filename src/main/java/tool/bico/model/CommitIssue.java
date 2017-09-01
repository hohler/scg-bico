package tool.bico.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ForeignKey;
import javax.persistence.ConstraintMode;

@Entity
@Table(name="commitissues")
public class CommitIssue {

	public enum Priority {
		BLOCKER,
		CRITICAL,
		MAJOR,
		MINOR,
		TRIVIAL,
		OTHER,
		NA;
		
		private String formatName() {
			String name = name();
			return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		}
		public String getName() {
			return formatName();
	    }
		
		public String toString() {
			return formatName();
		}
	}
	
	public enum Type {
		ACCESS,
		BUG,
		DEPENDENCY_UPGRADE,
		DOCUMENTATION,
		IMPROVEMENT,
		REQUEST,
		TASK,
		TEST,
		FEATURE,
		WISH,
		OTHER,
		SUBTASK,
		DEPRECATION,
		REFACTOR,
		MERGE,
		NA;
		
		private String formatName() {
			String name = name();
			return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		}
	    public String getName() {
	    	return formatName();
	    }
	    
	    public String toString() {
	    	return formatName();
	    }
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany(targetEntity = Commit.class, fetch = FetchType.LAZY, cascade={CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(name = "commits_commitissues",
    joinColumns = @JoinColumn(name = "commitissue_id",
            nullable = false,
            updatable = false),
    inverseJoinColumns = @JoinColumn(name = "commit_id",
            nullable = false,
            updatable = false),
    foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
    inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
	@OrderBy("id ASC")
	protected Set<Commit> commits;
	
	private Type type = Type.NA;
	
	private Priority priority = Priority.NA;
	
	@Column(nullable = true)
	private String name = "";
	
	@Column(nullable = true)
	private String link = "";
	
	@Column(nullable = true, columnDefinition = "TEXT")
	private String description = "";
	
	private boolean processed = false;
	
	@Column(nullable = true)
	private String typeByClassifier;
	
	private int classifierScore = 0;
	
	public CommitIssue() {
		this.commits = new HashSet<Commit>();
	}
	
	public CommitIssue(String name) {
		this.name = name;
		this.commits = new HashSet<Commit>();
	}
	
	public CommitIssue(String name, Type type, Priority priority) {
		this.name = name;
		this.type = type;
		this.priority = priority;
		this.commits = new HashSet<Commit>();
	}
	
	public CommitIssue(CommitIssue issue) {
		type = issue.getType();
		priority = issue.getPriority();
		name = issue.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}
	
	public String getStringType() {
		return type.getName();
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Priority getPriority() {
		return priority;
	}
	
	public String getStringPriority() {
		return priority.getName();
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public Set<Commit> getCommits() {
		return commits;
	}

	/*public void setCommit(Commit commit) {
		this.commit = commit;
	}*/
	
	public void addCommit(Commit commit) {
		this.commits.add(commit);
	}
	
	public void removeCommit(Commit commit) {
		this.commits.remove(commit);
	}

	public Long getId() {
		return id;
	}
	

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return "Issue[id="+id+", name="+name+", type="+type+", priority="+priority+"]";
	}
	
	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode()+getType().hashCode()+getPriority().hashCode()+getDescription().hashCode();
	}

	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof CommitIssue)) return false;
	    CommitIssue that = (CommitIssue) other;
	    if(getName() != null && getName().length() > 0 && that.getName() != null && that.getName().length() > 0) {
	    	if(getName().equals(that.getName()) && hashCode() == that.hashCode()) return true;
	    }
	    return false;
	}

	public String getTypeByClassifier() {
		return typeByClassifier;
	}

	public void setTypeByClassifier(String typeByClassifier) {
		this.typeByClassifier = typeByClassifier;
	}

	public int getClassifierScore() {
		return classifierScore;
	}

	public void setClassifierScore(int classifierScore) {
		this.classifierScore = classifierScore;
	}
}