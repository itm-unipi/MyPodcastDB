package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Date;
import java.util.List;

public class AuthorReducedPodcastController {
    private String podcastId;

    @FXML
    private Label podcastCategory;

    @FXML
    private Label podcastReleaseDate;

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label podcastName;

    @FXML
    private HBox reducedPodcast;

    /****** Reduced Podcast Events *******/
    @FXML
    void onClickReducedPodcast(MouseEvent event) throws IOException {
        Logger.info(podcastId + " | " + podcastName.getText() + " | " + podcastReleaseDate.getText() + " | " + podcastCategory.getText());
        StageManager.showPage(ViewNavigator.PODCASTPAGE.getPage(), this.podcastId);
    }

    @FXML
    void OnMouseHoverReducedPodcast(MouseEvent event) {
        reducedPodcast.setStyle("-fx-background-color: #eeeeee");
    }

    @FXML
    void onMouseExitedReducedPodcast(MouseEvent event) {
        reducedPodcast.setStyle("-fx-background-color: transparent");
    }

    /*************************************/

    public void setData(String podcastId, String podcastName, Date podcastReleaseDate, String podcastCategory, String getArtworkUrl600 ) {
        this.podcastId = podcastId;
        this.podcastName.setText(podcastName);
        this.podcastCategory.setText(podcastCategory);

        // Setting release date
        String[] tokens = podcastReleaseDate.toString().split(" ");
        String date = tokens[2] + " " + tokens[1] + " " + tokens[5];
        this.podcastReleaseDate.setText(date);

        // Setting image preview
        Image image = ImageCache.getImageFromLocalPath("/img/logo.png");
        podcastImage.setImage(image);

        Platform.runLater(() -> {
            Image imageLoaded = ImageCache.getImageFromURL(getArtworkUrl600);
            this.podcastImage.setImage(imageLoaded);
        });
    }

}
