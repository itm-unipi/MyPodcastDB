package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.PodcastPreview;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PodcastPreviewController {

    @FXML
    private ImageView PodcastImage;

    @FXML
    private Label PodcastName;

    private PodcastPreview podcastPreview;

    @FXML
    void onClick(MouseEvent event) {
        Logger.info(podcastPreview.getPodcastId() + " : " + this.podcastPreview.getPodcastName());
    }

    public void setData(PodcastPreview podcast) {
        this.podcastPreview = podcast;

        Image image = new Image(podcast.getArtworkUrl600());
        this.PodcastImage.setImage(image);
        this.PodcastName.setText(podcast.getPodcastName());
    }
}
