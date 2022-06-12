package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorProfileService;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.IOException;

public class AuthorReducedPodcastController {
    private Podcast podcast;

    private BorderPane mainPage;

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

    @FXML
    private VBox boxDeletePodcast;

    /****** Reduced Podcast Events *******/
    @FXML
    void onClickReducedPodcast(MouseEvent event) throws IOException {
        Logger.info(this.podcast.getId() + " | " + this.podcast.getName() + " | " + this.podcast.getReleaseDate() + " | " + this.podcast.getPrimaryCategory());
        StageManager.showPage(ViewNavigator.PODCASTPAGE.getPage(), this.podcast.getId());
    }

    @FXML
    void deletePodcast(MouseEvent event) throws IOException {
        boolean result = DialogManager.getInstance().createConfirmationAlert(this.mainPage, "Delete Podcast", "Do you really want to delete this podcast?");

        if (result) {
            Logger.info("Deleting podcast " + this.podcast.getId() + " (" + this.podcast.getName() + ") that belongs to " + StageManager.getObjectIdentifier());
            AuthorProfileService authorProfileService = new AuthorProfileService();

            int deleteResult = authorProfileService.deletePodcastAsAuthor(this.podcast.getId());
            if (deleteResult == 0) {
                DialogManager.getInstance().createConfirmationAlert(this.mainPage, "Delete Podcast", "Podcast deleted successfully!");

                // Hiding the deleted podcast in the grid
                reducedPodcast.setVisible(false);
                reducedPodcast.setPrefHeight(0);
            } else {
                Logger.error("Error during the delete podcast operation");

                String alertText;
                if (deleteResult == -1) {
                    alertText = "Podcast don't exists!";
                } else {
                    // General message error
                    alertText = "Something went wrong! Please try again.";
                }

                DialogManager.getInstance().createErrorAlert(this.mainPage, "Delete Podcast", alertText);
            }
        } else {
            Logger.info("Operation aborted");
        }
    }

    @FXML
    void OnMouseHoverReducedPodcast(MouseEvent event) {
        String actorType = MyPodcastDB.getInstance().getSessionType();

        // Only the author of the podcast or admins can delete it
        if ((actorType.equals("Author") && StageManager.getObjectIdentifier().equals(((Author)MyPodcastDB.getInstance().getSessionActor()).getName()))) {
            boxDeletePodcast.setVisible(true);
            FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(0.3), boxDeletePodcast);
            fadeAuthorImage.setFromValue(0);
            fadeAuthorImage.setToValue(1.0);
            fadeAuthorImage.play();
        }

        reducedPodcast.setStyle("-fx-background-color: #eeeeee");
    }

    @FXML
    void onMouseExitedReducedPodcast(MouseEvent event) {
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if ((actorType.equals("Author") && StageManager.getObjectIdentifier().equals(((Author)MyPodcastDB.getInstance().getSessionActor()).getName()))) {
            FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(0.3), boxDeletePodcast);
            fadeAuthorImage.setFromValue(1.0);
            fadeAuthorImage.setToValue(0);
            fadeAuthorImage.play();
            boxDeletePodcast.setVisible(false);
        }

        reducedPodcast.setStyle("-fx-background-color: transparent");
    }

    @FXML
    void onMouseHoverDeletePodcast(MouseEvent event) {
        this.boxDeletePodcast.setStyle("-fx-background-color: white; -fx-background-radius: 25; -fx-border-color: #d3d3d3; -fx-border-radius: 25");
    }

    @FXML
    void onMouseExitedDeletePodcast(MouseEvent event) {
        this.boxDeletePodcast.setStyle("-fx-background-color: white; -fx-background-radius: 25; -fx-border-color: #eaeaea; -fx-border-radius: 25");
    }

    /*************************************/

    public void setData(Podcast podcast, BorderPane mainPage) {
        this.mainPage = mainPage;
        this.podcast = podcast;
        this.podcastName.setText(podcast.getName());
        this.podcastCategory.setText(podcast.getPrimaryCategory());

        // Setting release date
        String[] tokens = this.podcast.getReleaseDate().toString().split(" ");
        String date = tokens[2] + " " + tokens[1] + " " + tokens[5];
        this.podcastReleaseDate.setText(date);

        // Setting image preview
        Image image = ImageCache.getImageFromLocalPath("/img/logo.png");
        this.podcastImage.setImage(image);

        Platform.runLater(() -> {
            Image imageLoaded = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
            this.podcastImage.setImage(imageLoaded);
        });
    }
}
