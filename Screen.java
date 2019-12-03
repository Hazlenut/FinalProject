package engine;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public final class Screen {

    private Scene scene;
    private BorderPane anchor;
    private ButtonBar toolBar;
    private ArrayList<View> viewPanes;

    private static final int SEARCH_VIEW = 0;
    private static final int EVENT_CALENDAR = 1;
    private static final int SETTINGS = 2;

    public Screen(Dimension size) {

        anchor = new BorderPane();
        toolBar = new ButtonBar();
        initializeViews();
        anchor.setCenter(viewPanes.get(SEARCH_VIEW).getBorderPane());
        anchor.setTop(toolBar);
        scene = new Scene(anchor, size.getWidth(), size.getHeight());
        scene.setOnKeyPressed(this::keyListener);

    }

    private void initializeViews() {

        viewPanes = new ArrayList<>();

        String name = "Search View";
        viewPanes.add(new SearchView(name));
        Button button = new Button(name);
        button.setOnAction(event -> switchView(SEARCH_VIEW));
        toolBar.getButtons().add(button);
        name = "Event Calendar";
        button = new Button(name);
        viewPanes.add(new EventView(name));
        button.setOnAction(event -> switchView(EVENT_CALENDAR));
        toolBar.getButtons().add(button);
        name = "Settings";
        button = new Button(name);
        viewPanes.add(new SettingsView(name));
        button.setOnAction(event -> switchView(SETTINGS));
        toolBar.getButtons().add(button);

    }

    private void switchView(int viewIndex) {

        anchor.setCenter(viewPanes.get(viewIndex).getBorderPane());

    }

    private void keyListener(KeyEvent keyEvent) {

        Stage stage = (Stage)scene.getWindow();

        switch(keyEvent.getCode()) {
            case F1:
                Main.exitOperations();
                Platform.exit();
                break;
            case F4:
                ((SearchView)viewPanes.get(SEARCH_VIEW)).goToHome();
                break;
            case F5:
                Event.addLink(((SearchView)viewPanes.get(SEARCH_VIEW)).getCurrentURL());
                EventLibrary.updateEvents();
                ((EventView)viewPanes.get(EVENT_CALENDAR)).matchListToEventLibrary();
                break;
            case F9:
                switchView(SEARCH_VIEW);
                break;
            case F10:
                switchView(EVENT_CALENDAR);
                break;
            case F11:
                if(stage.isFullScreen()) {
                    stage.setFullScreen(false);
                }else{
                    stage.setFullScreen(true);
                }
                break;
            case F12:
                stage.setIconified(true);
                break;
        }

    }

    public Scene getScene() {

        return scene;
    }

}