// CSE 143, Homework 2 (SortedIntList)
//
// This program tests stage 2 of the SortedIntList project
// (the phase at which you add the unique boolean to your class).
// You can change the constant values to conduct larger
// tests to better verify your SortedIntList class.
//
// adapted from 2006/09/29 version which was by Alex Loddengaard

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class Test2 {
    private static final int TEST_SIZE = 25;
        
    //the lower this is, the more duplicates there will be
    private static final int MAX_INT = 10;
    
    @Test
    public void testConstructor1() {
        //test the first constructor
        SortedIntList list = new SortedIntList(true, SortedIntList.DEFAULT_CAPACITY);
        fill(list, SortedIntList.DEFAULT_CAPACITY);
        helper(list, true);
    }
    
    @Test
    public void testConstructor2() {
        //test the second constructor
    	SortedIntList list = new SortedIntList(true, TEST_SIZE);
        fill(list, TEST_SIZE);
        helper(list, true);
    }
                
    @Test
    public void testConstructor3() {
        //test the third constructor
        SortedIntList list = new SortedIntList(false, SortedIntList.DEFAULT_CAPACITY);
        fill(list, SortedIntList.DEFAULT_CAPACITY);
        helper(list, false);
    }
                
    @Test
    public void testConstructor4() {
        //test the fourth constructor
    	SortedIntList list = new SortedIntList(false, TEST_SIZE);
        helper(list, false);
    }
        
    // Briefly tests the toString method for a unique list.
    @Test(timeout = 1000)
    public void testToString2NonUnique() {
        SortedIntList list = new SortedIntList(5);
        list.add(5);
        list.add(-1);
        list.add(17);
        list.add(2);
        assertEquals("S:[-1, 2, 5, 17]", list.toString());
    }

    @Test(timeout = 1000)
    public void testToString3Unique() {
        SortedIntList list = new SortedIntList(true);
        list.add(6);
        list.add(3);
        list.add(6);
        list.add(42);
        list.add(8);
        list.add(3);
        list.add(3);
        list.add(42);
        assertEquals("S:[3, 6, 8, 42]U", list.toString());
    }
    
    // precondition:  l is a SortedIntList, size will not exceed l's capacity
    // postcondition: l is filled with TEST_SIZE random values
    private void fill(SortedIntList l, int size) {
        Random rand = new Random();
                
        // fill l with size elements
        for (int i = 0; i < size; i++) {
            l.add(rand.nextInt(MAX_INT));
        }
    }
        
    // precondition: l is a filled SortedIntList, unique should be true if all
    //               values should be unique
    // postcondition: tests is l is a valid SortedIntList and prints errors
    private void helper(SortedIntList l, boolean unique) {
        // loop through the list checking for order and duplicates
        for (int i = 1; i < l.size(); i++) {
            // check for duplicates if the list is supposed to be unique
            int prev = l.get(i - 1);
            int curr = l.get(i);
        	if (unique) {
            	assertNotSame("duplicate value found at indexes " + (i-1) +
            			" and " + i + " in list: " + l, prev, curr);
            }
            
            // check for order
        	assertTrue("not in sorted order; value " + prev + " at index " + (i-1) +
        			" must not be larger than value " + curr + " at index " + i + " in list: " + l,
        			prev <= curr);
        }
    }
}
