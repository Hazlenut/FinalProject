package engine;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;

public class EventView extends View {

    private ArrayList<Event> eventList;
    private Canvas canvas;
    private GraphicsContext graphics;

    public EventView(String name, Screen screen, Dimension size) {

        super(name, screen);

        eventList = new ArrayList<>();
        eventList.addAll(EventLibrary.getEvents());
        canvas = new Canvas(size.getWidth(), size.getHeight());
        getBorderPane().setCenter(canvas);
        graphics = canvas.getGraphicsContext2D();
        initialize();

    }

    @Override
    public Button[] getToolBarButtons() {

        //Code...

        return new Button[0];
    }

    private void initialize() {

        //Initialization code...

    }

    public void draw() {

        double i = 0;
        for(Event event : eventList) {
            graphics.setFont(new Font(15));
            graphics.fillText(event.toString(), (0.12 * canvas.getWidth()), i);
            i += graphics.getFont().getSize() * 1.12;
        }

    }

    @Override
    public void keyListener(KeyEvent keyEvent) {

        switch(keyEvent.getCode()) {

            case UP:
                //...
                break;
            case DOWN:
                //...
                break;
            default:
                //Code...
                break;

        }

    }

    public void update(Dimension size) {

        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.setWidth(size.getWidth());
        canvas.setHeight(size.getHeight());
        draw();

    }

    public void matchListToEventLibrary() {

        eventList.clear();
        eventList.addAll(EventLibrary.getEvents());

    }

}