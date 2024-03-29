package engine;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static engine.View.ViewType.*;
import static engine.UserManager.User;

public final class Screen extends Scene {

    private static final View.ViewType DEFAULT_VIEW_ON_LOGIN = EVENT_VIEW;

    private ButtonBar toolBar;
    private HomeView homeView;
    private EventView eventView;
    private SearchView searchView;
    private SettingsView settingsView;
    private User currentUser;
    private SimpleStringProperty currentUserName;
    private Button logOutButton;
    private MenuButton viewSelector;

    /**
     * Creates a new Screen with the default Views loaded in
     * @param width The width that this Screen should take up on the User's computer screen
     * @param height The height that this Screen should take up on the User's computer screen
     */
    public Screen(double width, double height) {

        super(new BorderPane(), width, height);

        //This code adds the GUI objects to the screen:

        homeView = new HomeView(this);
        getBorderPane().setCenter(homeView);
        toolBar = new ButtonBar();
        logOutButton = new Button("Log out");
        ButtonBar.setButtonData(logOutButton, ButtonBar.ButtonData.LEFT);
        logOutButton.setOnAction(event -> setCurrentUser(null));
        viewSelector = new MenuButton("Switch to");
        MenuItem eventViewItem = new MenuItem("Events");
        eventViewItem.setOnAction(event -> switchView(View.ViewType.EVENT_VIEW));
        MenuItem searchViewItem = new MenuItem("Web Browser");
        searchViewItem.setOnAction(event -> switchView(View.ViewType.SEARCH_VIEW));
        MenuItem settingViewItem = new MenuItem("Settings");
        settingViewItem.setOnAction(event -> switchView(View.ViewType.SETTINGS_VIEW));
        viewSelector.getItems().addAll(eventViewItem, searchViewItem, settingViewItem);
        ButtonBar.setButtonData(viewSelector, ButtonBar.ButtonData.RIGHT);
        toolBar.getButtons().addAll(logOutButton, viewSelector);
        currentUser = null;

        //Add an observable string so that the settingsView can change it's label to the current user's username:
        currentUserName = new SimpleStringProperty();

        //Initialize Views:
        initializeViews(false);

        setOnKeyPressed(this::keyPressed);

    }

    /**
     * Initializes the Views that this Screen holds; the Views must ALWAYS
     * be initialized in this order
     */
    private void initializeViews(boolean reset) {

        if(reset) {
            homeView.reset();
        }
        eventView = new EventView(this);
        searchView = new SearchView(this);
        settingsView = new SettingsView(this);

    }

    /**
     * @return The BorderPane which is this Screen's root
     */
    private BorderPane getBorderPane() {

        return (BorderPane)getRoot();
    }

    /**
     * @return The Stage which is this Screen's window
     */
    protected Stage getStage() {

        return ((Stage)getWindow());
    }

    /**
     * @return This Screen's SearchView
     */
    public SearchView getSearchView() {

        return searchView;
    }

    /**
     * @return The View that this Screen is currently set to
     */
    public View getCurrentView() {

        return ((View)getBorderPane().getCenter());
    }

    /**
     * @return The user currently logged in
     */
    public User getCurrentUser() {

        return currentUser;
    }

    /**
     * @return The userName of the user currently logged in
     */
    public StringProperty getCurrentUserName() {

        return currentUserName;
    }

    /**
     * This method sets the current user of the system
     * @param currentUser the user to be set as the current user
     * @return the previous currentUser
     */
    public User setCurrentUser(User currentUser) {

        User user = this.currentUser;
        this.currentUser = currentUser;
        if(currentUser == null) {
            getBorderPane().setCenter(homeView);
            getBorderPane().setTop(null);
            toolBar.getButtons().clear();
            toolBar.getButtons().addAll(logOutButton, viewSelector);
            initializeViews(true);
        }else{
            getBorderPane().setCenter(null);
            switchView(DEFAULT_VIEW_ON_LOGIN);
            getBorderPane().setTop(toolBar);
            currentUserName.set(currentUser.getUserName());
        }

        return user;
    }

    /**
     * This method switches the current view to the specified view
     * @param viewType the ViewType to switch to
     */
    public void switchView(View.ViewType viewType) {

        if(!(getCurrentView() instanceof HomeView)) {
            if(getCurrentView() != null) {
                toolBar.getButtons().removeAll(getCurrentView().getToolbarButtons());
            }
            switch(viewType) {
                case EVENT_VIEW:
                    getBorderPane().setCenter(eventView);
                    break;
                case SEARCH_VIEW:
                    getBorderPane().setCenter(searchView);
                    break;
                case SETTINGS_VIEW:
                    getBorderPane().setCenter(settingsView);
                    break;
            }
            if(getCurrentView() != null) {
                toolBar.getButtons().addAll(getCurrentView().getToolbarButtons());
            }
        }

    }

    /**
     * Default keyListener for the Screen object; it will pass on the keyEvent to the currentView if the Screen doesn't consume the KeyEvent
     */
    private void keyPressed(KeyEvent keyEvent) {

        switch(keyEvent.getCode()) {
            case F1:
                Platform.exit();
                break;
            case F4:
                getStage().setIconified(true);
                break;
            case F9:
                switchView(EVENT_VIEW);
                break;
            case F10:
                switchView(SEARCH_VIEW);
                break;
            case F11:
                switchView(SETTINGS_VIEW);
                break;
            case F12:
                getStage().setFullScreen(!getStage().isFullScreen());
                break;
            default:
                getCurrentView().keyPressed(keyEvent);
                break;
        }

    }

}