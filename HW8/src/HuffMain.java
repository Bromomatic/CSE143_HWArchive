// CSE 143 Homework 8: Huffman Coding  (instructor-provided file)
//
// author: Marty Stepp, 2009/03/06
// based on Huffman client programs by Stuart Reges, written 5/25/05
//
// This client program interacts with the user to compress and decompress files
// using your HuffmanTree class.

import java.io.*;
import java.util.*;

public class HuffMain {
    // set to true to always read/write files in 'byte' mode (for debugging)
    public static final boolean DEBUG = false;
    
    public static final Scanner CONSOLE = new Scanner(System.in);
    public static final String COUNTS_SUFFIX = ".counts";

    public static void main(String[] args) throws IOException {
        System.out.println("This program encodes/decodes Huffman compressed files.");
        System.out.print("(c)ompress or (d)ecompress? ");
        String choice = CONSOLE.nextLine().toLowerCase().trim();
        
        long startTime = System.currentTimeMillis();
        if (choice.startsWith("c")) {
            startTime = compress();
        } else if (choice.startsWith("d")) {
            startTime = decompress();
        }
        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("\nDone. (" + elapsed + "ms)");
    }
    
    // repeatedly prompts user until a valid file name is typed; returns it
    public static String getInputFileName() {
        System.out.print("Input file name? ");
        String fileName = CONSOLE.nextLine().trim();
        
        // stop loop if file exists or (blank is ok and filename is blank)
        while (!(new File(fileName)).exists()) {
            System.out.print("File not found. Input file name? ");
            fileName = CONSOLE.nextLine().trim();
        }
        return fileName;
    }
    
    // interacts with the user to compress a file and returns elapsed time in MS
    public static long compress() throws IOException {
        // prompt for input file name and count characters in input file
        String inputFileName = getInputFileName();
        // *** System.out.println("Reading file ...");
        Map<Character, Integer> counts = getCounts(new BufferedInputStream(new FileInputStream(inputFileName)));
        
        // use huffman tree to create bit encodings for each character
        HuffmanTree tree = new HuffmanTree(counts);
        System.out.println("\nCharacter encodings:");
        Map<Character, String> encodings = tree.createEncodings();
        printEncodings(encodings);

        // prompt for output file name (print to System.out if left blank)
        System.out.print("Output file name (blank for System.out)? ");
        String outputFileName = CONSOLE.nextLine().trim();
        OutputStream output = new OpenPrintStream(System.out);
        if (outputFileName.length() > 0) {
            output = new BufferedOutputStream(new FileOutputStream(outputFileName));
        }

        // open source file, open output
        InputStream input = new BufferedInputStream(new FileInputStream(inputFileName));
        boolean bitMode = !DEBUG && !(output instanceof OpenPrintStream);
        BitOutputStream bitOut = new BitOutputStream(output, bitMode);
        if (encodings == null || !encodings.containsKey(BitOutputStream.EOF)) {
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
        
        // write character counts to a file (for decoding compressed file later)
        if (output instanceof BufferedOutputStream) {
            String countsOutputFileName = outputFileName + COUNTS_SUFFIX;
            System.out.println("Saving character counts to " + countsOutputFileName + " ...");
            OutputStream countOutput = new BufferedOutputStream(new FileOutputStream(countsOutputFileName));
            saveCounts(outputFileName, counts, countOutput);
            countOutput.close();
        }
        
        return startTime;
    }
    
    // Reads the given stream and counts the number of occurrences of each
    // character.  Returns these counts as a [char -> int] map.
    public static Map<Character, Integer> getCounts(InputStream input) throws IOException {
        Map<Character, Integer> counts = new TreeMap<Character, Integer>();
        int n;
        while ((n = input.read()) != -1) {
            char ch = (char) n;
            if (n >= BitOutputStream.EOF) {
                throw new IOException("Illegal character: '" + ch + 
                        "' (ASCII: " + (int) ch + ")");
            }
            if (counts.containsKey(ch)) {
                counts.put(ch, counts.get(ch) + 1);
            } else {
                counts.put(ch, 1);
            }
        }
        
        // insert pseudo-end-of-file character so bit stream sees end of input
        counts.put(BitOutputStream.EOF, 1);
        
        return counts;
    }
    
    // Saves the map of [char -> int count] mappings to the given output file.
    public static void saveCounts(String fileName, Map<Character, Integer> counts, 
            OutputStream output) throws IOException {
        Properties props = new Properties();
        for (char ch : counts.keySet()) {
            int count = counts.get(ch);
            props.setProperty(String.valueOf((int) ch), String.valueOf(count));
        }
        props.store(output, "CSE 143 Huffman counts file for " + fileName);
    }
    
    // Saves the map of [char -> int count] mappings to the given output file.
    public static Map<Character, Integer> loadCounts(InputStream input) throws IOException {
        Properties props = new Properties();
        props.load(input);
        Map<Character, Integer> counts = new TreeMap<Character, Integer>();
        for (Object key : props.keySet()) {
            char ch = (char) Integer.parseInt(key.toString());
            int count = Integer.parseInt(props.getProperty(key.toString()));
            counts.put(ch, count);
        }
        return counts;
    }
    
    // Prints out your HuffmanTree's map as returned by createEncodings.
    // Before printing, I mildly reformat characters to make them easy to read.
    public static void printEncodings(Map<Character, String> encodings) {
        if (encodings == null) {
            System.out.println("null");
        } else {
            for (char ch : encodings.keySet()) {
                String encoded = encodings.get(ch);
                System.out.print(BitOutputStream.toPrintable(ch) + "=" + encoded + " ");
            }
        }
        System.out.println();
        System.out.println();
    }
    
    // Interacts with user to decompress a file and returns time elapsed in MS.
    public static long decompress() throws IOException {
        String inputFileName = getInputFileName();
        
        // reload counts from count file
        String countsInputFileName = inputFileName + COUNTS_SUFFIX;
        System.out.println("Loading character counts from " + countsInputFileName + " ...");
        InputStream countInput = new BufferedInputStream(new FileInputStream(countsInputFileName));
        Map<Character, Integer> counts = loadCounts(countInput);

        // create tree and build character encodings
        HuffmanTree tree = new HuffmanTree(counts);
        System.out.println("\nCharacter encodings:");
        Map<Character, String> encodings = tree.createEncodings();
        printEncodings(encodings);
        
        // prompt for output file name
        System.out.print("Output file name (blank for System.out)? ");
        String outputFileName = CONSOLE.nextLine().trim();
        
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
    
    // little helper class because System.out should not be closed
    public static class OpenPrintStream extends PrintStream {
        public OpenPrintStream(PrintStream stream) {
            super(stream);
        }
        
        public void write(int b) {
            super.write(b);
            flush();
        }
        
        public void close() {}
    }
}
