import java.util.ArrayList;

//This class stores Airport information loaded from airport list file
public class Airport {
	private String cityName;
	private ArrayList<String> aliases;
	private ArrayList<Flight> flights;
	
	//Constructor method
	public Airport(String cityName) {
		this.cityName = cityName;
		aliases = new ArrayList<>();
		flights = new ArrayList<>();
	}

	//Getter method starts here
	public String getCityName() {
		return cityName;
	}

	public ArrayList<String> getAliases() {
		return aliases;
	}

	public ArrayList<Flight> getFlights() {
		return flights;
	}
	//Getter method ends here
	
	@Override //this overridden method is if 2 airport object is same or not
	public boolean equals(Object object) {
		Airport airport = (Airport) object;
		return airport.cityName.equals(cityName); // checks city name of 2 airport object same or not
	}
}
