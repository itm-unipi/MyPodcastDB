package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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

    public void setData(Author author) {
        this.author = author;
        List<Triplet<String, String, Date>> reducedPodcasts = author.getPodcasts();

        // BOH
        /*
        Image image = new Image(podcast.getArtworkUrl600());
        podcastImage.setImage(image);
        podcastName.setText(podcast.getPodcastName());
         */
    }

}
