import java.util.ArrayList;

public class TVSeries extends Film{
	private String[] genre;
	private ArrayList<Writer> writers;
	private String startDate;
	private String endDate;
	private int seasons;
	private int episodes;

	public TVSeries(int filmID, String title, String language, ArrayList<Director> directors, int length,
			String country, ArrayList<Person> performers, String[] genre,
			ArrayList<Writer> writers, String startDate, String endDate, int seasons, int episodes) {
		super(filmID, title, language, directors, length, country, performers);
		this.genre = genre;
		this.writers = writers;
		this.startDate = startDate;
		this.endDate = endDate;
		this.seasons = seasons;
		this.episodes = episodes;
	}

	public String[] getGenre() {
		return genre;
	}
	
	public void setGenre(String[] genre) {
		this.genre = genre;
	}
	
	public ArrayList<Writer> getWriters() {
		return writers;
	}
	
	public void setWriters(ArrayList<Writer> writers) {
		this.writers = writers;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public int getSeasons() {
		return seasons;
	}
	
	public void setSeasons(int seasons) {
		this.seasons = seasons;
	}
	
	public int getEpisodes() {
		return episodes;
	}
	
	public void setEpisodes(int episodes) {
		this.episodes = episodes;
	}
	
	public String getStartYear() {
		return startDate.split("\\.")[2];
	}
	
	public String getEndYear() {
		return endDate.split("\\.")[2];
	}
	
	@Override
	public String toString(){
		String output = getTitle() + " (" + getStartYear() + "-" + getEndYear() + ")\n";
		output += seasons + " seasons, " + episodes + " episodes\n";
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
