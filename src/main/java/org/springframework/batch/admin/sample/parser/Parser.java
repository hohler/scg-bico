package org.springframework.batch.admin.sample.parser;

public interface Parser {
	public String formatUrl(String url, String issue);
	//public CommitIssue parse(CommitIssue issue, String content);
	public IssueInfoHolder parse(String content);
}
