package engine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import static engine.UserManager.User;

//Can't be extended
public final class HomeView extends View {

    private TextField textField;
    private PasswordField passwordField;
    private boolean newUser;

    public HomeView(Screen screen) {

        super(screen);

        ((ButtonBar)getTop()).getButtons().clear();
        Label label = new Label("Welcome to Event Organizer");
        label.setStyle("-fx-font: 24 arial;");
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        textField = new TextField();
        passwordField = new PasswordField();
        Button enterButton = new Button("Enter");
        enterButton.setOnAction(event -> enter());
        CheckBox newUserCheckBox = new CheckBox("New User?");
        newUserCheckBox.setOnAction(event -> {
            newUser = newUserCheckBox.isSelected();
            if(newUser) {
                enterButton.setText("Create account");
            }else{
                enterButton.setText("Log in");
            }
        });
        HBox userNameNodes = new HBox(14, new Label("Username:"), textField);
        userNameNodes.setAlignment(Pos.CENTER);
        HBox passwordNodes = new HBox(14, new Label("Password: "), passwordField);
        passwordNodes.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(25, label, userNameNodes, passwordNodes, newUserCheckBox, enterButton);
        vBox.setAlignment(Pos.CENTER);
        setCenter(vBox);
        setBackground(new Background(new BackgroundFill(Color.LAVENDERBLUSH, CornerRadii.EMPTY, Insets.EMPTY)));

    }

    private void enter() {

        String username = textField.getText().replaceAll(" ", "");
        String password = passwordField.getText().replaceAll(" ", "");
        if(!username.equals("") && !password.equals("")) {
            User user;
            if(newUser) {
                user = UserManager.registerNewUser(username, password);
            }else{
                user = UserManager.getUser(username, password);
            }
            if(user != null) {
                getScreen().setCurrentUser(user);
                textField.setText("");
                passwordField.setText("");
                textField.setEffect(null);
                passwordField.setEffect(null);
                return;
            }
        }
        textField.setEffect(new DropShadow(30, Color.ORANGE));
        passwordField.setEffect(new DropShadow(30, Color.ORANGE));

    }

	@Override
	public void keyPressed(KeyEvent keyEvent) {

        //...

	}

}