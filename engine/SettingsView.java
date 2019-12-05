package engine;

import javafx.collections.ListChangeListener;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Date;

public class SettingsView extends View {

    private Button[] buttons;

    private Label saveLabel, userIDLabel;
    private Button goToLinkButton;
    private CheckBox checkBox1;
    private ListView<String> historyList;
    private String link;

    public SettingsView(String name, Screen screen) {

        super(name, screen);

        saveLabel = new Label("Last saved at: never");
        userIDLabel = new Label();
        checkBox1 = new CheckBox("Encrypt User Data?");
        checkBox1.selectedProperty().setValue(Attributes.getAttribute("Encrypted").equalsIgnoreCase("Yes"));
        checkBox1.selectedProperty().addListener((observable, oldValue, newValue) -> User.setEncrypted(checkBox1.isSelected()));
        getBorderPane().setTop(saveLabel);
        getBorderPane().setLeft(new VBox(15, checkBox1, userIDLabel));
        historyList = new ListView<>();
        link = null;
        historyList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<String>) c -> link = historyList.getSelectionModel().getSelectedItems().get(0));
        getBorderPane().setCenter(new TitledPane("History", historyList));
        goToLinkButton = new Button("Go to selected Link");
        goToLinkButton.setOnAction(event -> getScreen().setSearchViewWindowTo(link));
        getBorderPane().setRight(goToLinkButton);
        Button saveButton = new Button("Save Now");
        saveButton.setOnAction(event -> save());
        buttons = new Button[] {saveButton};

    }

    public void setUserIDLabel(String text) {

        userIDLabel.setText("  UserID = " + text);

    }

    private void save() {

        String timeStamp = new Date().toString();
        saveLabel.setText("Last saved at: " + timeStamp.substring(0, 19) + " " + timeStamp.substring(24));
        User.save();

    }

    public void updateHistoryList(ArrayList<String> webHistory) {

        historyList.getItems().clear();
        historyList.getItems().addAll(webHistory);

    }

    @Override
    public Button[] getToolBarButtons() {

        return buttons;
    }

    @Override
    public void keyListener(KeyEvent keyEvent) {

        switch(keyEvent.getCode()) {

            case S:
                save();
                break;

        }

    }

}