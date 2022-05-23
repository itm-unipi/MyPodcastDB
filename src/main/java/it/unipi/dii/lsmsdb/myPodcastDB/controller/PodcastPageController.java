package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import static java.lang.Math.floor;

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

    private Podcast podcast;

    public void initialize() throws IOException {
        // Podcast Test
        Podcast podcast = new Podcast("00000000", "Scaling Global", "00000000", "Slate Studios", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/60x60bb.jpg", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg", "Clean", "Trinidad & Tobago", "Business", null, new Date());
        String name = "Greener Pastures";
        String description = "Hear Greiner USA President, David Kirkland, talk about developing new competitive advantages and how going “green” was the key to his company unlocking new international business.";
        Date releaseDate = new Date();
        int time = 1450000;
        Episode episode = new Episode(name, description, releaseDate, time);
        for (int i = 0; i < 10; i++) {
            episode.setName(episode.getName() + i);
            podcast.addEpisode(episode.getName(), episode.getDescription(), episode.getReleaseDate(), episode.getTimeMillis());
            podcast.addReview("" + i, 5);
        }

        // TODO: addEpisode(Episode), via la list dal costruttore?

        // podcast initialization
        this.title.setText(podcast.getName());
        this.author.setText(podcast.getAuthorName());
        Image image = new Image(podcast.getArtworkUrl600());
        this.podcastImage.setImage(image);
        this.category.setText(podcast.getPrimaryCategory());
        this.numEpisodes.setText(podcast.getEpisodes().size() + " Episodes");
        float average = 0.0f;
        int count = 0;
        for (Entry<String, Integer> review : podcast.getReviews()) {
            average += review.getValue();
            count += 1;
        }
        this.numReviews.setText(count + " reviews");
        this.rating.setText("Rating: " + floor(average / count));

        int row = 0;
        int column = 0;
        for (Episode ep : podcast.getEpisodes()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("Episode.fxml"));

            // create new podcast element
            AnchorPane newEpisode = fxmlLoader.load();
            EpisodeController controller = fxmlLoader.getController();
            controller.setData(ep);

            // add new podcast to grid
            this.episodesGrid.add(newEpisode, column, row++);
        }
    }

    public void setData(Podcast podcast) {
        this.podcast = podcast;

        this.title.setText(podcast.getName());
        this.author.setText(podcast.getAuthorName());
        Image image = new Image(podcast.getArtworkUrl600());
        this.podcastImage.setImage(image);
        this.category.setText(podcast.getPrimaryCategory());
        this.numEpisodes.setText(podcast.getEpisodes().size() + " Episodes");
        float average = 0.0f;
        int count = 0;
        for (Entry<String, Integer> review : podcast.getReviews()) {
            average += review.getValue();
            count += 1;
        }
        this.rating.setText("Rating: " + floor(average / count));
    }
}
