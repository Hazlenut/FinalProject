package engine;

import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Date;

public class SettingsView extends View {

    private Button[] buttons;

    private Label saveLabel;
    private CheckBox checkBox1;
    private ListView<String> historyList;

    public SettingsView(String name, Screen screen) {

        super(name, screen);

        saveLabel = new Label("Last saved at: never");
        checkBox1 = new CheckBox("Encrypt Events File?");
        checkBox1.selectedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(checkBox1.isSelected());
        });
        getBorderPane().setTop(saveLabel);
        getBorderPane().setLeft(new HBox(15, checkBox1));
        historyList = new ListView<>();
        getBorderPane().setCenter(new TitledPane("History", historyList));

        Button saveButton = new Button("Save Now");
        saveButton.setOnAction(event -> save());
        buttons = new Button[] {saveButton};

    }

    private void save() {

        String timeStamp = new Date().toString();
        saveLabel.setText("Last saved at: " + timeStamp.substring(0, 19) + " " + timeStamp.substring(24));
        Main.saveOperations();

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
            default:
                //Code...
                break;

        }

    }

}