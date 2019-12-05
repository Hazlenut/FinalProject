package engine;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;

public class EventView extends View {

    private ArrayList<Event> eventList;
    private int currentEventIndex;
    private Dimension size;
    private Rectangle rectangle, rectangle2;
    private Canvas canvas;
    private GraphicsContext graphics;
    private TranslateTransition transition, transition2;
    private boolean translating;
    private Button[] buttons;

    public EventView(String name, Screen screen, Dimension size) {

        super(name, screen);

        eventList = new ArrayList<>();
        currentEventIndex = 0;
        this.size = size;
        Button attendingEvent = new Button("Attending Event");
        attendingEvent.setOnAction(event -> attendingEvent());
        Button notAttendingEvent = new Button("Not Attending Event");
        notAttendingEvent.setOnAction(event -> notAttendingEvent());
        Button nextEvent = new Button("Next Event");
        nextEvent.setOnAction(event -> translate());
        buttons = new Button[] {attendingEvent, notAttendingEvent, nextEvent};
        rectangle = new Rectangle();
        rectangle2 = new Rectangle();
        rectangle.setFill(Color.LIGHTGOLDENRODYELLOW);
        rectangle2.setFill(Color.LIGHTGOLDENRODYELLOW);
        getBorderPane().setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));
        StackPane stackPane = new StackPane();
        canvas = new Canvas(size.getWidth(), size.getHeight());
        stackPane.getChildren().addAll(rectangle, rectangle2, canvas);
        getBorderPane().setCenter(stackPane);
        translating = false;
        getBorderPane().requestFocus();

    }

    public void attendingEvent() {

        User.getCurrentUser().getEventsAttending().add(eventList.get(currentEventIndex));
        eventList.get(currentEventIndex-1).setPeople(eventList.get(currentEventIndex-1).getPeople() +1);
        //graphics.fillText(eventList.get(currentEventIndex).toString() + "\n Number of people attending: " + eventList.get(currentEventIndex).getPeople(), (getWidth() / 3.3), (getHeight() / 2));


    }

    public void notAttendingEvent() {

        User.getCurrentUser().getEventsAttending().remove(eventList.get(currentEventIndex));
        eventList.get(currentEventIndex-1).setPeople(eventList.get(currentEventIndex=1).getPeople() -1);

    }

    @Override
    public Button[] getToolBarButtons() {

        return buttons;
    }

    public void draw() {

        rectangle.setWidth(getWidthPercent(0.5));
        rectangle.setHeight(getHeightPercent(0.5));
        rectangle2.setWidth(getWidthPercent(0.5));
        rectangle2.setHeight(getHeightPercent(0.5));
        graphics = canvas.getGraphicsContext2D();
        graphics.setEffect(new Lighting(new Light.Distant()));
        graphics.setFont(new Font(20));
        graphics.setFill(Color.DARKSALMON);
        graphics.setEffect(new Lighting(new Light.Distant()));
        //graphics.fillText("Number of People attending: " + eventList.get(currentEventIndex).getPeople(),getWidth()/5, getHeight()/2.2);
        graphics.fillText(eventList.get(currentEventIndex).toString() + "\n Number of people attending: " + eventList.get(currentEventIndex).getPeople(), (getWidth() / 3.3), (getHeight() / 2));

    }

    @Override
    public void keyListener(KeyEvent keyEvent) {

        switch(keyEvent.getCode()) {

            case F5:
                translate();
                break;
            case F6:
                attendingEvent();
                break;
            case F7:
                notAttendingEvent();
                break;
            case SHIFT:
                for(Event event : EventLibrary.getEvents()) {
                    System.out.println(event);
                }
                System.out.println("- - - - - - -");
                for(Event event : eventList) {
                    System.out.println(event);
                }
                break;

        }

    }

    private void setShapes() {

        ((StackPane)getBorderPane().getCenter()).getChildren().removeAll(rectangle, rectangle2);
        rectangle = new Rectangle();
        rectangle2 = new Rectangle();
        canvas = new Canvas(size.getWidth(), size.getHeight());
        ((StackPane)getBorderPane().getCenter()).getChildren().addAll(rectangle, rectangle2, canvas);
        rectangle.setWidth(getWidthPercent(0.5));
        rectangle.setHeight(getHeightPercent(0.5));
        rectangle2.setWidth(getWidthPercent(0.5));
        rectangle2.setHeight(getHeightPercent(0.5));
        rectangle.setFill(Color.LIGHTGOLDENRODYELLOW);
        rectangle2.setFill(Color.LIGHTGOLDENRODYELLOW);

    }

    private void translate() {

        if(!translating && currentEventIndex < (eventList.size() -1)) {
            translating = true;
            ((StackPane)getBorderPane().getCenter()).getChildren().removeAll(canvas);
            canvas = new Canvas();
            draw();
            rectangle.setEffect(new MotionBlur(24, 356));
            rectangle2.setEffect(new MotionBlur(24, 356));
            transition = new TranslateTransition(Duration.millis(1000), rectangle);
            transition.setByY(rectangle.getY() + getHeight());
            transition2 = new TranslateTransition(Duration.millis(1000), rectangle2);
            transition2.setByY(rectangle.getY() - getHeight());
            transition.setOnFinished(event -> {
                setShapes();
                translating = false;
                draw();
                currentEventIndex++;
            });
            transition.play();
            transition2.play();
        }

    }

    private double getWidthPercent(double percent) {

        return (percent * getWidth());
    }

    private double getHeightPercent(double percent) {

        return (percent * getHeight());
    }

    public double getWidth() {

        return size.getWidth();
    }

    public double getHeight() {

        return size.getHeight();
    }

    public void update(Dimension size) {

        this.size = size;
        draw();

    }

    public void matchListToEventLibrary() {

        eventList.clear();
        eventList.addAll(EventLibrary.getEvents());

    }

}