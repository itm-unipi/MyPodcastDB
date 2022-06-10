package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.SearchService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class UserSearchPreviewController {
    private User user;

    @FXML
    private Label userFound;

    @FXML
    private ImageView userPicture;

    @FXML
    private HBox boxUserPreview;

    @FXML
    void onClickUser(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.USERPAGE.getPage(), this.user.getUsername());
    }

    @FXML
    void changeBackgroundUser(MouseEvent event) {
        this.boxUserPreview.setStyle("-fx-background-color: #eeeeee;");
    }

    @FXML
    void restoreBackgroundUser(MouseEvent event) {
        this.boxUserPreview.setStyle("-fx-background-color: transparent;");
    }

    public void setData(User user) {
        this.user = user;
        userFound.setText(user.getUsername());

        Image image = ImageCache.getImageFromLocalPath(user.getPicturePath());
        userPicture.setImage(image);
    }
}

