package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class SearchController {
    @FXML
    private ImageView userPicture;

    @FXML
    private ImageView home;

    @FXML
    private ScrollPane scrollSuggestedForCategory;

    @FXML
    private ImageView searchButton;

    @FXML
    private Label searchingForText;

    @FXML
    void onClickSearch(MouseEvent event) throws IOException {
        // TODO: E' NECESSARIO PASSARE IL PARAMETRO DI RICERCA
        StageManager.showPage(ViewNavigator.SEARCH.getPage());
    }

    @FXML
    void userProfile(MouseEvent event) {
        Logger.info("User profile clicked");
        //StageManager.showPage(ViewNavigator.USERPROFILE.getPage());
    }

    @FXML
    void onClickHome(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.USERHOME.getPage());
        Logger.info("User Home Clicked");
    }

    @FXML
    void onEnterPressed(KeyEvent event) throws IOException {
        // TODO: E' NECESSARIO PASSARE IL PARAMETRO DI RICERCA
        if (event.getCode() == KeyCode.ENTER)
            StageManager.showPage(ViewNavigator.SEARCH.getPage());
    }

    public void initialize() throws IOException {
        //this.searchingForText.setText();
    }
}
