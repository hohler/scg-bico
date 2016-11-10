package org.springframework.batch.admin.sample.processor;

import org.springframework.batch.admin.sample.model.CommitIssue;
import org.springframework.batch.admin.sample.model.Commit;
import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.parser.IssueTrackerParser;
import org.springframework.batch.item.ItemProcessor;

public class CommitProcessor implements ItemProcessor<Commit, Commit> {
	
	private String urlPattern;
	
	public CommitProcessor(Project project) {
		if(project == null) throw new NullPointerException("project is null!");
		urlPattern = project.getIssueTrackerUrlPattern();
		if(urlPattern == null) throw new NullPointerException("urlPattern is null");
	}

	@Override
	public Commit process(final Commit input) throws Exception {
		IssueTrackerParser itp;
		try {
			itp = new IssueTrackerParser(urlPattern);
			itp.setIssue(input.getCommitIssue());
			CommitIssue result = itp.parse();
			if(result != null) input.setCommitIssue(result);
			else input.setCommitIssue(input.getCommitIssue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}

	/*public void setProject(Project project) {
		this.project = project;
	}*/
	
	
}