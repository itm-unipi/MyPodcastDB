package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.PodcastPreview;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class PodcastPreviewController {

    @FXML
    private ImageView PodcastImage;

    @FXML
    private Label PodcastName;

    private PodcastPreview podcastPreview;

    @FXML
    private Tooltip podcastToolTip;

    @FXML
    private BorderPane podcastBorderPane;

    @FXML
    void onClick(MouseEvent event) {
        Logger.info(podcastPreview.getPodcastId() + " : " + this.podcastPreview.getPodcastName());
    }

    @FXML
    void podcastIn(MouseEvent event) {
        podcastBorderPane.setStyle("-fx-background-color: f2f2f2");
        PodcastName.setStyle("-fx-font-size: 12; -fx-font-weight: bold");
    }

    @FXML
    void podcastOut(MouseEvent event) {
        podcastBorderPane.setStyle("-fx-background-color: white");
        PodcastName.setStyle("-fx-font-size: 10; -fx-font-weight: bold");
    }

    public void setData(PodcastPreview podcast) {
        this.podcastPreview = podcast;

        Image image = new Image(podcast.getArtworkUrl600());
        this.PodcastImage.setImage(image);
        this.PodcastName.setText(podcast.getPodcastName());
        this.podcastToolTip.setText(podcast.getPodcastName());
    }
}
