// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.

/*
 * CST 751 Homework 4
 * Name: Jonathan Nguyen
 * 
 * Got help from Daniel at the tutoring center in understanding how to implement
 * a linked list correctly when inserting and removing. This help me pass efficient 
 * tests in seconds, where before my program would still be running.
 *  
 */

package edu.uwm.cs351;

import junit.framework.TestCase;

// This is a Homework Assignment for CS 351 at UWM

/**
 * A linked-list implementation of a Sequence variant
 * 
 * The data structure is a linked list with null and duplicate values allowed.
 * The fields should be:
 * <dl>
 * <dt>precursor</dt> .
 * <dt>head</dt> 
 * <dt>manyNodes</dt> 
 * </dl>
 * The class should define a private wellFormed() method
 * and perform assertion checks in each method.
 */

public class SequentialGallery implements Cloneable{

	/** Collection Fields */
	// TODO: Declare the private static Node class.
	// It should have a constructor but no methods unlike the textbook example.
	// The fields of Node should have "default" access (neither public, nor private)
	
	private static class Node{
		Painting data;
		Node next;
		Node(Painting p, Node n){
			data = p;
			next = n;
		}
	}
	
	/**
	 * This is a helper method that checks to see what the current element is. 
	 * @param none
	 * @postcondition
	 *   If precursor is null, then current element is the head node. If the current 
	 *   element is not null, then it exists in the list. Otherwise, there is no 
	 *   current element.
	 *   @return
	 *   the current element from the list, otherwise nul
	 **/
	private Node getCursor() {
		if(precursor == null) {
			return head;
		}
		if(precursor.next != null) {
			return precursor.next;
		}
		return null;
	}

	// TODO: Declare the private fields of SequentialGallery needed for sequences
	private int manyNodes; 
	private Node head, precursor; 
	
	private SequentialGallery(boolean ignored) {} // DO NOT CHANGE THIS

	private static boolean doReport = true; // only to be changed in JUnit test code

	private boolean _report(String s) {
		if (doReport) System.out.println(s);
		return false;
	}
	
	/**
	 * The Invariant
	 */
	private boolean wellFormed() {
		// Invariant:
		// 1. list must not include a cycle.
		// 2. manyNodes is number of nodes in list
		// 3. precursor is either null or points to a node in the list.

		// Implementation:
		// Do multiple checks: each time returning false if a problem is found.

		// We do the first one for you:
		// check that list is not cyclic
		//1.
		if(head != null) {
			// This check uses an interesting "Tortoise and Hare" algorithm
			Node fast = head.next;
			for(Node p = head; fast != null && fast.next != null; p = p.next) {
				if (p == fast) return _report("list is cyclic!");
				fast = fast.next.next;
			}
		}
		//2.
		int count = 0;
		boolean findPrecursor = false;
		for(Node p = head; p != null; p = p.next) {
			if (p == precursor) {
				findPrecursor = true;
			}
			count++;
		}
		if(count != manyNodes) return _report("given that the list has " + manyNodes + " nodes, and the acutal list has " + count + " nodes.");
		//3.
		if(precursor != null && !findPrecursor) return _report("precursor is not in the list"); 
		return true;
	}
	/**
	 * Create an empty SequentialGallery
	 * @param - none
	 * @postcondition
	 *   This Gallery is empty 
	 **/     
	public SequentialGallery()
	{
		assert wellFormed() : "invariant failed in constructor";
	}
	
	/**
	 * Add a new element to this sequence, before the current element. 
	 * @param element
	 *   the new element that is being added
	 * @postcondition
	 *   A new copy of the element has been added to this sequence. If there was
	 *   a current element, then the new element is placed before the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the end of the sequence. In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 **/
	public void insert(Painting element)
	{
		assert wellFormed() : "invariant wrong at start of insert";
		// TODO: Implement insert.
		if(precursor == null) { //inserting at the beginning of the list
			head = new Node(element,head);
		}
		else {
			precursor.next = new Node(element,getCursor()); //inserting in between and at the end of the list
		}
		manyNodes++;
		assert wellFormed() : "invariant wrong at end of insert";
	}

	/**
	 * Place the contents of another SequentialGallery (which may be the
	 * same sequence as this!) into this sequence before the current element (if any).
	 * @param addend
	 *   a sequence whose contents will be placed into this sequence
	 * @precondition
	 *   The parameter, addend, is not null. 
	 * @postcondition
	 *   The elements from addend have been placed into
	 *   this sequence. The current element of this sequence is now
	 *   the first element inserted (if any).  If the added sequence
	 *   is empty, this sequence and the current element (if any) are
	 *   unchanged.
	 * @exception NullPointerException
	 *   Indicates that addend is null. 
	 **/
	public void insertAll(SequentialGallery addend)
	{
		assert wellFormed() : "invariant wrong at start of addAll";
		assert addend.wellFormed() : "invariant of parameter wrong at start of addAll";
		// TODO: Implemented by student.
		if(addend == null)throw new NullPointerException("The addend list is null");
		if(addend.head == null)return;
		SequentialGallery copy = addend.clone();
		Node oldCurrent = null;
		Node p = null;
		if(precursor == null) {
			oldCurrent = head;
			head = copy.head;
		}
		else{
			oldCurrent = precursor.next;
		}
		p = head;
		while(p.next != null) {
			if(p == precursor) {
				precursor.next = copy.head;
			}
			p = p.next; 
		}
		if(oldCurrent == null && precursor != null) {
			p.next = copy.head;
		}
		else {
			p.next = oldCurrent;
		}
		manyNodes += addend.manyNodes;
		assert wellFormed() : "invariant wrong at end of insertAll";
		assert addend.wellFormed() : "invariant of parameter wrong at end of insertAll";
	}  
	
	/**
	 * Create a new SequentialGallery that contains all the elements from one sequence
	 * followed by another.
	 * @param g1
	 *   the first of two sequences
	 * @param g2
	 *   the second of two sequences
	 * @precondition
	 *   Neither g1 nor g2 is null.
	 * @return
	 *   a new sequence that has the elements of g1 followed by the
	 *   elements of s2 (with no current element)
	 * @exception NullPointerException.
	 *   Indicates that one of the arguments is null.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for the new sequence.
	 **/   
	public static SequentialGallery catenation(SequentialGallery g1, SequentialGallery g2)
	{
		if(g1 == null || g2 == null) throw new NullPointerException("One or both of the sequence is empty!");
		SequentialGallery g = g1.clone();
		SequentialGallery copy = g2.clone();
		if(g.head == null) {
			g.head = copy.head;
			g.precursor = g.head;
		}
		else {
			Node p = g.head;
			while(p.next != null) {	
				p = p.next; 
			}
			if(p.next == null) {
				p.next = copy.head;
				while(p.next != null) {
					p = p.next; 
				}
			}
			g.precursor = p;
		}
		g.manyNodes += copy.manyNodes;
		return g;
	}
	/**
	 * Accessor method to get the current element of this sequence. 
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true.
	 * @return
	 *   the current element of this sequence
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   getCurrent may not be called.
	 **/
	public Painting getCurrent()
	{
		assert wellFormed() : "invariant wrong at start of getCurrent()";
		// TODO: Implement getCurrent
		if(!hasCurrent()) throw new IllegalStateException("there is no current element in the sequence to return!");
		// This method shouldn't modify any fields, hence no assertion at end
		return getCursor().data;
	}


	/**
	 * Accessor method to determine whether this sequence has a specified 
	 * current element that can be retrieved with the 
	 * getCurrent method. 
	 * @param - none
	 * @return
	 *   true (there is a current element) or false (there is no current element at the moment)
	 **/
	public boolean hasCurrent()
	{
		assert wellFormed() : "invariant wrong at start of getCurrent()";
		// TODO: Implement hasCurrent
		return getCursor() != null;
		// This method shouldn't modify any fields, hence no assertion at end
	}

	/**
	 * Remove the current element from this sequence.
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true.
	 * @postcondition
	 *   The current element has been removed from this sequence, and the 
	 *   following element (if there is one) is now the new current element. 
	 *   If there was no following element, then there is now no current 
	 *   element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   removeCurrent may not be called. 
	 **/
	public void removeCurrent()
	{
		assert wellFormed() : "invariant wrong at start of removeCurrent()";
		// TODO: Implement removeCurrent()
		// See textbook pp.176-78, 181-184
		if(!hasCurrent()) throw new IllegalStateException("there is no current element to remove");
		if(precursor == null) {
			head = head.next;
		}
		else {
			precursor.next = precursor.next.next;
		}
		manyNodes--;
		assert wellFormed() : "invariant wrong at end of removeCurrent()";
	}

	/**
	 * Determine the number of elements in this sequence.
	 * @param - none
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size()
	{
		assert wellFormed() : "invariant wrong at start of size()";
		// TODO: Very little work.
		return manyNodes;
		// This method shouldn't modify any fields, hence no assertion at end
	}

	/**
	 * Set the current element at the front of this sequence.
	 * @param - none
	 * @postcondition
	 *   The front element of this sequence is now the current element (but 
	 *   if this sequence has no elements at all, then there is no current 
	 *   element).
	 **/ 
	public void start()
	{
		assert wellFormed() : "invariant wrong at start of start()";
		// TODO: Very little work
		precursor = null;
		assert wellFormed() : "invariant wrong at end of start()";
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node cursor = (precursor == null) ? head : precursor.next;
		sb.append("{");
		boolean first = true;
		for (Node p = head; p != null; p = p.next) {
			if(first) first = false;
			else sb.append(",");
			if(p == cursor) sb.append("*");
			sb.append(p.data);
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Move forward, so that the current element is now the next element in
	 * this sequence.
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true. 
	 * @postcondition
	 *   If the current element was already the end element of this sequence 
	 *   (with nothing after it), then there is no longer any current element. 
	 *   Otherwise, the new element is the element immediately after the 
	 *   original current element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   advance may not be called.
	 **/
	public void advance( )
	{
		assert wellFormed() : "invariant wrong at start of advance()";
		// TODO: Implement advance()
		if(!hasCurrent())throw new IllegalStateException("there is no current element to advacne too");
		if(precursor == null) {
			precursor = head;
		}
		else {
			precursor = precursor.next;
		}
		assert wellFormed() : "invariant wrong at end of advance()";
	}
	
	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 *   Whatever was current in the original object is now current in the clone.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	public SequentialGallery clone()
	{  	 
		assert wellFormed() : "invariant wrong at start of clone()";

		SequentialGallery result;

		try
		{
			result = (SequentialGallery) super.clone();
		}
		catch (CloneNotSupportedException e)
		{  
			// This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		// TODO: Implemented clone().
		// Now do the hard work of cloning the list.
		// See pp. 193-197, 228
		// Setting precursor correctly is tricky.
		
		Node dummy = new Node(null,null);
		Node copy = dummy;
		for(Node p = head; p != null; p = p.next) {
			Node c = new Node(p.data,null); 
			copy = copy.next = c; 
			if(p == precursor) result.precursor = c;
		}
		result.head = dummy.next;
	
		assert wellFormed() : "invariant wrong at end of clone()";
		assert result.wellFormed() : "invariant wrong for result of clone()";
		return result;
	}

	public static class TestInvariant extends TestCase {
		
		protected SequentialGallery self;
		private Painting h1 = new Painting(new Raster(10, 10), "nice pixels", 100);
		private Painting h2 = new Painting(new Raster(10, 10), "extra nice pixels", 1000);
		
		@Override
		protected void setUp() {
			self = new SequentialGallery(false);
			//iterator = self.new MyIterator(false);
			//Gallery self = new Gallery();
			doReport = false;
		}
		
		public void testNull() {
			assertTrue("empty gallery allowed", self.wellFormed());
		}

		public void test00() {
			self.manyNodes = 1;
			assertFalse(self.wellFormed());
			self.manyNodes = 0;

			assertTrue(self.wellFormed());
		}
		
		public void test01() {
			self.head = new Node(h1,null);
			assertFalse(self.wellFormed());
			self.manyNodes = 2;
			assertFalse(self.wellFormed());
			self.manyNodes = 1;

			assertTrue(self.wellFormed());
		}
		
		public void test02() {
			self.head = new Node(h1,null);
			self.manyNodes = 1;
			self.precursor = new Node(h1,null);
			assertFalse(self.wellFormed());
			self.precursor = new Node(h2,self.head);
			assertFalse(self.wellFormed());
			self.precursor = self.head;

			assertTrue(self.wellFormed());
		}
		
		public void test03() {
			self.head = new Node(h1,null);
			self.head.next = self.head;
			self.manyNodes = 1;
			assertFalse(self.wellFormed());
			self.manyNodes = 2;
			assertFalse(self.wellFormed());
			self.manyNodes = 3;
			assertFalse(self.wellFormed());
			self.manyNodes = 0;
			assertFalse(self.wellFormed());
			self.manyNodes = -1;
			assertFalse(self.wellFormed());
		}
		
		public void test05() {
			self.head = new Node(h1,null);
			self.head = new Node(h2,self.head);
			self.head = new Node(null,self.head);
			self.head = new Node(h2,self.head);
			self.head = new Node(h1,self.head);
			self.manyNodes = 1;
			assertFalse(self.wellFormed());
			self.manyNodes = 2;
			assertFalse(self.wellFormed());
			self.manyNodes = 3;
			assertFalse(self.wellFormed());
			self.manyNodes = 4;
			assertFalse(self.wellFormed());
			self.manyNodes = 0;
			assertFalse(self.wellFormed());
			self.manyNodes = -1;
			assertFalse(self.wellFormed());
			self.manyNodes = 5;

			assertTrue(self.wellFormed());
		}
		
		public void test06() {
			Node n1,n2,n3,n4,n5;
			self.head = n5 = new Node(h1,null);
			self.head = n4 = new Node(h2,self.head);
			self.head = n3 = new Node(null,self.head);
			self.head = n2 = new Node(h2,self.head);
			self.head = n1 = new Node(h1,self.head);
			self.manyNodes = 5;

			self.precursor = new Node(h1,null);
			assertFalse(self.wellFormed());
			self.precursor = new Node(h1,n1);
			assertFalse(self.wellFormed());
			self.precursor = new Node(h1,n2);
			assertFalse(self.wellFormed());
			self.precursor = new Node(h2,n3);
			assertFalse(self.wellFormed());
			self.precursor = new Node(null,n4);
			assertFalse(self.wellFormed());
			self.precursor = new Node(h2,n5);
			assertFalse(self.wellFormed());
			
			self.precursor = n1;
			assertTrue(self.wellFormed());
			self.precursor = n2;
			assertTrue(self.wellFormed());
			self.precursor = n3;
			assertTrue(self.wellFormed());
			self.precursor = n4;
			assertTrue(self.wellFormed());
			self.precursor = n5;
			assertTrue(self.wellFormed());
		}
	}
}
