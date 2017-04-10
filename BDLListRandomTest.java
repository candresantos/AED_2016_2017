import java.util.Random;

/**
 * A class to exercise the methods BDLList, useful to check the correctness of 
 * your BDLList implementation with ConGu tool.
 * 
 * It uses instances of lists of Integers.
 * 
 * @author antonialopes (AED 16/17 @ FCUL-DI)
 */
public class BDLListRandomTest {

	public static void main(String[] args) throws InterruptedException {	
		new BDLListRandomTest(new BDLList<Integer>(), 101011).run();		
	}

	/*
	 * Number of operations on lists to test.
	 */
	private static final int MAX_OPERATIONS = 7;

	/*
	 * The larger integer to include in the list.
	 */
	private static final int MAX_INTEGER = 10;

	/*
	 * Our random object to generate operations and doubles.
	 */
	private final Random rand;

	/*
	 * The list manipulated during the test execution.
	 */
	private BDLList<Integer> list;

	/*
	 * The number of operation calls to perform during the test execution.
	 */
	private final int howMany;


	public BDLListRandomTest(
			BDLList<Integer> list, int howMany) {
		this.list = list;
		this.howMany = howMany;
		this.rand = new Random();
	}

	/**
	 * Creates an integer to put in the list
	 */
	private Integer newInteger() {
		int next = rand.nextInt(MAX_INTEGER);
		return new Integer(next);
	}

	public void run() {
		for (int i = 0; i < howMany; i++) {
			int value, index;
			switch (rand.nextInt(MAX_OPERATIONS)) {
			case 0: //make
				if (i%10 == 0){ //to allow the list to grow this 
					//cannot be executed with the same probability 
					System.out.println("make()");
					list = new BDLList<Integer>();
					break;
				}
			case 1: //isEmpty
				System.out.print("isEmpty()? ");
				System.out.println(list.isEmpty());
				break;
			case 2: //size
				System.out.print("size() = ");			
				System.out.println(list.size());			
				break;
			case 3: //get
				if (!list.isEmpty()) {
					index = rand.nextInt(list.size());
					System.out.print("get("+ index + ") = ");
					System.out.println(list.get(index));
				}
				break;
			case 4: //set
				if (!list.isEmpty()) {
					index = rand.nextInt(list.size());
					value = newInteger();
					System.out.println("set("+ index + "," + value + ")");
					list.set(index, value);
				}
				break;
			case 5: //addLast
				value = newInteger();
				System.out.println("add("+ value + ")");
				list.add(value);
				break;
			case 6: //add
				if (!list.isEmpty()) {
					index = rand.nextInt(list.size());
					value = newInteger();
					System.out.println("add("+ index + "," + value + ")");
					list.add(index, value);
				}
				break;
			}
			//System.out.println(">>> " + list.toStringForDebugging());
			System.out.println(">> " + list);
		}
		System.out.println("Done!");		
	}
}
