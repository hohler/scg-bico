package ch.unibe.scg;

import java.util.ArrayList;

import ch.unibe.scg.repository.Commit;
import ch.unibe.scg.repository.GitLoader;
import ch.unibe.scg.repository.GithubRepository;

public class Playground {
	public static void main(String[] args) {
		
		/*GitLoader gitLoader = new GitLoader("https://github.com/apache/flume.git", "trunk");
		boolean result = gitLoader.init();*/
		
		GithubRepository g = new GithubRepository("https://github.com/apache/flume");
		ArrayList<Commit> result = g.getCommits();
		System.out.println("commits: "+result.size());
		
	}
}
