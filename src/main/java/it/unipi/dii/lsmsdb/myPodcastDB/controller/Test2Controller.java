package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class Test2Controller {

    @FXML
    private Button backButton;

    @FXML
    void onBackClick(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.TEST.getPage());
    }

}