package org.springframework.batch.admin.sample.repository;

import java.util.ArrayList;

import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.Project;

public class Repository implements IRepository {
	
	public IRepository repository;
	
	public Repository(Project project) {
		if(project.getType() == Project.Type.GIT) {
			repository = new GitRepository(project);
		} else
		if(project.getType() == Project.Type.GITHUB) {
			repository = new GithubRepository(project.getUrl());
		}
	}
	
	public Repository(String url, String branch) {
		repository = new GitRepository(url, branch);
	}

	@Override
	public ArrayList<Commit> getCommits() {
		if(repository == null) return null;
		return repository.getCommits();
	}
}
