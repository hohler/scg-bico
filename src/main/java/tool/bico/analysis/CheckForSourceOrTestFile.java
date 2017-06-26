package tool.bico.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckForSourceOrTestFile {
	public static boolean check(String filePath) {
		Pattern regex;
		regex = Pattern.compile("/test/.*(Test)?.*.java", Pattern.CASE_INSENSITIVE);
		
		Matcher m = regex.matcher(filePath);
		return m.find();		
	}
}
