package org.springframework.batch.admin.sample.repository;

import java.util.ArrayList;

import org.springframework.batch.admin.sample.model.Commit;

public interface IRepository {
	public ArrayList<Commit> getCommits();
}
