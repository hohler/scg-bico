package tool.bico.repository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;

import tool.bico.model.Commit;
import tool.bico.model.CommitFile;
import tool.bico.model.Project;

public class GitRepository implements IRepository {

	private Repository repository;
	private Project project;
	
	public GitRepository(Project project) {
		this.project = project;
		GitLoader gitLoader = new GitLoader(project.getUrl(), project.getBranch());
		boolean result = gitLoader.init();
		if(result) init(gitLoader);
		else System.err.println("Could not load git repository");
	}
	
	public GitRepository(String url, String branch) {
		GitLoader gitLoader = new GitLoader(url, branch);
		boolean result = gitLoader.init();
		if(result) init(gitLoader);
		else System.err.println("Could not load git repository");
	}
	
	public GitRepository(GitLoader gitLoader) {
		init(gitLoader);
	}
	
	private void init(GitLoader gitLoader) {
		RepositoryBuilder builder = new RepositoryBuilder();
		try {
			repository = builder.setGitDir( new File(gitLoader.getRepoDir().getPath() + "/.git/") )
					.readEnvironment()
					.findGitDir()
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Iterator<Commit> getCommitIterator() {
		
		Git git = new Git(repository);
		
		try {
			Iterable<RevCommit> commits = git.log().call();
			Stack<RevCommit> commits_reversed = new Stack<>();
			commits.forEach(commits_reversed::push);
			
			GitRepositoryCommitIterator it = new GitRepositoryCommitIterator(repository, commits_reversed);
			
			git.close();
			
			return it;
		} catch (GitAPIException e1) {
			System.err.println("Git API fail");
			e1.printStackTrace();
		}
		git.close();
		
		// returns iterator that returns null on "next" and false on "hasNext"
		ArrayList<Commit> fail = new ArrayList<Commit>();
		return fail.iterator();
	}
	
	public ArrayList<Commit> getCommits() {
		
		ArrayList<Commit> result = new ArrayList<Commit>();
		Git git = new Git(repository);
		
		try {
			Iterable<RevCommit> commits = git.log().call();
			Stack<RevCommit> commits_reversed = new Stack<>();
			commits.forEach(commits_reversed::push);
			
			
			Commit parent = null;
			
			while(!commits_reversed.isEmpty()) {
				RevCommit commit = commits_reversed.pop();
				
				// branch itself
				if(commit.getParentCount() == 0) continue;
				
				Commit c = new Commit();
				result.add(c);
				c.setMessage(commit.getFullMessage());
				c.setParentCommit(parent);
				c.setRef(commit.getName());
				project.addCommit(c);
				int commitAdditions = 0;
				int commitDeletions = 0;
				
				parent = c;
				
				System.out.println("commit: "+commit.getShortMessage());

				RevCommit parentCommit = commit.getParent(0);
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				
			    try (DiffFormatter diffFormatter = new DiffFormatter(out)) {
			        diffFormatter.setRepository(repository);
			        diffFormatter.setDetectRenames(true);
			        for (DiffEntry entry : diffFormatter.scan(parentCommit, commit)) {
			            //diffFormatter.format(diffFormatter.toFileHeader(entry));
			            diffFormatter.format(entry);
			            out.flush();
			            String patch = out.toString();
			            out.reset();
			            
			            //analyse patch
			            int linesDeleted = 0;
			            int linesAdded = 0;
			            for (Edit edit : diffFormatter.toFileHeader(entry).toEditList()) {
			                linesDeleted += edit.getEndA() - edit.getBeginA();
			                linesAdded += edit.getEndB() - edit.getBeginB();
			            }
			            
			            commitDeletions += linesDeleted;
			            commitAdditions += linesAdded;
			            
			            CommitFile cf = new CommitFile();
			            cf.setChangeType(entry.getChangeType());
			            cf.setPatch(patch);
			            cf.setOldPath(entry.getOldPath());
			            cf.setNewPath(entry.getNewPath());
			            cf.setAdditions(linesAdded);
			            cf.setDeletions(linesDeleted);
			            c.addFile(cf);
			            
			        }
			    }
			    
			    c.setAdditions(commitAdditions);
			    c.setDeletions(commitDeletions);
			}
			
		} catch (GitAPIException e1) {
			System.err.println("Git API fail");
			e1.printStackTrace();
		} catch (IOException e) {
	    	System.err.println("Could not diff commits");
			e.printStackTrace();
		}
		
		return result;
	}
	
	
}
