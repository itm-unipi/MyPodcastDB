package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewPageController {

    @FXML
    private Label author;

    @FXML
    private Label category;

    @FXML
    private ProgressBar fiveStars;

    @FXML
    private Label noReviewsMessage;

    @FXML
    private ProgressBar fourStars;

    @FXML
    private Label numReviews;

    @FXML
    private ProgressBar oneStar;

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label rating;

    @FXML
    private GridPane reviewGrid;

    @FXML
    private ProgressBar threeStars;

    @FXML
    private Label title;

    @FXML
    private ProgressBar twoStars;

    @FXML
    void clickOnAuthor(MouseEvent event) {

    }

    @FXML
    void mouseOnAuthor(MouseEvent event) {

    }

    @FXML
    void mouseOutAuthor(MouseEvent event) {

    }

    public void initialize() throws IOException {
        // podcast test
        Podcast podcast = new Podcast("00000000", "Scaling Global", "00000000", "Slate Studios", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/60x60bb.jpg", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg", "Clean", "Trinidad & Tobago", "Business", null, new Date());
        String name = "Greener Pastures";
        String description = "Hear Greiner USA President, David Kirkland, talk about developing new competitive advantages and how going “green” was the key to his company unlocking new international business.";
        Date releaseDate = new Date();
        int time = 1450000;
        Episode episode = new Episode(name, description, releaseDate, time);
        for (int i = 0; i < 10; i++) {
            podcast.addEpisode(episode);
            podcast.addReview("" + i, 5);
        }

        // review test
        Review review = new Review("", "", "frank", "Yes, I like it", "Duis ut molestie justo, non mattis arcu. Donec ac arcu eget sapien dignissim pretium eu a mi. Nullam consectetur mauris id maximus vestibulum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nulla mi leo, pulvinar nec blandit a, dictum semper nibh. Proin ut mauris turpis. Duis vehicula volutpat dolor, sodales varius ipsum venenatis sed.", 5, new Date());
        List<Review> reviews = new ArrayList<>();
        for (int i = 10; i < 10; i++)
            reviews.add(review);

        // no reviews message
        if (!reviews.isEmpty()) {
            this.noReviewsMessage.setVisible(false);
            this.noReviewsMessage.setPadding(new Insets(-20, 0, 0, 0));
        }

        // set fields
        this.title.setText(podcast.getName());
        this.author.setText(podcast.getAuthorName());
        this.category.setText(podcast.getPrimaryCategory());
        Image podcastImage = new Image(podcast.getArtworkUrl600());
        this.podcastImage.setImage(podcastImage);
        this.rating.setText("" + podcast.getRating());
        this.numReviews.setText(" out of 5.0 • " + podcast.getReviews().size() + " reviews");

        // insert reviews in grid
        int row = 0;
        int column = 0;
        for (Review r : reviews) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("Review.fxml"));

            // create new review element
            AnchorPane newReview = fxmlLoader.load();
            ReviewController controller = fxmlLoader.getController();
            controller.setData(r);

            // add new podcast to grid
            this.reviewGrid.add(newReview, column, row++);
        }
    }
}
