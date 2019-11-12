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
	
	
}
