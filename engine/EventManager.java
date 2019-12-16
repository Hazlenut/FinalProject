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

    private static boolean initialized = false; //Initialization flag
    private static File eventDirectory; //Directory to store event data
    private static ObservableSet<Event> events; //A Set won't allow duplicate Events
    private static LinkedList<Link> searchQueue; //Queue that holds links that haven't been searched yet

    /**
     * Initializes the EventManager and loads in events (if any) that were previously saved as well as events from certain websites
     * @param path the path for the eventDirectory
     * @throws IllegalStateException if this method is called more than once
     */
    public static void initialize(String path) throws IllegalStateException {

        if(!initialized) {
            eventDirectory = new File(path + "eventdata");
            events = FXCollections.observableSet();
            if(!eventDirectory.mkdir()) { //This code loads in events if the eventdata directory exists; otherwise it creates a new eventDirectory
                try {
                    for(File file : eventDirectory.listFiles()) { //Loads in eventdata
                        Scanner input = new Scanner(Encryption.readAndDecrypt(file));
                        while(input.hasNextLine()) {
                            events.add(new Event(Integer.parseInt(input.nextLine()), input.nextLine(), input.nextLine(), LocalDate.parse(input.nextLine()), EventType.valueOf(input.nextLine())));
                        }
                    }
                }catch(NoSuchElementException | NullPointerException e) {
                    throw new IllegalStateException("Corrupted event file detected"); //Exception thrown if the eventdata file is corrupted
                }
            }
            Event.eventCount = events.size(); //Ensures that the events loaded in are accounted for
            UserManager.linkUsersToEvents(); //Adds events loaded in to individual user objects (if they are attending them)
            searchQueue = new LinkedList<>();
            //Links (events are gathered from these websites):
            searchQueue.add(new Link("https://www.amctheatres.com/movies", EventType.MOVIE));
            searchQueue.add(new Link("https://www.bandsintown.com/c/fairfax-va?came_from=253&sort_by_filter=Number+of+RSVPs", EventType.CONCERT));
            searchQueue.add(new Link("https://washingtondc.eventful.com/events/categories/sports", EventType.SPORT));
            searchQueue.add(new Link("https://www2.gmu.edu/today-mason", EventType.MASON_EVENT));
            new Thread(() -> {
                try {
                    findEvents(); //find events on a separate thread so that the GUI can launch quickly
                }catch(IOException e) {}
            }).start();
            initialized = true;
        }else{
            throw new IllegalStateException("Event manager has already been initialized"); //throw an exception if the EventManager is initialized more than once
        }

    }

    /**
     * The link class holds URLs as well as the EventType that they represent
     */
    private static class Link {

        private String link;
        private EventType type;

        /**
         * Creates a new link with the specified URL and EventType
         * @param link the URL
         * @param type the EventType
         */
        private Link(String link, EventType type) {

            this.link = link;
            this.type = type;

        }

        /**
         * @return the URL of this Link as a String
         */
        private String getLink() {

            return link;
        }

        /**
         * @return the EventType of this Link
         */
        private EventType getType() {

            return type;
        }

    }

    /**
     * This method will get the events off of the websites in the searchQueue
     * @throws IOException if Jsoup encounters an error
     * @throws IllegalStateException If the searchQueue contains invalid Links
     */
    private static void findEvents() throws IOException, IllegalStateException {

        while(!searchQueue.isEmpty()) { //Loop through every Link in the searchQueue
            Link link = searchQueue.poll();
            if(link == null) {
                continue;
            }
            Elements elements;
            try{
                //this code uses Jsoup to gets events from the specified URL:
                String html = Jsoup.connect(link.getLink()).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:71.0) Gecko/20100101 Firefox/71.0").get().html();
                elements = Jsoup.parse(html).select(link.getType().getQuery());
            }catch(IllegalArgumentException e) {
                throw new IllegalStateException("Search Queue must contain valid links"); //If invalid link is in the searchQueue
            }
            //Cleanup before adding events:
            switch(link.getType()) {
                case MOVIE:
                    for (int i = 0; i < 4; i++) {
                        elements.remove(elements.size()-1);
                    }
                    break;
            }
            //Add events:
            for(Element e : elements) {
                events.add(new Event(e.ownText(), link.getLink(), LocalDate.now(), link.getType()));
            }
        }

    }

    /**
     * @return an ObservableList with ALL events
     */
    public static ObservableSet<Event> getEvents() {

        return events;
    }

    /**
     * @return an ObservableList with events of a specified type
     */
    public static ObservableList<Event> getEventsSorted(EventType type) {

        ObservableList<Event> events = FXCollections.observableArrayList();
        for(Event event : EventManager.events) {
            if(event.getType() == type) {
                events.add(event);
            }
        }

        return events;
    }

    /**
     * @return an ObservableList with events from a certain date
     */
    public static ObservableList<Event> getEventsSorted(LocalDate date) {

        ObservableList<Event> events = FXCollections.observableArrayList();
        for(Event event : EventManager.events) {
            if(event.getDate().getDayOfYear() == date.getDayOfYear()) {
                events.add(event);
            }
        }

        return events;
    }

    /**
     * @return an ObservableList with events sorted alphabetically
     */
    public static ObservableList<Event> getEventsAlphabetically() {

        ObservableList<Event> list = FXCollections.observableArrayList(events);
        list.sort(Comparator.comparing(Event::getName));

     return list;
    }

    /**
     * This method must be called before exiting or else eventdata won't be saved
     */
    public static void save() {

        TreeSet<Event> events = new TreeSet<>();
        //Gathers all events that users are attending:
        for(UserManager.User user : UserManager.usersReadOnly()) {
            events.addAll(user.getEventsAttending());
        }
        //Delete previously saved files, as they are now outdated:
        File[] files = eventDirectory.listFiles();
        if(files != null) {
            for(File file : files) {
                file.delete();
            }
        }else if(!eventDirectory.mkdir()){
            throw new IllegalStateException("Error saving to eventdata");
        }
        //Add event data to list, which will be written to a file:
        ArrayList<String> eventStrings = new ArrayList<>();
        for(Event event : events) {
            eventStrings.add("" + event.getId());
            eventStrings.add(event.getName());
            eventStrings.add(event.getUrl());
            eventStrings.add(event.getDate().toString());
            eventStrings.add(event.getType().toString());
        }
        //encrypt and then save the data:
        while(!events.isEmpty()) {
            if(!Encryption.encryptAndWrite(new File(eventDirectory.getPath() + "\\events"), eventStrings)) {
                throw new IllegalStateException("Error while saving"); //throw exception if an error occurs while saving
            }
            break;
        }

    }

    /**
     * This class stores all of the data of an event gathered from a website
     */
    public static class Event implements Comparable<Event> {

        private static int eventCount = 0;

        private int id;
        private String name;
        private String url;
        private LocalDate date;
        private EventType type;

        /**
         * Creates a new Event object
         * @param name the Event name
         * @param url the Event's URL
         * @param date the Event's Date
         * @param type the EventType of this Event
         */
        private Event(String name, String url, LocalDate date, EventType type) {

            id = eventCount++;
            this.name = name;
            this.url = url;
            this.date = date;
            this.type = type;

        }

        /**
         * This constructor is nearly identical to the constructor above, but should only be used when loading in previously saved events
         */
        private Event(int id, String name, String url, LocalDate date, EventType type) {

            this.id = id;
            this.name = name;
            this.url = url;
            this.date = date;
            this.type = type;

        }

        /**
         * @return the ID of this event (used for associating users with events)
         */
        public int getId() {

            return id;
        }

        /**
         * @return the name of this Event
         */
        public String getName() {

            return name;
        }

        /**
         * @return the URL of this Event
         */
        public String getUrl() {

            return url;
        }

        /**
         * @return the Date of this event
         */
        public LocalDate getDate() {

            return date;
        }

        /**
         * @return the EventType of this Event
         */
        public EventType getType() {

            return type;
        }

        /**
         * compareTo is necessary so that Events can be stored in the ObservableSet
         */
        @Override
        public int compareTo(Event o) {

            return name.compareTo(o.getName());
        }

        /**
         * Test equality between two Event objects
         */
        @Override
        public boolean equals(Object obj) {

            if(obj instanceof Event) {
                Event e = (Event)obj;
                return e.getName().equals(name) && e.getUrl().equals(url) && e.getDate().equals(date) && e.getType() == type; //Only equal if all fields are equal
            }

            return false;
        }

        /**
         * @return a String representation of this Event
         */
        @Override
        public String toString() {

            return "[" + getType() + "] " + name + ", on " + date;
        }

    }

}