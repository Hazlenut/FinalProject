import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class SetEvents {
	private Movie movie;
	private ArrayList<Movie> moviesList;
	public int uploadOrder = 0;
	
	public SetEvents() {
		this.uploadOrder = uploadOrder + 1;
	}
	
	public static ArrayList<Movie> getMovies(int zipCode, int numShows) throws IOException  {
		ArrayList<Movie> list = new ArrayList<Movie>();
		String baseURL = "https://www.google.com/search";
		String fullURL = baseURL + "?q=movies near " + zipCode;
		String html;
			html = Jsoup.connect(fullURL).get().html();
		Document document = Jsoup.parse(html);
		Elements div = document.select("div.kltat");

		for (int i = 0; i < numShows; i++) {
			list.add(new Movie(div.get(i).ownText()));
		}

		return list;
	}

/*	public static void main(String[] args) {
		ArrayList<Movie> test = new ArrayList<Movie>();
		try {
			test = getMovies(20166, 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < test.size(); i++) {
			System.out.println(test.get(i).getShowName());
		}
	}*/

}
