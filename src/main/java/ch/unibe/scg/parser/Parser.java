package ch.unibe.scg.parser;

import ch.unibe.scg.model.Issue;

public interface Parser {
	public String formatUrl(String url, String issue);
	public Issue parse(String content);
}
