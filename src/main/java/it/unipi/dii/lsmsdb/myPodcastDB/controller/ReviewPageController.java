package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
    private ImageView logout;

    @FXML
    private Label noReviewsMessage;

    @FXML
    private ProgressBar fourStars;

    @FXML
    private Label numReviews;

    @FXML
    private ProgressBar oneStar;

    @FXML
    private BorderPane mainPage;

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label rating;

    @FXML
    private VBox reviewForm;

    @FXML
    private GridPane reviewGrid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchBarText;

    @FXML
    private ImageView star1;

    @FXML
    private ImageView star2;

    @FXML
    private ImageView star3;

    @FXML
    private ImageView star4;

    @FXML
    private ImageView star5;

    @FXML
    private TextArea textContent;

    @FXML
    private TextField textTitle;

    @FXML
    private ProgressBar threeStars;

    @FXML
    private Label title;

    @FXML
    private ProgressBar twoStars;

    private Review ownReview;
    private Podcast podcast;

    @FXML
    void clickOnAuthor(MouseEvent event) throws IOException {
        Logger.info("Click on author : " + this.podcast.getAuthorId());
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.podcast.getId());
    }

    @FXML
    void clickOnFifthStar(MouseEvent event) {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(star);
        this.star5.setImage(star);
        this.ownReview.setRating(5);

        Logger.info("Rating set to " + this.ownReview.getRating());
    }

    @FXML
    void clickOnFirstStar(MouseEvent event) {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(outlineStar);
        this.star3.setImage(outlineStar);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
        this.ownReview.setRating(1);

        Logger.info("Rating set to " + this.ownReview.getRating());
    }

    @FXML
    void clickOnFourthStar(MouseEvent event) {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(star);
        this.star5.setImage(outlineStar);
        this.ownReview.setRating(4);

        Logger.info("Rating set to " + this.ownReview.getRating());
    }

    @FXML
    void clickOnLogout(MouseEvent event) throws IOException {
        MyPodcastDB.getInstance().setSession(null, null);
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void clickOnSecondStar(MouseEvent event) {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(outlineStar);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
        this.ownReview.setRating(2);

        Logger.info("Rating set to " + this.ownReview.getRating());
    }

    @FXML
    void clickOnThirdStar(MouseEvent event) {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
        this.ownReview.setRating(3);

        Logger.info("Rating set to " + this.ownReview.getRating());
    }

    @FXML
    void clickOnTitle(MouseEvent event) throws IOException {
        Logger.info("Podcast ID to load : " + this.podcast.getId());
        StageManager.showPage("PodcastPage.fxml", this.podcast.getId());
    }

    @FXML
    void mouseOnAuthor(MouseEvent event) {
        this.author.setCursor(Cursor.HAND);
        this.author.setTextFill(Color.color(0.6, 0.6, 0.6));
    }

    @FXML
    void mouseOnTitle(MouseEvent event) {
        this.title.setCursor(Cursor.HAND);
        this.title.setTextFill(Color.color(0.6, 0.6, 0.6));
    }

    @FXML
    void mouseOutAuthor(MouseEvent event) {
        this.author.setCursor(Cursor.DEFAULT);
        this.author.setTextFill(Color.color(0.0, 0.0, 1.0));
    }

    @FXML
    void mouseOutTitle(MouseEvent event) {
        this.title.setCursor(Cursor.DEFAULT);
        this.title.setTextFill(Color.color(0.0, 0.0, 0.0));
    }

    @FXML
    void onSubmit(MouseEvent event) {
        // get the text
        String title = this.textTitle.getText();
        String content = this.textContent.getText();

        // fill the review
        this.ownReview.setTitle(title);
        this.ownReview.setContent(content);
        this.ownReview.setCreatedAt(new Date());

        // send the review
        Logger.info(this.ownReview.toString());
    }

    @FXML
    void onClickHome(MouseEvent event) throws IOException {
        Logger.info("Click on home");
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }

    @FXML
    void onClickSearch(MouseEvent event) throws IOException {
        Logger.info("Click on search");

        String searchString = this.searchBarText.getText();
        StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchString);
    }

    @FXML
    void onEnterPressed(KeyEvent event) throws IOException {
        Logger.info("Enter on search");

        if (event.getCode().equals(KeyCode.ENTER)) {
            String searchString = this.searchBarText.getText();
            StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchString);
        }
    }

    @FXML
    void userProfile(MouseEvent event) throws IOException {
        Logger.info("Click on user");
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author"))
            StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage());
        else if (actorType.equals("User"))
            StageManager.showPage(ViewNavigator.USERPAGE.getPage());
        else if (actorType.equals("Admin"))
            StageManager.showPage(ViewNavigator.ADMINDASHBOARD.getPage());
        else
            Logger.error("Unidentified Actor Type");
    }

    @FXML
    void mouseOnFirstStar(MouseEvent event) {
        this.star1.setCursor(Cursor.HAND);

        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(outlineStar);
        this.star3.setImage(outlineStar);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
    }

    @FXML
    void mouseOnSecondStar(MouseEvent event) {
        this.star2.setCursor(Cursor.HAND);

        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(outlineStar);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
    }

    @FXML
    void mouseOnThirdStar(MouseEvent event) {
        this.star3.setCursor(Cursor.HAND);

        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(outlineStar);
        this.star5.setImage(outlineStar);
    }

    @FXML
    void mouseOnFourthStar(MouseEvent event) {
        this.star4.setCursor(Cursor.HAND);

        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(star);
        this.star5.setImage(outlineStar);
    }

    @FXML
    void mouseOnFifthStar(MouseEvent event) {
        this.star5.setCursor(Cursor.HAND);

        Image star = ImageCache.getImageFromLocalPath("/img/star.png");

        this.star1.setImage(star);
        this.star2.setImage(star);
        this.star3.setImage(star);
        this.star4.setImage(star);
        this.star5.setImage(star);
    }

    private void readRating() {
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        Image outlineStar = ImageCache.getImageFromLocalPath("/img/outline_star.png");

        if (this.ownReview.getRating() == 5) {
            this.star1.setImage(star);
            this.star2.setImage(star);
            this.star3.setImage(star);
            this.star4.setImage(star);
            this.star5.setImage(star);
        } else if (this.ownReview.getRating() >= 4) {
            this.star1.setImage(star);
            this.star2.setImage(star);
            this.star3.setImage(star);
            this.star4.setImage(star);
            this.star5.setImage(outlineStar);
        } else if (this.ownReview.getRating() >= 3) {
            this.star1.setImage(star);
            this.star2.setImage(star);
            this.star3.setImage(star);
            this.star4.setImage(outlineStar);
            this.star5.setImage(outlineStar);
        } else if (this.ownReview.getRating() >= 2) {
            this.star1.setImage(star);
            this.star2.setImage(star);
            this.star3.setImage(outlineStar);
            this.star4.setImage(outlineStar);
            this.star5.setImage(outlineStar);
        } else if (this.ownReview.getRating() >= 1) {
            this.star1.setImage(star);
            this.star2.setImage(outlineStar);
            this.star3.setImage(outlineStar);
            this.star4.setImage(outlineStar);
            this.star5.setImage(outlineStar);
        } else {
            this.star1.setImage(outlineStar);
            this.star2.setImage(outlineStar);
            this.star3.setImage(outlineStar);
            this.star4.setImage(outlineStar);
            this.star5.setImage(outlineStar);
        }
    }

    @FXML
    void mouseOutFirstStar(MouseEvent event) {
        this.star1.setCursor(Cursor.DEFAULT);
        readRating();
    }

    @FXML
    void mouseOutSecondStar(MouseEvent event) {
        this.star2.setCursor(Cursor.DEFAULT);
        readRating();
    }

    @FXML
    void mouseOutThirdStar(MouseEvent event) {
        this.star3.setCursor(Cursor.DEFAULT);
        readRating();
    }

    @FXML
    void mouseOutFourthStar(MouseEvent event) {
        this.star4.setCursor(Cursor.DEFAULT);
        readRating();
    }

    @FXML
    void mouseOutFifthStar(MouseEvent event) {
        this.star5.setCursor(Cursor.DEFAULT);
        readRating();
    }

    public void initialize() throws IOException {
        Logger.info("Podcast ID : " + StageManager.getObjectIdentifier());

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
        this.podcast = podcast;

        // actor recognition
        String sessionType = MyPodcastDB.getInstance().getSessionType();
        if (!sessionType.equals("User")) {
            // only the user can write review
            this.reviewForm.setVisible(false);
            this.reviewForm.setStyle("-fx-min-width: 0; -fx-pref-width: 0px; -fx-min-height: 0; -fx-pref-height: 0px");
        }

        // review test
        Review review = new Review("", "", "frank", "Yes, I like it", "Duis ut molestie justo, non mattis arcu. Donec ac arcu eget sapien dignissim pretium eu a mi. Nullam consectetur mauris id maximus vestibulum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nulla mi leo, pulvinar nec blandit a, dictum semper nibh. Proin ut mauris turpis. Duis vehicula volutpat dolor, sodales varius ipsum venenatis sed.", 5, new Date());
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < 10; i++)
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
        Image podcastImage = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
        this.podcastImage.setImage(podcastImage);
        this.rating.setText("" + podcast.getRating());
        this.numReviews.setText(" out of 5.0 • " + podcast.getReviews().size() + " reviews");

        // progress bars
        this.oneStar.setProgress(0.1);
        this.twoStars.setProgress(0.1);
        this.threeStars.setProgress(0.2);
        this.fourStars.setProgress(0.3);
        this.fiveStars.setProgress(0.3);

        // insert reviews in grid
        int row = 0;
        int column = 0;
        for (Review r : reviews) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("Review.fxml"));

            // create new review element
            AnchorPane newReview = fxmlLoader.load();
            ReviewController controller = fxmlLoader.getController();
            controller.setData(r, this.mainPage);

            // add new podcast to grid
            this.reviewGrid.add(newReview, column, row++);
        }

        // initialize own review
        this.ownReview = new Review();
        this.ownReview.setPodcastId(podcast.getId());
        this.ownReview.setRating(0);
    }
}
