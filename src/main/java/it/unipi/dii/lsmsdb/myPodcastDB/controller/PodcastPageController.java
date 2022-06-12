package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.PodcastPageService;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import static java.lang.Math.round;

public class PodcastPageController {

    @FXML
    private Button addEpisode;

    @FXML
    private VBox addEpisodeWrapper;

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
    private Tooltip secondaryCatTooltip;

    @FXML
    private Label secondaryCategory;

    @FXML
    private Label showReviews;

    @FXML
    private ProgressBar threeStars;

    @FXML
    private Label title;

    @FXML
    private Tooltip titleTooltip;

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

    private PodcastPageService service;

    /**************************** Click and Enter Events ****************************/

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
        editController.setData(episode, true, this.mainPage);

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/logo.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        dialog.showAndWait();

        // check if empty
        if (episode.getName() == null|| episode.getReleaseDate() == null || episode.getDescription() == null || episode.getTimeMillis() == 0) {
            Logger.error("No episode created");
        } else {
            // update podcast in persistence
            Logger.info("Created episode : " + episode.toString());
            int result = this.service.addEpisode(this.podcast, episode);

            // if the service is successful update the page
            if (result == 0) {
                // if first episode enable the grid and disable the text
                if (podcast.getEpisodes() != null && podcast.getEpisodes().size() == 1) {
                    this.noEpisodeMessage.setVisible(false);
                    this.noEpisodeMessage.setPadding(new Insets(-20, 0, 0, 0));
                    this.scroll.setVisible(true);
                }

                // add episode to page
                FXMLLoader fxmlEpisodeLoader = new FXMLLoader();
                fxmlEpisodeLoader.setLocation(getClass().getClassLoader().getResource("Episode.fxml"));

                // create new podcast element
                AnchorPane newEpisode = fxmlEpisodeLoader.load();
                EpisodeController controller = fxmlEpisodeLoader.getController();
                controller.setData(episode, this.podcast, this.mainPage, this.service, this);

                // add new podcast to grid and update podcast info
                this.episodesGrid.add(newEpisode, this.column, this.row++);
                this.numEpisodes.setText(podcast.getEpisodes().size() + " episodes");
            }

            // error
            else if (result != 0) {
                // already used title
                if (result == -2)
                    DialogManager.getInstance().createErrorAlert(this.mainPage, "This title has already been used");
                // other error
                else
                    DialogManager.getInstance().createErrorAlert(this.mainPage, "Failed to add episode");
            }
        }

        this.mainPage.setEffect(null);
    }

    @FXML
    void clickOnAuthor(MouseEvent event) throws IOException {
        Logger.info("Show author");
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.podcast.getAuthorName());
    }

    @FXML
    void clickOnDeletePodcast(MouseEvent event) throws IOException {
        // create the blur
        BoxBlur blur = new BoxBlur(3, 3 , 3);
        this.mainPage.setEffect(blur);

        // create the dialog
        int result = 0;
        boolean confirm = DialogManager.getInstance().createConfirmationAlert(this.mainPage, "Do you really want to delete this podcast?");
        if (confirm)
            result = this.service.deletePodcast(this.podcast);

        this.mainPage.setEffect(null);

        if (confirm) {
            // if successful go to author/admin page
            if (result == 0) {
                if (MyPodcastDB.getInstance().getSessionType().equals("Author"))
                    StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), ((Author)MyPodcastDB.getInstance().getSessionActor()).getName());
                else
                    StageManager.showPage(ViewNavigator.ADMINDASHBOARD.getPage());
            }

            // if failed show an alert
            else {
                this.mainPage.setEffect(blur);
                DialogManager.getInstance().createErrorAlert(this.mainPage, "Error in removing Podcast");
                this.mainPage.setEffect(null);
            }
        }
    }

    @FXML
    void clickOnHome(MouseEvent event) throws IOException {
        Logger.info("Click on home");
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
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
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Something goes wrong");
        }
    }

    @FXML
    void clickOnLogout(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
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
            // update podcast on persistence
            int result = this.service.updatePodcast(this.podcast, newPodcast);

            // if update is succesfull update the page
            if (result == 0) {
                this.podcast = newPodcast;

                // update page
                this.updatePodcastPage();
            }

            // error in update
            else if (result != 0) {
                DialogManager.getInstance().createErrorAlert(this.mainPage, "Failed to update episode");
            }
        }

        this.mainPage.setEffect(null);
    }

    @FXML
    void clickOnReviews(MouseEvent event) throws IOException {
        StageManager.showPage("ReviewPage.fxml", this.podcast.getId());
    }

    @FXML
    void clickOnSearch(MouseEvent event) throws IOException {
        Logger.info("Click on search");

        if (!this.searchBarText.getText().equals("")) {
            String searchString = this.searchBarText.getText();
            StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchString);
        }
    }

    @FXML
    void clickOnUser(MouseEvent event) throws IOException {
        Logger.info("Click on user ");
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author"))
            StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), ((Author)MyPodcastDB.getInstance().getSessionActor()).getName());
        else if (actorType.equals("User"))
            StageManager.showPage(ViewNavigator.USERPAGE.getPage(), ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername());
        else if (actorType.equals("Admin"))
            StageManager.showPage(ViewNavigator.ADMINDASHBOARD.getPage());
        else
            Logger.error("Unidentified Actor Type");
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
            DialogManager.getInstance().createErrorAlert(this.mainPage, "Something goes wrong");
        }
    }

    @FXML
    void enterOnSearch(KeyEvent event) throws IOException {
        // if enter is pressed go to search
        if (event.getCode().equals(KeyCode.ENTER) && !this.searchBarText.getText().equals("")) {
            String searchString = this.searchBarText.getText();
            StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchString);
        }
    }

    /******************************** Mouse on Event ********************************/

    @FXML
    void mouseOnAuthor(MouseEvent event) {
        this.author.setTextFill(Color.color(0.6, 0.6, 0.6));
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
    void mouseOnReview(MouseEvent event) {
        this.showReviews.setTextFill(Color.color(0.6, 0.6, 0.6));
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

    /******************************* Mouse out Event ********************************/

    @FXML
    void mouseOutAuthor(MouseEvent event) {
        this.author.setTextFill(Color.color(0.0, 0.0, 1.0));
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
    void mouseOutReview(MouseEvent event) {
        this.showReviews.setTextFill(Color.color(0.0, 0.0, 1.0));
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

    /***************************** Initialize e Utility *****************************/

    public void initialize() throws IOException {
        // Get the podcast info from service
        Podcast podcast = new Podcast();
        podcast.setId(StageManager.getObjectIdentifier());
        this.service = new PodcastPageService();

        // load podcast from service
        Boolean result;
        String sessionType = MyPodcastDB.getInstance().getSessionType();
        if (sessionType.equals("User")) {
            Boolean[] status = new Boolean[2];
            result = this.service.loadPodcastPageForUsers(podcast, status);
            if (result) {
                this.watchLatered = status[0];
                this.liked = status[1];
            } else {
                Platform.runLater(() -> {
                    try {
                        redirect();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
                return;
            }
        } else {
            result = this.service.loadPodcastPageForNotUser(podcast);
            if (!result) {
                Platform.runLater(() -> {
                    try {
                        redirect();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                return;
            }
        }

        // check service result
        if (result) {
            this.podcast = podcast;
        }

        // actor specific action
        if (sessionType.equals("Author") && ((Author)MyPodcastDB.getInstance().getSessionActor()).getName().equals(this.podcast.getAuthorName())) {
            // owner of podcast
            this.like.setVisible(false);
            this.watchlater.setVisible(false);
            Author author = (Author) MyPodcastDB.getInstance().getSessionActor();
            Image picture = ImageCache.getImageFromLocalPath(author.getPicturePath());
            userPicture.setImage(picture);
        } else if (sessionType.equals("User") || sessionType.equals("Author") || sessionType.equals("Unregistered")) {
            // visitator
            this.updatePodcast.setVisible(false);
            this.deletePodcast.setVisible(false);
            this.addEpisode.setVisible(false);
            this.addEpisodeWrapper.setStyle("-fx-max-height: 0; -fx-max-width: 0; -fx-pref-height: 0; -fx-pref-width: 0; -fx-min-height: 0; -fx-min-width: 0; ");

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

                // if unregistered remove even the profile icon
                if (sessionType.equals("Unregistered")) {
                    this.userPictureWrapper.setVisible(false);
                    this.userPictureWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");
                }
            }
        } else if (sessionType.equals("Admin")) {
            this.like.setVisible(false);
            this.watchlater.setVisible(false);
            this.updatePodcast.setVisible(false);
            this.addEpisode.setVisible(false);
            this.addEpisodeWrapper.setStyle("-fx-max-height: 0; -fx-max-width: 0; -fx-pref-height: 0; -fx-pref-width: 0; -fx-min-height: 0; -fx-min-width: 0; ");
        }

        // no episodes message
        if (podcast.getEpisodes() != null && !podcast.getEpisodes().isEmpty()) {
            this.noEpisodeMessage.setVisible(false);
            this.noEpisodeMessage.setPadding(new Insets(-20, 0, 0, 0));
        } else {
            this.scroll.setVisible(false);
        }

        // podcast initialization
        this.updatePodcastPage();
    }

    public void updatePodcastPage() throws IOException {
        this.title.setText(podcast.getName());
        this.titleTooltip.setText(podcast.getName());
        this.author.setText(podcast.getAuthorName());
        this.country.setText(podcast.getCountry());
        this.content.setText("Content: " + podcast.getContentAdvisoryRating());
        Image image = ImageCache.getImageFromURL(podcast.getArtworkUrl600());
        this.podcastImage.setImage(image);
        this.category.setText(podcast.getPrimaryCategory());
        List<String> secondaryCategories = podcast.getCategories();
        if (secondaryCategories.contains(podcast.getPrimaryCategory()))
            secondaryCategories.remove(podcast.getPrimaryCategory());
        if (secondaryCategories.isEmpty())
            secondaryCategories.add("None");
        this.secondaryCategory.setText(String.join(", ", secondaryCategories));
        this.secondaryCatTooltip.setText(String.join(", ", secondaryCategories));
        this.numEpisodes.setText(podcast.getEpisodes().size() + " episodes");
        int ratingIntermediate = (int)(podcast.getRating() * 10);
        this.rating.setText("" + (ratingIntermediate / 10) + "," + (ratingIntermediate % 10));
        this.numReviews.setText(" out of 5.0 • " + podcast.getReviews().size() + " reviews");

        // order episodes by release date
        Collections.sort(this.podcast.getEpisodes(), new Comparator<Episode>() {
            @Override
            public int compare(Episode e1, Episode e2) {
                return e1.getReleaseDate().compareTo(e2.getReleaseDate());
            }
        });

        // insert episodes in grid
        this.row = 0;
        this.column = 0;
        if (!this.episodesGrid.getChildren().isEmpty()) {
            this.episodesGrid.getChildren().clear();                        // remove all elements
        }
        for (Episode ep : podcast.getEpisodes()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("Episode.fxml"));

            // create new podcast element
            AnchorPane newEpisode = fxmlLoader.load();
            EpisodeController controller = fxmlLoader.getController();
            controller.setData(ep, this.podcast, this.mainPage, this.service, this);

            // add new podcast to grid
            this.episodesGrid.add(newEpisode, this.column, this.row++);
        }

        // if there are no episodes show the message
        if (podcast.getEpisodes() != null && podcast.getEpisodes().size() == 0) {
            this.noEpisodeMessage.setVisible(true);
            this.noEpisodeMessage.setPadding(new Insets(20, 0, 0, 0));
            this.scroll.setVisible(false);
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

    private void redirect() throws IOException {
        // hide the text
        this.title.setText("");
        this.author.setText("");
        this.country.setText("");
        this.numEpisodes.setText("");
        this.category.setText("");
        this.secondaryCategory.setText("");
        this.rating.setText("");
        this.numReviews.setText("");

        // create alert end redirect to homepage
        DialogManager.getInstance().createErrorAlert(this.mainPage, "The requested podcast is not available");
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }
}
