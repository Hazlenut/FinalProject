package engine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.animation.RotateTransition; 

public class HomeView extends View {

	private Label label;
	private Button enterButton, newUserButton;
	private TextField textField;
	private boolean usersExist;

    public HomeView(String name, Screen screen) {

        super(name, screen);
        
        usersExist = User.userFileExists();
        String text = usersExist ? "Welcome to Event Organizer - please enter your User ID" : "Welcome to Event Organizer";
        label = new Label(text);
        label.setStyle("-fx-font: 24 arial;");
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        VBox vBox = new VBox();
        textField = new TextField();
        textField.setOnKeyPressed(this::keyListener);
        textField.setPrefWidth(500);
        text = usersExist ? "Enter" : "Continue";
        enterButton = new Button(text);
        enterButton.setOnAction(event -> enter());
        newUserButton = new Button("New User?");
        newUserButton.setOnAction(event -> {
            User.registerNewUser();
            getScreen().logIn();
        });
        vBox.getChildren().addAll(label);
        if(usersExist) {
            vBox.getChildren().addAll(textField, newUserButton);
        }
        vBox.setSpacing(25);
        vBox.getChildren().add(enterButton);
        vBox.setAlignment(Pos.CENTER);
        getBorderPane().setCenter(vBox);
        getBorderPane().setBackground(new Background(new BackgroundFill(Color.LAVENDERBLUSH, CornerRadii.EMPTY, Insets.EMPTY)));

    }

    private void enter() {

        if(usersExist) {
            checkTextField();
        }else{
            User.registerNewUser();
            getScreen().logIn();
        }

    }

    private void checkTextField() {

        String text = textField.getText();
        if(!text.replaceAll(" ", "").equals("")) {
            if(User.validateUserID(text)) {
                User.logUserIn(text);
                getScreen().logIn();
            }else{
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("No User ID Found");
                a.show();
            }
        }

    }

    @Override
    public Button[] getToolBarButtons() {

        return new Button[0]; //HomeView doesn't add buttons
    }

	@Override
	public void keyListener(KeyEvent keyEvent) {

        switch(keyEvent.getCode()) {

            case ENTER:
                if(usersExist) {
                    checkTextField();
                }else{
                    User.registerNewUser();
                    getScreen().logIn();
                }
                break;
            case F1:
                Screen.shutdown();
                break;

        }

	}

}