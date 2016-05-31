// Shawn Stern   CSE 143   HW_2
//
// This simple test method makes sure that a number of methods in SortedIntList work properly.
// It checks to see that add follows predicted behavior, that unique lists will only accept
// unique values, and that calling setUnique(true) clears the list of duplicates properly.
//
// Will fail if any assertion fails.

import static org.junit.Assert.*;
import org.junit.Test;


public class SortedIntListTest {

	@Test
	public void testMain() {
		SortedIntList a = new SortedIntList();
		SortedIntList b = new SortedIntList(true, 20);
		
		testAdd(a);
		testAddUnique(b);
		testUniqueness(a);
	}	
	
	public void testAdd(SortedIntList x) {
		x.add(1);
		x.add(5);
		x.add(-86);
		x.add(42);
		x.add(42);
		x.add(-9);
		
		assertEquals("S:[-86, -9, 1, 5, 42, 42]", x.toString());		
	}
	
	public void testAddUnique(SortedIntList x) {
		x.add(1);
		x.add(1);
		x.add(1);
		x.add(1);
		x.add(5);
		x.add(-86);
		x.add(42);
		x.add(42);
		x.add(-9);
		x.add(-9);
		
		assertEquals("S:[-86, -9, 1, 5, 42]U", x.toString());
	}
	
	public void testUniqueness(SortedIntList x) {
		x.add(1);
		x.add(1);
		x.add(1);
		x.add(1);
		x.add(5);
		x.add(-86);
		x.add(42);
		x.add(42);
		x.add(-9);
		x.add(-9);
		
		x.setUnique(true);
		x.setUnique(false);
		
		boolean testUnique = x.getUnique();
		
		assertEquals("S:[-86, -9, 1, 5, 42]", x.toString());
		assertEquals(false, testUnique);
	}
}
