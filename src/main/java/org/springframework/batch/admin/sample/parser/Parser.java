package org.springframework.batch.admin.sample.parser;

import org.springframework.batch.admin.sample.model.CommitIssue;

public interface Parser {
	public String formatUrl(String url, String issue);
	public CommitIssue parse(CommitIssue issue, String content);
}
