package engine;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static engine.EventManager.Event;

//Can't be extended
public final class EventView extends View {

    private ArrayList<Event> eventList;

    public EventView(Screen screen) {

        super(screen);

        eventList = EventManager.getEvents();
        setRight(new ListView<>(new ObservableListWrapper<>(EventManager.getEvents())));
        setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        //...

    }

}