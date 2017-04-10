import java.util.StringJoiner;

/**
 * A block has a capacity and holds a sequence of elements (in a circular array).  
 * Blocks are mutable. Null values are NOT allowed. 
 * 
 * The type Block supports:
 * (1) the insertion of elements at both end points and also in a given position, 
 *     provided the block is not full, and 
 * (2) the removal of elements at both end points and also in a given position, 
 *     provided the block is not empty.  
 *   
 * @param <E> the type of the elements in this block
 * 
 * @author antonialopes  (AED 16/17 @ FCUL-DI)
 */
public class Block<E> implements Cloneable{

	/* ************ FIELDS ************ */

	/*
	 * The store.
	 */
	private E[] elems;

	/*
	 * The index of elems where the first element is stored.
	 */
	private int indexFirst;	

	/*
	 * The index of elems where the last element is stored.
	 */
	private int indexLast;	

	/*
	 * Cached total number of elements in the block. 
	 */
	private int size;


	/* ************ CONSTRUCTORS  ************ */

	/**
	 * Constructor for an empty block with a given capacity.
	 * @param capacity	The capacity of the block
	 * @requires capacity > 0
	 */
	@SuppressWarnings("unchecked")
	public Block(int capacity) {
		elems = (E[]) new Object[capacity];
		this.indexFirst = 1;
		this.indexLast = 0;
		this.size = 0;
	}


	/* ************ METHODS ************ */

	/**
	 * @return The block's capacity.
	 */
	public int capacity() {
		return elems.length;
	}

	/**
	 * @return If this block has no elements.
	 */
	public boolean isEmpty () {
		return size() == 0;
	}
	
	/**
	 * @return If the number of elements in the block is equals to its capacity.
	 */
	public boolean isFull(){
		return size() == elems.length;
	}

	/**
	 * Add an element at the beginning of the block's sequence.
	 * @param element 	The element to add.
	 * @requires !isFull() && element!=null
	 */
	public void addFirst(E element) {
		indexFirst = dec(indexFirst);
		elems[indexFirst] = element;
		size++;
	}

	/**
	 * Add an element at the end of the block's sequence.
	 * @param element 	The element to add.
	 * @requires !isFull() && element!=null
	 */
	public void addLast(E element) {
		indexLast = inc(indexLast);
		elems[indexLast] = element;
		size++;
	}

	/**
	 * Inserts the specified element at the specified position in the block's sequence. 
	 * Shifts the element currently at that position and any subsequent elements 
	 * to the right (adds one to their indices).
	 * @param element	The element to add.
	 * @param index		The position where the element should be added.
	 * @requires !isFull() && 0 <= index && index < size() && element!=null
	 */
	public void add(int index, E element){
		if (index == 0)
			//no need to shift elements in this case
			addFirst(element);
		else {
			int actIndex = add(index, indexFirst);
			addLast(elems[indexLast]);
			for(int i = dec(indexLast); i != actIndex && i != indexFirst; i = dec(i)){
				elems[i] = elems[dec(i)];
			}
			elems[actIndex] = element;
		}
	}

	/**
	 * Removes the element at the specified position in the block's sequence. 
	 * Shifts the elements at any subsequent elements 
	 * to the left (subtracts one to their indices).
	 * @param index The position where the element should be removed.
	 * @requires 0 <= index && index < size()
	 */
	public void remove(int index){
		if (index == 0)
			removeFirst();
		else if (index == size() - 1)
			removeLast();
		else {
			int currentIndex = add(indexFirst, index); 
			while (currentIndex != indexLast){
				elems[currentIndex] = elems[inc(currentIndex)];
				currentIndex = inc(currentIndex);
			}
			indexLast = dec(indexLast);
			size--;
		}
	}

	/**
 	 * @return The element at the beginning of the block's sequence.	
	 * @requires !isEmpty()
	 */
	public E getFirst() {
		return elems[indexFirst];
	}

	/**
	 * @return The element at the end of this block's sequence.
	 * @requires !isEmpty()
	 */
	public E getLast(){
		return elems[indexLast];
	}

	/**
	 * @return The element at the specified position in the block's sequence. 
	 * @requires 0 <= index && index < size()
	 */
	public E get(int index) {
		return elems[add(indexFirst,index)];
	}

	/**
	 * Remove the element at the beginning of the block's sequence. 
	 * @requires !isEmpty()
	 */
	public void removeFirst() {
		elems[indexFirst] = null; 
		indexFirst = inc(indexFirst);
		size--;
	}

	/**
 	 * Remove the element at the end of the block's sequence. 
 	 * @requires !isEmpty()
	 */
	public void removeLast() {
		elems[indexLast] = null; 
		indexLast = dec(indexLast);
		size--;
	}

	/**
	 * Replaces the element at the specified position in the block's sequence 
 	 * with the specified element.
	 * @param index The position of the element to replaced.
	 * @param element The element 
	 * @requires 0 <= index && index < size() && element!=null
	 */
	public void set(int index, E element) {
		elems[add(indexFirst,index)] = element;	
	}

	/**
	 * @return The number of elements in this block.
	 */
	public int size() {
		return  size;
	}

	// private matter

	/*
	 * Arithmetic modulo the capacity of the block.
	 */
	
	//@requires i>=0
	private int inc(int i) {
		return (i + 1) % elems.length;
	}
	//@requires i>=0
	private  int dec(int i) {
		return (i + elems.length - 1) % elems.length;
	}

	//@requires i>=0 && j>=0
	private  int add(int i, int j) {
		return (i + j) % elems.length;
	}


	// clone, equals, toString

	/**
	 * A faithful copy of this block.
	 * Clones the backbone (the array) of the block, but not its elements.
	 */
	@Override
	public Block<E> clone() {
		try {
			@SuppressWarnings("unchecked")
			Block<E> result = (Block<E>) super.clone();
			result.elems = elems.clone();
			return result;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	/**
	 * Is this block equal to a given object?
	 * 
	 * @param other The object.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof Block
				&& equalBlocks((Block<E>) other);
	}

	/*
	 * Are two blocks equals?
	 * 
	 * Blocks are equal if have the same capacity and hold the same linear collection
	 * of elements.
	 * @param other
	 *            The other queue.
	 */
	private boolean equalBlocks(Block<E> other) {
		if ( this.capacity() != other.capacity() || this.size() != other.size() ) 
			return false;
		int curr = this.indexFirst;
		int otherCurr = other.indexFirst;
		for (int i = 1; i <= size(); i++){
			if (!this.elems[curr].equals(other.elems[otherCurr]))
				return false;
			curr = inc(curr);
			otherCurr = inc(otherCurr);
		}
		return true;
	}

	/**
	 * @return A textual representation of this block 
	 * with elements separated by commas and inside [ and ].
	 */
	@Override
	public String toString () {
		StringJoiner result = new StringJoiner (",", "[", "]");
		if (!isEmpty()) {	
			int curr = this.indexFirst;
			for (int i = 1; i <= size(); i++){
				result.add(elems[curr].toString());
				curr = inc(curr);
			}
		}
		return result.toString();
	}

	/**
	 * @return Textual representation of this block 
	 * with internal representation of the block.
	 * Useful for debugging.
	 */
	public String toStringForDebugging () {
		StringJoiner result = new StringJoiner (",", "[", "]");
		for (int i = 0; i < elems.length; i++){
			if (i == indexFirst)
				result.add(" >" + elems[i] + " ");
			else if (i == indexLast)
				result.add(elems[i] + "< ");
			else
				result.add(elems[i].toString());
		}
		return result.toString();
	}

}
