import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
	private final static String DATA_DIR = "data/set1/";
	private static ArrayList<Person> persons = new ArrayList<>();
	private static ArrayList<Film> films = new ArrayList<>();

	private static Person getPerson(int id) {
		for (Person person : persons) {
			if (person.getId() == id) {
				return person;
			}
		}
		return null;
	}

	private static Person getUser(int id) {
		for (Person person : persons) {
			if (person instanceof User && person.getId() == id) {
				return person;
			}
		}
		return null;
	}
	
	private static Film getFilm(int id) {
		for (Film film : films) {
			if (film.getFilmID() == id) {
				return film;
			}
		}
		return null;
	}
	
	private static ArrayList<Director> getDirectors(String line) {
		ArrayList<Director> directors = new ArrayList<>();
		String[] directorsID = line.split(",");
		for (String directorID : directorsID) {
			Director director = (Director)getPerson(Integer.parseInt(directorID));
			if(director == null) 
				return new ArrayList<>();
			directors.add(director);
		}
		return directors;
	}

	private static ArrayList<Person> getPerformers(String line) {
		ArrayList<Person> performers = new ArrayList<>();
		String[] ids = line.split(",");
		for (String id : ids) {
			Person person = getPerson(Integer.parseInt(id)); 
			if (person == null) 
				return new ArrayList<>();
			performers.add(person);
		}
		return performers;
	}

	private static ArrayList<Writer> getWriters(String line) {
		ArrayList<Writer> writers = new ArrayList<>();
		String[] ids = line.split(",");
		for (String id : ids) {
			Writer writer = (Writer)getPerson(Integer.parseInt(id));
			if(writer == null) 
				return new ArrayList<>();
			writers.add(writer);
		}
		return writers;
	}

	private static void readFilmData(String filmsDataFile) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filmsDataFile)));

		String line = null;
		Scanner scanner = null;

		while((line = bufferedReader.readLine())!= null) {
			scanner = new Scanner(line);
			scanner = scanner.useDelimiter("\t");

			String category = scanner.next();
			int filmID = scanner.nextInt();
			String title = scanner.next();
			String language = scanner.next();
			ArrayList<Director> directors = getDirectors(scanner.next());
			int length = scanner.nextInt();
			String country = scanner.next();
			ArrayList<Person> performers = getPerformers(scanner.next());

			if (category.contains("FeatureFilm")) {
				String[] genre =  scanner.next().split(",");
				String releaseDate = scanner.next();
				ArrayList<Writer> writers = getWriters(scanner.next());
				int budget = scanner.nextInt();
				films.add(new FeatureFilm(filmID, title, language, directors, length, country, performers, releaseDate, budget, writers, genre));
			} else if (category.contains("ShortFilm")) {
				String[] genre =  scanner.next().split(",");
				String releaseDate = scanner.next();
				ArrayList<Writer> writers = getWriters(scanner.next());
				films.add(new ShortFilm(filmID, title, language, directors, length, country, performers, genre, releaseDate, writers));
			} else if (category.contains("Documentary")) {
				String releaseDate = scanner.next();
				films.add(new Documentary(filmID, title, language, directors, length, country, performers, releaseDate));
			} else if (category.contains("TVSeries")) {
				String[] genre = scanner.next().split(",");;
				ArrayList<Writer> writers = getWriters(scanner.next());
				String startDate = scanner.next();
				String endDate = scanner.next();
				int seasons = scanner.nextInt();
				int episodes = scanner.nextInt();
				films.add(new TVSeries(filmID, title, language, directors, length, country, performers, genre, writers, startDate, endDate, seasons, episodes));
			}

			scanner.close();
		}

		bufferedReader.close();
	}

	private static void readPeopleData(String peopesDataFile) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(peopesDataFile)));

		String line = null;
		Scanner scanner = null;

		while((line = bufferedReader.readLine())!= null) {
			scanner = new Scanner(line);
			scanner = scanner.useDelimiter("\t");

			String category = scanner.next();
			int id = scanner.nextInt();
			String name = scanner.next();
			String surname = scanner.next();
			String country = scanner.next();
			
			if (category.contains("Director")) {
				String agent = scanner.next();
				persons.add(new Director(id, name, surname, country, agent));
			} else if (category.contains("Writer")) {
				String writingType = scanner.next();
				persons.add(new Writer(id, name, surname, country, writingType));
			} else if (category.equals("Actor:")) {
				int height = scanner.nextInt();
				persons.add(new Actor(id, name, surname, country, height));
			} else if (category.contains("ChildActor")) {
				int age = scanner.nextInt();
				persons.add(new ChildActor(id, name, surname, country, age));
			} else if (category.contains("StuntPerformer")) {
				int height = scanner.nextInt();
				String[] actorsID = scanner.next().split(",");
				persons.add(new StuntPerformer(id, name, surname, country, height, actorsID));
			} else if (category.contains("User")) {
				persons.add(new User(id, name, surname, country));
			}
			scanner.close();
		}
		bufferedReader.close();
	}
	
	private static void printChildActor(String country) {
		System.out.println("ChildActors:");
		boolean found = false;
		for (Person person : persons) {
			if (person instanceof ChildActor && person.getCountry().equals(country)) {
				found = true;
				System.out.println(person.getName() + " " + person.getSurname() + " " + ((ChildActor)person).getAge());
			}
		}
		
		if (!found) {
			System.out.println("No result");
		}
	}
	
	private static void printActor(String country) {
		System.out.println("Actors:");
		boolean found = false;
		for (Person person : persons) {
			if (person instanceof Actor && person.getCountry().equals(country)) {
				found = true;
				System.out.println(person.getName() + " " + person.getSurname() + " " + ((Actor)person).getHeight() + " cm");
			}
		}
		
		if (!found) {
			System.out.println("No result");
		}
	}
	
	private static void printStuntPerformer(String country) {
		System.out.println("StuntPerformers:");
		boolean found = false;
		for (Person person : persons) {
			if (person instanceof StuntPerformer && person.getCountry().equals(country)) {
				found = true;
				System.out.println(person.getName() + " " + person.getSurname() + " " + ((StuntPerformer)person).getHeight() + " cm");
			}
		}
		
		if (!found) {
			System.out.println("No result");
		}
	}
	
	private static void printWritter(String country) {
		System.out.println("Writers:");
		boolean found = false;
		for (Person person : persons) {
			if (person instanceof Writer && person.getCountry().equals(country)) {
				found = true;
				System.out.println(person.getName() + " " + person.getSurname() + " " + ((Writer)person).getWritingType());
			}
		}
		
		if (!found) {
			System.out.println("No result");
		}
	}

	private static void printDirector(String country) {
		System.out.println("Directors:");
		boolean found = false;
		for (Person person : persons) {
			if (person instanceof Director && person.getCountry().equals(country)) {
				found = true;
				System.out.println(person.getName() + " " + person.getSurname() + " " + ((Director)person).getAgent());
			}
		}
		
		if (!found) {
			System.out.println("No result");
		}
	}
	
	private static void readCommands(String commandFile) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(commandFile)));

		String line = null;
		Scanner scanner = null;

		while((line = bufferedReader.readLine())!= null) {
			scanner = new Scanner(line);
			scanner = scanner.useDelimiter("\t");

			String command = scanner.next();
			System.out.println(line + "\n");
			if (command.contains("RATE")) {
				int userID = scanner.nextInt();
				int filmID = scanner.nextInt();
				int rate = scanner.nextInt();
				
				Person person = getUser(userID);
				Film film = getFilm(filmID);
				
				if (film == null || person == null) {
					System.out.println("Command Failed\nUser ID: " + userID + "\nFilm ID: " + filmID);
				} else {
					if (film.getRatingBy(person) != null) {
						System.out.println("This film was earlier rated");
					} else {
						film.getRatings().add(new Rating(person, rate));
						System.out.println("Film rated successfully\nFilm type: " + film.getClass().getSimpleName() + "\nFilm title: " + film.getTitle());
					}
				}
				
			} else if (command.contains("ADD")) {
				scanner.next();
				int id = scanner.nextInt();
				String title = scanner.next();
				String language = scanner.next();
				ArrayList<Director> directors = getDirectors(scanner.next());
				int length = scanner.nextInt();
				String country = scanner.next();
				ArrayList<Person> performers = getPerformers(scanner.next());
				String[] genre =  scanner.next().split(",");
				String releaseDate = scanner.next();
				ArrayList<Writer> writers = getWriters(scanner.next());
				int budget = scanner.nextInt();
				
				Film film = getFilm(id);
				if(film != null || performers.isEmpty() || writers.isEmpty() || directors.isEmpty()) {
					System.out.println("Command Failed\nFilm ID: " + id + "\nFilm title: " + title);
				} else {
					films.add(new FeatureFilm(id, title, language, directors, length, country, performers, releaseDate, budget, writers, genre));
					System.out.println("FeatureFilm added successfully\nFilm ID: " + id + "\nFilm title: " + title);
				}
				
			} else if (command.contains("VIEWFILM")) {
				int id = scanner.nextInt();
				Film film = getFilm(id);
				if (film == null) {
					System.out.println("Command Failed\nFilm ID: " + id);
				} else {
					System.out.println(film.toString());
				}
				
			} else if (command.contains("LIST")) {
				if (line.matches("LIST\tUSER\t.*")) {
					scanner.next();
					int userID = scanner.nextInt();
					
					Person person = getUser(userID);
					
					if (person == null) {
						System.out.println("Command Failed\nUser ID: " + userID);
					} else {
						boolean isFound = false;
						
						for (Film film : films) {
							Rating rating = film.getRatingBy(person);
							if (rating != null) {
								isFound = true;
								System.out.println(film.getTitle() + ": " + rating.getRate());
							}
						}
						
						if (!isFound) {
							System.out.println("There is not any ratings so far");
						}
					}
				} else if (line.matches("LIST\tFILM\tSERIES")) {
					boolean found = false;
					for (Film film : films) {
						if (film instanceof TVSeries) {
							TVSeries tvSeries = (TVSeries) film;
							found = true;
							System.out.println(film.getTitle() + " (" + tvSeries.getStartYear() + "-" + tvSeries.getEndYear() + ")\n" + tvSeries.getSeasons() + " seasons and " + tvSeries.getEpisodes() + " episodes\n");
						}
					}
					
					if (!found) {
						System.out.println("No result");
					}
				} else if(line.matches("LIST\tFILMS\tBY\tRATE\tDEGREE")) {
					Comparator<Film> comparator = new Comparator<Film>() {
				        @Override
				        public int compare(Film film1, Film film2) {
				        	float diff = film2.getAvgRating() - film1.getAvgRating();
				        	if (diff < 0) {
				        		return -1;
				        	} else if (diff > 0) {
				        		return 1;
				        	} else {
				        		return 0;
				        	}
				        }
				    };
				    
				    printFeatureFilm(comparator);
				    System.out.println();
				    printShortFilm(comparator);
				    System.out.println();
				    printDocumentary(comparator);
				    System.out.println();
				    printTVSeries(comparator);
				    
				} else if (line.matches("LIST\tFEATUREFILMS\tBEFORE\t.*")) {
					String[] parts = line.split("LIST\tFEATUREFILMS\tBEFORE\t");
					boolean found = false;
					for (Film film : films) {
						if (film instanceof FeatureFilm  &&  ((FeatureFilm) film).isReleasedBefore(Integer.parseInt(parts[1]))) {
							System.out.println("Film title: " + film.getTitle() +  " (" + ((FeatureFilm) film).getReleaseYear() + ")\n" + film.getLength() + " min\nLanguage: " + film.getLanguage() + "\n");
							found = true;
						}
					}
					
					if (!found) {
						System.out.println("No result");
					}
				} else if (line.matches("LIST\tFEATUREFILMS\tAFTER\t.*")) {
					boolean found = false;
					String[] parts = line.split("LIST\tFEATUREFILMS\tAFTER\t");
					for (Film film : films) {
						if (film instanceof FeatureFilm  &&  ((FeatureFilm) film).isReleasedAfter(Integer.parseInt(parts[1]))) {
							System.out.println("Film title: " + film.getTitle() + " (" + ((FeatureFilm) film).getReleaseYear() + ")\n" + film.getLength() + " min\nLanguage: " + film.getLanguage() + "\n");
							found = true;
						}
					}
					
					if (!found) {
						System.out.println("No result");
					}
				} else if (line.matches("LIST\tFILMS\tBY\tCOUNTRY\t.*")) {
					String[] parts = line.split("LIST\tFILMS\tBY\tCOUNTRY\t");
					boolean found = false;
					for (Film film : films) {
						if (film.getCountry().equals(parts[1])) {
							found = true;
							System.out.println("Film title: " + film.getTitle() + "\n" + film.getLength() + " min\nLanguage: " + film.getLanguage() + "\n");
						}
					}
					
					if (!found) {
						System.out.println("No result");
					}
					
				} else if (line.matches("LIST\tARTISTS\tFROM\t.*")) {
					String[] parts = line.split("LIST\tARTISTS\tFROM\t");
					printDirector(parts[1]);
					System.out.println();
					printWritter(parts[1]);
					System.out.println();
					printActor(parts[1]);
					System.out.println();
					printChildActor(parts[1]);
					System.out.println();
					printStuntPerformer(parts[1]);
				} 
				
				
			} else if (command.contains("EDIT")) {
				scanner.next();
				int userID = scanner.nextInt();
				int filmID = scanner.nextInt();
				int rate = scanner.nextInt();
				
				Person person = getPerson(userID);
				Film film = getFilm(filmID);
				
				if (film == null || person == null || film.getRatingBy(person) == null) {
					System.out.println("Command Failed\nUser ID: " + userID + "\nFilm ID: " + filmID);
				} else {
					film.getRatingBy(person).setRate(rate);
					System.out.println("New ratings done successfully\nFilm title: " + film.getTitle() + "\nYour rating: " + rate);
				}
				
			} else if (command.contains("REMOVE")) {
				scanner.next();
				int userID = scanner.nextInt();
				int filmID = scanner.nextInt();

				Person person = getPerson(userID);
				Film film = getFilm(filmID);
				
				if (film == null || person == null || film.getRatingBy(person) == null) {
					System.out.println("Command Failed\nUser ID: " + userID + "\nFilm ID: " + filmID);
				} else {
					film.getRatings().remove(film.getRatingBy(person));
					System.out.println("Your film rating was removed successfully\nFilm title: " + film.getTitle());
				}
			}

			scanner.close();
			System.out.println("\n-----------------------------------------------------------------------------------------------------");
		}
		bufferedReader.close();
	}

	private static void printTVSeries(Comparator<Film> comparator) {
		System.out.println("TVSeries:");
		ArrayList<Film> tVSeries = new ArrayList<>();
		boolean isFound = false;
		for (Film film : films) {
			if (film instanceof TVSeries) {
				tVSeries.add(film);
				isFound = true;
			}
		}
		
		if (isFound) {
			Collections.sort(tVSeries, comparator);
			for (Film film : tVSeries) {
				System.out.println(film.getTitle() + " (" + ((TVSeries)film).getStartYear() + "-" + ((TVSeries)film).getEndYear()  + ") Ratings: " + film.getAvgRating() + "/10 from " + film.getRatings().size() + " users");
			}
		} else {
			System.out.println("No result");
		}
	}

	private static void printDocumentary(Comparator<Film> comparator) {
		System.out.println("Documentary:");
		ArrayList<Film> documentary = new ArrayList<>();
		boolean isFound = false;
		for (Film film : films) {
			if (film instanceof Documentary) {
				documentary.add(film);
				isFound = true;
			}
		}
		
		if (isFound) {
			Collections.sort(documentary, comparator);
			for (Film film : documentary) {
				System.out.println(film.getTitle() + " (" + ((Documentary)film).getReleaseYear() + ") Ratings: " + film.getAvgRating() + "/10 from " + film.getRatings().size() + " users");
			}
		} else {
			System.out.println("No result");
		}
	}

	private static void printShortFilm(Comparator<Film> comparator) {
		System.out.println("ShortFilm:");
		ArrayList<Film> shortFilms = new ArrayList<>();
		boolean isFound = false;
		for (Film film : films) {
			if (film instanceof ShortFilm) {
				shortFilms.add(film);
				isFound = true;
			}
		}
		
		if (isFound) {
			Collections.sort(shortFilms, comparator);
			for (Film film : shortFilms) {
				System.out.println(film.getTitle() + " (" + ((ShortFilm)film).getReleaseYear() + ") Ratings: " + film.getAvgRating() + "/10 from " + film.getRatings().size() + " users");
			}
		} else {
			System.out.println("No result");
		}
	}

	private static void printFeatureFilm(Comparator<Film> comparator) {
		System.out.println("FeatureFilm:");
		ArrayList<Film> featureFilms = new ArrayList<>();
		boolean isFound = false;
		for (Film film : films) {
			if (film instanceof FeatureFilm) {
				featureFilms.add(film);
				isFound = true;
			}
		}
		
		if (isFound) {
			Collections.sort(featureFilms, comparator);
			for (Film film : featureFilms) {
				System.out.println(film.getTitle() + " (" + ((FeatureFilm)film).getReleaseYear() + ") Ratings: " + film.getAvgRating() + "/10 from " + film.getRatings().size() + " users");
			}
		} else {
			System.out.println("No result");
		}
	}

	public static void main(String[] args) {
		try {
			String peopesDataFile = args[0];
			String filmsDataFile = args[1];
			String commandFile = args[2];
			String ouputFile = args[3];
			
			PrintStream printStream = new PrintStream(new File(ouputFile));
			System.setOut(printStream);
	       	readPeopleData(peopesDataFile);
			readFilmData(filmsDataFile);
			readCommands(commandFile);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
