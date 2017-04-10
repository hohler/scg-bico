package tool.bico.repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.revwalk.RevCommit;

import tool.bico.model.Commit;
import tool.bico.model.CommitFile;

import org.eclipse.jgit.lib.Repository;

public class GitRepositoryCommitIterator implements Iterator<Commit> {

	
	private Stack<RevCommit> data;
	private Repository repository;
	
	private Commit parentCommit = null;
	
	public GitRepositoryCommitIterator(Repository repository, Stack<RevCommit> data) {
		this.repository = repository;
		this.data = data;
	}

	@Override
	public boolean hasNext() {
		return !data.isEmpty();
	}

	@Override
	public Commit next() {
		if(!hasNext()) return null;
		RevCommit commit = data.pop();
		
		// branch itself, get another commit from the stack
		if(commit.getParentCount() == 0) commit = data.pop();
		
		Commit c = new Commit();
		c.setMessage(commit.getFullMessage().replaceAll("\\x00",  ""));
		c.setParentCommit(parentCommit);
		c.setRef(commit.getName());
		int commitAdditions = 0;
		int commitDeletions = 0;
		
		c.setTimestamp(commit.getCommitTime());
		
		if(commit.getParentCount() > 1) c.setMergeCommit(true);
		
		parentCommit = c;
		
		System.out.println("commit: "+commit.getShortMessage());

		RevCommit parentCommit = commit.getParent(0);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
	    try (DiffFormatter diffFormatter = new DiffFormatter(out)) {
	        diffFormatter.setRepository(repository);
	        diffFormatter.setDetectRenames(true);
	        for (DiffEntry entry : diffFormatter.scan(parentCommit, commit)) {
	            //diffFormatter.format(diffFormatter.toFileHeader(entry));
	            diffFormatter.format(entry);
	            out.flush();
	            String patch = out.toString();
	            out.reset();
	            
	            //analyse patch
	            int linesDeleted = 0;
	            int linesAdded = 0;
	            for (Edit edit : diffFormatter.toFileHeader(entry).toEditList()) {
	                linesDeleted += edit.getEndA() - edit.getBeginA();
	                linesAdded += edit.getEndB() - edit.getBeginB();
	            }
	            
	            commitDeletions += linesDeleted;
	            commitAdditions += linesAdded;
	            
	            CommitFile cf = new CommitFile();
	            cf.setChangeType(entry.getChangeType());
	            //if(!entry.getOldPath().matches("\\.(pdf|ai|psd|tiff|jpg|png|jpeg|gif|bmp|ico)$") && !entry.getNewPath().matches("\\.(pdf|ai|psd|tiff|jpg|png|jpeg|gif|bmp|ico)$")) cf.setPatch(patch);
	            cf.setPatch(patch.replaceAll("\\x00",  ""));
	            cf.setOldPath(entry.getOldPath());
	            cf.setNewPath(entry.getNewPath());
	            cf.setAdditions(linesAdded);
	            cf.setDeletions(linesDeleted);
	            c.addFile(cf);
	            
	        }
	    } catch (CorruptObjectException e) {
			e.printStackTrace();
		} catch (MissingObjectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    c.setAdditions(commitAdditions);
	    c.setDeletions(commitDeletions);
	    
		return c;
	}

}
