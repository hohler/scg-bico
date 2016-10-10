package ch.unibe.scg.parser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.unibe.scg.model.Commit;

public class IssueStringParser {
	private Pattern regex;
	public IssueStringParser(String regex) {
		this.regex = Pattern.compile(regex);
	}
	
	public void parse(List<Commit> list) {
		for(Commit c : list) {
			this.parse(c);
		}
	}
	
	public void parse(Commit commit) {
		Matcher m = regex.matcher(commit.getMessage());
		if(m.find()) {
			if(m.group(1) == null) System.err.println(commit.getMessage().split("\\r?\\n")[0]);
			commit.initIssue( m.group(1).toUpperCase() );
		} else {
			System.err.println("Can't parse issue "+commit.getMessage().split("\\r?\\n")[0]);
		}
	}
}
