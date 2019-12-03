package engine;

public enum EventType {

    MOVIE, CONCERT, SPORT, OTHER;

    public String getQuery() {

        switch(this) {
            case MOVIE:
                return "h3";
            case CONCERT:
                return "h3";
            case SPORT:
                return "h3";
            default:
            case OTHER:
                return "h1";
        }

    }

    public static EventType getEventType(String id) {

        switch(id) {
            case "Movie":
                return MOVIE;
            case "Concert":
                return CONCERT;
            case "Sport":
                return SPORT;
            default:
                return OTHER;
        }

    }

    //Metadata - movie = rating, release, etc.

}