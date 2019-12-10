package engine;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public abstract class View extends BorderPane {

    private Screen screen;
    private ButtonBar buttonBar;

    protected View(Screen screen) {

        this.screen = screen;
        buttonBar = new ButtonBar();
        Button logOutButton = new Button("Log out");
        ButtonBar.setButtonData(logOutButton, ButtonBar.ButtonData.LEFT);
        logOutButton.setOnAction(event -> getScreen().setCurrentUser(null));
        MenuButton viewSelector = new MenuButton("Switch to");
        MenuItem eventViewItem = new MenuItem("Events");
        eventViewItem.setOnAction(event -> getScreen().switchView(ViewType.EVENT_VIEW));
        MenuItem searchViewItem = new MenuItem("Web Browser");
        searchViewItem.setOnAction(event -> getScreen().switchView(ViewType.SEARCH_VIEW));
        MenuItem settingViewItem = new MenuItem("Settings");
        settingViewItem.setOnAction(event -> getScreen().switchView(ViewType.SETTINGS_VIEW));
        viewSelector.getItems().addAll(eventViewItem, searchViewItem, settingViewItem);
        viewSelector.setAlignment(Pos.TOP_RIGHT);
        buttonBar.getButtons().addAll(logOutButton, viewSelector);
        setTop(buttonBar);

    }

    protected Screen getScreen() {

        return screen;
    }

    protected ButtonBar getButtonBar() {

        return buttonBar;
    }

    protected abstract void keyPressed(KeyEvent keyEvent);

    public enum ViewType {

        HOME_VIEW,
        EVENT_VIEW,
        SEARCH_VIEW,
        SETTINGS_VIEW

    }

}