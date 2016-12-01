package ch.unibe.scg.bico.repository;

import java.util.ArrayList;
import java.util.Iterator;

import ch.unibe.scg.bico.model.Commit;

public interface IRepository {
	public ArrayList<Commit> getCommits();
	public Iterator<Commit> getCommitIterator();
}
