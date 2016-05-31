// Shawn Stern     CSE 143     HW7
//
// Each QuestionNode object stores a single node of a binary tree of Strings.
// These strings are either a question, or an answer to a question in a game
// of "'20' questions".  ('20' because there's no limit on our tree's height)
//
// This class only has public fields, constructors, and a method to tell if 
// the current object is a leaf or not.

public class QuestionNode {
    public String data;				// String stored in this node
    public QuestionNode left;    	// reference to left subtree
    public QuestionNode right;   	// reference to right subtree

    // Constructs a leaf node with the given String and null links.
    public QuestionNode(String input) {
        this(input, null, null);
    }

    // Constructs a leaf or branch node with the given String and links.
    public QuestionNode(String input, QuestionNode left, QuestionNode right) {
        this.data = input;
        this.left = left;
        this.right = right;
    }
    
    // Returns true if the node is a leaf (has no children).
    public boolean isAnswer() {
    	return (left == null) && (right == null);
    }
}