public class Rating {
	private Person user;
	private int rate;
	
	public Rating(Person user, int rate) {
		this.user = user;
		this.rate = rate;
	}

	public Person getUser() {
		return user;
	}
	
	public void setUser(Person user) {
		this.user = user;
	}
	
	public int getRate() {
		return rate;
	}
	
	public void setRate(int rate) {
		this.rate = rate;
	}
}
