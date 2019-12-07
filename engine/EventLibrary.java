package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public final class EventLibrary {

    private static TreeSet<Event> events;

    static {

        events = new TreeSet<>();

    }

    public static void clear() {

        events = new TreeSet<>();

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
        Event.addLink("https://www.stubhub.com/sports-tickets/category/28/", EventType.SPORT);

        try{
            events.addAll(Event.findEvents());
        }catch(IOException e) {
            System.out.println("Default events failed");
        }

    }

    private static void loadPreviousEvents() {

        try(Scanner input = new Scanner(new File(Main.getDirectory() + "events.txt"))) {
            //
            loadDefaultLinks();
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

    public static void addCustomEvents(ArrayList<Event> events) {

        EventLibrary.events.addAll(events);

    }

    public static ArrayList<Event> getEvents() {

        return new ArrayList<>(events);
    }

    public static ArrayList<Event> getEventsOfAType(EventType type) {

        ArrayList<Event> events = new ArrayList<>();
        for(Event event : EventLibrary.events) {
            if(event.getType() == type) {
                events.add(event);
            }
        }

        return events;
    }

    public static boolean save(String userID) {

        try(FileWriter fileWriter = new FileWriter(new File(Main.getDirectory() + userID + "-events.txt"))) {
            fileWriter.write("events");
            for(Event event : events) {
                fileWriter.write(event.toFileString());
            }
        }catch(IOException e) {
            return false;
        }

        return true;
    }

}