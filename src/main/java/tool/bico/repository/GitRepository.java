package tool.bico.repository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.stream.StreamSupport;

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
		constructor(true);
	}
	
	public GitRepository(Project project, boolean pull) {
		this.project = project;
		constructor(pull);
	}
	
	private void constructor(boolean pull) {
		GitLoader gitLoader = new GitLoader(project.getUrl(), project.getBranch());
		boolean result = gitLoader.init(pull);
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
	
	public String getRepositoryPath() {
		if(repository == null) return null;
		return repository.getDirectory().getAbsolutePath();
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
		
		ArrayList<Commit> list = new ArrayList<>();
		getCommitIterator().forEachRemaining(list::add);
		
		return list;
	}
	
	
}
