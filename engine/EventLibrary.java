package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public final class EventLibrary {

    private static ArrayList<Event> events;

    static {

        events = new ArrayList<>();

    }

    public static void addEventsToLibrary() {

        if(Attributes.getAttribute("Previously Loaded").equalsIgnoreCase("Yes")) {
            loadPreviousEvents();
        }else{
            loadDefaultLinks();
            Attributes.updateAttribute("Previously Loaded", "No", "Yes");
        }

    }

    private static void loadDefaultLinks() {

        Event.addLink("https://www.amctheatres.com/movies", EventType.MOVIE);
        Event.addLink("https://www.stubhub.com/concert-tickets/category/1/", EventType.CONCERT);

        try{
            events = Event.findEvents();
        }catch(IOException e) {
            System.out.println("Default events failed");
        }

    }

    private static void loadPreviousEvents() {

        try(Scanner input = new Scanner(new File(Main.getDirectory() + "events.txt"))) {
            //Code...
            loadDefaultLinks(); //FixMe
        }catch(FileNotFoundException e) {
            loadDefaultLinks();
        }

    }

    public static boolean updateEvents() {

        try{
            events.addAll(Event.findEvents());
            return true;
        }catch(IOException e) {
            return false;
        }

    }

    public static ArrayList<Event> getEvents() {

        return events;
    }

    //Add methods that retrieve a List of specific events from the main event ArrayList,
    //like a List that only contains CONCERT events or MOVIE events...

    public static void save(String userID) {

        try(FileWriter fileWriter = new FileWriter(new File(Main.getDirectory() + "events.txt"))) {
            fileWriter.write("events");
            for(Event event : events) {
                //Code...
            }
        }catch(IOException e) {
            //Code...
        }

    }

}

    //Add this method to get Movie Events based off a geographic location:

    /*

    public static ArrayList<Movie> getMovies(int zipCode, int numShows) throws IOException {

        Elements div = document.select("PerformerCard__Details__name");
		ArrayList<Movie> list = new ArrayList<>();
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

	*/