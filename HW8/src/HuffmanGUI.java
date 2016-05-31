// CSE 143 Homework 8: Huffman Coding  (instructor-provided file)
//
// author: Marty Stepp, 2010/03/06
//
// This graphical client program interacts with the user to compress and
// decompress files using your HuffmanTree class.

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class HuffmanGUI implements ActionListener {
    // Runs the program and starts the GUI.
    public static void main(String[] args) {
        new HuffmanGUI().start();
    }
    
    // font size for input/output file text
    private static final Font MONOSPACED_FONT = new Font("monospaced", Font.PLAIN, 12);

    // whether or not to prevent re-compressing binary files or decompressing text files
    private static final boolean CHECK_BINARY = true;
    
    // fields
    private JFrame frame;
    private JTextField inputFileField, outputFileField;
    private JTextArea inputFileArea, outputFileArea;
    private JButton inputBrowse, inputLoad, outputBrowse, outputLoad, outputSwap, compress, decompress;
    private JLabel inputStatusLabel, outputStatusLabel, compressStatusLabel;
    private JFileChooser chooser;
    
    // Constructs the GUI, sets up all graphical components, layout, and events.
    public HuffmanGUI() {
        // make Java look like current operating system
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        // main window frame
        frame = new JFrame("CSE 143 Huffman Coding");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // NW panel for input file
        Container textAndBrowse = new JPanel(new BorderLayout());
        inputFileField = new JTextField(20);
        inputFileField.addActionListener(this);
        textAndBrowse.add(new JLabel("Input file: "), BorderLayout.WEST);
        textAndBrowse.add(inputFileField);
        Container textAndBrowseEast = new JPanel(new FlowLayout());
        inputBrowse = createButton("Browse...", 'B', this, textAndBrowseEast);
        inputLoad = createButton("Reload", 'L', this, textAndBrowseEast);
        textAndBrowse.add(textAndBrowseEast, BorderLayout.EAST);
        
        // text area in which to load input file contents
        inputFileArea = new JTextArea(20, 40);
        inputFileArea.setFont(MONOSPACED_FONT);
        inputFileArea.setEditable(false);
        inputFileArea.setFocusable(false);
        inputStatusLabel = new JLabel("No file loaded");
        
        Container south = new JPanel(new BorderLayout());
        south.add(new JScrollPane(inputFileArea));
        south.add(inputStatusLabel, BorderLayout.SOUTH);
        
        Container inputFilePanel = new JPanel(new BorderLayout());
        inputFilePanel.add(textAndBrowse, BorderLayout.NORTH);
        inputFilePanel.add(south, BorderLayout.CENTER);

        // NE panel for output file
        // a bit redundant, but oh well; -1 otherwise good style
        Container textAndBrowse2 = new JPanel(new BorderLayout());
        outputFileField = new JTextField(20);
        outputFileField.addActionListener(this);
        textAndBrowse2.add(new JLabel("Output file: "), BorderLayout.WEST);
        textAndBrowse2.add(outputFileField);
        Container textAndBrowseEast2 = new JPanel(new FlowLayout());
        outputBrowse = createButton("Browse...", 'B', this, textAndBrowseEast2);
        // outputLoad = createButton("Reload", 'L', this, textAndBrowseEast2);
        outputSwap = createButton("Swap", 'S', this, textAndBrowseEast2);
        textAndBrowse2.add(textAndBrowseEast2, BorderLayout.EAST);
        
        // text area in which to load output file contents
        outputFileArea = new JTextArea(20, 40);
        outputFileArea.setFont(MONOSPACED_FONT);
        outputFileArea.setEditable(false);
        outputFileArea.setFocusable(false);
        outputStatusLabel = new JLabel("No file loaded");
        
        Container south2 = new JPanel(new BorderLayout());
        south2.add(new JScrollPane(outputFileArea));
        south2.add(outputStatusLabel, BorderLayout.SOUTH);
        
        Container outputFilePanel = new JPanel(new BorderLayout());
        outputFilePanel.add(textAndBrowse2, BorderLayout.NORTH);
        outputFilePanel.add(south2, BorderLayout.CENTER);

        // bottom-middle panel for buttons to compress/decompress
        Container compressPanel = new JPanel(new BorderLayout());
        Container compressCenter = new JPanel(new FlowLayout());
        compress = createButton("Compress", 'C', this, compressCenter);
        decompress = createButton("Decompress", 'D', this, compressCenter);
        compressStatusLabel = new JLabel("No file has been compressed yet");
        compressStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        compressPanel.add(compressCenter);
        compressPanel.add(compressStatusLabel, BorderLayout.SOUTH);

        // overall window's layout
        JPanel contentPane = new JPanel(new BorderLayout(15, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.add(inputFilePanel, BorderLayout.WEST);
        contentPane.add(outputFilePanel, BorderLayout.EAST);
        contentPane.add(compressPanel, BorderLayout.SOUTH);
        
        // size frame and place it in the center of the screen
        frame.add(contentPane);
        frame.pack();
        center(frame);
    }
    
    // Called whenever the user clicks a button or presses Enter on a text field.
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == inputBrowse || source == outputBrowse) {
            JTextField field = (source == inputBrowse) ? inputFileField : outputFileField;
            JTextArea area = (source == inputBrowse) ? inputFileArea : outputFileArea;
            JLabel label = (source == inputBrowse) ? inputStatusLabel : outputStatusLabel;
            
            // pop up a Browse... file chooser
            String currentDir = System.getProperty("user.dir");
            if (chooser == null) {
                chooser = new JFileChooser(currentDir);
            }
            
            // load the selected file
            int result = chooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File inputFile = chooser.getSelectedFile();
                if (inputFile != null) {
                    // shorten displayed file names if possible for prettier GUI
                    String filename = inputFile.getAbsolutePath();
                    if (filename.startsWith(currentDir + File.separator)) {
                        filename = filename.replace(currentDir + File.separator, "");
                    }
                    field.setText(filename);
                    load(filename, area, label);
                }
            }
        } else if (source == inputLoad || source == outputLoad) {
            // load file from disk and display on text area
            JTextField field = (source == inputLoad) ? inputFileField : outputFileField;
            JTextArea area = (source == inputLoad) ? inputFileArea : outputFileArea;
            JLabel label = (source == inputLoad) ? inputStatusLabel : outputStatusLabel;

            String filename = field.getText().trim();
            load(filename, area, label);
        } else if (source == inputFileField) {
            // user pressed Enter on input file text field; load file and display it
            String inputFileName = inputFileField.getText().trim();
            if (inputFileName.length() == 0) {
                return;
            }
            load(inputFileName, inputFileArea, inputStatusLabel);
        } else if (source == outputFileField) {
            // user pressed Enter on output file text field; load file and display it
            String outputFileName = outputFileField.getText().trim();
            if (outputFileName.length() == 0) {
                return;
            }
            load(outputFileName, outputFileArea, outputStatusLabel);
        } else if (source == outputSwap) {
            // swap the input and output file displays (for easier testing)
            String inputFileName = inputFileField.getText().trim();
            String outputFileName = outputFileField.getText().trim();
            if (inputFileName.length() == 0 || outputFileName.length() == 0) {
                return;
            }
            outputFileField.setText(inputFileName);
            inputFileField.setText(outputFileName);
            load(outputFileName, inputFileArea, inputStatusLabel);
            outputFileArea.setText("");
            // load(inputFileName, outputFileArea, outputStatusLabel);
        } else if (source == compress) {
            // compress the currently selected file
            compress();
        } else if (source == decompress) {
            // decompress the currently selected file
            decompress();
        }
    }

    // Helper to load the contents of the given file and display them into the
    // given text area.  The given label displays status updates along the way.
    // Works for both text and binary files.
    public void load(final String filename, final JTextArea area, final JLabel label) {
        final File file = new File(filename);
        if (file.exists()) {
            new Thread(new Runnable() {
                public void run() {
                    label.setText("Loading ...");
                    try {
                        if (isBinaryFile(filename)) {
                            // process binary file, bit by bit
                            area.setText(readEntireFileBits(filename));
                            area.setLineWrap(true);
                            label.setText("Binary file, " + file.length() 
                                    + " bytes  (" + file.length() / 1024 + " kb) "
                                    + "(last byte may show trailing 0s)");
                        } else {
                            // process regular text file, byte by byte
                            area.setText(readEntireFile(filename));
                            area.setLineWrap(false);
                            label.setText("Text file, " + file.length() + " bytes (" + file.length() / 1024 + " kb)");
                        }
                        area.setSelectionStart(0);   // scroll to top
                        area.setSelectionEnd(0);
                    } catch (IOException ioe) {
                        area.setText("");
                        label.setText("No file loaded");
                        ioe.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "I/O error occurred:\n"
                                + ioe.getMessage(), "I/O error", JOptionPane.ERROR_MESSAGE);
                    } catch (OutOfMemoryError mem) {
                        area.setText("(file too large to display)");
                        label.setText("No file loaded");
                        mem.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Out of memory error occurred:\n"
                                + mem.getMessage(), "Memory error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }).start();
        } else {
            // user has not yet selected an input and output file
            area.setText("");
            label.setText("No file loaded");
        }
    }
    
    // Runs the GUI by displaying its graphical frame.
    public void start() {
        frame.setVisible(true);
    }
    
    // Helper to compress the currently selected input file to the selected output file.
    private void compress() {
        final String inputFileName = inputFileField.getText().trim();
        final String outputFileName = outputFileField.getText().trim();
        if (inputFileName.length() == 0 || outputFileName.length() == 0) {
            return;
        }
        
        // run compression in a separate thread of execution because it can be slow
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (CHECK_BINARY && isBinaryFile(inputFileName)) {
                        JOptionPane.showMessageDialog(frame, "Cannot compress a file that is already binary!",
                                "Cannot compress", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    // get file's character counts
                    compressStatusLabel.setText("Compression in progress ...");
                    outputFileArea.setText("...");
                    outputStatusLabel.setText("Counting characters ...");
                    Map<Character, Integer> counts = HuffMain.getCounts(new FileInputStream(inputFileName));
                    HuffmanTree huffmanTree = new HuffmanTree(counts);

                    outputStatusLabel.setText("Creating encodings ...");
                    Map<Character, String> encodings = huffmanTree.createEncodings();
                    if (encodings == null || !encodings.containsKey(BitOutputStream.EOF)) {
                        throw new IOException("Your encodings map does not contain an "
                                + "encoding for end-of-file (EOF) character (ASCII 256)");
                    }
                    
                    // compress
                    InputStream input = new FileInputStream(inputFileName);
                    FileOutputStream fileOut = new FileOutputStream(outputFileName);
                    BitOutputStream bitOut = new BitOutputStream(fileOut);
                    String eofEncoding = encodings.get(BitOutputStream.EOF);
                    bitOut.setEOFEncoding(eofEncoding);

                    // use huffman tree and counts to compress the input file
                    // (also time it so we can display the runtime afterward)
                    outputStatusLabel.setText("Compressing ...");
                    long startTime = System.currentTimeMillis();
                    huffmanTree.compress(input, bitOut);
                    bitOut.close();
                    fileOut.close();

                    outputStatusLabel.setText("Saving encodings ...");
                    String countsFileName = outputFileName + HuffMain.COUNTS_SUFFIX;
                    OutputStream countOutput = new BufferedOutputStream(new FileOutputStream(countsFileName));
                    HuffMain.saveCounts(countsFileName, counts, countOutput);

                    // display compressed file
                    load(outputFileName, outputFileArea, outputStatusLabel);

                    long endTime = System.currentTimeMillis();
                    long elapsed = endTime - startTime;
                    compressStatusLabel.setText("Compression complete (" + elapsed + "ms)");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "I/O error occurred:\n"
                            + ioe.getMessage(), "I/O error", JOptionPane.ERROR_MESSAGE);
                } catch (OutOfMemoryError mem) {
                    mem.printStackTrace();
                    outputFileArea.setText("(file too large to display)");
                    JOptionPane.showMessageDialog(frame, "Out of memory error occurred:\n"
                            + mem.getMessage(), "Memory error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    private void decompress() {
        final String inputFileName = inputFileField.getText().trim();
        final String outputFileName = outputFileField.getText().trim();
        if (inputFileName.length() == 0 || outputFileName.length() == 0) {
            return;
        }
        
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (CHECK_BINARY && !isBinaryFile(inputFileName)) {
                        JOptionPane.showMessageDialog(frame, "Cannot decompress a text (non-binary) file",
                                "Cannot compress", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    // get file's character counts
                    compressStatusLabel.setText("Decompression in progress ...");
                    outputFileArea.setText("...");
                    outputStatusLabel.setText("Loading encodings ...");
                    String countsFileName = inputFileName + HuffMain.COUNTS_SUFFIX;
                    Map<Character, Integer> counts = HuffMain.loadCounts(new BufferedInputStream(new FileInputStream(countsFileName)));
                    HuffmanTree huffmanTree = new HuffmanTree(counts);

                    outputStatusLabel.setText("Creating encodings ...");
                    
                    // decompress the file
                    InputStream input = new BufferedInputStream(new FileInputStream(inputFileName));
                    OutputStream output = new BufferedOutputStream(new FileOutputStream(outputFileName));
                    BitOutputStream bitOut = new BitOutputStream(output);
                    BitInputStream bitIn = new BitInputStream(input, bitOut, true);
                    
                    // use huffman tree and counts to decompress the input file
                    // (also time it so we can display the runtime afterward)
                    outputStatusLabel.setText("Decompressing ...");
                    long startTime = System.currentTimeMillis();
                    huffmanTree.decompress(bitIn, bitOut);
                    bitIn.close();
                    output.close();
                    bitOut.close();
                    long endTime = System.currentTimeMillis();
                    long elapsed = endTime - startTime;
                    
                    // display decompressed file
                    load(outputFileName, outputFileArea, outputStatusLabel);
                    compressStatusLabel.setText("Decompression complete (" + elapsed + "ms)");
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(frame, "I/O error occurred:\n"
                            + ioe.getMessage(), "I/O error", JOptionPane.ERROR_MESSAGE);
                    ioe.printStackTrace();
                } catch (OutOfMemoryError mem) {
                    JOptionPane.showMessageDialog(frame, "Out of memory error occurred:\n"
                            + mem.getMessage(), "Memory error", JOptionPane.ERROR_MESSAGE);
                    mem.printStackTrace();
                }
            }
        }).start();
    }

    // Moves the given window to the center of the screen.
    private static void center(Component comp) {
        Dimension size = comp.getSize();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        comp.setLocation(Math.max(0, (screen.width - size.width) / 2), Math
                .max(0, (screen.height - 24 - size.height) / 2));
    }

    // Helper method to create a JButton with the given properties.
    private static JButton createButton(String text, char mnemonic,
            ActionListener listen, Container panel) {
        JButton button = new JButton(text);
        if (mnemonic != '\0') {
            button.setMnemonic(mnemonic);
        }
        button.addActionListener(listen);
        if (panel != null) {
            panel.add(button);
        }
        button.setFocusable(false);
        return button;
    }
    
    // Returns true if the given filename refers to a binary file.
    // Figures this out unscientifically by looking at each byte for non-ASCII
    // characters or for null bytes (0s).  Gives up with false after 65536 bytes.
    private static boolean isBinaryFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            return false;
        }

        boolean result = false;
        int threshold = 65536;   // number of bytes to check
        Reader in = new BufferedReader(new FileReader(file));
        for (int i = 0; i < threshold; i++) {
            int b = in.read();
            if (b == 0 || b > 128) {
                result = true;
                break;
            } else if (b == -1) {
                break;
            }
        }
        
        return result;
    }
    
    // Returns the entire contents of the given file as a string.
    // To be used with text files.
    private static String readEntireFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }
        StringBuilder sb = new StringBuilder((int) file.length() + 10);
        Reader in = new BufferedReader(new FileReader(file));
        while (in.ready()) {
            sb.append((char) in.read());
        }
        return sb.toString();
    }
    
    // Returns the entire contents of the given file's bits as a string.
    // To be used with binary files.
    private static String readEntireFileBits(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }
        StringBuilder sb = new StringBuilder((int) file.length() * 8 + 1024);
        BitInputStream in = new BitInputStream(new FileInputStream(filename));
        while (in.hasNextBit()) {
            sb.append(String.valueOf(in.readBit()));
        }
        return sb.toString();
    }
}
