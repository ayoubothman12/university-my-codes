import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;


public class Main {
	static HashMap<String, Airport> airportMap = new HashMap<>(); // this hashmap stores airport list with cityname & alias
	static ArrayList<Airport> airports = new ArrayList<>(); //this list only stores uniqe airport list
	static int maxLength = 0; 
	static int diameterDistance = Integer.MAX_VALUE;

	
	//main method
	public static void main(String[] args) {
		try {
			PrintStream printStream = new PrintStream(new File("output.txt"));
			System.setOut(printStream); //redirect all output to PrintStream
			
			readAirportData(args[0]); //read airport data
			readFlightData(args[1]); //read flight data
			readCommands(args[2]); //read commands
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// this comparator compares time between 2 flight plan
	static Comparator<FlightPlan> timeComparator = new Comparator<FlightPlan>() {
		@Override
		public int compare(FlightPlan o1, FlightPlan o2) {
			return o1.getDuration().compareTo(o2.getDuration());
		}
	};
	
	// this comparator compares price between 2 flight plan
	static Comparator<FlightPlan> priceComparator = new Comparator<FlightPlan>() {
		@Override
		public int compare(FlightPlan o1, FlightPlan o2) {
			return o1.getPrice() -  o2.getPrice() ;
		}
	};
	
	//this method reads airport information from given file 
	private static void readAirportData(String fieName) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fieName)));

		String line = null;
		Scanner scanner = null;
		
		while((line = bufferedReader.readLine())!= null) {
			scanner = new Scanner(line);
			scanner = scanner.useDelimiter("\t"); //Information are separated by TAB char
			String cityName =scanner.next();
			Airport airport = new Airport(cityName); //creates airport object
			
			airportMap.put(cityName, airport); // stores airport in hash map by city name
			airports.add(airport);
			
			while (scanner.hasNext()) {
				String alias = scanner.next();
				airport.getAliases().add(alias);
				airportMap.put(alias, airport); // stores airport in hash map by alias
			}			
			scanner.close();
		}
		bufferedReader.close();
	}
	
	//This method read flight data from given file
	private static void readFlightData(String fieName) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fieName)));

		String line = null;
		Scanner scanner = null;

		while((line = bufferedReader.readLine())!= null) {
			scanner = new Scanner(line);
			scanner = scanner.useDelimiter("\t");
			
			String id = scanner.next();
			String connection[] = scanner.next().split("->"); //to alias and from alias separated by ->
			String dateTime = scanner.next();
			String duration = scanner.next();
			int price = scanner.nextInt();
			Airport from = airportMap.get(connection[0]); // get airport object from hash map using alias
			
			// create flight object
			Flight flight = new Flight(id, connection[0], connection[1], dateTime, duration, price);
			from.getFlights().add(flight); //adding flight in airport object flight list
			
			scanner.close();
		}

		bufferedReader.close();
	}
	
	//This DFS method traverse till it reach to airport or there is no flights from the airport 
	// & if destination airport found it then path (flight plan) is store in flight plan list
	static void traverse(Flight startFlight, Airport to, ArrayList<FlightPlan> flightPlans, FlightPlan flightPlan, HashMap<Airport, Boolean> visited) {
		Airport from = airportMap.get(startFlight.getTo()); //get arrival airport object using flight to data
		
		if (!from.equals(to)) { // if not reached to destination
			
			if (visited.get(from) == null || !visited.get(from)) { //if airport not visited yet
				visited.put(from, true); //add airport in visited list
				ArrayList<Flight> flights = from.getFlights(); //get all flight list of arrival airport
				
				for (Flight flight : flights) { //loop through the list
					// check if its transit connecting flight or not (arrival time must be less than start time of connecting flight)
					if (startFlight.getArivalTime().isBefore(flight.getStartTime())) { 
						flightPlan.add(flight); 
						traverse(flight, to, flightPlans, flightPlan, visited); // calls this dfs method with next flight
					}
				}
				visited.put(from, false); // reverse visited field as same airport can be used for other flight plan
			}			
		} else { //reached to given destination
			flightPlans.add(flightPlan); //adding flight plan in list
		}
	}
	
	
	//this method returns a list using from city, to city and date
	static ArrayList<FlightPlan> listAll(String fromCity, String toCity, String strDate) {
		ArrayList<FlightPlan> flightPlans = new ArrayList<>();
		
		Airport from = airportMap.get(fromCity);//get airport object from hash map using city name
		Airport to = airportMap.get(toCity); //get airport object from hash map using city name
		LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")); // parse date
		
		HashMap<Airport, Boolean> visited = new HashMap<>(); //Airport visited list for DFS call
		
		for (Flight flight : from.getFlights()) { //loops through the flight list of starting airport
			if (flight.getStartTime().toLocalDate().equals(date)) { // checks if date matched
				FlightPlan flightPlan = new FlightPlan();
				flightPlan.add(flight);
				traverse(flight, to, flightPlans, flightPlan, visited);	
			}
		}
		return flightPlans;
	}
	
	//this method returns proper flight list
	static ArrayList<FlightPlan> listProper(ArrayList<FlightPlan> flightPlans) {
		Comparator<FlightPlan> comparator = new Comparator<FlightPlan>() { // this comparator checks both price & duration
			@Override
			public int compare(FlightPlan o1, FlightPlan o2) {
				//if both price & duration is grater
				if (o1.getPrice() -  o2.getPrice() > 1 && o1.getDuration().compareTo(o2.getDuration()) > 1 )
					return o1.getPrice() -  o2.getPrice();
				//if both price & duration is equal
				else if (o1.getPrice() ==  o2.getPrice() && o1.getDuration().compareTo(o2.getDuration()) == 0 )
					return 0;
				else 
					return o1.getPrice() -  o2.getPrice() ;
			}
		};
		
		flightPlans.sort(comparator); //sorts using comparator
		FlightPlan maximum = flightPlans.get(0); //maximum value is 0 index after sorting
		
		ArrayList<FlightPlan> properrList = new ArrayList<>();
		
		for (FlightPlan flightPlan : flightPlans) {
			if (comparator.compare(maximum, flightPlan) >= 0) { //if any flight plan is equal to maximum
				properrList.add(flightPlan);
			}
		}
		return properrList;
	}
	
	
	//this method print flight plan list according given comparator (time/price)
	static void printList(ArrayList<FlightPlan> flightPlans, Comparator<FlightPlan> comparator) {
		if (flightPlans.size() == 0) { //if list is empty
			System.out.println("No suitable flight plan is found");
		} else {
			
			flightPlans.sort(comparator); //sorts using comparator
			FlightPlan maximum = flightPlans.get(0); //maximum value is 0 index after sorting
			
			for (FlightPlan flightPlan : flightPlans) {
				if (comparator.compare(maximum, flightPlan) >= 0) { //if any flight plan is equal to maximum
					System.out.println(flightPlan);
				}
			}
		}
	}
	
	//this method print flight plan list which cost is less than a given price
	static void printList(ArrayList<FlightPlan> flightPlans, int maxPrice) {
		ArrayList<FlightPlan> properrList = listProper(flightPlans);
		
		int count = 0;
		for (FlightPlan flightPlan : properrList) {
			if (flightPlan.getPrice() < maxPrice) {
				System.out.println(flightPlan);
				count++;
			}
		}		
		
		if (count == 0) { //if there no any plan
			System.out.println("No suitable flight plan is found");
		}
	}
	
	//this method print flight plan list which arrival is less than given time
	static void printList(ArrayList<FlightPlan> flightPlans, LocalDateTime dateTime) {
		ArrayList<FlightPlan> properrList = listProper(flightPlans);
		
		int count = 0;
		for (FlightPlan flightPlan : properrList) {
			Flight lastFlight = flightPlan.getFlights().get(flightPlan.getFlights().size()-1);
			
			if (lastFlight.getArivalTime().isBefore(dateTime)) {
				System.out.println(flightPlan);
				count++;
			}
		}		
		
		if (count == 0) {
			System.out.println("No suitable flight plan is found");
		}
	}
		
	//This DFS method traverse until there no any other airport starting by a flight & calculates length & cost
	static void calculateDiameter(Flight flight, HashMap<Airport, Boolean> visited, int length, int cost ) {
		Airport airport = airportMap.get(flight.getTo()); //get arrival airport
		
		if (visited.get(airport) == null || !visited.get(airport)) { // if the airport not visited yet
			visited.put(airport, true); // mark visited
			
			for (Flight mFlight : airport.getFlights()) { // get flight list of the airport
				
				//if flight count/length is maximum && cost is minimum
				if (length + 1 > maxLength && cost + mFlight.getPrice() < diameterDistance) {
					maxLength = length + 1;
					diameterDistance = cost + mFlight.getPrice();

				//if flight count/length is same but cost is minimum
				} else if (length + 1 == maxLength && cost+ mFlight.getPrice() < diameterDistance) {
					diameterDistance = cost + mFlight.getPrice();
				}
	
				//calls this DFS method with next flight, current length/flight count & price
				calculateDiameter(mFlight, visited,  length + 1, cost + mFlight.getPrice());
			}
			
			visited.put(airport, false);
		}
	}
	
	
	// this method calculates diameter (longest flight count but less cost)
	private static void diameter() { 
		HashMap<Airport, Boolean> visited = new HashMap<>();
		
		for (Airport airport : airports) { //loops through all airport
			for (Flight flight : airport.getFlights()) { // get out bounding flight list of the airport 
				// this DFS method calculates flight distance and compares with maximum distance
				calculateDiameter(flight, visited, 1, flight.getPrice());				
			}
		}
		System.out.println("The diameter of graph : " + diameterDistance);
	}
	
	
	//this method read commands from given file and executes the command accordingly
	private static void readCommands(String fieName) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fieName)));

		String line = null;
		Scanner scanner = null;
		boolean printNewLine = false;
		
		
		while((line = bufferedReader.readLine())!= null) { //reads until end of file
			if (printNewLine) { // before each command there is 2 new line except first
				System.out.println("\n");
			}
			
			printNewLine = true;
			
			scanner = new Scanner(line);
			scanner = scanner.useDelimiter("\t");
			String command =scanner.next();
			System.out.println("command : " + line);
			
			if (command.equals("diameterOfGraph") || command.equals("pageRankOfNodes")) { //for these 2 command no input
				if (command.equals("diameterOfGraph")) {
					diameter();
				} else if (command.equals("pageRankOfNodes")) {
					System.out.println("Not implemented");
				}
			} else { //for these command city name, date taken
				String connection[] = scanner.next().split("->");
				String date = scanner.next();
				
				// list all flight plan by given from city & destination city and date
				ArrayList<FlightPlan> flightPlans = listAll(connection[0], connection[1], date);
				
				if (command.equals("listAll")) {
					if (flightPlans.size() == 0) {
						System.out.println("No suitable flight plan is found");
					} else {
						for (FlightPlan flightPlan : flightPlans) {
							System.out.println(flightPlan.toString());
						}
					}	
				} else if (command.equals("listProper")) {
					ArrayList<FlightPlan> proper = listProper(flightPlans);
					
					if (proper.size() == 0) { //if list is empty
						System.out.println("No suitable flight plan is found");
					} else {
						for (FlightPlan flightPlan : proper) {
							System.out.println(flightPlan.toString());
						}
					}
					
				} else if (command.equals("listCheapest")) {
					printList(flightPlans, priceComparator);
				} else if (command.equals("listQuickest")) {
					printList(flightPlans, timeComparator);
					
				} else if (command.equals("listCheaper")) {
					int maxPrice = scanner.nextInt();
					printList(flightPlans, maxPrice);
					
				} else if (command.equals("listQuicker")) {
					String strDateTime = scanner.next();
					
					LocalDateTime localDateTime = LocalDateTime.parse(strDateTime, 
							DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm EEE"));
					
					printList(flightPlans, localDateTime);
				} else if (command.equals("listExcluding")) {
					
					String company = scanner.next();
					int count = 0;
					for (FlightPlan flightPlan : listProper(flightPlans)) {
						if (!flightPlan.containsCompany(company)) {
							System.out.println(flightPlan.toString());
							count++;
						}
					}
					if (count == 0) {
						System.out.println("No suitable flight plan is found");
					}
				} else if (command.equals("listOnlyFrom")) {
					String company = scanner.next();
					int count = 0;
					for (FlightPlan flightPlan : listProper(flightPlans)) {
						if (flightPlan.isBySameCompany(company)) {
							System.out.println(flightPlan.toString());
							count++;
						}
					}
					if (count == 0) {
						System.out.println("No suitable flight plan is found");
					}
				} 
			}
			scanner.close();
		}

		bufferedReader.close();
	}
	
}
