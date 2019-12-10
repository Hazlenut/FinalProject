package engine;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.util.LinkedList;

//Can't be extended
public final class SearchView extends View {

    private static final String HOME = "https://www.google.com"; //Home page of the webView

    private LinkedList<String> history; //Stores the navigable history that the webView goes through
    private int currentPageIndex; //Used for properly navigating pages
    private boolean newPage; //Used for properly navigating pages
    private WebView webView;

    /**
     * Creates a new SearchView with the webView set to HOME and with the default buttons added
     * @param screen The Screen instance which holds this SearchView
     */
    public SearchView(Screen screen) {

        super(screen);

        //Add all custom buttons that will be displayed on the toolbar:
        Button button1 = new Button("Previous");
        button1.setOnAction(event -> previousPage());
        Button button2 = new Button("Next");
        button2.setOnAction(event -> nextPage());
        Button button3 = new Button("Google");
        button3.setOnAction(event -> goToHome());
        getToolbarButtons().add(button1); //Add those buttons to the Button ArrayList
        getToolbarButtons().add(button2);
        getToolbarButtons().add(button3);

        //Add the webView:
        webView = new WebView();
        setCenter(webView);
        webView.getEngine().load(HOME); //Make sure webView is set to HOME

        //This code handles the logic for navigating through the history:
        history = new LinkedList<>();
        newPage = true;
        currentPageIndex = -1;
        webView.getEngine().getHistory().getEntries().addListener((ListChangeListener<WebHistory.Entry>) c -> {
            if(newPage) {
                if(currentPageIndex < (history.size() - 1)) {
                    for(int i = 0; i <= (history.size() - currentPageIndex - 1); i++) {
                        history.pollLast();
                    }
                }
                history.add(webView.getEngine().getLocation());
                currentPageIndex++;
            }else{
                newPage = true;
            }
        });

    }

    /**
     * This method gets called if the Screen holding this SearchView doesn't consume the keyEvent
     * @param keyEvent the KeyEvent passed from the Screen
     */
    public void keyPressed(KeyEvent keyEvent) {

        switch(keyEvent.getCode()) {

            case F4:
                goToHome();
                break;
            case LEFT:
                previousPage();
                break;
            case RIGHT:
                nextPage();
                break;

        }

    }

    /**
     * Sets the webView to the specified URL (unless it is already on that URL)
     * @param url the URL of the intended website
     */
    public void goTo(String url) {

        webView.getEngine().load(url);

    }

    /**
     * Sets the webView back to HOME
     */
    public void goToHome() {

        goTo(HOME);

    }

    /**
     * Navigates the webView to the previous page in the history (if the webView has more than 1 page in the history), otherwise do nothing
     */
    public void previousPage() {

        if(currentPageIndex > 0) { //First check the page's index
            newPage = false;
            webView.getEngine().load(history.get(--currentPageIndex));
        }

    }

    /**
     * Navigates the webView to the next page in the history (if the webView has gone back at all), otherwise do nothing
     */
    public void nextPage() {

        if(currentPageIndex < (history.size() - 1)) { //First check the page's index
            newPage = false;
            webView.getEngine().load(history.get(++currentPageIndex));
        }

    }

    /**
     * @return String the String representation of the URL that the webView is currently on
     */
    public String getCurrentURL() {

        return webView.getEngine().getLocation();
    }

    /**
     * @return ObservableList the list of ALL history entries that the webView has been to, including unreachable pages (Note: this will reset every time a user logs out)
     */
    public ObservableList<WebHistory.Entry> getHistory() {

        return webView.getEngine().getHistory().getEntries();
    }

}