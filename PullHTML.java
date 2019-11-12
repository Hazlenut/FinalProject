import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import java.util.*;

public class PullHTML {
	
	//concerts not working rn
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>();
		String html = Jsoup.connect("https://www.stubhub.com/concert-tickets/category/1/").get().html();
		Document document = Jsoup.parse(html);
		Elements div = document.select("PerformerCard__Details__name");
		for (Element d: div) {
			list.add(d.ownText());
		}
		
		for(String s: list) {
			System.out.println(s);
		}
	}

}
