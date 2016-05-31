// Shawn Stern     CSE 143     HW8
//
// Each HuffmanNode object stores a char, and an int representing how many times the 
// attached char has occurred (we're assuming from a file of some kind).
//
// This class only has public fields, constructors, a toString method for testing,
// a method allowing nodes to be compared to each other, and a test to see if the
// node is a leaf.


public class HuffmanNode implements Comparable<HuffmanNode> {
	public char data;
	public int count;
	public HuffmanNode left;
	public HuffmanNode right;
	
	// Constructs a HuffmanNode object with the passed int value.
	// If no char is passed, we assume the node is just part of the tree instead of
	// a leaf with an encoded character.
	public HuffmanNode(int x) {
		this('0', x);  // If no particular char is passed, just use a placeholder.
	}
	
	// Constructs a HuffmanNode object with the passed integer and char values.
	public HuffmanNode(char a, int x) {
		count = x;
		data = a;
	}	
	
	// Returns a string representation of the node for testing purposes.
	// Ex.  ['a'|3]
	public String toString() {
		return "['" + data + "'|" + count + "]";	
	}
	
	// Allows HuffmanNodes to be compared based on the frequency of their occurrence in a file.
	// Nodes are assumed to be equal if they occur the same number of times.
	public int compareTo(HuffmanNode other) {
		return count - other.count;
	}
	
	// Returns if the current node is a leaf (and therefore a char to be encoded)
	public boolean isChar() {
		return (left == null) && (right == null);
	}
}