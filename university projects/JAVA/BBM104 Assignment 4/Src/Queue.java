import java.util.Comparator;
import java.util.LinkedList;

public class Queue {
	private LinkedList<Token> tokens;
	
	public Queue() { //constructor method
		tokens = new LinkedList<>();
	}
	
	public void enqueue(Token token) { // enqueue token into link list
		tokens.addFirst(token); //added item at first position 
		tokens.sort(new Comparator<Token>() { // after that list sorted according to this comparator
			@Override
			public int compare(Token o1, Token o2) { // override compare method
				return o1.getCount() - o2.getCount();
			}
		});
	}
	
	public void dequeue(Token token) { //dequeue item from link list
		tokens.remove(token);
	}
	
	public Token getToken(String itemName) { // get token by given token number
		for (int i = tokens.size() -1; i >= 0 ; i--) { // loops from end to start
			if (tokens.get(i).getItem().equals(itemName)) {
				return tokens.get(i);
			}
		}

		return null;
	}
	
	public void print() { // prints all token in the link list
		System.out.println("Token Box:");
		boolean isNewline = false;
		for (Token token : tokens) {
			if(isNewline) {
				System.out.println();	
			}
			isNewline = true;
			System.out.print(token.toString());
		}
	}
}
