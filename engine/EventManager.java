package engine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

//Can't be extended
public final class EventManager implements Comparator<engine.EventManager.Event>{

    //Can't be instantiated
    private EventManager() {}

    private static boolean initialized = false;
    private static ArrayList<Event> events;
    private static LinkedList<Link> searchQueue;

    public static void initialize() throws IOException, IllegalStateException {

        if(!initialized) {
            events = new ArrayList<>();
            searchQueue = new LinkedList<>();
            searchQueue.add(new Link("https://www.amctheatres.com/movies", EventType.MOVIE));
            searchQueue.add(new Link("https://www.stubhub.com/concert-tickets/category/1/", EventType.CONCERT));
            searchQueue.add(new Link("https://www.stubhub.com/sports-tickets/category/28/", EventType.SPORT));
            searchQueue.add(new Link("https://www.stubhub.com/theater-and-arts-tickets/category/174/", EventType.ART));
            findEvents();
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
                elements = Jsoup.parse(Jsoup.connect(link.getLink()).get().html()).select(link.getType().getQuery());
            }catch(IllegalArgumentException e) {
                throw new IllegalStateException("Search Queue must contain valid links");
            }
            switch(link.getType()) {
                case MOVIE:
                    for (int i = 0; i < 4; i++) {
                        elements.remove(elements.size()-1);
                    }
                    for(Element e : elements) {
                        events.add(new Event(e.ownText(), new Date(), link.getType(), 0, 0, 0));
                    }
                    break;
                default:
                    for(Element e : elements) {
                        events.add(new Event(e.ownText(), new Date(), link.getType(), 0, 0, 0));
                    }
            }
        }

	}

	public static ArrayList<Event> getEvents() {

        return events;
    }

    public static ArrayList<Event> getEvents(EventType type) {

        ArrayList<Event> events = new ArrayList<>();
        for(Event event : events) {
            if(event.getType() == type) {
                events.add(event);
            }
        }

        return events;
    }
    public static ArrayList<Event> sortAlphabet(ArrayList<Event> events) {
    	EventManager comp = new EventManager();
    	System.out.println("TEST: " + events.get(0).getName());
    	Collections.sort(events, comp);
    	for(int i = 0; i < events.size(); i++) {
    		System.out.println(events.get(i));
    	}
    	return events;
    }
    
    public static class Event implements Comparable<Event> {

        private String name;
        private Date date;
        private EventType type;

        private int likes;
        private int dislikes;
        private int people;
        private double points;
        private int popularity;
        private int time;

        public Event(String name, Date date, EventType type, int likes, int dislikes, int people) {

            this.name = name;
            this.date = date;
            this.type = type;
            this.likes = likes;
            this.dislikes = dislikes;
            this.people = people;
            this.time = getTime();
            //replace 10 with age of event
            if(likes*2 <= dislikes*1.5) {
                this.points = 0;
            }else {
                this.points = ((likes * 2) + (dislikes*-1.5) + -0.05*(Math.pow(time, 2)));
            }

        }

        public int getPopularity() {

            return popularity;
        }

        public void setPopularity() {

            popularity = Integer.parseInt((Integer.toString((likes+dislikes)/100)));

        }

        public double getPoints() {

            return points;
        }

        public void setPoints(double points) {

            this.points = points;

        }

        public int getLikes() {

            return likes;
        }

        public int getPeople() {

            return people;
        }

        public int getDisplayedPoints() {

            return (2 * likes) - dislikes;
        }

        public void addPoint() {

            //user.addPoints();
            //user.addPoints();
            this.points++;

        }

        public void removePoint() {

            //user.removePoints();
            this.points--;

        }

        public int getTime() {

            setTime();

            return time;
        }

        public void setTime() {

            //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            time = new Date().getHours();

        }

        public void setPeople(int people) {

            this.people = people;

        }

        public void setLikes(int likes) {

            this.likes = likes;

        }

        public int getDislikes() {

            return dislikes;
        }

        public void setDislikes(int dislikes) {

            this.dislikes = dislikes;

        }
        
        public String getName() {

            return name;
        }

        public void setName(String name) {

            this.name = name;

        }

        public Date getDate() {

            return date;
        }

        public void setDate(Date date) {

            this.date = date;

        }

        public EventType getType() {

            return type;
        }

        @Override
        public int compareTo(Event event) {

            //...

            return 0;
        }

        @Override
        public String toString() {

            return getType() + " Event: " + name + ", on " + date;
        }

    }

    @Override
	public int compare(Event o1, Event o2) {
		// TODO Auto-generated method stub
		System.out.println("here");
		return o1.getName().compareTo(o2.getName());
	}



}