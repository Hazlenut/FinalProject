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
                return "h2";
            case SPORT:
                return "h4";
          case MASON_EVENTS:
            return "span";
          default:
          case OTHER:
            return "h1";
        }

    }

}