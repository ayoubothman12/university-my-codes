import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//This class stores Flight information loaded from flight list file
public class Flight {
	private String id;
	private String from;
	private String to;
	private LocalDateTime startTime; //flight start date time
	private LocalDateTime arivalTime; // flight arrival date time
	private int price; //flight cost/price
	private Duration duration; //flight duration
	
	//Constructor method
	public Flight(String id, String from, String to, String strStartTime, String strDuration, int price) {
		this.id = id;
		this.from = from;
		this.to = to;
		int hours = Integer.parseInt(strDuration.split(":")[0]); //paring string hour into integer 
		int minutes = Integer.parseInt(strDuration.split(":")[1]); //paring string minutes into integer
		duration = Duration.ofHours(hours).plusMinutes(minutes); // creates duration object from parsed hours & minutes
		
		//creating start time by paring date time format
		this.startTime = LocalDateTime.parse(strStartTime, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm EEE"));
		
		//creating arrival date time by adding duration hours and minutes
		this.arivalTime = startTime.plusHours(hours);
		this.arivalTime = arivalTime.plusMinutes(minutes);
		this.price = price;
	}

	
	//getter methods start here
	public String getId() {
		return id;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public int getPrice() {
		return price;
	}

	public Duration getDuration() {
		return duration;
	}
	
	public LocalDateTime getArivalTime() {
		return arivalTime;
	}
	//getter methods ends here
}
