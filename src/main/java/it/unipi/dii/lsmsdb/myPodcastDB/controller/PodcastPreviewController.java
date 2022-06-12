package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class PodcastPreviewController {

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label podcastName;

    @FXML
    private VBox podcastPreviewBox;

    private Podcast podcastPreview;

    @FXML
    private Tooltip podcastToolTip;

    @FXML
    private VBox podcastContainer;

    @FXML
    private HBox boxLikes;

    @FXML
    private Label counterLikeLabel;

    @FXML
    private HBox boxRating;

    @FXML
    private Label ratingLabel;

    @FXML
    private HBox boxTopCountry;

    @FXML
    private Label ratingCountryLabel;

    @FXML
    void onClick(MouseEvent event) {
        Logger.info(podcastPreview.getId() + " : " + this.podcastPreview.getName() + " selected");
    }

    @FXML
    void onFocus(MouseEvent event) {
        podcastPreviewBox.setStyle("-fx-border-color: #DBDBDB; -fx-background-color: #E5E5E5; -fx-background-radius: 7px; -fx-border-radius: 7px;");
    }

    @FXML
    void onExit(MouseEvent event) {
        podcastPreviewBox.setStyle("-fx-border-color: transparent;");
    }

    @FXML
    void podcastInfo(MouseEvent event) throws IOException {
        Logger.info(podcastPreview.getId() + " : " + podcastPreview.getName());
        StageManager.showPage(ViewNavigator.PODCASTPAGE.getPage(), podcastPreview.getId());
    }

    @FXML
    void podcastIn(MouseEvent event) {
        podcastContainer.setStyle("-fx-background-color: f2f2f2; -fx-background-radius: 10;");
        podcastName.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
    }

    @FXML
    void podcastOut(MouseEvent event) {
        podcastContainer.setStyle("-fx-background-color: white");
        podcastName.setStyle("-fx-font-size: 10; -fx-font-weight: bold");
    }

    public void setData(Podcast podcast, int typeLabel, String valueLabel) {
        // TypeLabel can be: 0 -> no label, 1 -> likes, 2 -> rating, 3 -> star

        this.podcastPreview = podcast;

        // Setting GUI elements
        Image image = ImageCache.getImageFromLocalPath("/img/loading.jpg");
        this.podcastImage.setImage(image);
        this.podcastName.setText(podcast.getName());
        this.podcastToolTip.setText(podcast.getName());
        podcastImage.setImage(image);
        podcastName.setText(podcast.getName());

        // Setting label in the podcast preview
        if (typeLabel == 1) {
            // Counter likes
            boxLikes.setVisible(true);
            counterLikeLabel.setText(valueLabel);
        } else if (typeLabel == 2) {
            // Average rating
            boxRating.setVisible(true);
            ratingLabel.setText(valueLabel);
        } else if (typeLabel == 3) {
            // Top country podcast
            boxTopCountry.setVisible(true);
            ratingCountryLabel.setText(valueLabel);
        }

        Platform.runLater(() -> {
            Image imageLoaded = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
            this.podcastImage.setImage(imageLoaded);
        });
    }
}
