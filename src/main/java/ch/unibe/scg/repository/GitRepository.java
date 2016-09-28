package ch.unibe.scg.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitRepository implements IRepository {

	private Repository repository;
	
	public GitRepository(GitLoader gitLoader) {
	
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			repository = builder.setGitDir(gitLoader.getRepoDir())
					.readEnvironment()
					.findGitDir()
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Commit> getCommits() {
		
		RevWalk walk = new RevWalk(repository);
		
		for(RevCommit commit : walk) {
			RevTree tree = commit.getTree();
			
			
		}
		
		walk.close();
		return null;
	}
	
	
}
