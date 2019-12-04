package engine;

import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Date;

public class HomeView extends View {
	
	private Label test;
	private Button[] buttons;

    public HomeView(String name, Screen screen) {

        super(name, screen);

        test = new Label("Welcome to Event Organizer");
        getBorderPane().setTop(test);

    }



    @Override
    public Button[] getToolBarButtons() {

        return buttons;
    }



	@Override
	public void keyListener(KeyEvent keyEvent) {
		// TODO Auto-generated method stub
		
	}



}