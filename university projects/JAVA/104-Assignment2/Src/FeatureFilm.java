import java.util.ArrayList;

public class FeatureFilm extends Film {
	private String releaseDate;
	private int budget;
	private ArrayList<Writer> writers;
	private String[] genre;

	public FeatureFilm(int filmID, String title, String language, ArrayList<Director> directors, int length,
			String country, ArrayList<Person> performers, String releaseDate, int budget,
			ArrayList<Writer> writers, String[] genre) {
		super(filmID, title, language, directors, length, country, performers);
		this.releaseDate = releaseDate;
		this.budget = budget;
		this.writers = writers;
		this.genre = genre;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public String getReleaseYear() {
		return releaseDate.split("\\.")[2];
	}

	
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public ArrayList<Writer> getWriters() {
		return writers;
	}

	public void setWriters(ArrayList<Writer> writers) {
		this.writers = writers;
	}

	public String[] getGenre() {
		return genre;
	}

	public void setGenre(String[] genre) {
		this.genre = genre;
	}
	
	public boolean isReleasedAfter(int year) {
		return Integer.parseInt(releaseDate.split("\\.")[2]) >= year;
	}

	public boolean isReleasedBefore(int year) {
		return Integer.parseInt(releaseDate.split("\\.")[2]) < year;
	}
	
	@Override
	public String toString(){
		String output = getTitle() + " (" + getReleaseYear() + ")\n";
		for (int i = 0; i < genre.length; i++) {
			if (i == 0)
				output += genre[i];
			else
				output += ", " + genre[i];
		}

		output += "\nWriters: ";

		for (int i = 0; i < writers.size(); i++) {
			String value = writers.get(i).getName() + " " + writers.get(i).getSurname();
			if (i == 0) 
				output += value;
			else
				output += ", " + value;
		}
		output += "\nDirectors: ";
		for (int i = 0; i < getDirectors().size(); i++) {
			String value= getDirectors().get(i).getName() + " " + getDirectors().get(i).getSurname();
			if (i == 0) 
				output += value;
			else
				output += ", " + value;
		}

		output += "\nStars: ";
		for (int i = 0; i < getPerformers().size(); i++) {
			String value = getPerformers().get(i).getName() + " " + getPerformers().get(i).getSurname();
			if (i == 0) 
				output += value;
			else
				output += ", " + value;
		}
		
		if (getRatings().isEmpty()) {
			output += "\nAwaiting for votes";
		} else {
			output += "\nRatings: " + getAvgRating() + "/10 from "+getRatings().size() + " users\n";
		}
		return output;
	}
}
