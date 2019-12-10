package engine;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import static engine.View.ViewType.*;
import static engine.UserManager.User;

public final class Screen extends Scene {

    private HomeView homeView;
    private EventView eventView;
    private SearchView searchView;
    private SettingsView settingsView;
    private User currentUser;
    private SimpleStringProperty userInfo;

    public Screen(double width, double height) {

        super(new Group(), width, height);

        homeView = new HomeView(this);
        setRoot(homeView);
        currentUser = null;
        userInfo = new SimpleStringProperty();
        initializeViews();
        setOnKeyPressed(this::keyPressed);

    }

    private void initializeViews() {

        eventView = new EventView(this);
        searchView = new SearchView(this);
        settingsView = new SettingsView(this);

    }

    protected Stage getStage() {

        return ((Stage)getWindow());
    }

    protected EventView getEventView() {

        return eventView;
    }

    public SearchView getSearchView() {

        return searchView;
    }

    public SettingsView getSettingsView() {

        return settingsView;
    }

    public View getCurrentView() {

        return (View)getRoot();
    }

    public User getCurrentUser() {

        return currentUser;
    }

    public SimpleStringProperty getUserInfo() {

        return userInfo;
    }

    public void setCurrentUser(User currentUser) {

        this.currentUser = currentUser;
        if(currentUser == null) {
            switchView(HOME_VIEW);
        }else{
            switchView(EVENT_VIEW);
        }

    }

    public void switchView(View.ViewType viewType) {

        switch(viewType) {
            case HOME_VIEW:
                setRoot(homeView);
                break;
            case EVENT_VIEW:
                setRoot(eventView);
                break;
            case SEARCH_VIEW:
                setRoot(searchView);
                break;
            case SETTINGS_VIEW:
                setRoot(settingsView);
                break;
        }

    }

    private void keyPressed(KeyEvent keyEvent) {

        switch(keyEvent.getCode()) {
            case F1:
                Platform.exit();
                break;
            case F9:
                switchView(SEARCH_VIEW);
                break;
            case F10:
                switchView(EVENT_VIEW);
                break;
            case F11:
                getStage().setFullScreen(!getStage().isFullScreen());
                break;
            case F12:
                getStage().setIconified(true);
                break;
            default:
                getCurrentView().keyPressed(keyEvent);
                break;
        }

    }

}