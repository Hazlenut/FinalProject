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

    private TextField textField; //userNames are entered here
    private PasswordField passwordField; //passwords are entered here
    private boolean newUser; //flag which keeps track of whether a user is logging in or creating a new account

    /**
     * Creates a new HomeView which includes the User log in GUI
     * @param screen The Screen instance which holds this HomeView
     */
    public HomeView(Screen screen) {

        super(screen);

        Label heading = new Label("Welcome to Event Organizer"); //Add a heading Label
        heading.setStyle("-fx-font: 24 arial;"); //Style the heading
        heading.setWrapText(true);
        heading.setTextAlignment(TextAlignment.CENTER);
        //Create / align the textField & passwordField:
        textField = new TextField();
        textField.setAlignment(Pos.CENTER);
        passwordField = new PasswordField();
        passwordField.setAlignment(Pos.CENTER);
        Button enterButton = new Button("Log in"); //Log in button
        enterButton.setOnAction(event -> attemptEntry());
        CheckBox newUserCheckBox = new CheckBox("New User?"); //This checkbox sets the newUser flag
        newUserCheckBox.setOnAction(event -> {
            newUser = newUserCheckBox.isSelected();
            if(newUser) {
                enterButton.setText("Create account");
            }else{
                enterButton.setText("Log in");
            }
        });
        //Add more Labels:
        HBox userNameNodes = new HBox(14, new Label("Username:"), textField);
        userNameNodes.setAlignment(Pos.CENTER);
        HBox passwordNodes = new HBox(14, new Label("Password: "), passwordField);
        passwordNodes.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(25, heading, userNameNodes, passwordNodes, newUserCheckBox, enterButton);
        vBox.setAlignment(Pos.CENTER);
        setCenter(vBox);
        setBackground(new Background(new BackgroundFill(Color.LAVENDERBLUSH, CornerRadii.EMPTY, Insets.EMPTY))); //Change the background color

    }

    /**
     * This method contains the user log in logic:
     * - If a user already has that userName, reject entry
     * - If a user doesn't have that userName, accept
     * - new users can be create as long as there isn't a userName conflict
     */
    private void attemptEntry() {

        String username = textField.getText().replaceAll(" ", ""); //Get userName from textField
        String password = passwordField.getText().replaceAll(" ", ""); //Get password from passwordField
        if(!username.equals("") && !password.equals("")) { //Ensure both fields have some non-empty String
            User user;
            if(newUser) {
                user = UserManager.registerNewUser(username, password); //Attempt to register a new User (this returns null if that userName is taken)
            }else{
                user = UserManager.getUser(username, password); //Attempt to log in the user (this returns null if that userName and password combo is incorrect)
            }
            if(user != null) { //Log in the user if the above code successfully retrieves a non-null user
                getScreen().setCurrentUser(user);
                //Then clear the textField and passwordField:
                return;
            }
        }
        //Add effects to make it obvious that the log in failed:
        textField.setEffect(new DropShadow(30, Color.ORANGE));
        passwordField.setEffect(new DropShadow(30, Color.ORANGE));
        //Add tooltips to give the user helpful hints:
        if(newUser) {
            textField.setTooltip(new Tooltip("Username is already in use"));
            passwordField.setTooltip(null);
        }else{
            textField.setTooltip(new Tooltip("Invalid username or password"));
            passwordField.setTooltip(new Tooltip("Invalid username or password"));
        }

    }

    /**
     * This method gets called if the Screen holding this HomeView doesn't consume the keyEvent
     * @param keyEvent the KeyEvent passed from the Screen
     */
	@Override
	public void keyPressed(KeyEvent keyEvent) {

        //...

	}

    /**
     * This method resets the textField and passwordField
     */
	public void reset() {

        textField.setText("");
        passwordField.setText("");
        textField.setEffect(null);
        passwordField.setEffect(null);
        textField.setTooltip(null);
        passwordField.setTooltip(null);

    }

}