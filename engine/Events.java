package engine;
import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
public class Events {
	
	//need to edit to add times 
	public ArrayList<String> amcMovies() throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		String html = Jsoup.connect("https://www.amctheatres.com/movies").get().html();
		Document document = Jsoup.parse(html);
		Elements div = document.select("h3");
		for (Element d: div) {
			list.add(d.ownText());
		}
		for (int i = 0; i < 4; i++) {
			list.remove(list.size()-1);
		}
		return list;
	}
	
	public ArrayList<String> concertShowings() throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		//add code
		
		return list;
	}
	
	public static ArrayList<Movie> getMovies(int zipCode, int numShows) throws IOException {
		ArrayList<Movie> list = new ArrayList<Movie>();
		String baseURL = "https://www.google.com/search";
		String fullURL = baseURL + "?q=movies near " + zipCode;
		String html = Jsoup.connect(fullURL).get().html();
		Document document = Jsoup.parse(html);
		Elements div = document.select("div.kltat span");
		for(int i = 0; i < numShows; i++) {
			list.add(new Movie(div.get(i).ownText()));
		}
		
		
		return list;
	}
	public static void main(String[] args) {
		ArrayList<Movie> test = new ArrayList<Movie>();
		try {
			test = getMovies(20166, 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < test.size(); i++) {
			System.out.println(test.get(i).getShowName());
		}
	}
	
}
