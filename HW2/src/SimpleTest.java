
// CSE 143, Homework 2 (SortedIntList)
// An example of a very simple test case to help check
// if add is adding in sorted order. It can be expanded
// and made more complex as further tests are needed.
// 
// This one is not a JUnit test file; it's just a program
// with a main method.  The other testing files we have
// provided (Test1 - Test3.java) are JUnit tests.

public class SimpleTest {
    public static void main(String[] args) {
        SortedIntList list = new SortedIntList();
        
        int x = list.indexOf(0);
        System.out.println(x);
        list.add(1);
        int y = list.indexOf(1);
        System.out.println(y);
        System.out.println("list = " + list);
        int z = list.indexOf(5);
        System.out.println(z);
        list.add(5);
        list.add(-3);
        list.add(-99);
        
        System.out.println("list = " + list);
    }
}
