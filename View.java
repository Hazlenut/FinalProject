package engine;

import javafx.scene.layout.BorderPane;

public abstract class View {

    private BorderPane borderPane;
    private String name;

    public View(String name) {

        borderPane = new BorderPane();
        this.name = name;

    }

    public BorderPane getBorderPane() {

        return borderPane;
    }

    public String getName() {

        return name;
    }

}