package tool.bico;

public class Tools {

	// the array int[] m MUST BE SORTED
	public static int median(int[] m) {
		if(m.length == 1) return m[0];
		if(m.length == 0) return 0;
	    int middle = m.length / 2;
	    if (m.length % 2 == 1) {
	        return m[middle];
	    } else {
	        return (m[middle-1] + m[middle]) / 2;
	    }
	}
	
	//merge two sorted int[] arrays into one sorted int array
	public static int[] merge(int[] a, int[] b) {
	    int[] answer = new int[a.length + b.length];
	    int i = a.length - 1, j = b.length - 1, k = answer.length;

	    while (k > 0)
	        answer[--k] = (j < 0 || (i >= 0 && a[i] >= b[j])) ? a[i--] : b[j--];
	    
        return answer;
	}
}
