package engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class EventLoader {

    public static ArrayList<Event> loadEvents(String userID) {

        try(Scanner input = new Scanner(new File(userID + "-events.txt"))) {
            ArrayList<Event> events = new ArrayList<>();
            while(input.hasNextLine()) {
                events.add(new Event(input.nextLine(), new Date(), EventType.OTHER, new Integer(input.nextLine()), new Integer(input.nextLine()), new Integer(input.nextLine())));
            }
            return events;
        }catch(Exception e) {
            return null;
        }

    }

}