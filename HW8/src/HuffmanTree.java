// Shawn Stern     CSE 143     HW8
//
// This HuffmanTree object represents the overall binary tree of character frequencies in a
// given text (or other kind!) file.  A client can construct a new HuffmanTree, query the object
// for a map of character encodings based on the tree's structure, and compress/decompress 
// an input stream into a respective output stream.

import java.util.*;
import java.io.*;

public class HuffmanTree {
	private HuffmanNode root;	// Root of our Huffman Tree.  Lonely field is lonely :(
	
	// Takes a map of characters and their count values, and constructs a Huffman Tree object.
	//
	// Throws an IllegalArgumentException if passed map is null.
	public HuffmanTree(Map<Character, Integer> counts) {
		if (counts == null) {
			throw new IllegalArgumentException();
		}
		
		Queue<HuffmanNode> q = new PriorityQueue<HuffmanNode>();
		// Fill our queue with HuffmanNodes
		for (char a : counts.keySet()) {
			HuffmanNode temp = new HuffmanNode(a, counts.get(a));
			q.add(temp);			
		}
				
		//Build Tree!
		while (q.size() > 1) {
			// Pull out nodes
			HuffmanNode temp1 = q.remove();
			HuffmanNode temp2 = q.remove();
			// Make new node with combined counts as count, and temps as children
			HuffmanNode newDaddy = new HuffmanNode(temp1.count + temp2.count);
			newDaddy.left = temp1;
			newDaddy.right = temp2;
			// Put back in and continue the madness until loop finishes
			q.add(newDaddy);
		}
		
		// Last node left in the queue by this point should be our root node. 
		root = q.remove();
	}
	
	// Creates and returns a map of characters to their respective encoded binary strings.
	public Map<Character, String> createEncodings() {
		Map<Character, String> encodings = new TreeMap<Character, String>();
		encodings = encodingHelper(encodings, root, "");
		
		return encodings;
	}
	
	// Accepts an encoding map, a node, and a string.  Generates the Huffman codes
	// for all the characters in a tree and returns them.
	// Precondition:  none of the passed parameters are null.
	private Map<Character, String> encodingHelper(Map<Character, String> encodingMap, 
		HuffmanNode curr, String code) {
		// If current node is an actual character to be encoded, add it to the map!
		if (curr.isChar()) {
			encodingMap.put(curr.data, code);
		} else {
			// Continue making codes for the lulz
			encodingHelper(encodingMap, curr.left, code + "0");
			encodingHelper(encodingMap, curr.right, code + "1");
		}
		
		return encodingMap;		
	}
	
	// Accepts an input stream and output stream, and writes a compressed version of the input
	// to the output based on this object's HuffmanTree.
	//
	// Throws an IllegalArgumentException if any passed parameter is null.
	public void compress(InputStream input, BitOutputStream output) throws IOException {
		if (input == null || output == null) {
			throw new IllegalArgumentException();
		}
		
		Map<Character, String> code = createEncodings();
		
		while (true) {
			int n = input.read();
			// If stream gives us a -1, we've consumed the entire file, break!
			if (n == -1) {
				break;
			}
			char c = (char) n;
			String out = code.get(c);
			output.writeBits(out);
		}
		
		// Close streams!
		input.close();
		output.close();	
	}
	
	// Accepts an input stream and output stream, and writes the decompressed version of the input
	// to the output based on this object's HuffmanTree.
	//
	// Precondition:  Passed inputstream has enough bits to form a complete file
	// (so that we don't get stuck in the middle of a traversal because we ran out of bits)
	// Throws an IllegalArgumentException if any passed parameter is null.
	public void decompress(BitInputStream input, OutputStream output) throws IOException {
		if (input == null || output == null) {
			throw new IllegalArgumentException();
		}
		
		// While the inputstream still has something left in it, print chars to the stream.
		while (input.hasNextBit()) {
			decompressHelper(input, output, root);
		}
		
		// Close streams!
		input.close();
		output.close();
	}
	
	// Accepts a set of input/output streams and a Huffman Node.  Prints one char per call.
	// Precondition:  Passed inputstream has enough bits to form a complete file
	// (so that we don't get stuck in the middle of a traversal because we ran out of bits)
	// and no bit of value -1 is passed (handled by the hasNextBit() call)
	private void decompressHelper(BitInputStream input, OutputStream output, HuffmanNode curr) 
		throws IOException {
		// Base case, we've reached a printable character.  Print it and end this call.
		if (curr.isChar()) {
			output.write(curr.data);
		} else {
			// Recursive case, read the next bit and traverse left/right based on it's value
			int n = input.readBit();
			if (n == 0) {
				decompressHelper(input, output, curr.left);
			} else {
				decompressHelper(input, output, curr.right);
			}
		}
	}
}
