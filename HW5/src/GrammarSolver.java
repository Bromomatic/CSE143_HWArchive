// Shawn Stern     CSE_143_AK     HW5
//
// Grammar Solver takes a given List of BNF rules and uses it to produce
// non-terminals which are valid by BNF.  (Even if they don't really make sense)
// The GrammarSolver object can return a sorted string of the non-terminals it contains,
// can return true/false if the passed string is a non-terminal for the given grammar, 
// and can produce the above-mentioned valid BNF statements.
// *****
// If a random string like "   derp  " is passed to generate, it will return the same string.


import java.util.*;  // For maps, arrays, etc.

public class GrammarSolver {
	private static final Random rand = new Random();  // For random selection of rules.
	private Map<String, String[]> grammar;  // Map storing non-terminals to their respective rules.
	
	// Initializes a new GrammarSolver object for use by the client.
	// Throws an IllegalArgumentException if passed list is null or of 0 length.
	// Precondition: String is in the correct BNF format so we can cut it up based on ::= .
	public GrammarSolver(List<String> rules) {
		if (rules == null || rules.size() == 0) {
			throw new IllegalArgumentException();
		}

		grammar = new TreeMap<String, String[]>();  // Declare as TreeMap for sorting
		
		// For each line, break into a pair of Non-Terminal and rules(s), and put into map
		for (String a : rules) {
			// Pre-trim string in case the user is a jerk and leads the non-terminal with spaces
			a.trim();
			// If the passed list is in the correct format, this should split the string 
			// into only 2 pieces.
			String[] parts = a.split("::=");
			
			String nonTerminal = parts[0];			
			String[] ruleset = parts[1].split("[|]");
			
			grammar.put(nonTerminal, ruleset);
		}
	}
	
	// Returns if the passed string is a non-terminal in the given grammar.
	// Throws an IllegalArgumentException if passed string is null or of 0 length.
	public boolean contains(String symbol) {
		if (symbol == null || symbol.length() == 0) {
			throw new IllegalArgumentException();
		}
		
		return grammar.containsKey(symbol);
		
	}
	
	// Returns the set of non-terminals that the given grammar operates on, in sorted order.
	public Set<String> getSymbols() {
		return grammar.keySet();
	}

	// Uses the grammar to randomly generate an occurrence of the passed symbol and return it.
	// This occurrence could be a noun, adjective, sentence, etc. 
	// If somehow the user passes a string that isn't in the grammar, it'll simply be returned.
	// Throws an IllegalArgumentException if passed string is null or of 0 length.
	public String generate(String symbol) {
		if (symbol == null || symbol.length() == 0) {
			throw new IllegalArgumentException();
		}
		// Base case.
		// If the passed symbol is a terminal, nothing left to do but return it!
		if (!contains(symbol)) {
			return symbol;
		}
		// Recursive case. 
		// Fully expand the passed symbol and choose random rules/terminals.
		else {
			String[] rules = grammar.get(symbol);
			String[] randRules = rules[rand.nextInt(rules.length)].trim().split("[ \t]+");
			String result = "";
			
			for (int i = 0; i < randRules.length; i++) {
				result = result + generate(randRules[i]) + " ";
			}
			
			// Return a trimmed string to avoid unwanted trailing spaces
			return result.trim();
		}		
	}
}