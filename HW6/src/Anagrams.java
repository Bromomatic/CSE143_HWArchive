// Shawn Stern     CSE 143 AK     HW6
//
// Anagrams takes a given dictionary of words and finds all anagram phrases that match
// a passed word or phrase.  Can print a list of all possible anagrams, or a list of
// anagrams which are composed only of a certain number of words up to a maximum.
// Anagrams can also return a set containing all words from the dictionary that are 
// contained within the passed word/phrase.
//
//
// SIDENOTE:  helper getWords method = super-fast backtracking. So many hours...

import java.util.*;  // For sets, stacks, etc.

public class Anagrams {
	private Set<String> allWords; // Field to store the object's dictionary.
	
	// Creates a new Anagram solver object, using the passed dictionary.  
	// (Anagrams can only work if they have words included in the particular dictionary file) 
	// Precondition:  Passed list is sorted (TreeSet, etc.)
	// Throws IllegalArgumentException if passed set is null.
	public Anagrams(Set<String> dictionary) {
		if (dictionary == null) {
			throw new IllegalArgumentException();
		}
		
		allWords = dictionary;	
		// More efficient than making a brand new list and copying elements.
	}
	
	// Returns an alphabetically sorted set of words from the given dictionary that can be made
	// from the passed string.
	// Throws IllegalArgumentException if passed string is null.
	public Set<String> getWords(String phrase) {
		if (phrase == null) {
			throw new IllegalArgumentException();
		}
		
		LetterInventory inventory = new LetterInventory(phrase);
		
		return getWords(inventory, allWords);
	}
	
	// Private helper method for getWords and printHelper.  Accepts a LetterInventory and a
	// set of strings.  Returns a cut-down version of the passed set which contains only
	// words that can be made with the LetterInventory.
	// Precondition:  passed objects are non-null and play nicely.
	private Set<String> getWords(LetterInventory inventory, Set<String> inputSet) {
		Set<String> possible = new TreeSet<String>();
		
		for (String a : inputSet) {
			if(inventory.contains(a)) {
				possible.add(a);
			}
		}
		
		return possible;
	}

	// Prints all anagrams that can be formed by consuming all the letters in the given phrase.
	// Throws IllegalArgumentException if passed string is null.
	public void print(String phrase) {		
		print(phrase, 0);
	}
	
	// Prints only the anagrams which are made up of 'max' number of words or less and can be 
	// made by consuming all letters in the given phrase.
	// For example, calling marty stepp with a max of 2 will only print anagrams composed of 
	// 2 words or less such as [sperm, patty] and [empty, strap].
	// Throws IllegalArgumentException if passed string is null or if the integer is negative.
	public void print(String phrase, int max) {
		if (phrase == null || max < 0) {
			throw new IllegalArgumentException();
		}		
		
		Set<String> choices = getWords(phrase);			// Set initial choices
		Stack<String> chosen = new Stack<String>();		// Make a blank stack of 'chosen' anagrams
		LetterInventory wordPool = new LetterInventory(phrase); // Convert phrase to inventory
		
		printHelper(wordPool, choices, chosen, max);	// See what happens!
	}
	
	// Helper method that can accept all the parameters needed to track how many letters are left 
	// in the word, choices, chosen, and a limiting length.  Prints the valid anagrams, depending
	// on the value of max.
	private void printHelper(LetterInventory p, Set<String> choices, Stack<String> chosen, int max) {
		// Order of tests matters!  Check for max == 0 first.
		if (p.isEmpty() && (max == 0 || chosen.size() <= max)) {
			// Base case, we've used up every letter in the passed phrase!
			System.out.println(chosen);
		} else if (max == 0 || chosen.size() < max){
			// Reduce the size of our choices each time we do a recursive call!  This MARKEDLY
			// speeds up the operation of the program.
			Set<String> currentChoices = getWords(p, choices);
			for (String b : currentChoices) {
				//choose
				p.subtract(b);
				chosen.push(b);
				
				// explore
				printHelper(p, currentChoices, chosen, max);
				
				// un-choose
				p.add(b);
				chosen.pop();
			}
		}
		// else, the anagram is invalid either because it's too long compared to max, or because
		// it doesn't use all letters in the LetterInventory.  Do nothing.
	}
}
