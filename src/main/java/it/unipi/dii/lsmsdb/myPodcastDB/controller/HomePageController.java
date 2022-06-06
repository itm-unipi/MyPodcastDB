package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AdminService;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorService;
import it.unipi.dii.lsmsdb.myPodcastDB.service.UserService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Box;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomePageController {

    private User userPreview;

    private final String actorType;

    // Declaring lists in order to contain queries results
    private List<Triplet<Podcast, Float, Boolean>> topRated;

    private List<Pair<Podcast, Integer>> mostLikedPodcasts;

    private List<Triplet<Author, Integer, Boolean>> mostFollowedAuthors;

    private List<Podcast> watchlist;

    private List<Podcast> topGenres;

    private List<Podcast> basedOnFollowedUsers;

    private List<Podcast> basedOnWatchlist;

    private List<Pair<Author, Boolean>> suggestedAuthors;

    private int limit;

    private int row;

    private int column;

    @FXML
    private BorderPane MainPage;

    @FXML
    private ImageView home;

    @FXML
    private ImageView actorPicture;

    @FXML
    private ImageView logout;

    @FXML
    private Label username;

    @FXML
    private ImageView searchButton;

    @FXML
    private TextField searchText;

    @FXML
    private GridPane gridMostFollowedAuthors;

    @FXML
    private GridPane gridSuggestedAuthors;

    @FXML
    private GridPane gridMostLikedPodcasts;

    @FXML
    private GridPane gridPodcastsBasedOnWatchlist;

    @FXML
    private GridPane gridSuggestedForAuthor;

    @FXML
    private GridPane gridSuggestedForCategory;

    @FXML
    private GridPane gridSuggestedForUser;

    @FXML
    private GridPane gridTopRated;

    @FXML
    private GridPane gridWatchlist;

    @FXML
    private ScrollPane scrollMostFollowedAuthors;

    @FXML
    private ScrollPane scrollSuggestedAuthors;

    @FXML
    private ScrollPane scrollMostLikedPodcasts;

    @FXML
    private ScrollPane scrollPodcastsBasedOnWatchlist;

    @FXML
    private ScrollPane scrollSuggestedForAuthor;

    @FXML
    private ScrollPane scrollSuggestedForCategory;

    @FXML
    private ScrollPane scrollSuggestedForUser;

    @FXML
    private ScrollPane scrollTopRated;

    @FXML
    private ScrollPane scrollWatchlist;

    @FXML
    private VBox boxActorProfile;

    @FXML
    private VBox boxLogout;

    @FXML
    private VBox boxBasedOnUsers;

    @FXML
    private VBox boxPodcastsBasedOnWatchlist;

    @FXML
    private VBox boxSuggestedAuthors;

    @FXML
    private VBox boxTopGenres;

    @FXML
    private VBox boxTopRated;

    @FXML
    private VBox boxWatchlist;

    @FXML
    private VBox boxMostLikedPodcasts;

    @FXML
    private VBox boxMostFollowedAuthors;

    @FXML
    private Button btnShowMoreSuggested;

    @FXML
    private HBox boxBtnShowMoreSuggested;

    public HomePageController() {
        // Load information about the actor of the session
        this.actorType = MyPodcastDB.getInstance().getSessionType();

        // Declaring lists in order to contain queries results
        this.topRated = new ArrayList<>();
        this.mostLikedPodcasts = new ArrayList<>();
        this.mostFollowedAuthors = new ArrayList<>();
        this.watchlist = new ArrayList<>();
        this.topGenres = new ArrayList<>();
        this.basedOnFollowedUsers = new ArrayList<>();
        this.basedOnWatchlist = new ArrayList<>();
        this.suggestedAuthors = new ArrayList<>();

        // Limit for each query
        this.limit = 5;
    }

    /*********** Navigator Events (Profile, Home, Search) *************/

    @FXML
    void onClickActorProfile(MouseEvent event) throws IOException {
        switch (this.actorType) {
            case "Author" ->
                    StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), ((Author) MyPodcastDB.getInstance().getSessionActor()).getName());
            case "User" ->
                    StageManager.showPage(ViewNavigator.USERPAGE.getPage(), ((User) MyPodcastDB.getInstance().getSessionActor()).getUsername());
            case "Admin" ->
                    StageManager.showPage(ViewNavigator.ADMINDASHBOARD.getPage());
            default -> Logger.error("Unidentified Actor Type!");
        }
    }

    @FXML
    void onClickSearch(MouseEvent event) throws IOException {
        if (!searchText.getText().isEmpty()) {
            String text = searchText.getText();
            Logger.info("Searching for " + text);
            StageManager.showPage(ViewNavigator.SEARCH.getPage(), text);
        } else {
            Logger.error("Field cannot be empty!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(MainPage.getScene().getWindow());
            alert.setTitle("Search Error");
            alert.setHeaderText(null);
            alert.setContentText("Search field cannot be empty!");
            alert.setGraphic(null);;
            alert.showAndWait();
        }
    }

    @FXML
    void onEnterPressedSearch(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            if (!searchText.getText().isEmpty()) {
                String text = searchText.getText();
                Logger.info("Searching for " + text);
                StageManager.showPage(ViewNavigator.SEARCH.getPage(), text);
            } else {
                Logger.error("Field cannot be empty!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(MainPage.getScene().getWindow());
                alert.setTitle("Search Error");
                alert.setHeaderText(null);
                alert.setContentText("Search field cannot be empty!");
                alert.setGraphic(null);;
                alert.showAndWait();
            }
        }
    }

    @FXML
    void onClickHome(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }

    @FXML
    void onClickLogout(MouseEvent event) throws IOException {
        Logger.info("Logout button clicked");
        // TODO: clear the session
        MyPodcastDB.getInstance().setSession(null, null);
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    /***** ARROWS NEXT AND BACK *****/

    @FXML
    void nextMostLikedPodcasts(MouseEvent event) {
        Logger.info("next podcast in mostLikedPodcasts");
        double scrollValue = 1;
        if (scrollMostLikedPodcasts.getHvalue() == 1.0)
            scrollValue = -1;
        scrollMostLikedPodcasts.setHvalue(scrollMostLikedPodcasts.getHvalue() + scrollValue);
    }

    @FXML
    void backMostLikedPodcasts(MouseEvent event) {
        Logger.info("back podcast in mostLikedPodcasts");
        double scrollValue = 1;
        if (scrollMostLikedPodcasts.getHvalue() == 0.0)
            scrollMostLikedPodcasts.setHvalue(1.0);
        else
            scrollMostLikedPodcasts.setHvalue(scrollMostLikedPodcasts.getHvalue() - scrollValue);
    }

    @FXML
    void nextSuggestedAuthor(MouseEvent event) {
        Logger.info("next suggested author");
        double scrollValue = 1;
        if (scrollSuggestedForAuthor.getHvalue() == 1.0)
            scrollValue = -1;
        scrollSuggestedForAuthor.setHvalue(scrollSuggestedForAuthor.getHvalue() + scrollValue);
    }

    @FXML
    void backSuggestedAuthor(MouseEvent event) {
        Logger.info("back podcast author");
        double scrollValue = 1;
        if (scrollSuggestedForAuthor.getHvalue() == 0.0)
            scrollSuggestedForAuthor.setHvalue(1.0);
        else
            scrollSuggestedForAuthor.setHvalue(scrollSuggestedForAuthor.getHvalue() - scrollValue);
    }

    @FXML
    void nextWatchlist(MouseEvent event) {
        Logger.info("next podcast in watchlist");
        double scrollValue = 1;
        if (scrollWatchlist.getHvalue() == 1.0)
            scrollValue = -1;
        scrollWatchlist.setHvalue(scrollWatchlist.getHvalue() + scrollValue);
    }

    @FXML
    void backWatchlist(MouseEvent event) {
        Logger.info("back podcast in watchlist");
        double scrollValue = 1;
        if (scrollWatchlist.getHvalue() == 0.0)
            scrollWatchlist.setHvalue(1.0);
        else
            scrollWatchlist.setHvalue(scrollWatchlist.getHvalue() - scrollValue);
    }

    @FXML
    void backSuggestedCategory(MouseEvent event) {
        Logger.info("back podcast category");
        double scrollValue = 1;
        if (scrollSuggestedForCategory.getHvalue() == 0.0)
            scrollSuggestedForCategory.setHvalue(1.0);
        else
            scrollSuggestedForCategory.setHvalue(scrollSuggestedForCategory.getHvalue() - scrollValue);
    }

    @FXML
    void backSuggestedUser(MouseEvent event) {
        Logger.info("back podcast user");
        double scrollValue = 1;
        if (scrollSuggestedForUser.getHvalue() == 0.0)
            scrollSuggestedForUser.setHvalue(1.0);
        else
            scrollSuggestedForUser.setHvalue(scrollSuggestedForUser.getHvalue() - scrollValue);
    }

    @FXML
    void backMostFollowedAuthors(MouseEvent event) {
        Logger.info("Clicked on back most followed authors");
    }

    @FXML
    void backPodcastsBasedOnWatchlist(MouseEvent event) {
        Logger.info("Clicked on back podcasts based on your watchlist");
    }

    @FXML
    void backTopRated(MouseEvent event) {
        Logger.info("Clicked on back top country");
    }

    @FXML
    void backSuggestedAuthors(MouseEvent event) {
        Logger.info("Clicked on back suggested authors");
    }

    @FXML
    void nextMostFollowedAuthors(MouseEvent event) {
        Logger.info("Clicked on next most followed authors");
    }

    @FXML
    void nextPodcastsBasedOnWatchlist(MouseEvent event) {
        Logger.info("Clicked on next podcasts based on watchlist");
    }

    @FXML
    void nextTopRated(MouseEvent event) {
        Logger.info("Clicked on next top rated");
    }

    @FXML
    void nextSuggestedAuthors(MouseEvent event) {
        Logger.info("Clicked on next suggested authors");
    }

    @FXML
    void nextSuggestedCategory(MouseEvent event) {
        Logger.info("next suggested category");
        double scrollValue = 1;
        if (scrollSuggestedForCategory.getHvalue() == 1.0)
            scrollValue = -1;
        scrollSuggestedForCategory.setHvalue(scrollSuggestedForCategory.getHvalue() + scrollValue);
    }

    @FXML
    void nextSuggestedUser(MouseEvent event) {
        Logger.info("next suggested user");
        double scrollValue = 1;
        if (scrollSuggestedForUser.getHvalue() == 1.0)
            scrollValue = -1;
        scrollSuggestedForUser.setHvalue(scrollSuggestedForUser.getHvalue() + scrollValue);
    }

    /********* LOADING GRIDS *********/

    void clearIndexes() {
        this.row = 0;
        this.column = 0;
    }

    void hideEmptyGrids() {
        if (topRated.isEmpty()) {
            boxTopRated.setVisible(false);
            boxTopRated.setStyle("-fx-pref-height: 0");
        }

        if (mostLikedPodcasts.isEmpty()) {
            boxMostLikedPodcasts.setVisible(false);
            boxMostLikedPodcasts.setStyle("-fx-pref-height: 0");
        }

        if (mostFollowedAuthors.isEmpty()) {
            boxMostFollowedAuthors.setVisible(false);
            boxMostFollowedAuthors.setStyle("-fx-pref-height: 0");
        }

        if (watchlist.isEmpty()) {
            boxWatchlist.setVisible(false);
            boxWatchlist.setStyle("-fx-pref-height: 0");
        }

        if (topGenres.isEmpty()) {
            boxTopGenres.setVisible(false);
            boxTopGenres.setStyle("-fx-pref-height: 0");
        }

        if (basedOnWatchlist.isEmpty()) {
            boxPodcastsBasedOnWatchlist.setVisible(false);
            boxPodcastsBasedOnWatchlist.setStyle("-fx-pref-height: 0");
        }

        if (suggestedAuthors.isEmpty()) {
            boxSuggestedAuthors.setVisible(false);
            boxSuggestedAuthors.setStyle("-fx-pref-height: 0");
        }
    }

    // Show podcasts in the user watchlist
    void loadWatchlistGrid() throws IOException {
        clearIndexes();
        for (Podcast podcast : this.watchlist){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast, 0, null);

            this.gridWatchlist.add(newPodcast, this.column++, this.row);
        }
    }

    // Suggested on podcast's category user liked
    void loadSuggestedBasedOnCategoryGrid() throws IOException {
        clearIndexes();
        for (Podcast podcast : this.topGenres){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast, 0, null);

            this.gridSuggestedForCategory.add(newPodcast, this.column++, this.row);
        }
    }

    // Suggestions based on authors in user's watchlist
    void loadSuggestedBasedOnWatchlistGrid() throws IOException {
        clearIndexes();
        for (Podcast podcast : this.basedOnWatchlist){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast, 0, null);

            this.gridPodcastsBasedOnWatchlist.add(newPodcast, this.column++, this.row);
        }
    }

    // Suggest authors based on user you follow
    void loadSuggestedAuthorsBasedOnUserGrid() throws IOException {
        clearIndexes();
        for (Pair<Author, Boolean> author : this.suggestedAuthors) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

            AnchorPane newAuthor = fxmlLoader.load();
            AuthorPreviewController controller = fxmlLoader.getController();
            controller.setData(author.getValue0(), author.getValue1(), 0, null);

            this.gridSuggestedAuthors.add(newAuthor, this.column++, this.row);
        }
    }

    void loadSuggestedOnFollowedUsers() throws IOException {
        clearIndexes();
        for (Podcast podcast : this.basedOnFollowedUsers){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast, 0, null);

            this.gridSuggestedForUser.add(newPodcast, this.column++, this.row);
        }

        // Making the grid visible
        boxBasedOnUsers.setVisible(true);
        boxBasedOnUsers.setPrefHeight(Region.USE_COMPUTED_SIZE);

        // Disabling the "Show more suggested" button
        boxBtnShowMoreSuggested.setVisible(false);
        boxBtnShowMoreSuggested.setPrefHeight(0);

        btnShowMoreSuggested.setVisible(false);
        btnShowMoreSuggested.setPrefHeight(0);
    }

    // Grids available to everyone
    void loadTopRatedPodcasts() throws IOException {
        clearIndexes();
        for (Triplet<Podcast, Float, Boolean> podcast : this.topRated){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            // Selecting the right controller's setData to distinguish the top rated podcast in user's country from the general top rated
            if (podcast.getValue2())
                controller.setData(podcast.getValue0(), 3, podcast.getValue1().toString());
            else
                controller.setData(podcast.getValue0(), 2, podcast.getValue1().toString());

            this.gridTopRated.add(newPodcast, this.column++, this.row);
        }
    }

    void loadMostLikedPodcasts() throws IOException {
        clearIndexes();
        for (Pair<Podcast, Integer> podcast : this.mostLikedPodcasts){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast.getValue0(), 1, podcast.getValue1().toString());

            this.gridMostLikedPodcasts.add(newPodcast, this.column++, this.row);
        }
    }

    void loadMostFollowedAuthors() throws IOException {
        clearIndexes();
        for (Triplet<Author, Integer, Boolean> author : this.mostFollowedAuthors) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

            AnchorPane newAuthor = fxmlLoader.load();
            AuthorPreviewController controller = fxmlLoader.getController();
            controller.setData(author.getValue0(), author.getValue2(), 1, author.getValue1().toString());

            this.gridMostFollowedAuthors.add(newAuthor, this.column++, this.row);
        }
    }

    @FXML
    void onClickShowBasedOnFollowedUsers(MouseEvent event) throws IOException {
        UserService userService = new UserService();
        userService.loadMoreSuggested(this.basedOnFollowedUsers, this.limit);
        loadSuggestedOnFollowedUsers();
    }

    @FXML
    void onHoverBtnShow(MouseEvent event) {
        this.btnShowMoreSuggested.setStyle("-fx-background-color: skyblue; -fx-background-radius: 10;  -fx-background-insets: 0; -fx-border-color: #87c7ff; -fx-border-radius: 10");
    }

    @FXML
    void onExitedBtnShow(MouseEvent event) {
        this.btnShowMoreSuggested.setStyle("-fx-background-color: #5b9bd2; -fx-background-radius: 10; -fx-background-insets: 0; -fx-border-color: transparent; -fx-border-radius: 10");
    }

    public void initialize() throws IOException {
        switch (this.actorType) {
            case "Author" -> {
                Author sessionActor = (Author) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an actor: " + sessionActor.getName());

                // Setting GUI parameters
                this.username.setText("Welcome " + sessionActor.getName() + "!");
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                this.actorPicture.setImage(image);

                // Homepage load as author
                AuthorService authorService = new AuthorService();
                authorService.loadHomepage(this.topRated, this.mostLikedPodcasts, this.mostFollowedAuthors, 5);
            }
            case "User" -> {
                User sessionActor = (User) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an user: " + sessionActor.getUsername());

                // Setting GUI params
                this.username.setText("Welcome " + sessionActor.getUsername() + "!");
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                this.actorPicture.setImage(image);

                // Homepage load as user
                UserService userService = new UserService();
                userService.loadHomepageRegistered(this.topRated, this.mostLikedPodcasts, this.mostFollowedAuthors, this.watchlist, this.topGenres, this.basedOnWatchlist, this.suggestedAuthors, this.limit);
            }
            case "Admin" -> {
                Admin sessionActor = (Admin) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an administrator: " + sessionActor.getName());

                // Setting GUI params
                this.username.setText("Welcome " + sessionActor.getName() + "!");
                Image image = ImageCache.getImageFromLocalPath("/img/userPicture.png");
                this.actorPicture.setImage(image);

                // Homepage load as admin
                AdminService adminService = new AdminService();
                adminService.loadHomepage(this.topRated, this.mostLikedPodcasts, this.mostFollowedAuthors, this.limit);
            }
            case "Unregistered" -> {
                this.username.setText("Welcome to MyPodcastDB!");
                Logger.info("I'm an unregistered user");

                // Disabling User Profile Page and Logout Button
                this.boxActorProfile.setVisible(false);
                this.boxActorProfile.setStyle("-fx-min-height: 0; -fx-pref-height: 0; -fx-min-width: 0; -fx-pref-width: 0;");

                // Homepage load as unregistered user
                UserService userService = new UserService();
                userService.loadHomepageUnregistered(this.topRated, this.mostLikedPodcasts, this.mostFollowedAuthors, this.limit);
            }
            default -> Logger.error("Unidentified Actor Type");
        }

        // Showing grids available to every actor
        loadTopRatedPodcasts();
        loadMostLikedPodcasts();
        loadMostFollowedAuthors();

        if (actorType.equals("User")) {
            // Making visible the "Show more suggested" button for the user
            btnShowMoreSuggested.setVisible(true);
            btnShowMoreSuggested.setPrefHeight(Region.USE_COMPUTED_SIZE);

            boxBtnShowMoreSuggested.setVisible(true);
            boxBtnShowMoreSuggested.setPrefHeight(80);

            // Loading grids for registered users
            loadWatchlistGrid();
            loadSuggestedBasedOnCategoryGrid();
            loadSuggestedBasedOnWatchlistGrid();
            loadSuggestedAuthorsBasedOnUserGrid();
        }

        // Hiding the empty grids
        hideEmptyGrids();
        Logger.success("Homepage loading done!");
    }
}
