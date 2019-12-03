package engine;


import javafx.collections.ObservableList;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

public class SearchView extends View {

    private WebView webView;

    private static final String HOME = "https://www.google.com";

    public SearchView(String name) {

        super(name);

        webView = new WebView();
        getBorderPane().setCenter(webView);
        webView.getEngine().load(HOME);

    }

    public void goToHome() {

        webView.getEngine().load(HOME);

    }

    public String getCurrentURL() {

        ObservableList<WebHistory.Entry> entries = webView.getEngine().getHistory().getEntries();

        return entries.get(entries.size() - 1).getUrl();
    }
    
}