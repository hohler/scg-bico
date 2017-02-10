package tool.bico.parser;

public class SwitchSubstring {
    private static final SwitchSubstring DONE = new SwitchSubstring(null) {
        @Override
        public SwitchSubstring when(String subString, Runnable r) {
            return this;
        }

        @Override
        public void orElse(Runnable r) {
        }
    };

    private final String str;
    private boolean currentStatement;

    private SwitchSubstring(String str) {
        this.str = str;
    }

    public SwitchSubstring when(String subString, Runnable r) {
        if(str.contains(subString)) {
            r.run();
            return DONE;
        }
        return this;
    }
    
    public SwitchSubstring when(String... subStrings) {
    	currentStatement = false;
    	if(str == null) return this;
        for(String s : subStrings) {
	    	if(str.contains(s)) {
	            currentStatement = true;
	        }
        }
        return this;
    }
    
    public SwitchSubstring then(Runnable r) {
    	if(currentStatement) {
    		r.run();
    		return DONE;
    	}
    	currentStatement = false;
    	return this;
    }

    public void orElse(Runnable r) {
        r.run();
    }

    public static SwitchSubstring of(String str) {
        return new SwitchSubstring(str);
    }
}