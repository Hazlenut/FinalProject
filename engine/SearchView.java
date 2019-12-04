package engine;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

public class SearchView extends View {

    private LinkedList<String> history;
    private int currentPageIndex;
    private boolean newPage;

    private WebView webView;
    private Button[] buttons;

    private static final String HOME = "https://www.google.com";

    public SearchView(String name, Screen screen) {

        super(name, screen);

        Button button1 = new Button("Previous");
        button1.setOnAction(event -> previousPage());
        Button button2 = new Button("Next");
        button2.setOnAction(event -> nextPage());
        Button button3 = new Button("Home");
        button3.setOnAction(event -> goToHome());
        Button button4 = new Button("Get Events");
        button4.setOnAction(event -> getScreen().getEventsFromSearchView());
        buttons = new Button[] {button1, button2, button3, button4};
        webView = new WebView();
        getBorderPane().setCenter(webView);
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

    @Override
    public Button[] getToolBarButtons() {

        return buttons;
    }

    @Override
    public void keyListener(KeyEvent keyEvent) {

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
//            case SHIFT:
//                for(String s : history) {
//                    System.out.println(s);
//                }
//                System.out.println(currentPageIndex);
//                break;
//            case ALT:
//                for(WebHistory.Entry entry : webView.getEngine().getHistory().getEntries()) {
//                    System.out.println(entry.getUrl());
//                }
//                break;
            default:
                //Code...
//                NodeList list = webView.getEngine().getDocument().getDocumentElement().getElementsByTagName("h1");
//                for(int i = 0; i < list.getLength(); i++) {
//                    if(!list.item(0).getTextContent().equals("\n")) {
//                        System.out.println(list.item(0).getTextContent().trim().replaceAll(" ", ""));
//                    }
//                }
                break;

        }

    }

    public void goToHome() {

        webView.getEngine().load(HOME);

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