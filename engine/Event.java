package engine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Event {

    private static LinkedList<Link> searchQueue;

    static{

        searchQueue = new LinkedList<>();

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
        }

		return events;
	}

	public static void addLink(String link, EventType type) {

        Link l = new Link(link, type);
        if(!searchQueue.contains(l)) {
            searchQueue.add(l);
        }

    }

    public static void addLink(String link) {

        addLink(link, EventType.OTHER);

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
    private double clout;

    public Event(String name, Date date, EventType type, int likes, int dislikes, int people) {

        this.name = name;
        this.date = date;
        this.type = type;
        this.likes = likes;
        this.dislikes = dislikes;
        this.people = people;
        //replace 10 with age of event
        this.clout = ((likes * 2) + (dislikes*-1.5)/10);
        

    }
    public double getClout() {
    	return clout;
    }
    public void setClout(double clout) {
    	this.clout= clout;
    }
    public int getLikes() {
		return likes;
	}
    
    public int getPeople() {
    	return people;
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
    public String toString() {

        return getType() + " Event: " + name + " on " + date;
    }

}