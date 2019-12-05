package engine;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    private static Stage stage;
    private static Screen mainView;
    private static String directory;
    
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

        stage = primaryStage;
        User.initialize();
        stage.setTitle(Attributes.getAttribute("Title"));
        Rectangle2D screen = javafx.stage.Screen.getPrimary().getVisualBounds();
        mainView = new Screen(new Dimension((int)(0.35 * screen.getWidth()), (int)(0.5 * screen.getHeight())));
        stage.setScene(mainView.getScene());
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setOnCloseRequest(event -> Screen.shutdown());
        stage.widthProperty().addListener((observable, oldValue, newValue) -> mainView.updateEventViews());
        stage.heightProperty().addListener((observable, oldValue, newValue) -> mainView.updateEventViews());
        stage.show();

    }

    public static void exitToHomeScreen() {

        stage.setScene(null);
        mainView = new Screen(new Dimension((int)stage.getWidth(), (int)stage.getHeight()));
        stage.setScene(mainView.getScene());

    }

    public static String getDirectory() {
        
        return directory;
    }

}