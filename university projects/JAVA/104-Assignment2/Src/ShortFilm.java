import java.util.ArrayList;

public class ShortFilm extends Film {
	private String[] genre;
	private String releaseDate;
	private ArrayList<Writer> writers;
	
	public ShortFilm(int filmID, String title, String language, ArrayList<Director> directors, int length,
			String country, ArrayList<Person> performers, String[] genre,
			String releaseDate, ArrayList<Writer> writers) {
		super(filmID, title, language, directors, length, country, performers);
		this.genre = genre;
		this.releaseDate = releaseDate;
		this.writers = writers;
	}

	public String[] getGenre() {
		return genre;
	}

	public void setGenre(String[] genre) {
		this.genre = genre;
	}

	public String getReleaseYear() {
		return releaseDate.split("\\.")[2];
	}
	
	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public ArrayList<Writer> getWriters() {
		return writers;
	}

	public void setWriters(ArrayList<Writer> writers) {
		this.writers = writers;
	}
	
	@Override
	public String toString(){
		String output = getTitle() + " (" + getReleaseYear()+ ")\n";
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
