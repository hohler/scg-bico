package tool.bico.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import tool.bico.model.CommitIssue;
import tool.bico.model.CommitIssue.Type;
import tool.bico.utils.SwitchSubstring;

public class CommitMessageClassifier {
	
	public static Type classify(String commitMessage) {
		List<Type> types = new ArrayList<>();
		
		types = detectTypes(commitMessage);
		if(types.size() == 0) {
			return Type.NA;
		}
		
		Map<Type, Integer> occurences = new HashMap<>();
		
		for(Type type : types) {
			Integer i = occurences.get(type);
			if(i == null) {
				i = new Integer(1);
				occurences.put(type,  i);
			} else {
				occurences.put(type, new Integer(i.intValue()+1));
			}
		}
		
		Type highest = null;
		Integer highestOccurence = 0;
		
		for(Entry<Type, Integer> e : occurences.entrySet()) {
			if(highestOccurence < e.getValue()) {
				highest = e.getKey();
				highestOccurence = e.getValue();
			}
		}
		
		return highest;
	}
	
	private static List<Type> detectTypes(String commitMessage) {
		
		List<Type> types = new ArrayList<>();
		
		SwitchSubstring.of(commitMessage.toLowerCase())
		.when("bug", "fix", "wrong", "error", "fail", "problem", "patch").then(() -> types.add(CommitIssue.Type.BUG));
		
		SwitchSubstring.of(commitMessage.toLowerCase())
		.when("deprecation", "deprecated").then(() -> types.add(CommitIssue.Type.DEPRECATION));
		
		SwitchSubstring.of(commitMessage.toLowerCase())
		.when("new", "add", "requirement", "initial", "create", "feature", "implementation").then(() -> types.add(CommitIssue.Type.FEATURE));
		
		SwitchSubstring.of(commitMessage.toLowerCase())
		.when("refactor", "refactoring").then(() -> types.add(CommitIssue.Type.REFACTOR));
		
		SwitchSubstring.of(commitMessage.toLowerCase())
		.when("docs", "documentation", "doc").then(() -> types.add(CommitIssue.Type.DOCUMENTATION));
		
		SwitchSubstring.of(commitMessage.toLowerCase())
		.when("clean", "better", "enhancement", "improvement", "optimization").then(() -> types.add(CommitIssue.Type.IMPROVEMENT));
		
		SwitchSubstring.of(commitMessage.toLowerCase())
		.when("merge").then(() -> types.add(CommitIssue.Type.MERGE));
		
		SwitchSubstring.of(commitMessage.toLowerCase())
		.when("test", "junit", "coverage", "asset").then(() -> types.add(CommitIssue.Type.TEST));
		
		SwitchSubstring.of(commitMessage.toLowerCase())
		.when("dependency").then(() -> types.add(CommitIssue.Type.DEPENDENCY_UPGRADE));
		
		return types;
	}
}
