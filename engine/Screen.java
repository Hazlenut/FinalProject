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
    private ArrayList<View> views;
    private int currentViewIndex;

    public static final int SEARCH_VIEW = 0;
    public static final int EVENT_VIEW = 1;
    public static final int SETTINGS_VIEW = 2;
    public static final int HOME_VIEW = 3;
    private static final int VIEW_COUNT = 4;

    public Screen(Dimension size, int view) {

        if(view < 0 || view >= VIEW_COUNT) {
            view = 0;
        }

        anchor = new BorderPane();
        toolBar = new ButtonBar();
        initializeViews(size);
        anchor.setCenter(views.get(view).getBorderPane());
        toolBar.getButtons().addAll(views.get(view).getToolBarButtons());
        currentViewIndex = view;
        anchor.setTop(toolBar);
        scene = new Scene(anchor, size.getWidth(), size.getHeight());
        scene.setOnKeyPressed(this::keyListener);
        ((SearchView)views.get(SEARCH_VIEW)).getHistory().addListener((ListChangeListener<WebHistory.Entry>) c -> ((SettingsView)views.get(SETTINGS_VIEW)).updateHistoryList(getSearchViewHistory()));

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
        name = "Home";
        button = new Button(name);
        views.add(new HomeView(name, this));
        button.setOnAction(event -> switchView(HOME_VIEW));
        toolBar.getButtons().add(button);

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

        switch(keyEvent.getCode()) {
            case F1:
                Main.saveOperations();
                Platform.exit();
                break;
            case F2:
            	switchView(HOME_VIEW);
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

        for(View view : views) {
            if(view instanceof EventView) {
                ((EventView)view).update(new Dimension((int)scene.getWidth(), (int)scene.getHeight()));
            }
        }

    }

    public Scene getScene() {

        return scene;
    }

}