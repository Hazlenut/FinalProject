package engine;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {

        launch(args);

    }

    /**
     * This method starts the program, and initializes the GUI , user system, and events
     */
    @Override
    public void start(Stage primaryStage) {

        if(getParameters().getUnnamed().size() > 0) {
            UserManager.initialize(getParameters().getRaw().get(0));
            EventManager.initialize(getParameters().getRaw().get(0));
        }else{
            UserManager.initialize("");
            EventManager.initialize("");
        }
        Rectangle2D screenSize = javafx.stage.Screen.getPrimary().getVisualBounds();
        Screen screen = new Screen((0.37 * screenSize.getWidth()), (0.45 * screenSize.getHeight()));
        primaryStage.setScene(screen);
        primaryStage.setTitle("Event Organizer");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.show();

    }

    /**
     * This method saves the user & event data before shutting down the program
     */
    @Override
    public void stop() throws Exception {

        EventManager.save();
        UserManager.save();
        super.stop();

    }

}