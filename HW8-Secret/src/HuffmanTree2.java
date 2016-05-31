// Shawn Stern     CSE 143     HW8
//
// Super sneaky ninja version of HuffmanTree that includes the character counts of a file
// as a header of the compressed version.  This is awesome because now we only have one 
// compressed file instead of a compressed file and a clunky companion file just for counts.
//
// It's basically HuffmanTree, but sexier.
//
// Dev note:  I originally implemented this with the encodings as the header, but that gave
// me a huge recursive headache so I ended up doing it with counts, which is so much easier.
// 28 substantive lines vs 45, whee!

import java.util.*;
import java.io.*;

public class HuffmanTree2 extends HuffmanTree {
	private Map<Character, Integer> counts;		// Stores char counts for comp/decomp of file
		
	// Build a new HuffmanTree based on the passed counts, and keep a hold of our counts.
	//
	// Throws an IllegalArgumentException if passed map is null. (Throw is part of super method)
	public HuffmanTree2(Map<Character, Integer> counts) {
		super(counts);
		this.counts = counts;
	}
	
	// Accepts streams for a file to be compressed.
	// Compresses the file, adding an appropriate header that lists an ASCII code for a 
	// character and an integer count of its frequency, one char per line.  Blank line between
	// the list and the compressed contents of the file.
	//
	// Throws IllegalArgumentException if any passed parameter is null.
	public void compress(InputStream input, BitOutputStream output) throws IOException {
		if (input == null || output == null) {
			throw new IllegalArgumentException();
		}
		
		PrintStream p = new PrintStream(output);
		
		// Print encodings to file
		for (char a : counts.keySet()) {
			p.println((int) a + " " + counts.get(a));
		}
		
		// Blank line between encodings and actual file data.
		p.println();
		// This handles the actual compression of the file, and closes all streams.
		super.compress(input, output);
	}
	
	// Accepts streams for a compressed file.  Generates a new HuffmanTree based on the header
	// of the compressed file before actual decompression.
	//
	// Throws IllegalArgumentException if any passed parameter is null.
	public void decompress(BitInputStream input, OutputStream output) throws IOException {
		if (input == null || output == null) {
			throw new IllegalArgumentException();
		}

		// Reset our map of character counts from the file header
		while (true) {
			String temp = input.readLine();			
			// Stop adding elements once we reach a blank line
			if (temp.equals("")) {
				break;
			}
			
			Scanner s = new Scanner(temp);	// Line scanner, haven't seen this since 142!
			int a = s.nextInt();  			// Character in ACII encoding
			int b = s.nextInt();  			// # of occurrences of that char
			counts.put((char) a, b);		// Map overwrites existing EOF char, counts are fine	
		}
				
		// Reassign root to be a new tree, constructed with counts extracted from the file!
		changeRoot(counts);
		
		// Decompress the dang file already!  All this prep work is making me anxious...
		// Streams are also closed by super method.
		super.decompress(input, output);		
	}
}