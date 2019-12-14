package engine;

import java.util.ArrayList;

import com.sun.javafx.collections.ObservableListWrapper;

import engine.EventManager.Event;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//Can't be extended
public final class EventView extends View {
	private ButtonBar toolBar;
    ArrayList<Event> events = EventManager.getEvents();
    ListView alist = new ListView<>(new ObservableListWrapper<>(events)); //Add a ListView which displays the Events stored in EventManager
  
    /**
     * Creates a new EventView
     * @param screen The Screen instance holding this EventView
     */
    public EventView(Screen screen) {

        super(screen);
        
        toolBar = screen.getToolBar();
        
        setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY))); //Change the background color
        setLeft(alist);
        alist.setPrefWidth(800);
        
        MenuButton viewSelector = new MenuButton("Sort");
        MenuItem eventViewItem = new MenuItem("Alphabet");
        eventViewItem.setOnAction(event ->  { 
        	ListView a = new ListView<>(new ObservableListWrapper<>(EventManager.sortAlphabet(events)));
        	
        	changeList(a);
        	setLeft(alist);
        });
        MenuItem searchViewItem = new MenuItem("Web Browser");
        //searchViewItem.setOnAction(event -> 1 +1);
        MenuItem settingViewItem = new MenuItem("Settings");
       // settingViewItem.setOnAction();
        viewSelector.getItems().addAll(eventViewItem, searchViewItem, settingViewItem);
        ButtonBar.setButtonData(viewSelector, ButtonBar.ButtonData.NO);
        toolBar.getButtons().addAll(viewSelector);
        setOnKeyPressed(this::keyPressed);
    }
    public void changeList(ListView<Event> list) {
    	alist = list;
    }

    /**
     * This method gets called if the Screen holding this EventView doesn't consume the keyEvent
     * @param keyEvent the KeyEvent passed from the Screen
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {

        //...

    }

}