package engine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

//Can't be extended
public final class EventManager {

    //Can't be instantiated
    private EventManager() {}

    private static boolean initialized = false;
    private static File eventDirectory; //Directory to store event data
    private static ObservableSet<Event> events; //TreeSet won't allow duplicate Events
    private static LinkedList<Link> searchQueue;

    public static void initialize(String path) throws IllegalStateException {

        if(!initialized) {
            eventDirectory = new File(path + "eventdata");
            events = FXCollections.observableSet();
            if(!eventDirectory.mkdir()) {
                try {
                    for(File file : eventDirectory.listFiles()) {
                        Scanner input = new Scanner(Encryption.readAndDecrypt(file));
                        while(input.hasNextLine()) {
                            events.add(new Event(Integer.parseInt(input.nextLine()), input.nextLine(), input.nextLine(), LocalDate.parse(input.nextLine()), EventType.valueOf(input.nextLine())));
                        }
                    }
                }catch(NoSuchElementException | NullPointerException e) {
                    throw new IllegalStateException("Corrupted event file detected");
                }
            }
            Event.eventCount = events.size();
            UserManager.linkUsersToEvents();
            searchQueue = new LinkedList<>();
            searchQueue.add(new Link("https://www.amctheatres.com/movies", EventType.MOVIE));
            searchQueue.add(new Link("https://www.bandsintown.com/c/fairfax-va?came_from=253&sort_by_filter=Number+of+RSVPs", EventType.CONCERT));
            searchQueue.add(new Link("https://www2.gmu.edu/today-mason", EventType.MASON_EVENT));
            new Thread(() -> {
                try {
                    findEvents(); //find events on a separate thread so that the GUI can launch quickly
                }catch(IOException e) {}
            }).start();
            initialized = true;
        }else{
            throw new IllegalStateException("Event manager has already been initialized");
        }

    }

    private static class Link {

        private String link;
        private EventType type;

        private Link(String link, EventType type) {

            this.link = link;
            this.type = type;

        }

        private String getLink() {

            return link;
        }

        private EventType getType() {

            return type;
        }

    }

	private static void findEvents() throws IOException, IllegalStateException {

		while(!searchQueue.isEmpty()) {
            Link link = searchQueue.poll();
            if(link == null) {
                continue;
            }
            Elements elements;
            try{
                String html = Jsoup.connect(link.getLink()).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:71.0) Gecko/20100101 Firefox/71.0").get().html();
                elements = Jsoup.parse(html).select(link.getType().getQuery());
            }catch(IllegalArgumentException e) {
                throw new IllegalStateException("Search Queue must contain valid links");
            }
            switch(link.getType()) {
                case MOVIE:
                    for (int i = 0; i < 4; i++) {
                        elements.remove(elements.size()-1);
                    }
                    break;
                case CONCERT:
                    for (int i = 0; i < 4; i++) {
                        elements.remove(elements.size()-1);
                    }
                    break;
                case MASON_EVENT:
                    for(int i = 0; i < 4; i++) {
                        elements.remove(elements.size()-1);
                    }
                    break;
            }
            for(Element e : elements) {
                events.add(new Event(e.ownText(), link.getLink(), LocalDate.now(), link.getType()));
            }
        }

	}

	public static ObservableSet<Event> getEvents() {

        return events;
    }

    public static ObservableList<Event> getEventsSorted(EventType type) {

        ObservableList<Event> events = FXCollections.observableArrayList();
        for(Event event : EventManager.events) {
            if(event.getType() == type) {
                events.add(event);
            }
        }

        return events;
    }

    public static ObservableList<Event> getEventsSorted(LocalDate date) {

        ObservableList<Event> events = FXCollections.observableArrayList();
        for(Event event : EventManager.events) {
            if(event.getDate().getDayOfYear() == date.getDayOfYear()) {
                events.add(event);
            }
        }

        return events;
    }

    public static ObservableList<Event> getEventsAlphabetically() {

        ObservableList<Event> list = FXCollections.observableArrayList(events);
        list.sort(Comparator.comparing(Event::getName));

    	return list;
    }

    public static void save() { //Call this before exiting to save event data

        TreeSet<Event> events = new TreeSet<>();
        for(UserManager.User user : UserManager.usersReadOnly()) {
            events.addAll(user.getEventsAttending());
        }
        File[] files = eventDirectory.listFiles();
        if(files != null) {
            for(File file : files) {
                file.delete();
            }
        }else if(!eventDirectory.mkdir()){
            throw new IllegalStateException("Error saving to eventdata");
        }
        ArrayList<String> eventStrings = new ArrayList<>();
        for(Event event : events) {
            eventStrings.add("" + event.getId());
            eventStrings.add(event.getName());
            eventStrings.add(event.getUrl());
            eventStrings.add(event.getDate().toString());
            eventStrings.add(event.getType().toString());
        }
        while(!events.isEmpty()) {
            if(!Encryption.encryptAndWrite(new File(eventDirectory.getPath() + "\\events"), eventStrings)) {
                throw new IllegalStateException("Error while saving");
            }
            break;
        }

    }

    public static class Event implements Comparable<Event> {

        private static int eventCount = 0;

        private int id;
        private String name;
        private String url;
        private LocalDate date;
        private EventType type;

        private Event(String name, String url, LocalDate date, EventType type) {

            id = eventCount++;
            this.name = name;
            this.url = url;
            this.date = date;
            this.type = type;

        }

        private Event(int id, String name, String url, LocalDate date, EventType type) {

            this.id = id;
            this.name = name;
            this.url = url;
            this.date = date;
            this.type = type;

        }

        public int getId() {

            return id;
        }

        public String getName() {

            return name;
        }

        public String getUrl() {

            return url;
        }

        public LocalDate getDate() {

            return date;
        }

        public EventType getType() {

            return type;
        }

        @Override
        public int compareTo(Event o) {

            return name.compareTo(o.getName());
        }

        @Override
        public boolean equals(Object obj) {

            if(obj instanceof Event) {
                Event e = (Event)obj;
                return e.getName().equals(name) && e.getUrl().equals(url) && e.getDate().equals(date) && e.getType() == type;
            }

            return false;
        }

        @Override
        public String toString() {

            return "[" + getType() + "] " + name + ", on " + date;
        }

    }

}