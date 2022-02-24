public class StuntPerformer extends Person {
	private int height;
	private String[] actorsID;
	
	public StuntPerformer(int id, String name, String surname, String country, int height, String[] actorsID) {
		super(id, name, surname, country);
		this.height = height;
		this.actorsID = actorsID;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String[] getActorsIDs() {
		return actorsID;
	}

	public void setActorsIDs(String[] actorsID) {
		this.actorsID = actorsID;
	}

}
