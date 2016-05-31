// CSE 143 Homework 8: Huffman Coding  (instructor-provided file)
//
// author: Marty Stepp, 2009/03/06
// based on Huffman client programs by Stuart Reges, written 5/25/05
//
// This client program interacts with the user to compress and decompress files
// using your HuffmanTree class.
//
// This version supports command-line arguments and can be run such as:
//   java HuffMain2 c example.txt example.huf
//   java HuffMain2 d example.huf example.out

import java.io.*;
import java.util.*;

public class HuffMain2 extends HuffMain {
    public static void main(String[] args) throws IOException {
        System.out.println("This program encodes/decodes extra credit Huffman files.");
        
        String choice;
        if (args.length >= 1) {
            choice = args[0];
        } else {
            System.out.print("(c)ompress or (d)ecompress? ");
            choice = CONSOLE.nextLine().toLowerCase().trim();
        }
        
        long startTime = System.currentTimeMillis();
        String inputFileName  = args.length >= 2 ? args[1] : "";
        String outputFileName = args.length >= 3 ? args[2] : "";
        if (choice.startsWith("c")) {
            startTime = compress(inputFileName, outputFileName);
        } else if (choice.startsWith("d")) {
            startTime = decompress(inputFileName, outputFileName);
        }
        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("\nDone. (" + elapsed + "ms)");
    }
    
    // interacts with the user to compress a file and returns elapsed time in MS
    public static long compress(String inputFileName, String outputFileName) throws IOException {
        // prompt for input file name and count characters in input file
        boolean suppliedInputFileName = true;
        if (inputFileName == "") {
            suppliedInputFileName = false;
            inputFileName = getInputFileName();
        }
        System.out.println("Reading file and counting characters ...");
        Map<Character, Integer> counts = getCounts(new BufferedInputStream(new FileInputStream(inputFileName)));
        
        // use huffman tree to create bit encodings for each character
        HuffmanTree tree = new HuffmanTree2(counts);
        System.out.println("\nCharacter encodings:");
        Map<Character, String> encodings = tree.createEncodings();
        printEncodings(encodings);

        // prompt for output file name (print to System.out if left blank)
        if (!suppliedInputFileName && outputFileName == "") {
            System.out.print("Output file name (blank for System.out)? ");
            outputFileName = CONSOLE.nextLine().trim();
        }
        
        OutputStream output = new OpenPrintStream(System.out);
        if (outputFileName.length() > 0) {
            output = new BufferedOutputStream(new FileOutputStream(outputFileName));
        }

        // open source file, open output
        InputStream input = new BufferedInputStream(new FileInputStream(inputFileName));
        boolean bitMode = !DEBUG && !(output instanceof OpenPrintStream);
        BitOutputStream bitOut = new BitOutputStream(output, bitMode);
        if (!encodings.containsKey(BitOutputStream.EOF)) {
            throw new RuntimeException("Your encodings map does not contain an "
                    + "encoding for end-of-file (EOF) character (ASCII 256)");
        }
        bitOut.setEOFEncoding(encodings.get(BitOutputStream.EOF));

        // compress the file
        System.out.println("Compressing ...");
        long startTime = System.currentTimeMillis();
        tree.compress(input, bitOut);
        if (output instanceof BufferedOutputStream) {
            bitOut.close();
            output.close();
        } else {
            System.out.println();
        }
        
        return startTime;
    }

    // Interacts with user to decompress a file and returns time elapsed in MS.
    public static long decompress(String inputFileName, String outputFileName) throws IOException {
        boolean suppliedInputFileName = true;
        if (inputFileName == "") {
            suppliedInputFileName = false;
            inputFileName = getInputFileName();
        }
        
        // create tree (no counts are passed this time but 1 EOF; you're on your own!)
        Map<Character, Integer> counts = new TreeMap<Character, Integer>();
        counts.put(BitOutputStream.EOF, 1);
        HuffmanTree tree = new HuffmanTree2(counts);
        
        // prompt for output file name
        if (!suppliedInputFileName && outputFileName == "") {
            System.out.print("Output file name (blank for System.out)? ");
            outputFileName = CONSOLE.nextLine().trim();
        }
        
        // set up in/out streams (wrap in BitIn/OutStreams for EOF and bit mode)
        InputStream input = new BufferedInputStream(new FileInputStream(inputFileName));
        OutputStream output = new OpenPrintStream(System.out);
        if (outputFileName.length() > 0) {
            output = new BufferedOutputStream(new FileOutputStream(outputFileName));
        }
        BitOutputStream bitOut = new BitOutputStream(output);
        boolean bitMode = !DEBUG;
        BitInputStream bitIn = new BitInputStream(input, bitOut, bitMode);
        
        // use huffman tree and counts to decompress the input file
        System.out.println("Decompressing " + inputFileName + " ...");
        long startTime = System.currentTimeMillis();
        tree.decompress(bitIn, bitOut);

        if (outputFileName.length() > 0) {
            output.close();
        }
        
        return startTime;
    }
}
