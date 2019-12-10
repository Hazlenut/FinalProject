package engine;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {

        if(getParameters().getUnnamed().size() > 0) {
            UserManager.initialize(getParameters().getRaw().get(0));
        }else{
            UserManager.initialize("");
        }
        new Thread(() -> {
            try {
                EventManager.initialize();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }).start();
        Rectangle2D screenSize = javafx.stage.Screen.getPrimary().getVisualBounds();
        Screen screen = new Screen((0.37 * screenSize.getWidth()), (0.45 * screenSize.getHeight()));
        primaryStage.setScene(screen);
        primaryStage.setTitle("Event Organizer");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {

        UserManager.save();
        super.stop();

    }

}