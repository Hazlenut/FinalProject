package engine;

public enum EventType {

    MOVIE,
    CONCERT,
    SPORT,
    ART,
    MASON_EVENTS,
    OTHER;

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
          case MASON_EVENTS:
            return "href";
          default:
          case OTHER:
            return "h1";
        }

    }

}