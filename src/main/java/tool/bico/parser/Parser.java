package tool.bico.parser;

public interface Parser {
	public String formatUrl(String url, String issue);
	public IssueInfoHolder parse(String content);
}
