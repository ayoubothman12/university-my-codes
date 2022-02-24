import java.util.ArrayList;
public class Documentary extends Film{
	private String releaseDate;
	
	public Documentary(int filmID, String title, String language, ArrayList<Director> directors, int length,
			String country, ArrayList<Person> performers, String releaseDate) {
		super(filmID, title, language, directors, length, country, performers);
		this.releaseDate = releaseDate;
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
	
	@Override
	public String toString(){
		String output = getTitle() + " (" + getReleaseYear() + ")\n";
	
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
