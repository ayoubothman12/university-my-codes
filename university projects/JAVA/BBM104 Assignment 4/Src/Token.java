public class Token { // this class stores token information
	private String number;
	private String item;
	private int count;

	// constructor method
	public Token(String number, String item, int count) {
		this.number = number;
		this.item = item;
		this.count = count;
	}

	// getter methods starts here
	public String getNumber() {
		return number;
	}

	public String getItem() {
		return item;
	}

	public int getCount() {
		return count;
	}
	// getter methods ends here
	
	public void setCount(int count) {
		this.count = count;
	}

	@Override //this method overrides object class toString method
	public String toString() {
		return getNumber() + " " + getItem() + " " + getCount();
	}

}
