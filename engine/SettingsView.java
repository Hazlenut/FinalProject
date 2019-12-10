package engine;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebHistory;

//Can't be extended
public final class SettingsView extends View {

    private ListView<WebHistory.Entry> historyList;
    private String link;

    public SettingsView(Screen screen) {

        super(screen);

        setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));
        Label userInfoLabel = new Label("   User data here");
        userInfoLabel.setFont(Font.font("arial", 22));
        setTop(new VBox(15, getTop(), userInfoLabel));
        historyList = new ListView<>(getScreen().getSearchView().getHistory());
        historyList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<WebHistory.Entry>) c -> link = historyList.getSelectionModel().getSelectedItems().get(0).getUrl());
        Button goToLinkButton = new Button("Go to selected Link", historyList);
        goToLinkButton.setOnAction(event -> {
            if(link != null) {
                getScreen().getSearchView().goTo(link);
                getScreen().switchView(ViewType.SEARCH_VIEW);
            }
        });
        setCenter(goToLinkButton);

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        //...

    }

}