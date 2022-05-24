package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.javatuples.Triplet;

import java.util.Date;
import java.util.List;

public class AuthorReducedPodcastController {

    private Author author;

    @FXML
    private Label podcastReleaseDate;

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label podcastName;

    @FXML
    private HBox reducedPodcast;

    @FXML
    void OnMouseHover(MouseEvent event) {
        reducedPodcast.setStyle("-fx-background-color: #eeeeee");
    }

    @FXML
    void onMouseExited(MouseEvent event) {
        reducedPodcast.setStyle("-fx-background-color: transparent");
    }

    public void setData(Author author) {
        this.author = author;
        List<Triplet<String, String, Date>> reducedPodcasts = author.getPodcasts();

        /*
        Image image = new Image(podcast.getArtworkUrl600());
        podcastImage.setImage(image);
        podcastName.setText(podcast.getPodcastName());
         */
    }

}
