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

    private static final String HOME = "https://www.google.com";

    private LinkedList<String> history;
    private int currentPageIndex;
    private boolean newPage;
    private WebView webView;

    public SearchView(Screen screen) {

        super(screen);

        Button button1 = new Button("Previous");
        button1.setOnAction(event -> previousPage());
        Button button2 = new Button("Next");
        button2.setOnAction(event -> nextPage());
        Button button3 = new Button("Google");
        button3.setOnAction(event -> goToHome());
        getButtonBar().getButtons().addAll(button1, button2, button3);
        webView = new WebView();
        setCenter(webView);
        webView.getEngine().load(HOME);
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

    public void goTo(String url) {

        webView.getEngine().load(url);

    }

    public void goToHome() {

        goTo(HOME);

    }

    public void previousPage() {

        if(currentPageIndex > 0) {
            newPage = false;
            webView.getEngine().load(history.get(--currentPageIndex));
        }

    }

    public void nextPage() {

        if(currentPageIndex < (history.size() - 1)) {
            newPage = false;
            webView.getEngine().load(history.get(++currentPageIndex));
        }

    }

    public String getCurrentURL() {

        return webView.getEngine().getLocation();
    }

    public ObservableList<WebHistory.Entry> getHistory() {

        return webView.getEngine().getHistory().getEntries();
    }

}