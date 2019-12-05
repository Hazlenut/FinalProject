package engine;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

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

        //Initialization graphics code...

    }

    public void draw() {

        graphics = canvas.getGraphicsContext2D();
        graphics.clearRect(0, 0, getWidth(), getHeight());

        graphics.setStroke(Color.RED);
        graphics.fillRect(getWidthPercent(0.1), getHeightPercent(0.1), getWidthPercent(0.8), getHeightPercent(0.8));

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
            case SHIFT:
                for(Event event : eventList) {
                    System.out.println(event);
                }
                break;
            default:
                //Code...
                break;

        }

    }

    private double getWidthPercent(double perecent) {

        return (perecent * getWidth());
    }

    private double getHeightPercent(double percent) {

        return (percent * getHeight());
    }

    public double getWidth() {

        return canvas.getWidth();
    }

    public double getHeight() {

        return canvas.getHeight();
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