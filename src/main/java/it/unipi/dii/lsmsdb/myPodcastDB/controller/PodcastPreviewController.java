package it.unipi.dii.lsmsdb.myPodcastDB.controller;
import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.WatchlistCache;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.service.HomePageService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class PodcastPreviewController {
    private boolean inWatchlist;

    private String actorType;

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
    private ImageView watchlistStatus;

    @FXML
    private HBox boxWatchlist;

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
    private HBox watchlistAddedMessage;

    @FXML
    private HBox watchlistRemovedMessage;

    @FXML
    void onHoverPodcast(MouseEvent event) {
        this.podcastPreviewBox.setStyle("-fx-border-color: #DBDBDB; -fx-background-color: #E5E5E5; -fx-background-radius: 7px; -fx-border-radius: 7px;");

        if (this.actorType.equals("User")) {
            this.boxWatchlist.setVisible(true);
            FadeTransition fadeButton = new FadeTransition(Duration.seconds(0.4), this.boxWatchlist);
            fadeButton.setFromValue(0);
            fadeButton.setToValue(1.0);
            fadeButton.play();
        }

    }

    @FXML
    void onExitedPodcast(MouseEvent event) {
        podcastPreviewBox.setStyle("-fx-border-color: transparent;");

        if (this.actorType.equals("User")) {
            this.boxWatchlist.setVisible(false);
            FadeTransition fadeButton = new FadeTransition(Duration.seconds(0.4), this.boxWatchlist);
            fadeButton.setFromValue(1.0);
            fadeButton.setToValue(0);
            fadeButton.play();

            watchlistAddedMessage.setVisible(false);
            watchlistRemovedMessage.setVisible(false);
        }
    }

    @FXML
    void onClickBtnWatchlist(MouseEvent event) {
        HomePageService homePageService = new HomePageService();
        if (this.inWatchlist) {
            Logger.info("Removing from watchlist (and cache)");
            this.watchlistStatus.setImage(ImageCache.getImageFromLocalPath("/img/addWatchlist.png"));
            this.inWatchlist = !homePageService.removePodcastFromWatchlist(podcastPreview);

            watchlistAddedMessage.setVisible(false);
            watchlistRemovedMessage.setVisible(true);
            FadeTransition boxMessage = new FadeTransition(Duration.seconds(1.5), watchlistRemovedMessage);
            boxMessage.setFromValue(1.0);
            boxMessage.setToValue(0);
            boxMessage.play();

        } else {
            // TODO: aggiungere alert se watchlist è troppo grande
            Logger.info("Adding in watchlist (and cache)");
            this.watchlistStatus.setImage(ImageCache.getImageFromLocalPath("/img/removeWatchlist.png"));
            this.inWatchlist = homePageService.addPodcastInWatchlist(podcastPreview);

            watchlistAddedMessage.setVisible(true);
            watchlistRemovedMessage.setVisible(false);
            FadeTransition boxMessage = new FadeTransition(Duration.seconds(1.5), watchlistAddedMessage);
            boxMessage.setFromValue(1.0);
            boxMessage.setToValue(0);
            boxMessage.play();
        }
    }

    @FXML
    void onClickPodcast(MouseEvent event) throws IOException {
        Logger.info(podcastPreview.getId() + " : " + podcastPreview.getName());
        StageManager.showPage(ViewNavigator.PODCASTPAGE.getPage(), podcastPreview.getId());
    }

    public void setData(Podcast podcast, int typeLabel, String valueLabel) {
        // TypeLabel can be: 0 -> no label, 1 -> likes, 2 -> rating, 3 -> star
        this.podcastPreview = podcast;
        this.actorType = MyPodcastDB.getInstance().getSessionType();

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

        // Check if podcast is in the user's watchlist
        this.inWatchlist = (WatchlistCache.getPodcast(this.podcastPreview.getId()) != null);
        if (inWatchlist) {
            Logger.info("Podcast in the watchlist");
            this.watchlistStatus.setImage(ImageCache.getImageFromLocalPath("/img/removeWatchlist.png"));
        }

        Platform.runLater(() -> {
            Image imageLoaded = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
            this.podcastImage.setImage(imageLoaded);
        });
    }
}
