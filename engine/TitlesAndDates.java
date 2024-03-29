package engine;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class TitlesAndDates {

    public String[] concertTitles() throws IOException, IllegalStateException {
        Document html;
        Elements elements;
        Elements elements2;
        String[] titles = new String[25];

        try{
            html = Jsoup.parse(Jsoup.connect("https://www.bandsintown.com/c/fairfax-va?came_from=253&sort_by_filter=Number+of+RSVPs").get().html());
            elements = html.select("h2");
            elements2 = elements.select("a");
            for(int i =0; i <= elements2.size()-1; i++)
            {
                titles[i] = elements2.eq(i).html();
            }
        }catch(IllegalArgumentException e) {
            throw new IllegalStateException("Search Queue must contain valid links");
        }
        return titles;

    }

    public String[] sportTitles() throws IOException, IllegalStateException {
        Document html;
        Elements elements;
        Elements elements2;
        String[] titles = new String[25];

        try{
            html = Jsoup.parse(Jsoup.connect("https://washingtondc.eventful.com/events/categories/sports").timeout(50000000).get().html());
            elements = html.select("h4");
            elements2 = elements.select("span");
            for(int i =0; i <= elements2.size()-1; i++)
            {
                titles[i] = elements2.eq(i).html();
            }
        }catch(IllegalArgumentException e) {
            throw new IllegalStateException("Search Queue must contain valid links");
        }
        return titles;

    }

    public String[] sportDates() throws IOException, IllegalStateException {
        Document html;
        Elements elements;
        Elements elements2;
        String[] titles = new String[25];
        try{
            html = Jsoup.parse(Jsoup.connect("https://washingtondc.eventful.com/events/categories/sports").timeout(50000000).get().html());
            elements = html.select("h4");
            elements2 = elements.select("strong");
            for(int i =0; i <= elements2.size()-1; i++)
            {
                titles[i] = elements2.eq(i).html();
            }
        }catch(IllegalArgumentException e) {
            throw new IllegalStateException("Search Queue must contain valid links");
        }
        return titles;

    }

}