package engine;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {
    
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

        primaryStage.setTitle(Attributes.getAttribute("Title"));
        EventLibrary.addEventsToLibrary();
        Rectangle2D screen = javafx.stage.Screen.getPrimary().getVisualBounds();
        Screen mainView = new Screen(new Dimension((int)(0.35 * screen.getWidth()), (int)(0.5 * screen.getHeight())), Screen.SEARCH_VIEW);
        primaryStage.setScene(mainView.getScene());
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setOnCloseRequest(event -> saveOperations());
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            mainView.updateEventViews();
        });
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            mainView.updateEventViews();
        });
        primaryStage.show();

    }

    public static void saveOperations() {
        
        Attributes.save();
        //Save EventLibrary...
        
    }
    
    public static String getDirectory() {
        
        return directory;
    }
    
}