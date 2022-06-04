package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AdminService;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Date;

public class AuthorReducedPodcastController {
    private String podcastId;

    private String authorId;

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
        Logger.info(podcastId + " | " + podcastName.getText() + " | " + podcastReleaseDate.getText() + " | " + podcastCategory.getText());
        StageManager.showPage(ViewNavigator.PODCASTPAGE.getPage(), this.podcastId);
    }

    @FXML
    void deletePodcast(MouseEvent event) throws IOException {
        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(stage);
        alert.setTitle("Delete Podcast");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to delete this podcast?");
        alert.setGraphic(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            Logger.info("Deleting podcast " + podcastId + " (" + podcastName + ") that belongs to " + StageManager.getObjectIdentifier());
            String actorType = MyPodcastDB.getInstance().getSessionType();

            int deleteResult = 0;
            if (actorType.equals("Author")) {
                AuthorService authorService = new AuthorService();
                deleteResult = authorService.deletePodcast(this.podcastId);
            } else {
                AdminService adminService = new AdminService();
                deleteResult = adminService.deletePodcast(this.authorId, this.podcastId);
            }

            if (deleteResult == 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(stage);
                alert.setTitle("Delete Account");
                alert.setHeaderText(null);
                alert.setContentText("Podcast deleted successfully!");
                alert.setGraphic(null);
                alert.showAndWait();
                //StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), StageManager.getObjectIdentifier());
                StageManager.showPage(ViewNavigator.HOMEPAGE.getPage(), StageManager.getObjectIdentifier());
            } else {
                Logger.error("Error during the delete podcast operation");

                alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(stage);
                alert.setTitle("Delete Podcast Error");
                alert.setHeaderText(null);

                if (deleteResult == -1) {
                    alert.setContentText("Podcast not found!");
                } else {
                    // General message error
                    alert.setContentText("Something went wrong!");
                }

                alert.setGraphic(null);;
                alert.showAndWait();
            }

        } else {
            Logger.info("Operation aborted");
        }
    }

    @FXML
    void OnMouseHoverReducedPodcast(MouseEvent event) {
        String actorType = MyPodcastDB.getInstance().getSessionType();

        // Only the author of the podcast or admins can delete it
        if ((actorType.equals("Author") && StageManager.getObjectIdentifier().equals(((Author)MyPodcastDB.getInstance().getSessionActor()).getName()))
                || actorType.equals("Admin")) {
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

        if ((actorType.equals("Author") && StageManager.getObjectIdentifier().equals(((Author)MyPodcastDB.getInstance().getSessionActor()).getName()))
                || actorType.equals("Admin")) {
            FadeTransition fadeAuthorImage = new FadeTransition(Duration.seconds(0.3), boxDeletePodcast);
            fadeAuthorImage.setFromValue(1.0);
            fadeAuthorImage.setToValue(0);
            fadeAuthorImage.play();
            boxDeletePodcast.setVisible(false);
        }

        reducedPodcast.setStyle("-fx-background-color: transparent");
    }

    /*************************************/

    public void setData(String authorId, String podcastId, String podcastName, Date podcastReleaseDate, String podcastCategory, String ArtworkUrl600 ) {
        this.authorId = authorId;
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
            Image imageLoaded = ImageCache.getImageFromURL(ArtworkUrl600);
            this.podcastImage.setImage(imageLoaded);
        });
    }
}
