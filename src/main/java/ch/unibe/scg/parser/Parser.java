package ch.unibe.scg.parser;

public interface Parser {
	public String formatUrl(String url, String issue);
	public Issue parse(String content);
}
