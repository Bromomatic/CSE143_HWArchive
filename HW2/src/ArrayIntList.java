// CSE 143, Homework 2 (SortedIntList)

import java.util.*;   // for Arrays class

/**
An ArrayIntList object can be used to store a list of integers.
This file is provided to you by the instructor.
You should not modify this file!  Your code will be tested with an
unmodified version of this class.
adapted from 9/29/06 version by Stuart Reges
*/
public class ArrayIntList {
    /** default value used for the initial capacity of a list's internal array */
    protected static final int DEFAULT_CAPACITY = 10;

    // These fields are declared 'protected' so subclasses can access them.
    
    /** This array stores the overall list of integers. */
    protected int[] elementData;

    /** The current number of elements in the list. */
    protected int size; 

    /** Constructs an empty list of a default capacity. */
    public ArrayIntList() {
        this(DEFAULT_CAPACITY);
    }

    /**
    Constructs an empty list with the given capacity.
    Precondition: capacity >= 0
    Throws an illegal argument exception if capacity is negative.
    */
    public ArrayIntList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("bad capacity: " + capacity);
        }
        elementData = new int[capacity];
        size = 0;
    }

    /**
    Appends the given value to the end of the list.
    Resizes the list's internal array to fit the value if necessary.
    */
    public void add(int value) {
        add(size, value);
    }

    /**
    Inserts the given value at the given index, shifting subsequent values right.
    Precondition: 0 <= index <= size()
    Throws an out-of-bounds exception if index is out of range.
    */
    public void add(int index, int value) {
        checkIndex(index, 0, size);
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elementData[i] = elementData[i - 1];
        }
        elementData[index] = value;
        size++;
    }
    
    /**
    Removes all elements from this list.
    */
    public void clear() {
        size = 0;
        Arrays.fill(elementData, 0);
    }
    
    /**
    Returns true if the given value is contained in this list, else false.
    */
    public boolean contains(int value) {
        return indexOf(value) >= 0;
    }

    /**
    Makes sure that this list's internal array is large enough to store at
    least the given number of elements.
    Postcondition: elementData.length >= capacity
    */
    public void ensureCapacity(int capacity) {
        if (capacity > elementData.length) {
            elementData = Arrays.copyOf(elementData, Math.max(capacity, 2 * elementData.length));
        }
    }
    
    /**
    Returns the integer at the given index in the list.
    Precondition: 0 <= index < size()
    Throws an out-of-bounds exception if index is out of range.
    */
    public int get(int index) {
        checkIndex(index, 0, size - 1);
        return elementData[index];
    }

    /**
    Returns the position of the first occurence of the given value,
    or -1 if the value is not found.
    */
    public int indexOf(int value) {
        for (int i = 0; i < size; i++) {
            if (elementData[i] == value) {
                return i;
            }
        }
        return -1;
    }
    
    /**
    Returns true if this list does not contain any elements, else false.
    */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
    Removes the value at the given index, shifting subsequent values left.
    Precondition: 0 <= index < size()
    Throws an out-of-bounds exception if index is out of range.
    */
    public void remove(int index) {
        checkIndex(index, 0, size - 1);
        for (int i = index; i < size - 1; i++) {
            elementData[i] = elementData[i + 1];
        }
        size--;
    }

    /**
    Returns the number of elements in the list.
    */
    public int size() {
        return size;
    }

    /**
    Returns a comma-separated, bracketed String representation of the list,
    such as "[42, -3, 5, 19, -1, 0, 7]".
    Returns "[]" for an empty list.
    */
    public String toString() {
        if (size == 0) {
            return "[]";
        } else {
            String result = "[" + elementData[0];
            for (int i = 1; i < size; i++) {
                result += ", " + elementData[i];
            }
            result += "]";
            return result;
        }
    }
    
    /**
    Throws an array index out-of-bounds (OOB) exception if index is not 
    between min and max inclusive.
    */
    private void checkIndex(int index, int min, int max) {
        if (index < min || index > max) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }
}
