package engine;

public enum EventType {

    MOVIE, CONCERT, SPORT, ART, MASONEVENTS, OTHER;

    public String getQuery() {

        switch(this) {
            case MOVIE:
                return "h3";
            case CONCERT:
                return "h3";
            case SPORT:
                return "h3";
          case ART:
            return "h3";
          case MASONEVENTS:
            return "href";
          default:
          case OTHER:
            return "h1";
        }

    }

    public static EventType getEventType(int index) {

        switch(index) {
            case 0:
                return MOVIE;
            case 1:
                return CONCERT;
            case 2:
                return SPORT;
            case 3:
                return ART;
            case 4:
                return MASONEVENTS;
            default:
                return OTHER;
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
            case "Art":
                return ART;
            case "Mason Event":
                return MASONEVENTS;
            default:
                return OTHER;
        }

    }

    //Metadata - movie = rating, release, etc.

}