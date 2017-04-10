import java.util.Iterator;
import java.util.NoSuchElementException;

public class BDLList<T> implements Iterable<T>, Cloneable{
	/*
	 * A node of the linked list holding consecutive 
	 * elements of the list in a Block.
	 */
	private static final class Node<T>{
		
		/* *** Fields *** */
		private Node<T> next;
		private Node<T> prev;
		private Block<T> elem;
		//Size of the list until this node (included)
		private int localSize = 0;
		
		/* *** Constructor *** */
		/**
		 * Constructor to a new node.
		 * @param elem - element
		 * @param next - next node
		 * @param prev - previous node
		 */
		private Node(Block<T> elem, Node<T> next, Node<T> prev){
			
			this.elem = elem;
			this.next = next;
			this.prev = prev;
			
		}
		
		/**
		 * Constructor to a new node.
		 * @param elem - element
		 * @param next - next node
		 * @param prev - previous node
		 * @param localSize - Size of the previous nodes + this node
		 */
		private Node(Block<T> elem, Node<T> next, Node<T> prev, int localSize){
			
			this.elem = elem;
			this.next = next;
			this.prev = prev;
			this.localSize = localSize;
			
		}
		
		/* *** Methods *** */
		/**
		 * Adds the value to the indexth position of the list this node 
		 * belongs to.
		 * @param index - the position
		 * @param value - the value
		 * @requires index >= 0 && index < localSize && value != null
		 * @ensures It adds value to the indexth position of the list.
		 */
		private void add(int index, T value){
			elem.add(aIndex(index), value);
			localSize++;
			if(this.next != null)
				refresh(this);
		}
		
		/**
		 * Adds the value to the end of the node.
		 * @param value - the value
		 * @requires value != null
		 * @ensures It adds value to the end of the node.
		 */
		private void addL(T value){
			elem.addLast(value);
			localSize++;
		}
		
		/**
		 * Adds the value to the beginning of the node.
		 * @param value - the value
		 * @requires value != null
		 * @ensures It adds value to the beginning of the node.
		 */
		private void addF(T value){
			elem.addFirst(value);
			localSize++;
		}
		
		/**
		 * Removes the last element of this node.
		 */
		private void removeL(){
			elem.removeLast();
			localSize--;
		}
		
		/**
		 * Removes the first element of this node.
		 */
		private void removeF(){
			elem.removeFirst();
			localSize--;
		}
		
		/**
		 * Sets the indexth position of the list this node belongs to to value.
		 * @param index - the index
		 * @param value - the value
		 * @requires index >= 0 && index < localSize && value != null
		 * @ensures It sets value to the indexth position of the list.
		 */
		private void set(int index, T value){
			elem.set(aIndex(index), value);
		}
		
		/**
		 * What's the indexth value of the list this node belongs to?
		 * @param index - the index
		 * @requires index >= 0 && index < localSize
		 * @ensures It returns the value of the indexth position of the list
		 * this node belongs to.
		 * @return The value of the indexth position.
		 */
		private T get(int index){
			return elem.get(aIndex(index));
		}
		
		/**
		 * What's the first element of this node?
		 * @return The first element of this node.
		 */
		private T getF(){
			return elem.getFirst();
		}
		
		/**
		 * What's the last element of this node?
		 * @return The last element of this node.
		 */
		private T getL(){
			return elem.getLast();
		}
		
		/**
		 * What's the size of the Block of this node?
		 * @return The size of the Block of this node.
		 */
		private int size(){
			return elem.size();
		}
		
		/**
		 * What's the size of the list until this node (included)?
		 * @return The size of the list until this node (included).
		 */
		private int length(){
			return localSize;
		}
		
		/**
		 * Is this node full?
		 * @return true if full, false otherwise.
		 */
		private boolean isFull(){
			return elem.isFull();
		}
		
		/**
		 * Converts the index of list to an index of this specific node
		 * @param index - the index
		 * @requires index >= 0 && index < localSize && index > prev.localSize
		 * @ensures It returns the correct index equivalent to this node.
		 * @return The index equivalent to this node.
		 */
		private int aIndex(int index){
			return index+size()-length();
		}
		
		/**
		 * Shift one value (last one) recursivly to the right (to the next node)
		 * from this node until target, and adds a value to the indexth position
		 * in target.
		 * @param target - the node needing room
		 * @param index - the index
		 * @param value - the value
		 * @requires target != null && index <= 0 && index < localSize
		 * @ensures It will shift the values accordingly.
		 */
		private void shiftr(Node<T> target, int index, T value){
			if(target == this){
				add(index, value);
			} else {
				addF(prev.getL());
				prev.removeL();
				prev.shiftr(target, index, value);
			}
		}
		
		private void shiftl(Node<T> target, int i){
			if(target != this){
				while(size()<BSIZE){
					addL(next.getF());
					next.removeF();
				}
				next.shiftl(target, i+1);
			}
			if(i == 0){
				refresh(this);
			}
		}
		
		/**
		 * Updates localSize of every subsequent node after start
		 * @param start - The node where to start updating
		 * @requires start != null
		 * @ensures It will refresh every subsequent node, making localSize the
		 * real localSize.
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static void refresh(Node start){
			Node node = start;
			while(node != null){
				if(node.prev != null)
					node.localSize = node.prev.length()+node.size();
				node = node.next;
			}
		}
	}
	
	/* ************ FIELDS ************* */
	private int size = 0;
	private Node<T> first;
	private Node<T> last;
	
	/* ********* FINAL FIELDS ********** */ 
	private final static int BSIZE = 4;
	
	/* ************ METHODS ************ */
	
	/**
	 * Is this list empty?
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * What's the current size of this list?
	 * @return The size of the list.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * What's the element at the indexth position in the list?
	 * @param index - the index
	 * @requires index >= 0 && index < size()
	 * @ensures It returns the element at the indexth position.
	 * @return The element at the indexth position.
	 */
	public T get(int index) {
		//look for the node containing index, and get it's indexth element.
		return lookfor(index).get(index);
	}
	
	/**
	 * Add an element to the indexth position in the list.
	 * @param index - the index
	 * @param value - the value
	 * @requires index >= 0 && index < size() && value != null
	 * @ensures It adds value to the indexth position.
	 */
	public void add(int index, T value) {
		if(isEmpty()){ //empty, just add it to the first position
			add(value);
		} else {
			Node<T> currNode = lookfor(index); //Node containing index
			if(currNode.isFull()){ //It's already full
				Node<T> target = lookforEmpty(currNode);
				if(target == null){ //End of the list
					Block<T> temp = new Block<T>(BSIZE+1);
					//Set next to a new node containing the new block
					last.next = new Node<T>(temp, null, last, last.length());
					last = last.next;
					/*Shifts one value (the last one) to the right, 
					from currNode, until the last node, to make room for value.*/
					last.shiftr(currNode, index, value);
				} else if (target.isFull()) { //Didn't find any not full.
					Block<T> temp = new Block<T>(BSIZE+1);
					//Create a new node connecting currNode and it's next node.
					currNode = new Node<T>(temp, currNode.next, currNode, 
															currNode.length());
					currNode.next.prev = currNode;
					currNode.prev.next = currNode;
					/*
					 * Pull the last value of the previous node to the new node
					 * making room for value
					 */
					currNode.shiftr(currNode.prev, index, value);
					/*
					 * Shift values (by order) to the left, from target, until
					 * currNode, balancing everything until every node has 
					 * at least BSIZE elements
					 */
					currNode.shiftl(target, 0);
				} else { //Found a node that isn't full
					/*Shifts one value (the last one) to the right, 
					from currNode, until the target node, to make room for value.*/
					target.shiftr(currNode, index, value);
				}
			} else { //It isn't full
				currNode.add(index, value);
			}
		}
		size++;
	}
	
	/**
	 * Set the indexth position in the list to a given value.
	 * @param index - the index
	 * @param value - the value
	 * @requires index >= 0 && index < size() && value != null
	 * @ensures It sets the indexth position to value.
	 */
	public void set(int index, T value) {
		//look for the node containing index, and set it's indexth element to
		//value
		lookfor(index).set(index, value);
	}
	
	/**
	 * Add value to the end of the list.
	 * @param value - the value
	 * @requires value != null
	 * @ensures It will add value to the end of the list.
	 */
	public void add(T value) {
		if(isEmpty()) { //empty
			//New block
			Block<T> temp = new Block<T>(BSIZE+1);
			//New node
			first = new Node<T>(temp, null, null);
			last = first;
		} else if(last.isFull()) { //Last node is full
			//New block
			Block<T> temp = new Block<T>(BSIZE+1);
			//Set next to a new node containing the new block
			last.next = new Node<T>(temp, null, last, last.length());
			//Update last
			last = last.next;
		}
		//Add the value to the end of the list
		last.addL(value);
		//Update size
		size++;
	}
	
	/**
	 * What's the node holding the indexth position?
	 * @param index - the position
	 * @requires index >= 0 && index < size()
	 * @ensures It returns the node holding the indexth position.
	 * @return The node holding the indexth position.
	 */
	private Node<T> lookfor(int index){
		Node<T> result = first;
		//While the size up to the node doesn't contain index.
		while(result.length() <= index){
			result = result.next;
		}
		
		return result;
	}
	
	/**
	 * What's the first not full node, at a distance of BSIZE from curr?
	 * @param curr - the node to start the search from
	 * @requires curr != null
	 * @ensures It returns as the tag return specifies.
	 * @return null if didn't find any not full nodes at a distance of BSIZE,
	 * Or the last node that it searched for. If that node is full, didn't find
	 * any, if it's not, it found.
	 */
	private Node<T> lookforEmpty(Node<T> curr) {
		Node<T> result = curr;
		boolean found = false;
		for(int i = 1; i <= BSIZE && result != null && !found; i++){
			if(result.isFull()) //It's full, check next node
				result = result.next;
			else //Found the node
				found = true;
		}
		return result;
	}
	
	/**
	 * Contextual representation of BDLList
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("[");
		for(T e: this){
			result.append(e+", ");
		}
		if(!isEmpty())
			result.delete(result.length()-2, result.length());
		result.append("]");
		return result.toString();
	}
	
	/**
	 * Contextual representation of BDLList for debbuging
	 * @return Contextual representation of BDLList for debbuging
	 */
	public String toStringforDebbugging() {
		StringBuilder result = new StringBuilder();
		Node<T> curr = first;
		while(curr!=null){
			result.append("<-"+curr.elem.toString()+"->");
		}
		if(result.length() == 0)
			result.append("Nothing");
		return result.toString();
	}
	
	/**
	 * A custom iterator class that traverses the elements of this list.
	 * This iterator works by traversing the nodes as a proper linked list.
	 */
	private final class BDLListIterator implements Iterator<T> {
		private int currIndex = 0;
		
		/**
		 * Does it have another value?
		 */
		@Override
		public boolean hasNext() {
			return currIndex<size() && !isEmpty();
		}
		
		/**
		 * Next value.
		 */
		@Override
		public T next() {
			if (currIndex>size()) 
				throw new NoSuchElementException();
			T result = get(currIndex);
			currIndex++;
			return result;
		}
		
		
	}
	
	/**
	 * An iterator for the elements in the BDLList.
	 */
	@Override
	public Iterator<T> iterator() {
		return new BDLListIterator();
	}
}
