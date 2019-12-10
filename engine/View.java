package engine;

import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;

//Base class for all Views
public abstract class View extends BorderPane {

    private Screen screen; //The screen which holds a given View
    private ArrayList<Button> buttons; //Stores the buttons a View adds to the Screen's toolbar

    /**
     * @param screen The Screen instance holding this View
     */
    protected View(Screen screen) {

        this.screen = screen;
        buttons = new ArrayList<>();

    }

    /**
     * @return this View's Screen instance
     */
    protected Screen getScreen() {

        return screen;
    }

    /**
     * @return This View's Buttons (so that the Screen holding this View can add the buttons to its toolbar)
     */
    public ArrayList<Button> getToolbarButtons() {

        return buttons;
    }

    /**
     * This method is abstract because each view potentially has different responses to the same KeyEvents
     * @param keyEvent the KeyEvent passed from the Screen
     */
    protected abstract void keyPressed(KeyEvent keyEvent);

    /**
     * This enum represents the different View
     */
    public enum ViewType {

        HOME_VIEW,
        EVENT_VIEW,
        SEARCH_VIEW,
        SETTINGS_VIEW

    }

}