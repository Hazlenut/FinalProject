package engine;

import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class EventView extends View {

    private ListView<String> eventList;

    public EventView(String name) {

        super(name);

        eventList = new ListView<>();
        matchListToEventLibrary();
        getBorderPane().setLeft(eventList);
        int numRows = 5, numColumns = 7; //(7 days per week & 4 weeks in a month, with another row to represent the last few days)
        GridPane gridPane = new GridPane();
        //Add stuff to the Gridpane...
        getBorderPane().setCenter(gridPane);

    }

    public void matchListToEventLibrary() {

        eventList.getItems().clear();
        for(Event event : EventLibrary.getEvents()) {
            eventList.getItems().add(event.toString());
        }

    }

}