import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Movie extends Event{
	private int times;
	private String releaseDate;
	private String rating;
	private String showName;
	public Movie() {
		
	}
	public Movie(String showName) {
		this.showName = showName;
	}
	public Movie(int times, String releaseDate, String rating, String showName) {
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
	public String getRating() {
		return rating;
	}
	public void setRating() throws IOException {
		//zqhAOd
		ArrayList<Movie> list = new ArrayList<Movie>();
		String baseURL = "https://www.google.com/search";
		String fullURL = baseURL + "?q=movies near " + getShowName();
		String html = Jsoup.connect(fullURL).get().html();
		Document document = Jsoup.parse(html);
		Elements div = document.select("zqhAOd");
		this.rating = div.html();
	}
	
	
	
	
}
