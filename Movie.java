import java.util.ArrayList;

public class Movie {
	private int times;
	private String releaseDate;
	private char rating;
	private String showName;
	private ArrayList<String> showtimes;
	public Movie() {
		
	}
	public Movie(String showName) {
		this.showName = showName;
	}
	public Movie(int times, String releaseDate, char rating, String showName) {
		this.times = times;
		this.releaseDate = releaseDate;
		this.rating = rating;
		this.showName = showName;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public char getRating() {
		return rating;
	}
	public void setRating(char rating) {
		this.rating = rating;
	}
	public ArrayList<String> getShowtimes() {
		return showtimes;
	}
	public void setShowtimes(ArrayList<String> showtimes) {
		this.showtimes = showtimes;
	}
	
}