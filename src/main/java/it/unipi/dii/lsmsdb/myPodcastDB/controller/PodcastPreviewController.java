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
import javafx.scene.layout.VBox;

public class PodcastPreviewController {

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label PodcastName;

    private PodcastPreview podcastPreview;

    @FXML
    private Tooltip podcastToolTip;

    @FXML
    private VBox podcastContainer;

    @FXML
    void onClick(MouseEvent event) {
        Logger.info(podcastPreview.getPodcastId() + " : " + this.podcastPreview.getPodcastName() + " selected");
    }

    @FXML
    void podcastIn(MouseEvent event) {
        podcastContainer.setStyle("-fx-background-color: f2f2f2; -fx-background-radius: 10;");
        PodcastName.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
    }

    @FXML
    void podcastOut(MouseEvent event) {
        podcastContainer.setStyle("-fx-background-color: white");
        PodcastName.setStyle("-fx-font-size: 10; -fx-font-weight: bold");
    }

    public void setData(PodcastPreview podcast) {
        this.podcastPreview = podcast;

        Image image = new Image(podcast.getArtworkUrl600());
        this.podcastImage.setImage(image);
        this.PodcastName.setText(podcast.getPodcastName());
        this.podcastToolTip.setText(podcast.getPodcastName());
    }
}
