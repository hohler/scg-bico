package org.springframework.batch.admin.sample.repository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.CommitFile;
import org.springframework.batch.admin.sample.model.Project;

public class GitRepository implements IRepository {

	private Repository repository;
	
	public GitRepository(Project project) {
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
	
	public ArrayList<Commit> getCommits() {
		
		ArrayList<Commit> result = new ArrayList<Commit>();
		//RevWalk walk = new RevWalk(repository);
		Git git = new Git(repository);
		
		try {
			Iterable<RevCommit> commits = git.log().call();
			
			for(RevCommit commit : commits) {
				
				// branch itself
				if(commit.getParentCount() == 0) break;
				
				Commit c = new Commit();
				result.add(c);
				c.setMessage(commit.getFullMessage());
				
				System.out.println("commit: "+commit.getShortMessage());

				RevCommit parentCommit = commit.getParent(0);
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				
			    try (DiffFormatter diffFormatter = new DiffFormatter(out)) {
			        diffFormatter.setRepository(repository);
			        for (DiffEntry entry : diffFormatter.scan(parentCommit, commit)) {
			            //diffFormatter.format(diffFormatter.toFileHeader(entry));
			        	
			            diffFormatter.format(entry);
			            out.flush();
			            String patch = out.toString();
			            out.reset();
			            
			            CommitFile cf = new CommitFile();
			            cf.setChangeType(entry.getChangeType());
			            cf.setPatch(patch);
			            cf.setOldPath(entry.getOldPath());
			            cf.setNewPath(entry.getNewPath());
			            
			            c.addFile(cf);
			            
			        }
			    }	
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
