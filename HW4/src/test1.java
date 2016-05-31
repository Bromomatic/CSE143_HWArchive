public class test1 {
	public static void main(String[] args) {
		String s = "Hello";
		doubleReverse(s);
		System.out.println();
		String b = "douchebag";
		doubleReverse(b);
	}
	
	public static void doubleReverse(String s) {
		//doubleReverseHelper(s, s.length() - 1);  //Alternate solution
		if(s.length() != 0) {
			doubleReverse(s.substring(1));
			System.out.print(s.charAt(0));
			System.out.print(s.charAt(0));
		}
	}
	
	public static void doubleReverseHelper(String s, int i) {
		if(i >= 0) {
			System.out.print(s.charAt(i));
			System.out.print(s.charAt(i));
			doubleReverseHelper(s, i - 1);
		}
	}
}