// Shawn Stern   CSE 143   HW_3
//
// This object manages a queue of HTML tags.  The list can be initialized as either empty,
// or it can copy the contents of a pre-existing HTML document.  Tags may be added and removed,
// and the list of tags can also be validated to check if they are a valid combination.

import java.util.*;		// For Stacks and Queues.

public class HtmlValidator {
    Queue<HtmlTag> allTags;
    
    // Constructs an empty queue for storing HTML tags.
    public HtmlValidator() {
        allTags = new LinkedList<HtmlTag>();
    }
    
    // Constructs a queue based on the passed queue of HTML tags.
    // Precondition:  passed queue is not of type null, throws an exception if null.
    public HtmlValidator(Queue<HtmlTag> tags) {
    	if (tags == null) {
            throw new IllegalArgumentException();
        }
    	
        allTags = tags;
    }
    
    // Accepts an HTML tag and adds it to the queue.
    // Precondition:  passed tag is not of type null, throws an exception if null.
    // Postcondition:  a tag has been added to the queue.
    public void addTag(HtmlTag tag) {
    	if (tag == null) {
            throw new IllegalArgumentException();
        }
    	
        allTags.add(tag);
    }
    
    // Returns all tags in the HTML document.
    public Queue<HtmlTag> getTags() {
        return allTags;
    }
    
    // Accepts a string, and removes all tags which match the string.
    // Precondition:  passed string is not null, throws an exception if null.
    // Postcondition:  original queue structure is maintained, minus the removed elements.
    public void removeAll(String element) {
    	if (element == null) {
            throw new IllegalArgumentException();
        }
        
        int size = allTags.size();
        for (int i = 0; i < size; i++) {
            HtmlTag temp = allTags.remove();
            
            // If the tag isn't a match, put it back into the queue.
            if (!temp.getElement().equals(element)) {
                allTags.add(temp);
            }
        }
    }
    
    // Prints a text representation of all tags in the document.  Tags are indented
    // properly, and each occupies its own line.
    // Reports relevant errors, such as unclosed or unexpected tags in the document.
    // Postcondition:  the queue is in its original state, no modifications.
    public void validate() {
        Stack<HtmlTag> tags = new Stack<HtmlTag>();	// Stack to keep track of tag continuity
        int indentation = 0;
        
        // Go through the entire queue once, printing the resulting tags and/or errors.
        int size = allTags.size();
        for (int i = 0; i < size; i++) {
            HtmlTag temp = allTags.remove();		// Current tag, for comparison purposes.
            
            // Checking for ending tags first seems to be the best solution, due to the 
            // implementation of the isSelfClosing method in HtmlTag.  Otherwise, errors
            // wouldn't be reported for tags like </!doctype>.
            
            if (!temp.isOpenTag()) {				// Check if tag is an ending tag first.
                // Peek into the stack and compare tags (if not empty)
                if (!tags.isEmpty() && tags.peek().matches(temp)) {
                	indentation--;
                    printTag(temp, indentation);
                    tags.pop();
                } else {
                    // If we encounter a closing tag and the stack is empty or the tags don't 
                	// match, print an unexpected tag error.
                    System.out.println("ERROR unexpected tag: " + temp.toString());
                }
            } else if (temp.isSelfClosing()) { 		// Check if tag is self-closing.
                printTag(temp, indentation);
            } else { 								// Anything left over must be an opening tag.
                printTag(temp, indentation);
                tags.push(temp);
                indentation++;
            }
            
            // Restore the queue to it's prior state for any subsequent operations.
            allTags.add(temp);
        }
        
        // If the stack still has elements left in it, print errors until it is empty.
        while(!tags.isEmpty()) {
            HtmlTag error = tags.pop();
            System.out.println("ERROR unclosed tag: " + error.toString());
        }
    }
    
    // Prints the given tag, with the given amount of indentation (1 tab is 4 spaces).
    private void printTag(HtmlTag a, int x) {
        for (int i = 0; i < x; i++) {
            System.out.print("    ");
        }
        
        System.out.println(a.toString());
    }
}