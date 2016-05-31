// CSE 143, Homework 3: HTML Validator
//
// Instructor-provided code.
// This program is a very simple test for your HTML validator object.
// Please feel free to modify this file to create your own simple test cases.

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;
import org.junit.Test;

/** A basic JUnit test for some methods of your HTML validator. */
public class SimpleTest {
    @Test(timeout = 2000)
	public void test1_ValidGetTags() {
        // first test for short valid code
        // <html><body><b>hello</b><i>how are <b>you</b><br/></i></body></html>
        Queue<HtmlTag> tags = new LinkedList<HtmlTag>();
        tags.add(new HtmlTag("html", true));   // <html>
        tags.add(new HtmlTag("body", true));   // <body>
        tags.add(new HtmlTag("b", true));      // <b>
        tags.add(new HtmlTag("b", false));     // </b>
        tags.add(new HtmlTag("i", true));      // <i>
        tags.add(new HtmlTag("b", true));      // <b>
        tags.add(new HtmlTag("b", false));     // </b>
        tags.add(new HtmlTag("br"));           // <br/>
        tags.add(new HtmlTag("i", false));     // </i>
        tags.add(new HtmlTag("body", false));  // </body>
        tags.add(new HtmlTag("html", false));  // </html>
        
        HtmlValidator validator = new HtmlValidator(tags);
        assertEquals("getTags", tags, validator.getTags());
    }
    
    @Test(timeout = 2000)
	public void test2_ValidValidate() {
        // first test for short valid code
        // <html><body><b>hello</b><i>how are <b>you</b><br/></i></body></html>
        Queue<HtmlTag> tags = new LinkedList<HtmlTag>();
        tags.add(new HtmlTag("html", true));   // <html>
        tags.add(new HtmlTag("body", true));   // <body>
        tags.add(new HtmlTag("b", true));      // <b>
        tags.add(new HtmlTag("b", false));     // </b>
        tags.add(new HtmlTag("i", true));      // <i>
        tags.add(new HtmlTag("b", true));      // <b>
        tags.add(new HtmlTag("b", false));     // </b>
        tags.add(new HtmlTag("br"));           // <br/>
        tags.add(new HtmlTag("i", false));     // </i>
        tags.add(new HtmlTag("body", false));  // </body>
        tags.add(new HtmlTag("html", false));  // </html>
        
        HtmlValidator validator = new HtmlValidator(tags);
        
        OutputCapturer.start();
        validator.validate();
        String output = OutputCapturer.stop();
        String expectedOutput = "<html>\n    <body>\n        <b>\n        </b>\n        <i>\n            <b>\n            </b>\n            <br>\n        </i>\n    </body>\n</html>\n";
        assertEquals("validate output", expectedOutput, output);
    }
    
    @Test(timeout = 2000)
	public void test3_InvalidAddTag() {
        // second test for invalid code and setTags
        HtmlValidator validator = new HtmlValidator();
        Queue<HtmlTag> tags = new LinkedList<HtmlTag>();
        tags.add(new HtmlTag("html", true));   // <html>
        tags.add(new HtmlTag("body", true));   // <body>
        tags.add(new HtmlTag("b", true));      // <b>
        tags.add(new HtmlTag("body", false));  // </body>
        tags.add(new HtmlTag("b", false));     // </b>
        tags.add(new HtmlTag("html", false));  // </html>
        
        // students should not use a for-each loop over a Queue!
        for (HtmlTag tag : tags) {
        	validator.addTag(tag);
        }
        assertEquals("getTags", tags, validator.getTags());
    }
    
    @Test(timeout = 2000)
	public void test4_InvalidValidate() {
        HtmlValidator validator = new HtmlValidator();
        Queue<HtmlTag> tags = new LinkedList<HtmlTag>();
        tags.add(new HtmlTag("html", true));   // <html>
        tags.add(new HtmlTag("body", true));   // <body>
        tags.add(new HtmlTag("b", true));      // <b>
        tags.add(new HtmlTag("body", false));  // </body>
        tags.add(new HtmlTag("b", false));     // </b>
        tags.add(new HtmlTag("html", false));  // </html>
        
        // students should not use a for-each loop over a Queue!
        for (HtmlTag tag : tags) {
        	validator.addTag(tag);
        }

        OutputCapturer.start();
        validator.validate();
        String output = OutputCapturer.stop();
        String expectedOutput = "<html>\n    <body>\n        <b>\nERROR unexpected tag: </body>\n        </b>\nERROR unexpected tag: </html>\nERROR unclosed tag: <body>\nERROR unclosed tag: <html>\n";
        assertEquals("validate output", expectedOutput, output);

        OutputCapturer.start();
        validator.validate();
        output = OutputCapturer.stop();
        assertEquals("validate output (2nd call)", expectedOutput, output);
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
