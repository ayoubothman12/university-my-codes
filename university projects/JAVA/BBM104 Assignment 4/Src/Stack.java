import java.util.ArrayList;

public class Stack {
	private String itemName;
	private ArrayList<String> items;
	
	public Stack(String itemName) { // constructor with item name
		this.itemName = itemName;
		items = new ArrayList<>();
	}
	
	public String getItemName() { // get item name
		return itemName;
	}
	
	public void push(String item) { // push item number in stack
		items.add(item);
	}
	
	public String pop() { // pop top item number from stack
		if (!isEmpty()) {
			String item = items.get(items.size() - 1);
			items.remove(item); // removes item from the list
			return item;
		}
		
		return null;
	}
	
	public boolean isEmpty() { // checks if stack is empty
		return items.size() == 0;
	}

	public void print() { // prints all item number from the stack
		if (isEmpty()) {
			System.out.println();
		} else {
			for (int i = items.size() -1; i >=0; i--) {
				System.out.println(items.get(i));
			}
		}
		
	}
}
