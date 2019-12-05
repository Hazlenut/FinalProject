package engine;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebHistory;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public final class Screen {

    private Scene scene;
    private BorderPane anchor;
    private ButtonBar toolBar;
    private HomeView home;
    private ArrayList<View> views;
    private int currentViewIndex;

    public static final int HOME_VIEW = -1;
    public static final int SEARCH_VIEW = 0;
    public static final int EVENT_VIEW = 1;
    public static final int SETTINGS_VIEW = 2;

    public Screen(Dimension size) {

        anchor = new BorderPane();
        toolBar = new ButtonBar();
        home = new HomeView("Home", this);
        anchor.setCenter(home.getBorderPane());
        currentViewIndex = HOME_VIEW;
        scene = new Scene(anchor, size.getWidth(), size.getHeight());
        scene.setOnKeyPressed(this::keyListener);
        initializeViews(size);

    }

    private void initializeViews(Dimension size) {

        views = new ArrayList<>();

        String name = "Event Search";
        views.add(new SearchView(name, this));
        Button button = new Button(name);
        button.setOnAction(event -> switchView(SEARCH_VIEW));
        toolBar.getButtons().add(button);
        name = "All Events";
        button = new Button(name);
        views.add(new EventView(name,this, size));
        button.setOnAction(event -> switchView(EVENT_VIEW));
        toolBar.getButtons().add(button);
        name = "Settings";
        button = new Button(name);
        views.add(new SettingsView(name, this));
        button.setOnAction(event -> switchView(SETTINGS_VIEW));
        toolBar.getButtons().add(button);
        ((SearchView)views.get(SEARCH_VIEW)).getHistory().addListener((ListChangeListener<WebHistory.Entry>) c -> ((SettingsView)views.get(SETTINGS_VIEW)).updateHistoryList(getSearchViewHistory()));

    }

    private void switchView(int viewIndex) {

        toolBar.getButtons().removeAll(views.get(currentViewIndex).getToolBarButtons());
        anchor.setCenter(views.get(viewIndex).getBorderPane());
        toolBar.getButtons().addAll(views.get(viewIndex).getToolBarButtons());
        currentViewIndex = viewIndex;
        updateEventViews();

    }

    private Stage getStage() {

        return (Stage)scene.getWindow();
    }

    private void keyListener(KeyEvent keyEvent) {

        if(currentViewIndex != HOME_VIEW) {
            switch(keyEvent.getCode()) {
                case F1:
                    Main.saveOperations();
                    Platform.exit();
                    break;
                case F5:
                    getEventsFromSearchView();
                    break;
                case F8:
                    if(getStage().isFullScreen()) {
                        getStage().setFullScreen(false);
                    }
                    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
                    getStage().setWidth(0.25 * size.getWidth());
                    getStage().setHeight(0.34 * size.getHeight());
                    getStage().centerOnScreen();
                    break;
                case F9:
                    switchView(SEARCH_VIEW);
                    break;
                case F10:
                    switchView(EVENT_VIEW);
                    break;
                case F11:
                    if(getStage().isFullScreen()) {
                        getStage().setFullScreen(false);
                    }else{
                        getStage().setFullScreen(true);
                    }
                    break;
                case F12:
                    getStage().setIconified(true);
                    break;
                default:
                    views.get(currentViewIndex).keyListener(keyEvent);
                    break;
            }
        }else{
            home.keyListener(keyEvent);
        }
        updateEventViews();

    }

    public void logIn() {

        currentViewIndex = SEARCH_VIEW;
        anchor.setCenter(views.get(currentViewIndex).getBorderPane());
        toolBar.getButtons().addAll(views.get(currentViewIndex).getToolBarButtons());
        anchor.setTop(toolBar);
        EventLibrary.addEventsToLibrary();
        ((EventView)views.get(EVENT_VIEW)).matchListToEventLibrary();
        updateEventViews();

    }

    public ArrayList<String> getSearchViewHistory() {

        ArrayList<String> list = new ArrayList<>();
        for(WebHistory.Entry entry : ((SearchView)views.get(SEARCH_VIEW)).getHistory()) {
            list.add(entry.getUrl());
        }

        return list;
    }

    public void getEventsFromSearchView() {

        if(Event.addLink(((SearchView)views.get(SEARCH_VIEW)).getCurrentURL())){
            EventLibrary.updateEvents();
            ((EventView) views.get(EVENT_VIEW)).matchListToEventLibrary();
        }

    }

    public void updateEventViews() {

        if(currentViewIndex != HOME_VIEW) {
            for(View view : views) {
                if(view instanceof EventView) {
                    ((EventView)view).update(new Dimension((int)scene.getWidth(), (int)scene.getHeight()));
                }
            }
        }

    }

    public void setSearchViewWindowTo(String url) {

        if(url != null) {
            ((SearchView)views.get(SEARCH_VIEW)).goTo(url);
            switchView(SEARCH_VIEW);
        }

    }

    public int getCurrentViewIndex() {

        return currentViewIndex;
    }

    public Scene getScene() {

        return scene;
    }

}