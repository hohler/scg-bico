package tool.bico.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.CommitStats;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;

import tool.bico.model.Commit;

@Deprecated
public class GithubRepository implements IRepository {

	private String owner;
	private String name;
	private Repository repository;
	
	public GithubRepository(String gitHubUrl) {
		
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token("df5f225320a07d1de361bb3493d1f8aedb43fbfc");
		
		String[] splitted = gitHubUrl.split("/");
		name = splitted[splitted.length-1];
		owner = splitted[splitted.length-2];
		
		RepositoryService service = new RepositoryService(client);
		try {
			repository = service.getRepository(owner, name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ArrayList<Commit> getCommits() {
		ArrayList<Commit> result = new ArrayList<Commit>();
		
		CommitService service = new CommitService();
		List<RepositoryCommit> commits;
		try {
			commits = service.getCommits(repository);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		for(RepositoryCommit c : commits) {
			
			Commit commit = new Commit();
			
			CommitStats stats = c.getStats();
			
			if(stats != null) {
				commit.setAdditions(stats.getAdditions());
				commit.setDeletions(stats.getDeletions());
			}
			
			commit.setMessage(c.getCommit().getMessage());
			
			List<CommitFile> files = c.getFiles();
			
			if(files != null) {
				for(CommitFile f : files) {
					tool.bico.model.CommitFile file = new tool.bico.model.CommitFile();
					file.setAdditions(f.getAdditions());
					file.setDeletions(f.getDeletions());
					file.setFilename(f.getFilename());
					file.setPatch(f.getPatch());
					
					commit.addFile(file);
				}
			}
			
			result.add(commit);
		}
		
		return result;
	}

	@Override
	public Iterator<Commit> getCommitIterator() {
		return null;
	}

	

}
