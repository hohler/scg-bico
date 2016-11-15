package ch.unibe.scg.bico.repository;

import java.util.ArrayList;

import ch.unibe.scg.bico.model.Commit;

public interface IRepository {
	public ArrayList<Commit> getCommits();
}
