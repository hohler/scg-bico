package ch.unibe.scg.repository;

import java.util.ArrayList;

import ch.unibe.scg.model.Commit;

public interface IRepository {
	public ArrayList<Commit> getCommits();
}
