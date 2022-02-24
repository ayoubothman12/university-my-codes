import java.util.ArrayList;
import java.util.Comparator;

public class Film {
	private int filmID;
	private String title;
	private String language;
	private ArrayList<Director> directors;
	private int length;
	private String country;
	private ArrayList<Person> performers;
	private ArrayList<Rating> ratings;
	
	public Film(int filmID, String title, String language, ArrayList<Director> directors, int length, String country,
			ArrayList<Person> performers) {
		this.filmID = filmID;
		this.title = title;
		this.language = language;
		this.directors = directors;
		this.length = length;
		this.country = country;
		this.performers = performers;
		ratings = new ArrayList<>();
	}

	public int getFilmID() {
		return filmID;
	}
	
	public void setFilmID(int filmID) {
		this.filmID = filmID;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public ArrayList<Director> getDirectors() {
		return directors;
	}
	
	public void setDirectors(ArrayList<Director> directors) {
		this.directors = directors;
	}
	
	public ArrayList<Person> getPerformers() {
		return performers;
	}
	
	public void setPerformers(ArrayList<Person> performers) {
		this.performers = performers;
	}

	public ArrayList<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(ArrayList<Rating> ratings) {
		this.ratings = ratings;
	}
	
	public Rating getRatingBy(Person person) {
		for (Rating rating : ratings) {
			if(rating.getUser().getId() == person.getId()) {
				return rating;
			}
		}
		return null;
	}
	
	public float getAvgRating() {
		if (ratings.size() == 0) return 0;
		float sum = 0;
		for (Rating rating : ratings) {
			sum += rating.getRate();
		}
		
		return sum/(float)ratings.size();
	}
}
