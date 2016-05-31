// Shawn Stern   CSE 143   HW_2
//
// This object extends the default ArrayIntList object, adding additional functionality
// as well as modifying ArrayIntList's original methods.  The most obvious change is that
// SortedIntList is a sorted list, that also has an option to specify that its elements
// must be unique.  For the remainder of the program, this is referred to as 'uniqueness'.


import java.util.*;					// For Arrays

public class SortedIntList extends ArrayIntList {
	private boolean isUnique;		// Default of false, stores if list is unique or not.
	
	// Creates a default capacity SortedIntList object, allows duplicate values.
	public SortedIntList() {
		super();
	}
	
	// Creates a default capacity SortedIntList object with given uniqueness setting.
	public SortedIntList(boolean unique) {
		this();
		isUnique = unique;
	}
	
	// Creates a SortedIntList object with given capacity, allows duplicate values.
	// Precondition: capacity >= 0
	public SortedIntList(int capacity) {
		super(capacity);
	}
	
	// Creates a SortedIntList object with given capacity and given uniqueness setting.
	// Precondition: capacity >= 0
	public SortedIntList(boolean unique, int capacity) {
		this(capacity);
		isUnique = unique;
	}
	
	// Inserts the given integer into the list such that the list remains in sorted order.
	// If the list is currently set to unique, it will not add duplicate values.
	public void add(int value) {
		int index = indexOf(value);	
		
		// If unique and the returned index is positive, do nothing.
		// The index is positive only if the value already exists, and is therefore non-unique.
		if (isUnique && index >= 0) {
		} else {
			// If the returned index is negative, reverse engineer the correct insertion point.
			index = (index < 0) ? -index - 1 : index;
			super.add(index, value);
		}		
	}
	
	// Always throws an exception, because the user cannot be trusted to sort properly.
	public void add(int index, int value) {
		throw new UnsupportedOperationException();
	}
	
	// Returns the current state of the list's uniqueness setting.
	public boolean getUnique() {
		return isUnique;
	}
	
	// Searches the list for the given value, and returns an unmodified IP value.
	public int indexOf(int value) {
		return Arrays.binarySearch(elementData, 0, size, value);		
	}
	
	// Returns the max value of the list; if the list is empty it throws an exception.
	public int max() {
		noSize();
		
		return elementData[size - 1];
	}
	
	// Returns the min value of the list; if the list is empty it throws an exception.
	public int min() {
		noSize();
		
		return elementData[0];
	}
	
	// Assigns the uniqueness setting of the list to the given value.
	// If set to true, removes all duplicate values from the list.
	public void setUnique(boolean unique) {
		isUnique = unique;
		
		if (isUnique) {
			// Traverse the array looking for duplicates. If found, remove duplicate and
			// decrement counter, to avoid missing additional duplicates(if any).
			for (int i = 0; i < size - 1; i++) {
				int a = elementData[i];
				int b = elementData[i + 1];
				if (a == b) {
					remove(i + 1);
					i--;
				}
			}
		}
	}
	
	// Returns a string representation of the SortedIntList, preceded by a "S:" to denote
	// that it is sorted.  If uniqueness is currently enabled, the string will end with a "U".
	public String toString() {
		String base = "S:" + super.toString();
		
		// If unique, append a "U" to the end of the string
		base = (isUnique) ? base + "U" : base;
		
		return base;
	}
	
	// Throws an exception if the list is empty
	private void noSize() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		} 
	}
}