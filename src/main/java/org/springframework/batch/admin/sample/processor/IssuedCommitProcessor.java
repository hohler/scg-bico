package org.springframework.batch.admin.sample.processor;

import org.springframework.batch.admin.sample.model.CommitIssue;
import org.springframework.batch.admin.sample.model.IssuedCommit;
import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.parser.IssueTrackerParser;
import org.springframework.batch.item.ItemProcessor;

public class IssuedCommitProcessor implements ItemProcessor<IssuedCommit, IssuedCommit> {
	
	private String urlPattern;
	
	private Project project;
	
	public IssuedCommitProcessor(Project project) {
		if(project == null) throw new NullPointerException("project is null!");
		urlPattern = project.getIssueTrackerUrlPattern();
		if(urlPattern == null) throw new NullPointerException("urlPattern is null");
	}

	@Override
	public IssuedCommit process(final IssuedCommit input) throws Exception {
		IssueTrackerParser itp;
		IssuedCommit output = new IssuedCommit(input);
		try {
			itp = new IssueTrackerParser(urlPattern);
			itp.setIssue(input.getCommitIssue());
			CommitIssue result = itp.parse();
			if(result != null) output.setCommitIssue(result);
			else output.setCommitIssue(input.getCommitIssue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	
}