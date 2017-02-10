package tool.bico.repository;

import java.util.ArrayList;
import java.util.Iterator;

import tool.bico.model.Commit;

public interface IRepository {
	public ArrayList<Commit> getCommits();
	public Iterator<Commit> getCommitIterator();
}
