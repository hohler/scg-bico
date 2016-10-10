package ch.unibe.scg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ch.unibe.scg.model.Commit;
import ch.unibe.scg.model.CommitIssue;
import ch.unibe.scg.parser.IssueTrackerParser;

public class ThreadPool {

	private static final int NTHREADS = 50;
	
	private ArrayList<Commit> commits;
	private String urlPattern;
	
	public ThreadPool(ArrayList<Commit> commits, String urlPattern) {
		this.commits = commits;
		this.urlPattern = urlPattern;
	}
	
	public void run() {
		ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
		int perThread = commits.size() / NTHREADS;
        for (int i = 0; i < commits.size(); i+=perThread) {
        	int end = i+perThread;
        	if(end > commits.size()) end = commits.size()-1;
        	List<Commit> list = commits.subList(i, end);
            Runnable worker = new MyRunnable(list);
            executor.execute(worker);
        }
        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();
        // Wait until all threads are finish
        try {
			executor.awaitTermination(10, TimeUnit.MINUTES);
			System.out.println("Finished all threads");
		} catch (InterruptedException e) {
			System.err.println("TIMEOUT THREADS");
			e.printStackTrace();
		}
        
	}

	private class MyRunnable implements Runnable {
        private List<Commit> list;

        MyRunnable(List<Commit> list) {
        	this.list = list;
        }

	    @Override
		public void run() {
	    	IssueTrackerParser itp;
			try {
				itp = new IssueTrackerParser(urlPattern);
				for(Commit c : list) {
					itp.setIssue(c.getCommitIssue());
					CommitIssue issue = itp.parse();
					if(issue != null) {
						c.setCommitIssue(issue);
						System.out.println(issue);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
