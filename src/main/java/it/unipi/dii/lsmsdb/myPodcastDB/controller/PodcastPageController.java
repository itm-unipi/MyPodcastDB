package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PodcastPageController {

    @FXML
    private Label author;

    @FXML
    private Label category;

    @FXML
    private GridPane episodesGrid;

    @FXML
    private Button like;

    @FXML
    private Label numEpisodes;

    @FXML
    private Label numReviews;

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label rating;

    @FXML
    private Label showReviews;

    @FXML
    private Label title;

    @FXML
    private Button watchlater;

    public void initialize() throws IOException {
        String name = "Greener Pastures";
        String description = "Hear Greiner USA President, David Kirkland, talk about developing new competitive advantages and how going “green” was the key to his company unlocking new international business.";
        Date releaseDate = new Date();
        int time = 1450000;
        Episode episode = new Episode(name, description, releaseDate, time);
        List<Episode> episodes= new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            episode.setName(episode.getName() + i);
            episodes.add(episode);
        }

        // TODO: non funziona
        int row = 0;
        int column = 0;
        for (Episode ep : episodes) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("Episode.fxml"));

            // create new podcast element
            AnchorPane newEpisode = fxmlLoader.load();
            EpisodeController controller = fxmlLoader.getController();
            controller.setData(ep);

            // add new podcast to grid
            this.episodesGrid.add(newEpisode, column++, row);
        }

    }
}
