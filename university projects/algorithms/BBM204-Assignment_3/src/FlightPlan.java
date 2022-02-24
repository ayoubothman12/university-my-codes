import java.time.Duration;
import java.util.ArrayList;

//This class is used to store flight plan which comprise of 
//flight cost/price, duration & flight list from start to end
public class FlightPlan {
	private int price = 0;
	private Duration duration = Duration.ZERO; //initially duration zero
	private ArrayList<Flight> flights = new ArrayList<>();
	
	public void add (Flight flight) { //adding flight in list
		flights.add(flight);
		
		price += flight.getPrice(); //adding price
		duration = duration.plus(flight.getDuration()); //adding duration
		
		int prevFlight = flights.size() - 2;
		
		if (prevFlight >= 0) { // if there is previous flight
			// this calculates (transit time) the difference  between previous arrival & current flight start time 
			duration = duration.plus(Duration.between(flights.get(prevFlight).getArivalTime(), flight.getStartTime()));
		}
	}
	
	//getter methods starts here
	public ArrayList<Flight> getFlights() {
		return flights;
	}
	
	public int getPrice() {
		return price;
	}

	public Duration getDuration() {
		return duration;
	}
	//getter methods ends here
	
	//this method checks if any flight in this plan is from a given company
	public boolean containsCompany(String company) {
		for (Flight flight : flights) {
			if (flight.getId().startsWith(company)) {
				return true;
			}
		}
		return false;
	}
	
	//this method checks if all flight in this plan is from same company
	public boolean isBySameCompany(String company) {
		for (Flight flight : flights) {
			if (!flight.getId().startsWith(company)) {
				return false;
			}
		}
		return true;
	}
	
	@Override //this overridden method returns all flight information in given format
	public String toString() {
		String output = "";
		
		for (int i = 0; i < flights.size(); i++) {
			if (i != 0) {
				output += "||";
			}		
			output += flights.get(i).getId() + "\t" + flights.get(i).getFrom() + "->" + flights.get(i).getTo();		
		}
		
		long hours = duration.toMinutes()/60;
		long minutes = duration.toMinutes()%60;
		
		String res = hours<10? "0" + hours + ":": hours + ":" ;	//padding 0 infront of hour if less than 10
		res += minutes<10? "0" + minutes: minutes + "" ; 	//padding 0 infront of minutes if less than 10
		
		return output + "\t" + res + "/" + price;
	}
}
