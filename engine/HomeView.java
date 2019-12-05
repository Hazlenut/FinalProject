package engine;

import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class HomeView extends View {

	private Label label;
	private Button enterButton;
	private TextField textField;

    public HomeView(String name, Screen screen) {

        super(name, screen);

        label = new Label("Welcome to Event Organizer - please enter your User ID");
        VBox vBox = new VBox();
        textField = new TextField();
        enterButton = new Button("Enter");
        enterButton.setOnAction(event -> {
            if(checkTextField()) {
                //Code...
            }
        });
        vBox.getChildren().add(label);
        vBox.getChildren().add(textField);
        vBox.getChildren().add(enterButton);
        getBorderPane().setCenter(vBox);

    }

    private boolean checkTextField() {

        return false;
    }

    @Override
    public Button[] getToolBarButtons() {

        return new Button[0]; //HomeView doesn't add buttons
    }

	@Override
	public void keyListener(KeyEvent keyEvent) {

        switch(keyEvent.getCode()) {

            default:
                //Code...
                break;

        }

	}

}