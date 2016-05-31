import java.util.*;

public class midterm2stack {
	public static void main(String[] args) {
		Stack<Integer> s = new Stack<Integer>();
		s.push(1);
		s.push(2);
		s.push(3);
		s.push(4);
		s.push(9);
		//s.push(8);
		boolean t = isSorted(s);
		if(t) {
			System.out.println("TRUE!");
		} else {
			System.out.println("FALSE!");
		}
		
		System.out.println(s);
	}
	

	public static void s2q(Stack<Integer> s, Queue<Integer> q) {
	    while (!s.isEmpty()) {
	        q.add(s.pop());           // Transfers the entire contents
	    }                             // of stack s to queue q
	}


	public static void q2s(Queue<Integer> q, Stack<Integer> s) {
	    while (!q.isEmpty()) {
	        s.push(q.remove());       // Transfers the entire contents 
	    }							  // of queue q to stack s
	}

	public static boolean isSorted(Stack<Integer> s) {
		   Queue<Integer> q = new LinkedList<Integer>();
		   
		   s2q(s, q);  // Convert to queue
		   
		   int count = 0;
		   int current = q.remove();
		   s.push(current);
		   while(!q.isEmpty()) {
		      int now = q.remove();
		      s.push(now);
		      if(now > current) {
		         count = 1;
		      }
		      current = now;
		   }
		   
		   s2q(s, q);  // Reorder the stack correctly!
		   q2s(q, s);
		   
		   if (count == 1) {
		      return false;
		   } else {
		      return true;
		   }
		}
}
