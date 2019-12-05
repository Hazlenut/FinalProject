package engine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Event implements Comparable<Event> {

    private static LinkedList<Link> searchQueue;
    private static ArrayList<Link> activeLinks;

    static{

        searchQueue = new LinkedList<>();
        activeLinks = new ArrayList<>();

    }

	public static ArrayList<Event> findEvents() throws IOException, IllegalStateException {

        if(searchQueue.isEmpty()) {
            return new ArrayList<>();
        }
		ArrayList<Event> events = new ArrayList<>();
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
                        events.add(new Event(e.ownText(), new Date(), link.getType(), 0, 0, 0)); //Fix Date system / code for other types besides movies
                    }
                    break;
                case CONCERT:
                    for(Element e : elements) {
                        events.add(new Event(e.ownText(), new Date(), link.getType(), 0, 0, 0));
                    }
                    break;
                case SPORT:
                    for(Element e : elements) {
                        events.add(new Event(e.ownText(), new Date(), link.getType(), 0, 0, 0));
                    }
                    break;
                case OTHER:
                    for(Element e : elements) {
                        events.add(new Event(e.ownText(), new Date(), link.getType(), 0, 0, 0));
                    }
            }
            activeLinks.add(link);
        }

		return events;
	}

	public static boolean addLink(String link, EventType type) {

        Link l = new Link(link, type);
        if(!searchQueue.contains(l) && !activeLinks.contains(l)) {
            searchQueue.add(l);
            return true;
        }else{
            return false;
        }

    }

    public static boolean addLink(String link) {

        return addLink(link, EventType.OTHER);
    }

    public static class Link {

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

        @Override
        public boolean equals(Object o) {

            return o instanceof Link && ((Link)o).getType() == type && ((Link)o).getLink().equals(link);
        }

    }

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
    public int compareTo(Event o) {

        return new Integer("56");
    }

    public String toFileString() {

        return name + "\n" + date.toString() + "\n" + likes + "\n" + dislikes + "\n" + people + "\n";
    }

    @Override
    public String toString() {

        return getType() + " Event: " + name + " on " + date;
    }

}