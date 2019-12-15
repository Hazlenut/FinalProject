package engine;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebHistory;

import java.util.Optional;

//Can't be extended
public final class SettingsView extends View {

    private ListView<WebHistory.Entry> historyList; //Stores the entire history of the webView, so that a user can go to settings and get back to an unreachable page
    private String link; //Stores the link that the user selects in the history, which the SearchView will load to if the goToLinkButton is clicked

    /**
     * Creates a new SettingsView with
     * @param screen The Screen instance which holds this SettingsView
     */
    public SettingsView(Screen screen) {

        super(screen);

        setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY))); //Add background color
        historyList = new ListView<>(getScreen().getSearchView().getHistory()); //The historyList will reference the history of the SearchView
        historyList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<WebHistory.Entry>) c -> {
            try {
                link = historyList.getSelectionModel().getSelectedItems().get(0).getUrl(); //Set link to currently selected Entry
            }catch(NullPointerException e) {}
        });
        Button goToLinkButton = new Button("Go to selected Link", historyList);
        goToLinkButton.setOnAction(event -> {
            if(link != null) {
                getScreen().getSearchView().goTo(link); //Set SearchView to this link
                link = null; //Reset link
                getScreen().switchView(ViewType.SEARCH_VIEW); //Go to SearchView
                historyList.getSelectionModel().clearSelection(); //Clear selection
            }
        });
        setCenter(goToLinkButton);
        Label currentUser = new Label();
        currentUser.setPadding(new Insets(25, 100, 0, 100));
        currentUser.setAlignment(Pos.CENTER);
        currentUser.setFont(new Font(30));
        getScreen().getCurrentUserName().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                currentUser.setText("Current User: " + newValue);
            }
        });
        setTop(currentUser);

        Button deleteUserButton = new Button("Delete account");
        deleteUserButton.setOnAction(event -> {
            Optional<ButtonType> result = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete your account?", ButtonType.YES, ButtonType.NO).showAndWait();
            if(result.get() == ButtonType.YES) {
                UserManager.removeUser(getScreen().setCurrentUser(null));
            }
        });
        getToolbarButtons().add(deleteUserButton);

    }

    /**
     * This method gets called if the Screen holding this SettingsView doesn't consume the keyEvent
     * @param keyEvent the KeyEvent passed from the Screen
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) { /* UNUSED */ }

}