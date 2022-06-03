package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.PodcastService;
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
import javafx.scene.layout.VBox;
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
    private Label category;

    @FXML
    private Label content;

    @FXML
    private Label country;

    @FXML
    private Button deletePodcast;

    @FXML
    private GridPane episodesGrid;

    @FXML
    private ProgressBar fiveStars;

    @FXML
    private ProgressBar fourStars;

    @FXML
    private ImageView home;

    @FXML
    private VBox homeWrapper;

    @FXML
    private ImageView like;

    @FXML
    private ImageView logout;

    @FXML
    private VBox logoutWrapper;

    @FXML
    private BorderPane mainPage;

    @FXML
    private Label noEpisodeMessage;

    @FXML
    private Label numEpisodes;

    @FXML
    private Label numReviews;

    @FXML
    private ProgressBar oneStar;

    @FXML
    private ImageView podcastImage;

    @FXML
    private Label rating;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchBarText;

    @FXML
    private ImageView searchButton;

    @FXML
    private Label showReviews;

    @FXML
    private ProgressBar threeStars;

    @FXML
    private Label title;

    @FXML
    private ProgressBar twoStars;

    @FXML
    private Button updatePodcast;

    @FXML
    private ImageView userPicture;

    @FXML
    private VBox userPictureWrapper;

    @FXML
    private ImageView watchlater;

    private Podcast podcast;
    private boolean liked;
    private boolean watchLatered;
    private int row, column;

    private PodcastService service;

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
    void clickOnUpdatePodcast(MouseEvent event) throws Exception {
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
            Logger.info("New podcast : " + newPodcast.toString());
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
        Boolean result = this.service.setLike(this.podcast.getId(), !this.liked);
        if (result) {
            this.liked = !this.liked;

            // update the image
            Image likeIcon;
            if (this.liked)
                likeIcon = ImageCache.getImageFromLocalPath("/img/Favorite_52px.png");
            else
                likeIcon = ImageCache.getImageFromLocalPath("/img/Favorite_50px.png");
            this.like.setImage(likeIcon);
        } else {
            // TODO: Error case
        }
    }

    @FXML
    void clickOnLogout(MouseEvent event) throws IOException {
        MyPodcastDB.getInstance().setSession(null, null);
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void clickOnWatchlater(MouseEvent event) {
        Boolean result = this.service.setWatchLater(this.podcast.getId(), !this.watchLatered);
        if (result) {
            this.watchLatered = !this.watchLatered;

            // update the image
            Image watchlaterIcon;
            if (this.watchLatered)
                watchlaterIcon = ImageCache.getImageFromLocalPath("/img/unpin.png");
            else
                watchlaterIcon = ImageCache.getImageFromLocalPath("/img/pin.png");
            this.watchlater.setImage(watchlaterIcon);
        } else {
            // TODO: Error case
        }
    }

    @FXML
    void mouseOnLike(MouseEvent event) {
        // update the image
        Image likeIcon;
        if (!this.liked)
            likeIcon = ImageCache.getImageFromLocalPath("/img/Favorite_52px.png");
        else
            likeIcon = ImageCache.getImageFromLocalPath("/img/Favorite_50px.png");
        this.like.setImage(likeIcon);
    }

    @FXML
    void mouseOnWatchlater(MouseEvent event) {
        // update the image
        Image watchlaterIcon;
        if (!this.watchLatered)
            watchlaterIcon = ImageCache.getImageFromLocalPath("/img/unpin.png");
        else
            watchlaterIcon = ImageCache.getImageFromLocalPath("/img/pin.png");
        this.watchlater.setImage(watchlaterIcon);
    }

    @FXML
    void mouseOutLike(MouseEvent event) {
        // update the image
        Image likeIcon;
        if (this.liked)
            likeIcon = ImageCache.getImageFromLocalPath("/img/Favorite_52px.png");
        else
            likeIcon = ImageCache.getImageFromLocalPath("/img/Favorite_50px.png");
        this.like.setImage(likeIcon);
    }

    @FXML
    void mouseOutWatchlater(MouseEvent event) {
        // update the image
        Image watchlaterIcon;
        if (this.watchLatered)
            watchlaterIcon = ImageCache.getImageFromLocalPath("/img/unpin.png");
        else
            watchlaterIcon = ImageCache.getImageFromLocalPath("/img/pin.png");
        this.watchlater.setImage(watchlaterIcon);
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
        else if (actorType.equals("Admin"))
            StageManager.showPage(ViewNavigator.ADMINDASHBOARD.getPage());
        else
            Logger.error("Unidentified Actor Type");
    }

    public void initialize() throws IOException {
        // Get the podcast info from service
        Podcast podcast = new Podcast();
        podcast.setId(StageManager.getObjectIdentifier());
        this.service = new PodcastService();

        // load podcast from service
        Boolean result;
        String sessionType = MyPodcastDB.getInstance().getSessionType();
        if (sessionType.equals("User")) {
            Boolean[] status = new Boolean[2];
            result = this.service.loadPodcastPageForUsers(podcast, status);
            this.watchLatered = status[0];
            this.liked = status[1];
        } else {
            result = this.service.loadPodcastPageForNotUser(podcast);
        }

        // check service result
        if (result) {
            this.podcast = podcast;
        } else {
            // TODO: Error case
        }

        // actor specific action
        if (sessionType.equals("Author") && ((Author)MyPodcastDB.getInstance().getSessionActor()).getName().equals(this.podcast.getAuthorName())) {
            // owner of podcast
            this.like.setVisible(false);
            this.watchlater.setVisible(false);
        } else if (sessionType.equals("User") || sessionType.equals("Author") || sessionType.equals("Unregistered")) {
            // visitator
            this.updatePodcast.setVisible(false);
            this.deletePodcast.setVisible(false);
            this.addEpisode.setVisible(false);

            // image setup
            if (sessionType.equals("User")) {
                // like image
                Image likeIcon;
                if (this.liked)
                    likeIcon = ImageCache.getImageFromLocalPath("/img/Favorite_52px.png");
                else
                    likeIcon = ImageCache.getImageFromLocalPath("/img/Favorite_50px.png");
                this.like.setImage(likeIcon);

                // watch later
                Image watchlaterIcon;
                if (this.watchLatered)
                    watchlaterIcon = ImageCache.getImageFromLocalPath("/img/unpin.png");
                else
                    watchlaterIcon = ImageCache.getImageFromLocalPath("/img/pin.png");
                this.watchlater.setImage(watchlaterIcon);

                // profile picture
                User user = (User)MyPodcastDB.getInstance().getSessionActor();
                Image picture = ImageCache.getImageFromLocalPath(user.getPicturePath());
                userPicture.setImage(picture);
            } else {
                this.like.setVisible(false);
                this.watchlater.setVisible(false);

                // if author update the picture profile
                if (sessionType.equals("Author")) {
                    Author author = (Author) MyPodcastDB.getInstance().getSessionActor();
                    Image picture = ImageCache.getImageFromLocalPath(author.getPicturePath());
                    userPicture.setImage(picture);
                }

                // if unregisterd remove even the profile icon
                if (sessionType.equals("Unregistered")) {
                    this.userPictureWrapper.setVisible(false);
                    this.userPictureWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");
                    // this.logoutWrapper.setVisible(false);
                    // this.logoutWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");
                }
            }
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
        int[] numReview = new int[5];
        for (Entry<String, Integer> review : this.podcast.getReviews()) {
            switch (review.getValue()) {
                case 1:
                    numReview[0]++;
                    break;
                case 2:
                    numReview[1]++;
                    break;
                case 3:
                    numReview[2]++;
                    break;
                case 4:
                    numReview[3]++;
                    break;
                case 5:
                    numReview[4]++;
                    break;
            }
        }
        this.oneStar.setProgress((float)numReview[0] / this.podcast.getReviews().size());
        this.twoStars.setProgress((float)numReview[1] / this.podcast.getReviews().size());
        this.threeStars.setProgress((float)numReview[2] / this.podcast.getReviews().size());
        this.fourStars.setProgress((float)numReview[3] / this.podcast.getReviews().size());
        this.fiveStars.setProgress((float)numReview[4] / this.podcast.getReviews().size());
    }
}
