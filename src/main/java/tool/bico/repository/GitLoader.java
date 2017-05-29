package tool.bico.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;


public class GitLoader {
	
	private static String REPOSITORY_PATH = "target/repositories/";
	
	private String gitUrl;
	private String branch;
	private ArrayList<String> branches;
	private String repoName;
	private File repoDir;

	public GitLoader(String gitUrl, String branch) {
		this.gitUrl = gitUrl;
		this.branch = branch;
		
		ArrayList<String> branches = new ArrayList<String>();
		branches.add("refs/origin/"+branch);
		
		// if repo does not exist, create; else pull
		String[] url_splitted = gitUrl.split("/");
		repoName = url_splitted[url_splitted.length-1];
		repoName = repoName.replaceAll(".git", ""); 
		
		repoDir = new File(REPOSITORY_PATH + repoName);
		System.err.println(repoDir.getAbsolutePath());
	}
	
	public boolean init() {
		return init(true);
	}
	
	public boolean init(boolean pull) {
		
		if(repoDir.exists()) {
			try {
				Git git = Git.open(repoDir);
				
				git.reset().setMode(ResetType.HARD).call();
				
				git.checkout().setName("origin/"+branch).call();
				// delete "mm" branch
				git.branchDelete().setBranchNames("mm").call();
				
				if(pull) git.pull().call();
				
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Could not pull from repo");
				return false;
			} catch (GitAPIException e) {
				System.err.println("Could not pull from repo");
				return false;
			}
		} else {
		
			try {
				Git git = Git.cloneRepository()
						.setBranchesToClone(branches)
						.setURI( gitUrl )
						.setDirectory( repoDir )
						.call();
				
				return true;
				
			} catch (GitAPIException e) {
				e.printStackTrace();
				System.err.println("Could not clone repo");
				return false;
			}
		}
	}
	
	public String getRepoName() {
		return this.repoName;
	}
	
	public File getRepoDir() {
		return this.repoDir;
	}
	
}
