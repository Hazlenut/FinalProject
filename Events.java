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
 public ArrayList<String> specialEvents() throws IOException {
   ArrayList<String> eventList = new ArrayList<String>();
   String html = Jsoup.connect("https://www.fairfaxva.gov/government/parks-recreation/special-events").get().html();
   Document document2 = Jsoup.parse(html);
   Elements div = document2.select("td");
   for (Element d : div) {
     eventList.add(d.ownText());
   }
   for (int i = 0; i < 4; i++) {
     eventList.remove(eventList.size()-1);
   }
   return eventList;
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
 
 public static ArrayList<SpecialEvents> getSpecialEvents(String city, String state, String type, String range) throws IOException {
   ArrayList<SpecialEvents> list = new ArrayList<SpecialEvents>();
   boolean cat1 = true;
   String baseURL = "https://www.stubhub.com/";
   String fullURL;
   if(type.equals("concerts")) {
     fullURL = baseURL + "concert-tickets/category/1/?pbG=" + city + "%2C%20" + state;
   }
   else if(type.equals("sports")) {
     fullURL = baseURL + "sports-tickets/category/28/?pbG=" + city + "%2C%20" + state;
   }
   else if(type.equals("theater") || type.equals("comedy")) {
     fullURL = baseURL + "theater-and-arts-tickets/category/174/?pbG=" + city + "%2C%20" + state;
   }
   else {
     fullURL = "https://www.google.com/search?q=events+near+fairfax+va";
     cat1 = false;
   }
   String html = Jsoup.connect(fullURL).get().html();
   Document document = Jsoup.parse(html);
   Elements div = document.select("h3");
   for(int i = 0; i < 10; i++) {
   list.add(new SpecialEvents(div.get(i).ownText()));
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
