package engine;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {
    
    private static String directory, currentUserID;
    private static boolean isEncrypted, newUser;
    
    public static void main(String[] args) {

        if(args.length > 0) {
           directory = args[0];
        }else{
           directory = "";
        }
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {

        isEncrypted = Attributes.getAttribute("Encrypted").equalsIgnoreCase("Yes");
        User.initialize();
        primaryStage.setTitle(Attributes.getAttribute("Title"));
        Rectangle2D screen = javafx.stage.Screen.getPrimary().getVisualBounds();
        Screen mainView = new Screen(new Dimension((int)(0.35 * screen.getWidth()), (int)(0.5 * screen.getHeight())));
        primaryStage.setScene(mainView.getScene());
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setOnCloseRequest(event -> {
            if(newUser && (mainView.getCurrentViewIndex() != Screen.HOME_VIEW)) {
                setCurrentUserID(User.generateID());
                new Alert(Alert.AlertType.INFORMATION, currentUserID).show();
                User.addUser(currentUserID);
            }else{
            }
            saveOperations();
        });
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> mainView.updateEventViews());
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> mainView.updateEventViews());
        primaryStage.show();

    }

    public static void setCurrentUserID(String userID) {

        currentUserID = userID;

    }

    public static void setNewUser(boolean newUser) {

        Main.newUser = newUser;

    }

    public static void saveOperations() {

        Attributes.save();
        EventLibrary.save(currentUserID);
        User.save(isEncrypted);
        
    }

    public static void setIsEncrypted(boolean isEncrypted) {

        if(Main.isEncrypted) {
            Attributes.updateAttribute("Encrypted", "Yes", "No");
        }else{
            Attributes.updateAttribute("Encrypted", "No", "Yes");
        }

        Main.isEncrypted = isEncrypted;

    }

    public static String getDirectory() {
        
        return directory;
    }
    
}