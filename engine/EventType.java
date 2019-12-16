package engine;

public enum EventType {

    //These enums are used to represent different events that are gathered from websites
    MOVIE,
    CONCERT,
    SPORT,
    ART,
    MASON_EVENT,
    OTHER;

    /**
     * @return the cssQuery that each EventType requires in order to be properly parsed by Jsoup
     */
    public String getQuery() {

        switch(this) {
            case MOVIE:
                return "h3";
            case CONCERT:
                return "h2";
            case SPORT:
                return "h4";
            case ART:
                return "h3";
            case MASON_EVENT:
                return "span";
            default:
            case OTHER:
                return "h1";
        }

    }

}