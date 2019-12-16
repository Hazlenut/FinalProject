package engine;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

//Can't be extended
public final class EventView extends View {

    private EventManager.Event currentlySelectedEvent;

    /**
     * Creates a new EventView
     * @param screen The Screen instance holding this EventView
     */
    public EventView(Screen screen) {

        super(screen);

        currentlySelectedEvent = null;

        //Add a ListView to display events:

        ListView<EventManager.Event> listView = new ListView<>(FXCollections.observableArrayList(EventManager.getEvents())); //Creates a ListView which displays the Events stored in EventManager
        EventManager.getEvents().addListener((SetChangeListener<EventManager.Event>) change -> listView.setItems(FXCollections.observableArrayList(EventManager.getEvents())));
        setBottom(listView);

        //Add a DatePicker to sort events by date:

        DatePicker datePicker = new DatePicker();
        datePicker.setPadding(new Insets(10, 10, 10, 10));
        datePicker.setShowWeekNumbers(true);
        datePicker.setOnAction(event -> listView.setItems(EventManager.getEventsSorted(datePicker.getValue())));

        //Add a HyperLink so that a user can view the Event on the internet:

        Hyperlink link = new Hyperlink("View online");
        link.setOnAction(event -> {
            try {
                getScreen().getSearchView().goTo(listView.getSelectionModel().getSelectedItems().get(0).getUrl());
                getScreen().switchView(ViewType.SEARCH_VIEW);
            }catch(NullPointerException e) {}
            link.setVisited(false);
        });
        setTop(new HBox(new VBox(3, new Label("Show events by date:"), datePicker), link));

        HBox hBox = new HBox(35);
        hBox.setAlignment(Pos.CENTER);
        Label currentEvent = new Label("Events:");
        currentEvent.setFont(new Font(35));
        hBox.getChildren().add(currentEvent);
        RadioButton attendingButton = new RadioButton("Attending?");
        attendingButton.setOnAction(event -> {
            if(attendingButton.isSelected()) {
                getScreen().getCurrentUser().getEventsAttending().add(currentlySelectedEvent);
            }else{
                getScreen().getCurrentUser().getEventsAttending().remove(currentlySelectedEvent);
            }
            int i = listView.getSelectionModel().getSelectedIndex();
            listView.getSelectionModel().clearSelection();
            listView.getSelectionModel().select(i);
        });

        //This code adds functionality for allowing the user to both select certain events and mark whether or not they are attending:

        Label attendanceLabel = new Label("0 users are attending this event");
        listView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<EventManager.Event>) c -> {
            try {
                EventManager.Event event = listView.getSelectionModel().getSelectedItems().get(0);
                currentEvent.setText(event.getName());
                attendingButton.setSelected(getScreen().getCurrentUser().isAttending(event));
                try{
                    int attendance = UserManager.usersAttendingEvent(event);
                    if(attendance == 1) {
                        attendanceLabel.setText("1 user is attending this event");
                    }else{
                        attendanceLabel.setText(attendance + " users are attending this event");
                    }
                    hBox.getChildren().add(attendingButton);
                    hBox.getChildren().add(attendanceLabel);
                }catch(IllegalArgumentException e) {}
                currentlySelectedEvent = event;
            }catch(NullPointerException e) {
                currentEvent.setText("Events:");
                hBox.getChildren().removeAll(attendingButton, attendanceLabel);
            }
        });
        setCenter(hBox);

        //This code adds a menu with different selections to sort the event list:

        MenuButton viewSelector = new MenuButton("Filter");

        MenuItem alphabetically = new MenuItem("Show all events");
        alphabetically.setOnAction(event -> {
            listView.setItems(EventManager.getEventsAlphabetically());
            viewSelector.setText("All events");
        });

        MenuItem movies = new MenuItem("Show movies");
        movies.setOnAction(event -> {
            listView.setItems(EventManager.getEventsSorted(EventType.MOVIE));
            viewSelector.setText("Movies");
        });

        MenuItem concerts = new MenuItem("Show concerts");
        concerts.setOnAction(event -> {
            listView.setItems(EventManager.getEventsSorted(EventType.CONCERT));
            viewSelector.setText("Concerts");
        });

        MenuItem sports = new MenuItem("Show sporting events");
        sports.setOnAction(event -> {
            listView.setItems(EventManager.getEventsSorted(EventType.SPORT));
            viewSelector.setText("Sporting events");
        });

        MenuItem arts = new MenuItem("Show art events");
        arts.setOnAction(event -> {
            listView.setItems(EventManager.getEventsSorted(EventType.ART));
            viewSelector.setText("Art events");
        });

        MenuItem masonEvents = new MenuItem("Show GMU events");
        masonEvents.setOnAction(event -> {
            listView.setItems(EventManager.getEventsSorted(EventType.MASON_EVENT));
            viewSelector.setText("GMU events");
        });

        MenuItem userEvents = new MenuItem("Show events you're attending");
        userEvents.setOnAction(event -> {
            listView.setItems(getScreen().getCurrentUser().getEventsAttending());
            viewSelector.setText("Events you're attending");
        });

        MenuItem otherEvents = new MenuItem("Show all other events");
        otherEvents.setOnAction(event -> {
            listView.setItems(EventManager.getEventsSorted(EventType.OTHER));
            viewSelector.setText("All other events");
        });

        viewSelector.getItems().addAll(alphabetically, movies, concerts, sports, arts, masonEvents, otherEvents, userEvents);
        viewSelector.setAlignment(Pos.CENTER);
        viewSelector.setPrefWidth(200);
        getToolbarButtons().add(viewSelector);

        setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY))); //Change the background color

    }

    /**
     * This method gets called if the Screen holding this EventView doesn't consume the keyEvent
     * @param keyEvent the KeyEvent passed from the Screen
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) { /* UNUSED */ }

}