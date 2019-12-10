package engine;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

//Can't be extended
public final class EventView extends View {

    /**
     * Creates a new EventView
     * @param screen The Screen instance holding this EventView
     */
    public EventView(Screen screen) {

        super(screen);

        setRight(new ListView<>(new ObservableListWrapper<>(EventManager.getEvents()))); //Add a ListView which displays the Events stored in EventManager
        setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY))); //Change the background color

    }

    /**
     * This method gets called if the Screen holding this EventView doesn't consume the keyEvent
     * @param keyEvent the KeyEvent passed from the Screen
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {

        //...

    }

}