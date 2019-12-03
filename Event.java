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
                        events.add(new Event(e.ownText(), new Date())); //Fix Date system / code for other types besides movies
                    }
                    break;
                case CONCERT:
                    for(Element e : elements) {
                        events.add(new Event(e.ownText(), new Date()));
                    }
                case SPORT:
                    for(Element e : elements) {
                        events.add(new Event(e.ownText(), new Date()));
                    }
                    break;
                case OTHER:
                    for(Element e : elements) {
                        events.add(new Event(e.ownText(), new Date()));
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

    public Event(String name, Date date) {

        this.name = name;
        this.date = date;

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

    @Override
    public String toString() {

        return "Event: " + name + " on " + date;
    }

}