import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class Main {
	static ArrayList<String> parts = new ArrayList<>();
	static ArrayList<Stack> itemStack = new ArrayList<>();
	static Queue tokenQueue = new Queue();
	
	public static void main(String[] args) {
		try {
			PrintStream printStream = new PrintStream(new File(args[4]));
			System.setOut(printStream); //redirect all output to PrintStream

			readParts(args[0]); // reads parts stores in array list
			readItems(args[1]); // reads items & push into stack
			readTokens(args[2]); // reads tokens & push into queue
			readTasks(args[3]); // reads & executes tasks 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Reads parts information from given file
	private static void readParts(String fileName) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));

		String line = null;

		while((line = bufferedReader.readLine())!= null) {
			parts.add(line); //add parts in array list 
		}

		bufferedReader.close();
	}
	
	// Reads items information from given file
	private static void readItems(String fileName) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));

		String line = null;

		while((line = bufferedReader.readLine())!= null) {
			String[] parts = line.split(" ");
			addItem(parts[1], parts[0]); // add items into stack
		}
		
		bufferedReader.close();
	}
	
	// Reads token information from given file
	private static void readTokens(String fileName) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));

		String line = null;

		while((line = bufferedReader.readLine())!= null) {
			String[] parts = line.split(" ");
			//push token into queue
			tokenQueue.enqueue(new Token(parts[0], parts[1], Integer.parseInt(parts[2])));
		}

		bufferedReader.close();
	}
	
	// Reads tasks from given file & executes tasks
	private static void readTasks(String fileName) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));

		String line = null;

		while((line = bufferedReader.readLine())!= null) {
			String[] parts = line.split("\t");
			if (parts[0].equals("PUT")) {// if task is put
				for (int i = 1; i < parts.length; i++) {
					String[] itemInfo = parts[i].split(",");
					
					// adds items in the stack
					for (int j = 1; j < itemInfo.length; j++) {
						addItem(itemInfo[0], itemInfo[j]);
					}
				}
			} else if (parts[0].equals("BUY")) { // else if task is buy
				for (int i = 1; i < parts.length; i++) {
					String[] itemInfo = parts[i].split(",");
					Token token = tokenQueue.getToken(itemInfo[0]);
					int count = Integer.parseInt(itemInfo[1]);
					Stack stack = getStack(itemInfo[0]);
					
					// this loop take items from stack & queue
					while (token != null && count != 0) {
						tokenQueue.dequeue(token); //dequeue token from queue
						int pickCount;
						if (token.getCount() > count) { // if requested item is less than item count
							token.setCount(token.getCount() - count);
							tokenQueue.enqueue(token);
							pickCount = count;
							count = 0;
						} else { // else requested item is greater than or equal item count
							pickCount = token.getCount();
							count -= token.getCount();
							token = tokenQueue.getToken(itemInfo[0]);
						}
						
						while (pickCount != 0) { // pops items from the stack
							stack.pop();
							pickCount--;
						}
					}
				} // for loop end
			} // else end
		}//while loop end
		
		for (String part : parts) { // this loop prints all available items in the stack
			System.out.println(part + ":");
			Stack stack = getStack(part); 
			if (stack != null) {
				stack.print();
			}
			
			System.out.println("---------------");
		}
		
		tokenQueue.print(); // prints all token from the queue
		
		bufferedReader.close();
	}

	//get stack with given name
	private static Stack getStack(String itemName) {
		for (Stack stack : itemStack) {
			if (stack.getItemName().equals(itemName)) {
				return stack;
			}
		}
		return null;
	}
	
	// adds items into the stack
	private static void addItem(String itemName, String itemID) {
		Stack stack = getStack(itemName); //get stack with given name
		
		if (stack == null) { // no stack created yet
			Stack newstack = new Stack(itemName);
			newstack.push(itemID);
			itemStack.add(newstack);
		} else { // already have created stack
			stack.push(itemID);
		}
	}
}
