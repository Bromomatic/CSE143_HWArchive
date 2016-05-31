// CSE 143 Homework 8: Huffman Coding  (instructor-provided file)

import java.io.*;

/**
 * The BitOutputStream and BitInputStream classes provide the ability to
 * write and read individual bits to a file in a compact form.  One minor
 * limitation of this approach is that the resulting file will always have
 * a number of bits that is a multiple of 8.  In effect, whatever bits are
 * output to the file are padded at the end with 0s to make the total
 * number of bits a multiple of 8.
 * 
 * @author Marty Stepp, Stuart Reges, and Owen Astrachan
 * @version 2009-03-06
 */
public class BitInputStream extends InputStream {
    private InputStream input;    // actual source to read from
    private int digits;           // buffer to build up next byte's digits <= 8
    private int numDigits;        // how many digits are currently in buffer
    private boolean bitMode;      // true if writing bits; false to debug ASCII
    private BitOutputStream partner;  // another stream to monitor for EOF

    /**
     * Creates a BitInputStream reading bits of input from the given stream source.
     * @param input the input stream to read.
     * @throws IOException if the input stream cannot be accessed.
     */
    public BitInputStream(InputStream input) throws IOException {
        this(input, true);
    }
    
    /** 
     * Creates a BitInputStream reading bits/bytes input from the given stream source.
     * @param input the input stream to read.
     * @param bitMode true to write bits at a time; false to write ASCII 
     *                characters (bytes) at a time for debugging.
     * @throws IOException if the input stream cannot be accessed.
     */
    public BitInputStream(InputStream input, boolean bitMode) throws IOException {
        this(input, null, bitMode);
    }
    
    /** 
     * Creates a BitInputStream reading bits/bytes input from the given stream 
     * source and partnered with the given bit output stream.
     * @param input the input stream to read.
     * @param partner a "partner" bit output stream to watch for EOF.
     * @param bitMode true to write bits at a time; false to write ASCII 
     *                characters (bytes) at a time for debugging.
     * @throws IOException if the input stream cannot be accessed.
     */
    public BitInputStream(InputStream input, BitOutputStream partner, 
            boolean bitMode) throws IOException {
        this.input = input;
        setBitMode(bitMode);
        setPartner(partner);
        digits = 0;
        numDigits = 0;
        read();    // initialize buffer
    }
    
    /**
     * Closes this stream for reading. 
     * @throws IOException if the input stream cannot be closed.
     */
    public void close() throws IOException {
        input.close();
    }

    /**
     * Returns whether this stream has more bits available to be read.
     * @return true if more bits are available, otherwise false.
     */
    public boolean hasNextBit() {
        if (partner != null) {
            return !partner.hasSeenEOF() && digits != -1;
        } else {
            return digits != -1;
        }
    }
    
    /**
     * Returns whether this stream is in real 'bit mode', reading a bit from the
     * file for each call to readBit.  The alternative is 'byte mode', 
     * where a full byte (character) is read from the file each time readBit is 
     * called, to make it easier to debug your program.
     * @return true if in bit mode, false if in byte mode.
     */
    public boolean inBitMode() {
        return bitMode;
    }
    
    /**
     * Reads and returns a single byte of information from this stream.
     * @return the byte of information read, or -1 if no bytes remain to read.
     * @throws IOException if the input stream cannot be read.
     */
    public int read() throws IOException {
        int result = readByte();
        if (BitOutputStream.DEBUG) System.out.println("  ** BitInputStream read: " + result + 
                " (" + BitOutputStream.toPrintable((char) result) + ")");
        return result;
    }

    /**
     * Reads and returns the next single bit of input from this stream.
     * @return the bit of information read, or -1 if no bits remain to read.
     * @throws IOException if the input stream cannot be read.
     */
    public int readBit() throws IOException {
        int result = -1;
        if (hasNextBit()) {
            if (inBitMode()) {
                // read a single bit from our 1-byte buffer
                result = digits % 2;
                if (BitOutputStream.DEBUG) System.out.println("  ** BitInputStream readBit: " + result);
                digits /= 2;
                numDigits++;
                if (numDigits == BitOutputStream.BYTE_SIZE) {
                    read();   // replenish buffer if empty
                }
            } else {
                // read an entire byte
                result = read();
                if (result != '0' && result != '1') {
                    throw new IOException("Illegal byte value: " + result + 
                            " (this is probably a binary-compressed file)");
                }
                result -= '0';
            }
        }
        return result;
    }

    /**
     * Reads and returns an entire line of text from this bit input stream as a String.
     * You would not normally want to call this method while you're reading bits
     * from a bit input stream; it is provided as a convenience for students doing
     * extra credit functionality in the assignment.
     * @return the line read;  an empty string "" if no input remains.
     * @throws IOException if the input stream cannot be read.
     */
    public String readLine() throws IOException {
        StringBuilder line = new StringBuilder();
        while (true) {
            int n = read();
            if (n < 0 || n == '\n') {
                break;
            }
            if (n != '\r') {
                line.append((char) n);
            }
        }
        if (BitOutputStream.DEBUG) System.out.println("  ** BitInputStream readLine: \"" + line + "\"");
        return line.toString();
    }
    
    /**
     * Sets whether this stream is in real 'bit mode', reading a bit from the
     * file for each call to readBit (as described under inBitMode).
     * Ignores the caller and always uses 'byte' mode if reading from System.in
     * or if the JVM bitstream.bitmode environment variable is set.
     * @param bitMode true to use bit mode, false to use byte mode.
     */
    public void setBitMode(boolean bitMode) {
        this.bitMode = bitMode && input != System.in
                && System.getProperty("bitstream.bitmode") == null;
    }

    /**
     *  Called by Java when the program is shutting down;
     *  included to help ensure that the stream is closed.
     */
    protected void finalize() {
        try {
            close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    // Refreshes the internal buffer with the next BYTE_SIZE bits.
    private int readByte() throws IOException {
        int result = digits;
        digits = input.read();
        numDigits = 0;
        return result;
    }
    
    // Sets this bit input stream's "partner" output stream; 
    // students should not call this method.
    // This is a hack so that the input stream can detect the outputting
    // of a pseudo-EOF character to a partnered output stream.
    private void setPartner(BitOutputStream partner) {
        this.partner = partner;
    }
}
