import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.Test;

public class DeBarrAssassinManagerTest {

	// Tests if empty ArrayList parameter throws an IllegalArgumentException.
	@Test (expected = IllegalArgumentException.class)
	public void testAssassinManager1() {
		ArrayList<String> names = new ArrayList<String>();
		AssassinManager manager = new AssassinManager(names);
	}
	
	// Tests if null ArrayList parameter throws an IllegalArgumentException.
	@Test (expected = IllegalArgumentException.class)
	public void testAssassinManager2() {
		AssassinManager manager = new AssassinManager(null);
	}
		
	@Test
	public void testPrintKillRing() {
		AssassinManager manager = new AssassinManager(listOfMany());
		
		OutputCapturer.start();
		manager.printKillRing();
		String output = OutputCapturer.stop();
		String expectedOutput = "  Richter is stalking Leon\n  Leon is stalking Simon\n" +
								"  Simon is stalking Christopher\n  Christopher is stalking Juste\n" +
								"  Juste is stalking Julius\n  Julius is stalking Trevor\n" +
								"  Trevor is stalking Sonia\n  Sonia is stalking Richter\n";
		assertEquals("printKillRing output", expectedOutput, output);
        
        manager.kill("Leon");
        manager.kill("Juste");
        manager.kill("Christopher");
        
		OutputCapturer.start();
        manager.printKillRing();
        output = OutputCapturer.stop();
        expectedOutput = "  Richter is stalking Simon\n" +
						 "  Simon is stalking Julius\n  Julius is stalking Trevor\n" +
						 "  Trevor is stalking Sonia\n  Sonia is stalking Richter\n";
        assertEquals("printKillRing output (after kill)", expectedOutput, output);
    }

	@Test
	public void testPrintGraveyard() {
		AssassinManager manager = new AssassinManager(listOfMany());
		
		killAllButOne(manager);
		
		OutputCapturer.start();
        manager.printGraveyard();
        String output = OutputCapturer.stop();
        String expectedOutput = "  Sonia was killed by Juste\n  Christopher was killed by Sonia\n  Julius was killed by Juste\n" +
        						"  Richter was killed by Sonia\n  Trevor was killed by Julius\n" +
        						"  Simon was killed by Richter\n  Leon was killed by Richter\n";
        assertEquals("printGraveyard output", expectedOutput, output);

        OutputCapturer.start();
        manager.printGraveyard();
        output = OutputCapturer.stop();
        assertEquals("printGraveyard output (2nd call)", expectedOutput, output);
	}

	// Tests if all values in AssassinManager are in killRing.
	@Test
	public void testKillRingContains() {
		AssassinManager manager = new AssassinManager(listOfMany());
		assertTrue(manager.killRingContains("Leon"));
		assertTrue(manager.killRingContains("Richter"));
		assertTrue(manager.killRingContains("Trevor"));
		assertTrue(manager.killRingContains("Simon"));
		assertTrue(manager.killRingContains("Sonia"));
		assertTrue(manager.killRingContains("Christopher"));
		assertTrue(manager.killRingContains("Julius"));
		assertTrue(manager.killRingContains("Juste"));
	}

	@Test
	public void testContains() {
		ArrayList<String> name = listOfMany();
		AssassinManager manager = new AssassinManager(name);
		assertFalse(manager.graveyardContains("Trevor"));
		
		// Checks if all ArrayList values are in killRing and not in graveyard.
		for (String s : name) {
			assertFalse(manager.graveyardContains(s));
			assertTrue(manager.killRingContains(s));
		}
		
		// Checks if names that were never in killRing return false. 
		assertFalse(manager.killRingContains("Mathias"));
		assertFalse(manager.killRingContains("Elisabetha"));
		assertFalse(manager.killRingContains(""));
		assertFalse(manager.killRingContains("Adrian"));
		assertFalse(manager.killRingContains(null));
		
		manager.kill("Sonia");
		manager.kill("Leon");
		manager.kill("Juste");
		
		// Checks that the three assassinated people are in the graveyard.
		assertTrue(manager.graveyardContains("Leon"));
		assertTrue(manager.graveyardContains("Juste"));
		assertTrue(manager.graveyardContains("Sonia"));	
	}
	
	@Test
	public void testIsGameOverAndWinner() {
		// Tests isGameOver() and winner() with an AssassinManager with one name. 
		AssassinManager manager1 = new AssassinManager(listOfOne());
		assertTrue(manager1.isGameOver());
		assertTrue(manager1.winner().equals("Mathias"));
		
		// Tests isGameOver() and winner() with an AssassinManager with multiple names.
		AssassinManager manager2 = new AssassinManager(listOfMany());
		assertFalse(manager2.isGameOver());
		assertTrue(manager2.winner() == null);
		
		killAllButOne(manager2);
		
		// Tests isGameOver() and winner() when one name remains.
		assertTrue(manager2.isGameOver());
		assertTrue(manager2.winner().equals("Juste"));
	}
	
	// Checks if an IllegalStateException is thrown when game is over.
	@Test (expected = IllegalStateException.class)
	public void testKillException1() {
		AssassinManager manager = new AssassinManager(listOfOne());
		manager.kill("Mathias");		
	}
	
	// Checks if an IllegalArgumentException is thrown when name is not in killRing.
	@Test (expected = IllegalArgumentException.class)
	public void testKillException2() {
		AssassinManager manager = new AssassinManager(listOfMany());
		manager.kill("Gabriel");
	}
	
	// Checks subsequent calls to kill for people in the front of AssassinManager.
	@Test
	public void testKill() {
		AssassinManager manager = new AssassinManager(listOfMany());
		manager.kill("rIchTER");
		manager.kill("LeoN");
		manager.kill("SIMON");
		manager.kill("christoPHER");
		
		OutputCapturer.start();
		manager.printKillRing();
		String output = OutputCapturer.stop();
		String expectedOutput = "  Juste is stalking Julius\n  Julius is stalking Trevor\n" +
								"  Trevor is stalking Sonia\n  Sonia is stalking Juste\n";
		assertEquals("printKillRing output", expectedOutput, output);
		
		OutputCapturer.start();
        manager.printGraveyard();
        output = OutputCapturer.stop();
        expectedOutput = "  Christopher was killed by Sonia\n  Simon was killed by Sonia\n" +
        				 "  Leon was killed by Sonia\n  Richter was killed by Sonia\n";
        assertEquals("printGraveyard output", expectedOutput, output);
	}

	// Checks subsequent calls to kill for people in the back of AssassinManager.
	@Test
	public void testKill2() {	
		AssassinManager manager = new AssassinManager(listOfMany());
		manager.kill("SONIA");
		manager.kill("trevor");
		manager.kill("JULIUS");
		manager.kill("juste");
		
		OutputCapturer.start();
		manager.printKillRing();
		String output = OutputCapturer.stop();
		String expectedOutput = "  Richter is stalking Leon\n  Leon is stalking Simon\n" +
								"  Simon is stalking Christopher\n  Christopher is stalking Richter\n";
		assertEquals("printKillRing output", expectedOutput, output);
		
		OutputCapturer.start();
        manager.printGraveyard();
        output = OutputCapturer.stop();
        expectedOutput = "  Juste was killed by Christopher\n  Julius was killed by Juste\n" +
        				 "  Trevor was killed by Julius\n  Sonia was killed by Trevor\n";
        assertEquals("printGraveyard output", expectedOutput, output);
	}
		
	// Checks subsequent calls to kill for people in the middle of AssassinManager.
	@Test
	public void testKill3() {
		AssassinManager manager = new AssassinManager(listOfMany());
		manager.kill("Simon");
		manager.kill("SoniA");
		manager.kill("leon");
		manager.kill("julius");
		
		OutputCapturer.start();
		manager.printKillRing();
		String output = OutputCapturer.stop();
		String expectedOutput = "  Richter is stalking Christopher\n  Christopher is stalking Juste\n" +
								"  Juste is stalking Trevor\n  Trevor is stalking Richter\n";
		assertEquals("printKillRing output", expectedOutput, output);
		
		OutputCapturer.start();
        manager.printGraveyard();
        output = OutputCapturer.stop();
        expectedOutput = "  Julius was killed by Juste\n  Leon was killed by Richter\n" +
        				 "  Sonia was killed by Trevor\n  Simon was killed by Leon\n";
        assertEquals("printGraveyard output", expectedOutput, output);	
	}

	// Constructs and returns a list with one name in it for constructing
	// an AssassinManager object.
	private ArrayList<String> listOfOne () {
		ArrayList<String> name = new ArrayList<String>();
		name.add("Mathias");
		return name;
	}
	
	// Constructs and returns a list with many names in it for constructing
	// an AssassinManager object.
	// AssassinManager constructed looks like this:
	//		Richter -> Leon -> Simon -> Christopher -> Juste -> 
	//		Julius -> Trevor -> Sonia -> Richter
	private ArrayList<String> listOfMany() {
		ArrayList<String> names = new ArrayList<String>();
		names.add("Richter");
		names.add("Leon");
		names.add("Simon");
		names.add("Christopher");
		names.add("Juste");
		names.add("Julius");
		names.add("Trevor");
		names.add("Sonia");
		return names;
	}
	
	// Kills all but one person from an AssassinManager object constructed
	// with listOfMany().
	private void killAllButOne(AssassinManager manager) {
		manager.kill("Leon");
		manager.kill("Simon");
		manager.kill("Trevor");
		manager.kill("Richter");
		manager.kill("Julius");
		manager.kill("Christopher");
		manager.kill("Sonia");
	}
	
	 /**
     * This class has static methods for capturing console output (for methods
     * that do printlns).  You can use it in your own tests if you like.
     */
    public static class OutputCapturer {
        private static final PrintStream SYSTEM_OUT = java.lang.System.out;
        private static final PrintStream SYSTEM_ERR = java.lang.System.err;
        private static ByteArrayOutputStream outputStream = null;
        
        /** Returns true if output is currently being captured. */
        public static boolean isCapturing() {
            return outputStream != null;
        }

        /** Begins capturing output. */
        public static synchronized void start() {
            if (isCapturing()) {
                stop();
            }
            
            outputStream = new ByteArrayOutputStream(16384);
            PrintStream out = new PrintStream(outputStream);
            System.setOut(out);
            System.setErr(out);
        }
        
        /** Stops capturing output and returns the string of captured output. */
        public static synchronized String stop() {
            String actualOutput = "";
            if (isCapturing()) {
                System.out.flush();
                System.err.flush();
                System.setOut(SYSTEM_OUT);
                System.setErr(SYSTEM_ERR);
                if (isCapturing()) {
                    actualOutput = outputStream.toString().replace("\r", "");
                }
                outputStream = null;
            }
            return actualOutput;
        }
    }
}
