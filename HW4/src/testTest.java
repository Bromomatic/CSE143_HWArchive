//Grady Williams
//Test Case for AssassinManager object.
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;
public class testTest {
    
    //Tests to make sure an IllegalArgumentException is thrown
    //if the ArrayList passed has size 0.
    @Test(expected = IllegalArgumentException.class)
    public void checkConstructor() {
		ArrayList<String> tester = new ArrayList();
		AssassinManager list = new AssassinManager(tester);
    }
    
    //Tests to make sure an IllegalArgumentException is 
    //thrown if the ArrayList passed is null.
    @Test(expected = IllegalArgumentException.class)
    public void checkConstructorAgain() {
		AssassinManager list = new AssassinManager(null);
    }
    
    //Tests to make sure the constructor actually makes a 
    //list.
    //CAUTION: killRingContains must work in order to pass the test.
    @Test
    public void checkConstructor3() {
		AssassinManager list = builder();
		assertTrue(list.killRingContains("Mike"));
		assertTrue(list.killRingContains("Bill"));
		assertTrue(list.killRingContains("Jordan"));
		assertTrue(list.killRingContains("Therese"));
		assertTrue(list.killRingContains("Tom"));
    }
    
    //Builds a list to perform tests on.
    private AssassinManager builder() {
		ArrayList<String> tester = new ArrayList();
		tester.add("Mike");
		tester.add("Bill");
		tester.add("Jordan");
		tester.add("Therese");
		tester.add("Tom");
		AssassinManager list = new AssassinManager(tester);
		return list;
    }
    
    //Tests the KillRingContains method.
    //CAUTION: A bad constructor will cause
    //this test to fail
    @Test
    public void testKillRingContains() {
		AssassinManager list = builder();
		assertTrue(list.killRingContains("MiKE"));
		assertTrue(list.killRingContains("BIlL"));
		assertTrue(list.killRingContains("Jordan"));
		assertTrue(list.killRingContains("Ther" + "ese"));
		assertTrue(list.killRingContains("TOM"));
    }
    
    //Tests the graveyardContains method.
    //CAUTION: A bad kill method will cause this to fail.
    @Test
    public void testGraveyardContains() {
		AssassinManager list = builder();
		assertFalse(list.graveyardContains("Mike"));
		list.kill("Mike");
		assertTrue(list.graveyardContains("Mike"));
		assertFalse(list.graveyardContains("Jordan"));
		list.kill("Jordan");
		assertTrue(list.graveyardContains("Jordan"));
		assertFalse(list.graveyardContains("TOM"));
		list.kill("tom");
		assertTrue(list.graveyardContains("Tom"));
    }
    
    //Tests the isGameOver method.
    @Test 
    public void testIsGameOver() {
		AssassinManager list = builder();
		assertFalse(list.isGameOver());
		ArrayList<String> test = new ArrayList<String>();
		test.add("winner");
		AssassinManager tester = new AssassinManager(test);
 		assertTrue(tester.isGameOver());
    }
    
    //Tests the winner method.
    @Test
    public void testWinner() {
		ArrayList<String> test = new ArrayList<String>();
		test.add("winner");
		AssassinManager tester = new AssassinManager(test);
 		assert(tester.winner().equals("winner"));
    }
    
    //Tests to make sure an IllegalArgumentException is 
    //thrown if a bad string is passed to the kill method.
    @Test (expected = IllegalArgumentException.class)
    public void testKill() {
		AssassinManager list = builder();
		list.kill("NotInList");
    }
    
    //Tests to make sure an IllegalStateException is thrown
    //if kill is called and the game is over.
    @Test (expected = IllegalStateException.class)
    public void testKill2() {
		AssassinManager list = builder();
		list.kill("Jordan");
		list.kill("Tom");
		list.kill("Therese");
		list.kill("Mike");
		list.kill("Bill");
    }
    
    //Tests to make sure the IllegalStateException takes
    //precedence if kill is called with a bad string and
    //the game is over.
    @Test (expected = IllegalStateException.class) 
    public void testKill3() {
		AssassinManager list = builder();
		list.kill("Jordan");
		list.kill("Tom");
		list.kill("Therese");
		list.kill("Mike");
		list.kill("NotInList");
    }
}