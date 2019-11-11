/*************************************************************************
 *  Compilation:  javac Bag.java
 *  Execution:    java Bag < input.txt
 *
 *  A generic bag or multiset, implemented using a linked list.
 *
 *  % more tobe.txt 
 *  to be or not to - be - - that - - - is
 *
 *  % java Bag < tobe.txt
 *  size of bag = 14
 *  is
 *  -
 *  -
 *  -
 *  that
 *  -
 *  -
 *  be
 *  -
 *  to
 *  not
 *  or
 *  be
 *  to
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  The <tt>Bag</tt> class represents a bag (or multiset) of 
 *  generic items. It supports insertion and iterating over the 
 *  items in arbitrary order.
 *  <p>
 *  The <em>add</em>, <em>isEmpty</em>, and <em>size</em>  operation 
 *  take constant time. Iteration takes time proportional to the number of items.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/13stacks">Section 1.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class Bag<Item> implements Iterable<Item> {
    private int N;         // number of elements in bag
    public Node first;     // beginning of bag
    public boolean live;   // false if this adj[i] was removed from list

    // helper linked list class
    public class Node {
        public Item item;
        private Node next;
    }
    
   /**
     * Create an empty stack.
     */
    public Bag() {
        first = null;
        N = 0;
        live = true;
        assert check();
    }

   /**
     * Is the BAG empty?
     */
    public boolean isEmpty() {
        return first == null;
    }

   /**
     * Return the number of items in the bag.
     */
    public int size() {
        return N;
    }

   /**
     * Add the item to the front of the bag.
     */
    public void add(Item item) {
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        N++;
        assert check();
    }
    
    /**
     * Add the item to the end of the bag.
     */
    public void append(Item item) {
    	Node curr = first;
    	Node prev = null;
    	while (curr!=null) {
    		prev = curr;
    		curr = curr.next;
    	}
    	if (prev==null) { add(item); }
    	else {
    		Node newNode = new Node();
    		newNode.item = item;
    		prev.next = newNode;
    		newNode.next = curr;
    		N++;
    	}
    }
    
    /**
     * Add the item to the end of the List (Bag).
     * only if that item was not found in the Bag.
     */
    public void add_ifnot_inBag(Item item) {
    	Node curr = first;
    	Node prev = null;
    	while (curr!=null) {
    		if (curr.item.equals(item)) return;
    	prev = curr;
    	curr = curr.next;
    	}
    	if (prev==null) add(item);
    	else {
    		Node last = new Node();
    		last.item = item;
    		prev.next = last;
    		N++;
    		last.next = null;
    	}
    }
    
    /**
     *  Removing an item from the bag
     */
    public void remove(Item item) {
    	Node curr = first;
    	Node prev = null;
    	while (curr!=null) {
    		if (curr.item.equals(item)) {
    			if (prev==null) first = curr.next;
    			else prev.next = curr.next;
    			N--;
    			break;
    		}
    		prev=curr;
    		curr=curr.next;
    	}
    }
    
    /**
     * Remove and return the first item from the bag.
     */
    public Item removeFirst() {
    	if (first==null) throw new NoSuchElementException();
    	Item result = first.item;
    	first = first.next;
    	--N;
    	return result;
    }
    
    // check internal invariants
    private boolean check() {
        if (N == 0) {
            if (first != null) return false;
        }
        else if (N == 1) {
            if (first == null)      return false;
            if (first.next != null) return false;
        }
        else {
            if (first.next == null) return false;
        }

        // check internal consistency of instance variable N
        int numberOfNodes = 0;
        for (Node x = first; x != null; x = x.next) {
            numberOfNodes++;
        }
        if (numberOfNodes != N) return false;

        return true;
    } 


   /**
     * Return an iterator that iterates over the items in the bag.
     */
    public Iterator<Item> iterator()  {
        return new ListIterator();  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
}