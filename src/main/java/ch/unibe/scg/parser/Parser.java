package ch.unibe.scg.parser;

import ch.unibe.scg.model.CommitIssue;

public interface Parser {
	public String formatUrl(String url, String issue);
	public CommitIssue parse(CommitIssue issue, String content);
}
