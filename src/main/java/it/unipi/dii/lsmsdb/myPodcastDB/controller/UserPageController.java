package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class UserPageController {

    @FXML
    private ImageView loginHomeButton;

    @FXML
    private ImageView loginSearchButton;

    @FXML
    private TextField loginSearchTextField;

    @FXML
    private Button userPageLogoutButton;

    @FXML
    void logoutButtonClick(MouseEvent event) throws IOException {

        Logger.info("Logout button pressed");
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

}
