package it.unipi.dii.lsmsdb.myPodcastDB.controller;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PodcastPreviewInUserPageController {

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label PodcastName;

    private Podcast podcastPreview;

    @FXML
    private Tooltip podcastToolTip;

    @FXML
    private VBox podcastContainer;

    @FXML
    void onClick(MouseEvent event) throws IOException {
        Logger.info(podcastPreview.getId() + " : " + this.podcastPreview.getName() + " selected");
        StageManager.showPage(ViewNavigator.PODCASTPAGE.getPage(), this.podcastPreview.getId());
    }

    @FXML
    void podcastIn(MouseEvent event) {
        podcastContainer.setStyle("-fx-background-color: f2f2f2; -fx-background-radius: 10;");
    }

    @FXML
    void podcastOut(MouseEvent event) {
        podcastContainer.setStyle("-fx-background-color: white");
    }


    public void setData(Podcast podcast) {
        this.podcastPreview = podcast;

        Image image = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
        this.podcastImage.setImage(image);
        this.PodcastName.setText(podcast.getName());
        this.podcastToolTip.setText(podcast.getName());
    }

}
