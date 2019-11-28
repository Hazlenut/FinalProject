import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("Event Organizer");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane, 0.4 * screenSize.getWidth(), 0.55 * screenSize.getHeight());
        TitledPane titledPane = new TitledPane();
        titledPane.setText("Web Browser");
        titledPane.setCollapsible(true);
        WebView view = new WebView();
        view.getEngine().load("https://www.google.com");
        titledPane.setContent(view);
        pane.setTop(titledPane);
        Button button1 = new Button("Button 1"), button2 = new Button("Button 2");
        button1.setOnAction(event -> {
            System.out.println("Button 1 Pressed");
            //Code here...
        });
        button2.setOnAction(event -> {
            System.out.println("Button 2 Pressed");
            //Code here...
        });
        
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().add(button1);
        buttonBar.getButtons().add(button2);
        SetEvents mv = new SetEvents();
        TextField zip = new TextField("Enter Zipcode");
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                l.setText(b.getText()); 
            } 
        }; 
        
        ArrayList<Button> buttons = mv.getMovies();
        pane.setBottom(buttonBar);
        primaryStage.setScene(scene);
        primaryStage.show();

//        ArrayList<String> list = new ArrayList<>();
//        String html = org.jsoup.Jsoup.connect("https://www.stubhub.com/concert-tickets/category/1/").get().html();
//        Document document = org.jsoup.Jsoup.parse(html);
//        Elements div = document.select("PerformerCard__Details__name");
//        for (Element d: div) {
//            list.add(d.ownText());
//        }
//        for(String s: list) {
//            System.out.println(s);
//        }

    }

    public static void main(String[] args) {

        launch(args);

    }

}