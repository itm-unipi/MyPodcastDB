package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import static java.lang.Math.floor;

public class PodcastPageController {

    @FXML
    private Button addEpisode;

    @FXML
    private Label author;

    @FXML
    private Button deletePodcast;

    @FXML
    private Label category;

    @FXML
    private Label content;

    @FXML
    private Label country;

    @FXML
    private GridPane episodesGrid;

    @FXML
    private ImageView like;

    @FXML
    private ImageView logout;

    @FXML
    private BorderPane mainPage;

    @FXML
    private Label numEpisodes;

    @FXML
    private Label numReviews;

    @FXML
    private Label noEpisodeMessage;

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label rating;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchBarText;

    @FXML
    private Label showReviews;

    @FXML
    private Label title;

    @FXML
    private Button updatePodcast;

    @FXML
    private ImageView watchlater;

    @FXML
    private ProgressBar fiveStars;

    @FXML
    private ProgressBar fourStars;

    @FXML
    private ProgressBar threeStars;

    @FXML
    private ProgressBar twoStars;

    @FXML
    private ProgressBar oneStar;

    private Podcast podcast;
    private boolean liked;
    private boolean watchLatered;
    private int row, column;

    @FXML
    void clickOnAddEpisode(MouseEvent event) throws IOException {
        Logger.info("Add episode");

        BoxBlur blur = new BoxBlur(3, 3 , 3);
        this.mainPage.setEffect(blur);

        // loading the fxml file of the popup dialog
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("EpisodeEdit.fxml"));

        // creating dialog Pane
        DialogPane episodeEditDialogPane = fxmlLoader.load();
        EpisodeEditController editController = fxmlLoader.getController();

        Episode episode = new Episode();

        // pass new episode object to dialog pane
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(this.mainPage.getScene().getWindow());
        dialog.setDialogPane(episodeEditDialogPane);
        dialog.setTitle("Update Podcast");
        editController.setData(episode, true);

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/logo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        dialog.showAndWait();

        // check if empty
        if (episode.getName() == null|| episode.getReleaseDate() == null || episode.getDescription() == null || episode.getTimeMillis() == 0) {
            Logger.error("No episode created");
        } else {
            // update podcast
            Logger.info("Created episode : " + episode.toString());
            this.podcast.addEpisode(episode);

            // add episode to page
            FXMLLoader fxmlEpisodeLoader = new FXMLLoader();
            fxmlEpisodeLoader.setLocation(getClass().getClassLoader().getResource("Episode.fxml"));

            // create new podcast element
            AnchorPane newEpisode = fxmlEpisodeLoader.load();
            EpisodeController controller = fxmlEpisodeLoader.getController();
            controller.setData(episode, this.podcast.getAuthorName(), this.mainPage);

            // add new podcast to grid
            this.episodesGrid.add(newEpisode, this.column, this.row++);
        }

        this.mainPage.setEffect(null);
    }

    @FXML
    void clickOnDeletePodcast(MouseEvent event) {
        Logger.info("Delete podcast");
    }

    @FXML
    void clickOnUpdatePodcast(MouseEvent event) throws IOException {
        BoxBlur blur = new BoxBlur(3, 3 , 3);
        this.mainPage.setEffect(blur);

        // loading the fxml file of the popup dialog
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastUpdate.fxml"));

        // creating dialog Pane
        DialogPane podcastUpdateDialogPane = fxmlLoader.load();
        PodcastUpdateController updateController = fxmlLoader.getController();

        // create a copy of podcast
        Podcast newPodcast = new Podcast(this.podcast);

        // pass podcast's data to dialog pane
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(this.mainPage.getScene().getWindow());
        dialog.setDialogPane(podcastUpdateDialogPane);
        dialog.setTitle("Update Podcast");
        updateController.setData(newPodcast);

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/logo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        dialog.showAndWait();

        // check if modified
        if (!this.podcast.equals(newPodcast)) {
            // update podcast
            Logger.info("New podcast : " + this.podcast.toString());
            this.podcast = newPodcast;

            // update page
            this.title.setText(podcast.getName());
            this.author.setText(podcast.getAuthorName());
            this.country.setText(podcast.getCountry());
            this.content.setText("Content: " + podcast.getContentAdvisoryRating());
            Image image = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
            this.podcastImage.setImage(image);
            this.category.setText(podcast.getPrimaryCategory());
            this.numEpisodes.setText(podcast.getEpisodes().size() + " episodes");
            this.rating.setText("" + podcast.getRating());
            this.numReviews.setText(" out of 5.0 • " + podcast.getReviews().size() + " reviews");

            // TODO: update rating and stars
        }

        this.mainPage.setEffect(null);
    }

    @FXML
    void mouseOnAuthor(MouseEvent event) {
        this.author.setTextFill(Color.color(0.6, 0.6, 0.6));
    }

    @FXML
    void mouseOutAuthor(MouseEvent event) {
        this.author.setTextFill(Color.color(0.0, 0.0, 1.0));
    }

    @FXML
    void mouseOnReview(MouseEvent event) {
        this.showReviews.setTextFill(Color.color(0.6, 0.6, 0.6));
    }

    @FXML
    void mouseOutReview(MouseEvent event) {
        this.showReviews.setTextFill(Color.color(0.0, 0.0, 1.0));
    }

    @FXML
    void clickOnAuthor(MouseEvent event) throws IOException {
        Logger.info("Show author");
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.podcast.getAuthorName());
    }

    @FXML
    void clickOnReviews(MouseEvent event) throws IOException {
        StageManager.showPage("ReviewPage.fxml", this.podcast.getId());
    }

    @FXML
    void clickOnLike(MouseEvent event) {
        this.liked = !this.liked;

        if (this.liked)
            Logger.info("Like");
        else
            Logger.info("Dislike");
    }

    @FXML
    void clickOnLogout(MouseEvent event) throws IOException {
        MyPodcastDB.getInstance().setSession(null, null);
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void clickOnWatchlater(MouseEvent event) {
        this.watchLatered = !this.watchLatered;

        if (this.watchLatered)
            Logger.info("Added to watchlater");
        else
            Logger.info("Removed from watchlater");
    }

    @FXML
    void mouseOnLike(MouseEvent event) {
        Logger.info("On like");
    }

    @FXML
    void mouseOnWatchlater(MouseEvent event) {
        Logger.info("On watchlater");
    }

    @FXML
    void mouseOutLike(MouseEvent event) {
        Logger.info("Out of like");
    }

    @FXML
    void mouseOutWatchlater(MouseEvent event) {
        Logger.info("Out of watchlater");
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
        Logger.info("Click on user ");
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author"))
            StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage());
        else if (actorType.equals("User"))
            StageManager.showPage(ViewNavigator.USERPAGE.getPage());
        else
            Logger.error("Unidentified Actor Type");
    }

    public void initialize() throws IOException {
        Logger.info("Podcast ID : " + StageManager.getObjectIdentifier());

        // Podcast Test
        Podcast podcast = new Podcast(StageManager.getObjectIdentifier(), "Scaling Global", "00000000", "Slate Studios", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/60x60bb.jpg", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg", "Clean", "Trinidad & Tobago", "Business", null, new Date());
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
        if (sessionType.equals("Author") && ((Author)MyPodcastDB.getInstance().getSessionActor()).getName().equals(this.podcast.getAuthorName())) {
            // owner of podcast
            this.like.setVisible(false);
            this.watchlater.setVisible(false);
        } else if (sessionType.equals("User") || sessionType.equals("Author") || sessionType.equals("Unregistered")) {
            // visitator
            this.updatePodcast.setVisible(false);
            this.deletePodcast.setVisible(false);
            this.addEpisode.setVisible(false);
        } else if (sessionType.equals("Admin")) {
            this.like.setVisible(false);
            this.watchlater.setVisible(false);
            this.updatePodcast.setVisible(false);
            this.addEpisode.setVisible(false);
        }

        // no reviews message
        if (!podcast.getEpisodes().isEmpty()) {
            this.noEpisodeMessage.setVisible(false);
            this.noEpisodeMessage.setPadding(new Insets(-20, 0, 0, 0));
        } else {
            this.scroll.setVisible(false);
        }

        // status test
        this.liked = false;
        this.watchLatered = false;

        // image setup
        Image likeIcon = ImageCache.getImageFromLocalPath("/img/hearts.png");
        this.like.setImage(likeIcon);
        Image watchlaterIcon = ImageCache.getImageFromLocalPath("/img/pin.png");
        this.watchlater.setImage(watchlaterIcon);

        // podcast initialization
        this.title.setText(podcast.getName());
        this.author.setText(podcast.getAuthorName());
        this.country.setText(podcast.getCountry());
        this.content.setText("Content: " + podcast.getContentAdvisoryRating());
        Image image = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
        this.podcastImage.setImage(image);
        this.category.setText(podcast.getPrimaryCategory());
        this.numEpisodes.setText(podcast.getEpisodes().size() + " episodes");
        this.rating.setText("" + podcast.getRating());
        this.numReviews.setText(" out of 5.0 • " + podcast.getReviews().size() + " reviews");

        // insert episodes in grid
        this.row = 0;
        this.column = 0;
        for (Episode ep : podcast.getEpisodes()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("Episode.fxml"));

            // create new podcast element
            AnchorPane newEpisode = fxmlLoader.load();
            EpisodeController controller = fxmlLoader.getController();
            controller.setData(ep, this.podcast.getAuthorName(), this.mainPage);

            // add new podcast to grid
            this.episodesGrid.add(newEpisode, this.column, this.row++);
        }

        // calculate the progress bar for ratings
        this.oneStar.setProgress(0.1);
        this.twoStars.setProgress(0.1);
        this.threeStars.setProgress(0.2);
        this.fourStars.setProgress(0.3);
        this.fiveStars.setProgress(0.3);
    }
}
