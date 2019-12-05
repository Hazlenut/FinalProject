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
    private int currentEventIndex, eventTypeIndex, eventTypeTotal;
    private Dimension size;
    private Rectangle rectangle, rectangle2;
    private Canvas canvas;
    private GraphicsContext graphics;
    private TranslateTransition transition, transition2;
    private boolean translating, shiftPressed;
    private Button[] buttons;

    public EventView(String name, Screen screen, Dimension size) {

        super(name, screen);

        eventList = new ArrayList<>();
        currentEventIndex = 0;
        eventTypeIndex = 0;
        eventTypeTotal = (EventType.values().length - 1);
        this.size = size;
        Button attendingEvent = new Button("Attending Event");
        attendingEvent.setOnAction(event -> attendingEvent());
        Button notAttendingEvent = new Button("Not Attending Event");
        notAttendingEvent.setOnAction(event -> notAttendingEvent());
        Button nextEvent = new Button("Next Event");
        nextEvent.setOnAction(event -> translate(1000));
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
        shiftPressed = false;
        
        
    }

    public void attendingEvent() {

        if(!eventList.isEmpty()) {
            graphics.fillText(eventList.get(currentEventIndex).toString(), (getWidth() / 3.3), (getHeight() / 2));
        }

    }

    public void notAttendingEvent() {

        if(!eventList.isEmpty()) {
            graphics.fillText(eventList.get(currentEventIndex).toString(), (getWidth() / 3.3), (getHeight() / 2));
        }

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
        if(!eventList.isEmpty()) {
            Event e = eventList.get(currentEventIndex);
            switch(e.getType()) {
                case MOVIE:
                    graphics.fillText(e.toString(), (getWidth() / 3.3), (getHeight() / 2));
                    break;
                case CONCERT:
                    graphics.fillText(e.toString(), (getWidth() / 3.3), (getHeight() / 2) - 10);
                    break;
                case SPORT:
                    graphics.fillText(e.toString(), (getWidth() / 3.3), (getHeight() / 2) + 10);
                    break;
                case OTHER:
                    graphics.fillText(e.toString(), (getWidth() / 3.3), (getHeight() / 2) + 20);
                    break;
            }
        }

    }

    @Override
    public void keyReleaseListener(KeyEvent keyEvent) {

        switch(keyEvent.getCode()) {

            case SHIFT:
                shiftPressed = false;
                break;

        }

    }

    @Override
    public void keyPressListener(KeyEvent keyEvent) {

        switch(keyEvent.getCode()) {

            case F5:
                if(shiftPressed) {
                    translate(1);
                }else{
                    translate(1000);
                }
                System.out.println(shiftPressed);
                break;
            case F6:
                attendingEvent();
                break;
            case F7:
                notAttendingEvent();
                break;
            case UP:
                if(eventTypeIndex < eventTypeTotal) {
                    eventTypeIndex++;
                }else{
                    eventTypeIndex = 0;
                }
                eventList = EventLibrary.getEventsOfAType(EventType.getEventType(eventTypeIndex));
                draw();
                currentEventIndex = 0;
                break;
            case DOWN:
                if(eventTypeIndex > 0) {
                    eventTypeIndex--;
                }else{
                    eventTypeIndex = eventTypeTotal;
                }
                eventList = EventLibrary.getEventsOfAType(EventType.OTHER);
                draw();
                currentEventIndex = 0;
                break;
            case SHIFT:
                shiftPressed = true;
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

    private void translate(long milliseconds) {

        if(!translating && currentEventIndex < (eventList.size() -1)) {
            translating = true;
            ((StackPane)getBorderPane().getCenter()).getChildren().removeAll(canvas);
            canvas = new Canvas();
            draw();
            rectangle.setEffect(new MotionBlur(24, 356));
            rectangle2.setEffect(new MotionBlur(24, 356));
            transition = new TranslateTransition(Duration.millis(milliseconds), rectangle);
            transition.setByY(rectangle.getY() + getHeight());
            transition2 = new TranslateTransition(Duration.millis(milliseconds), rectangle2);
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