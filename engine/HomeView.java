package engine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class HomeView extends View {

	private Label label;
	private Button enterButton, newUserButton;
	private TextField textField;

    public HomeView(String name, Screen screen) {

        super(name, screen);
        
        boolean usersExist = User.userFileExists();
        String text = usersExist ? "Welcome to Event Organizer - please enter your User ID" : "Welcome to Event Organizer";
        label = new Label(text);
        label.setStyle("-fx-font: 24 arial;");
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        VBox vBox = new VBox();
        textField = new TextField();
        textField.setPrefWidth(500);
        text = usersExist ? "Enter" : "Continue";
        enterButton = new Button(text);
        enterButton.setOnAction(event -> {
            if(usersExist) {
                checkTextField();
            }else{
                getScreen().logIn();
            }
        });
        newUserButton = new Button("New User?");
        newUserButton.setOnAction(event -> {
            Main.setNewUser(true);
            getScreen().logIn();
        });
        vBox.getChildren().addAll(label);
        if(usersExist) {
            vBox.getChildren().addAll(textField, newUserButton);
        }
        vBox.setSpacing(25);
        vBox.getChildren().add(enterButton);
        Main.setNewUser(!usersExist);
        vBox.setAlignment(Pos.CENTER);
        getBorderPane().setCenter(vBox);
        getBorderPane().setBackground(new Background(new BackgroundFill(Color.LAVENDERBLUSH, CornerRadii.EMPTY, Insets.EMPTY)));
		
        //BorderPane.setAlignment(vBox, Pos.CENTER);

    }

    private void checkTextField() {

        String text = textField.getText();
        if(!text.replaceAll(" ", "").equals("") && User.validateUserID(text)) {
            Main.setCurrentUserID(text);
            getScreen().logIn();
        }

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