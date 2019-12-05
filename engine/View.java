package engine;

import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public abstract class View {

    private Screen screen;
    private BorderPane borderPane;
    private String name;

    public View(String name, Screen screen) {

        this.screen = screen;
        borderPane = new BorderPane();
        this.name = name;

    }

    public BorderPane getBorderPane() {

        return borderPane;
    }

    public String getName() {

        return name;
    }

    public Screen getScreen() {

        return screen;
    }

    public abstract void keyPressListener(KeyEvent keyEvent);

    public abstract void keyReleaseListener(KeyEvent keyEvent);

    public abstract Button[] getToolBarButtons();

}