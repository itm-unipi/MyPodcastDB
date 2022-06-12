package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.HomePageService;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
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

    private List<Pair<Author, Integer>> mostFollowedAuthors;

    private List<Podcast> watchlist;

    private List<Podcast> topGenres;

    private List<Podcast> podcastsBasedOnFollowedUsers;

    private List<Podcast> basedOnWatchlist;

    private List<Author> suggestedAuthors;

    private int row;

    private int column;

    /*** Variables to manage the scroll behavior ****/
    private final int podcastsToRetrieve;
    private final int podcastsToLoadInGrid;
    private boolean noMorePodcastsWatchlist;
    private boolean noMorePodcastsTopGenres;
    private boolean noMoreBasedOnWatchlist;
    private final int authorsToRetrieve;
    private final int authorsToLoadInGrid;
    private boolean noMoreSuggestedAuthors;
    private boolean noMoreBasedOnUsers;
    /***********************************************/

    @FXML
    private BorderPane mainPage;

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
    private GridPane gridPodcastsSuggestedForUsers;

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
    private ScrollPane scrollWatchlist;

    @FXML
    private ScrollPane scrollPodcastsBasedOnWatchlist;

    @FXML
    private ScrollPane scrollSuggestedForAuthor;

    @FXML
    private ScrollPane scrollSuggestedForCategory;

    @FXML
    private ScrollPane scrollPodcastsSuggestedForUsers;

    @FXML
    private ScrollPane scrollTopRated;

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

    @FXML
    private ImageView leftArrowWatchlist;

    @FXML
    private ImageView rightArrowWatchlist;

    @FXML
    private ImageView leftArrowTopGenres;

    @FXML
    private ImageView rightArrowTopGenres;

    @FXML
    private ImageView leftArrowTopRated;

    @FXML
    private ImageView rightArrowTopRated;

    @FXML
    private ImageView leftArrowMostLiked;

    @FXML
    private ImageView rightArrowMostLiked;

    @FXML
    private ImageView leftArrowMostFollowedAuthors;

    @FXML
    private ImageView rightArrowMostFollowedAuthors;

    @FXML
    private ImageView leftArrowBasedOnWatchlist;

    @FXML
    private ImageView rightArrowBasedOnWatchlist;

    @FXML
    private ImageView leftArrowAuthorsBasedOnUser;

    @FXML
    private ImageView rightArrowAuthorsBasedOnUser;
    @FXML
    private ImageView leftArrowPodcastsBasedOnUsers;

    @FXML
    private ImageView rightArrowPodcastsBasedOnUsers;

    public HomePageController() {
        // Load information about the actor of the session
        this.actorType = MyPodcastDB.getInstance().getSessionType();

        // Declaring lists in order to contain queries results
        this.topRated = new ArrayList<>();
        this.mostLikedPodcasts = new ArrayList<>();
        this.mostFollowedAuthors = new ArrayList<>();
        this.watchlist = new ArrayList<>();
        this.topGenres = new ArrayList<>();
        this.podcastsBasedOnFollowedUsers = new ArrayList<>();
        this.basedOnWatchlist = new ArrayList<>();
        this.suggestedAuthors = new ArrayList<>();

        // Podcasts retrieved from the databases in one request (corresponds to the "limit")
        this.podcastsToRetrieve = 8;
        // Podcasts to add to the grid at each "scroll finished" (taken from the "podcasts in memory" retrieved)
        this.podcastsToLoadInGrid = 6;

        this.authorsToRetrieve = 10;
        this.authorsToLoadInGrid = 8;

        // Used to avoid useless call to the services
        this.noMorePodcastsWatchlist = false;
        this.noMorePodcastsTopGenres = false;
        this.noMoreBasedOnWatchlist = false;
        this.noMoreSuggestedAuthors = false;
        this.noMoreBasedOnUsers = false;
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
            DialogManager.getInstance().createErrorAlert(mainPage, "Search Error", "Search field cannot be empty!");
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
                DialogManager.getInstance().createErrorAlert(mainPage, "Search Error", "Search field cannot be empty!");
            }
        }
    }

    @FXML
    void onClickHome(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }

    @FXML
    void onClickLogout(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    /***** ARROWS NEXT AND BACK *****/

    @FXML
    void nextMostLikedPodcasts(MouseEvent event) throws IOException {
        double scrollValue = 1.0 / (gridMostLikedPodcasts.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollMostLikedPodcasts.setHvalue(scrollMostLikedPodcasts.getHvalue() + scrollValue);

        leftArrowMostLiked.setVisible(true);
        rightArrowMostLiked.setVisible(scrollMostLikedPodcasts.getHvalue() <= 0.98 || this.mostLikedPodcasts.size() > this.gridMostLikedPodcasts.getColumnCount());

        updateMostLikedGrid();
    }

    @FXML
    void backMostLikedPodcasts(MouseEvent event) {
        double scrollValue = 1.0 / (gridMostLikedPodcasts.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollMostLikedPodcasts.setHvalue(scrollMostLikedPodcasts.getHvalue() - scrollValue);

        rightArrowMostLiked.setVisible(true);
        leftArrowMostLiked.setVisible(scrollMostLikedPodcasts.getHvalue() >= 0.01);
    }

    @FXML
    void nextWatchlist(MouseEvent event) throws IOException {
        //Logger.info(" " + (scrollWatchlist.getHvalue() - ((float)(int)(scrollWatchlist.getHvalue()*10))/10));

        double scrollValue = 1.0 / (gridWatchlist.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollWatchlist.setHvalue(scrollWatchlist.getHvalue() + scrollValue);
        Logger.info("Scroll after: " + scrollWatchlist.getHvalue());

        leftArrowWatchlist.setVisible(true);
        rightArrowWatchlist.setVisible(scrollWatchlist.getHvalue() <= 0.98 || this.watchlist.size() > this.gridWatchlist.getColumnCount());

        updateWatchlistGrid();
    }

    @FXML
    void backWatchlist(MouseEvent event) {
        double scrollValue = 1.0 / (gridWatchlist.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollWatchlist.setHvalue(scrollWatchlist.getHvalue() - scrollValue);

        rightArrowWatchlist.setVisible(true);
        leftArrowWatchlist.setVisible(scrollWatchlist.getHvalue() >= 0.01);
    }

    @FXML
    void backSuggestedCategory(MouseEvent event) {
        double scrollValue = 1.0 / (gridSuggestedForCategory.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollSuggestedForCategory.setHvalue(scrollSuggestedForCategory.getHvalue() - scrollValue);

        rightArrowTopGenres.setVisible(true);
        leftArrowTopGenres.setVisible(scrollSuggestedForCategory.getHvalue() >= 0.01);
    }

    @FXML
    void backPodcastsSuggestedOnUsers(MouseEvent event) {
        double scrollValue = 1.0 / (gridPodcastsSuggestedForUsers.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollPodcastsSuggestedForUsers.setHvalue(scrollPodcastsSuggestedForUsers.getHvalue() - scrollValue);

        rightArrowPodcastsBasedOnUsers.setVisible(true);
        leftArrowPodcastsBasedOnUsers.setVisible(scrollPodcastsSuggestedForUsers.getHvalue() >= 0.01);
    }

    @FXML
    void backMostFollowedAuthors(MouseEvent event) {
        double scrollValue = 1.0 / (gridMostFollowedAuthors.getColumnCount() - this.authorsToLoadInGrid + 1.0);
        scrollMostFollowedAuthors.setHvalue(scrollMostFollowedAuthors.getHvalue() - scrollValue);

        rightArrowMostFollowedAuthors.setVisible(true);
        leftArrowMostFollowedAuthors.setVisible(scrollMostFollowedAuthors.getHvalue() >= 0.01);
    }

    @FXML
    void backPodcastsBasedOnWatchlist(MouseEvent event) {
        double scrollValue = 1.0 / (gridPodcastsBasedOnWatchlist.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollPodcastsBasedOnWatchlist.setHvalue(scrollPodcastsBasedOnWatchlist.getHvalue() - scrollValue);

        rightArrowBasedOnWatchlist.setVisible(true);
        leftArrowBasedOnWatchlist.setVisible(scrollPodcastsBasedOnWatchlist.getHvalue() >= 0.01);
    }

    @FXML
    void backTopRated(MouseEvent event) throws IOException {
        double scrollValue = 1.0 / (gridTopRated.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollTopRated.setHvalue(scrollTopRated.getHvalue() - scrollValue);

        rightArrowTopRated.setVisible(true);
        leftArrowTopRated.setVisible(scrollTopRated.getHvalue() >= 0.01);
    }

    @FXML
    void backSuggestedAuthors(MouseEvent event) throws IOException {
        double scrollValue = 1.0 / (gridSuggestedAuthors.getColumnCount() - this.authorsToLoadInGrid + 1.0);
        scrollSuggestedAuthors.setHvalue(scrollSuggestedAuthors.getHvalue() - scrollValue);

        rightArrowAuthorsBasedOnUser.setVisible(true);
        leftArrowAuthorsBasedOnUser.setVisible(scrollSuggestedAuthors.getHvalue() >= 0.01);
    }

    @FXML
    void nextMostFollowedAuthors(MouseEvent event) throws IOException {
        double scrollValue = 1.0 / (gridMostFollowedAuthors.getColumnCount() - this.authorsToLoadInGrid + 1.0);
        scrollMostFollowedAuthors.setHvalue(scrollMostFollowedAuthors.getHvalue() + scrollValue);

        leftArrowMostFollowedAuthors.setVisible(true);
        rightArrowMostFollowedAuthors.setVisible(scrollMostFollowedAuthors.getHvalue() <= 0.98 || this.mostFollowedAuthors.size() > this.gridMostFollowedAuthors.getColumnCount());

        updateMostFollowedGrid();
    }

    @FXML
    void nextPodcastsBasedOnWatchlist(MouseEvent event) throws IOException {
        double scrollValue = 1.0 / (gridPodcastsBasedOnWatchlist.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollPodcastsBasedOnWatchlist.setHvalue(scrollPodcastsBasedOnWatchlist.getHvalue() + scrollValue);

        leftArrowBasedOnWatchlist.setVisible(true);
        rightArrowBasedOnWatchlist.setVisible(scrollPodcastsBasedOnWatchlist.getHvalue() <= 0.98 || this.basedOnWatchlist.size() > this.gridPodcastsBasedOnWatchlist.getColumnCount());

        updateBasedOnWatchlistGrid();
    }

    @FXML
    void nextTopRated(MouseEvent event) throws IOException {
        double scrollValue = 1.0 / (gridTopRated.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollTopRated.setHvalue(scrollTopRated.getHvalue() + scrollValue);

        leftArrowTopRated.setVisible(true);
        rightArrowTopRated.setVisible(scrollTopRated.getHvalue() <= 0.98 || this.topRated.size() > this.gridTopRated.getColumnCount());

        updateTopRatedGrid();
    }

    @FXML
    void nextSuggestedAuthors(MouseEvent event) throws IOException {
        double scrollValue = 1.0 / (gridSuggestedAuthors.getColumnCount() - this.authorsToLoadInGrid + 1.0);
        scrollSuggestedAuthors.setHvalue(scrollSuggestedAuthors.getHvalue() + scrollValue);

        leftArrowAuthorsBasedOnUser.setVisible(true);
        rightArrowAuthorsBasedOnUser.setVisible(scrollSuggestedAuthors.getHvalue() <= 0.98 || this.suggestedAuthors.size() > this.gridSuggestedAuthors.getColumnCount());

        updateAuthorsBasedOnUserGrid();
    }

    @FXML
    void nextSuggestedCategory(MouseEvent event) throws IOException {
        double scrollValue = 1.0 / (gridSuggestedForCategory.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollSuggestedForCategory.setHvalue(scrollSuggestedForCategory.getHvalue() + scrollValue);

        leftArrowTopGenres.setVisible(true);
        rightArrowTopGenres.setVisible(scrollSuggestedForCategory.getHvalue() <= 0.98 || this.topGenres.size() > this.gridSuggestedForCategory.getColumnCount());

        updateTopGenresGrid();
    }

    @FXML
    void nextPodcastsSuggestedOnUsers(MouseEvent event) throws IOException {
        double scrollValue = 1.0 / (gridPodcastsSuggestedForUsers.getColumnCount() - this.podcastsToLoadInGrid + 1.0);
        scrollPodcastsSuggestedForUsers.setHvalue(scrollPodcastsSuggestedForUsers.getHvalue() + scrollValue);

        leftArrowPodcastsBasedOnUsers.setVisible(true);
        rightArrowPodcastsBasedOnUsers.setVisible(scrollPodcastsSuggestedForUsers.getHvalue() <= 0.98 || this.podcastsBasedOnFollowedUsers.size() > this.gridPodcastsSuggestedForUsers.getColumnCount());

        updatePodcastsBasedOnUsersGrid();
    }

    /********* LOADING GRIDS *********/
    void clearIndexes(boolean newLoad, GridPane grid) {
        if (newLoad) {
            this.row = 0;
            this.column = grid.getColumnCount();
        } else {
            this.row = 0;
            this.column = 0;
        }
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
    void loadWatchlistGrid(boolean newLoad) throws IOException {
        clearIndexes(newLoad, gridWatchlist);

        int maxValue = Math.min(this.watchlist.size(), (gridWatchlist.getColumnCount() + this.podcastsToLoadInGrid));

        for (Podcast podcast : this.watchlist.subList(this.gridWatchlist.getColumnCount(), maxValue)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast, 0, null);

            this.gridWatchlist.add(newPodcast, this.column++, this.row);
        }
    }

    // Suggested on podcast's category user liked
    void loadSuggestedBasedOnCategoryGrid(boolean newLoad) throws IOException {
        clearIndexes(newLoad, gridSuggestedForCategory);

        int maxValue = Math.min(this.topGenres.size(), (gridSuggestedForCategory.getColumnCount() + this.podcastsToLoadInGrid));

        for (Podcast podcast : this.topGenres.subList(gridSuggestedForCategory.getColumnCount(), maxValue)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast, 0, null);

            this.gridSuggestedForCategory.add(newPodcast, this.column++, this.row);
        }
    }

    // Suggestions based on authors in user's watchlist
    void loadSuggestedBasedOnWatchlistGrid(boolean newLoad) throws IOException {
        clearIndexes(newLoad, gridPodcastsBasedOnWatchlist);

        int maxValue = Math.min(this.basedOnWatchlist.size(), (gridPodcastsBasedOnWatchlist.getColumnCount() + this.podcastsToLoadInGrid));

        for (Podcast podcast : this.basedOnWatchlist.subList(gridPodcastsBasedOnWatchlist.getColumnCount(), maxValue)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast, 0, null);

            this.gridPodcastsBasedOnWatchlist.add(newPodcast, this.column++, this.row);
        }
    }

    // Suggest authors based on user you follow
    void loadSuggestedAuthorsBasedOnUserGrid(boolean newLoad) throws IOException {
        clearIndexes(newLoad, gridSuggestedAuthors);

        int maxValue = Math.min(this.suggestedAuthors.size(), (gridSuggestedAuthors.getColumnCount() + this.authorsToLoadInGrid));

        for (Author author : this.suggestedAuthors.subList(gridSuggestedAuthors.getColumnCount(), maxValue)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

            AnchorPane newAuthor = fxmlLoader.load();
            AuthorPreviewController controller = fxmlLoader.getController();
            controller.setData(author, false, 0, null);

            this.gridSuggestedAuthors.add(newAuthor, this.column++, this.row);
        }
    }

    void loadSuggestedOnFollowedUsers(boolean newLoad) throws IOException {
        clearIndexes(newLoad, gridPodcastsSuggestedForUsers);

        int maxValue = Math.min(this.podcastsBasedOnFollowedUsers.size(), (gridPodcastsSuggestedForUsers.getColumnCount() + this.podcastsToLoadInGrid));
        for (Podcast podcast : this.podcastsBasedOnFollowedUsers.subList(gridPodcastsSuggestedForUsers.getColumnCount(), maxValue)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast, 0, null);

            this.gridPodcastsSuggestedForUsers.add(newPodcast, this.column++, this.row);
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
    void loadTopRatedPodcasts(boolean newLoad) throws IOException {
        clearIndexes(newLoad, gridTopRated);

        int maxValue = Math.min(this.topRated.size(), (gridTopRated.getColumnCount() + this.podcastsToLoadInGrid));
        for (Triplet<Podcast, Float, Boolean> podcast : this.topRated.subList(gridTopRated.getColumnCount(), maxValue)) {
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

    void loadMostLikedPodcasts(boolean newLoad) throws IOException {
        clearIndexes(newLoad, gridMostLikedPodcasts);

        int maxValue = Math.min(this.mostLikedPodcasts.size(), (gridMostLikedPodcasts.getColumnCount() + this.podcastsToLoadInGrid));
        for (Pair<Podcast, Integer> podcast : this.mostLikedPodcasts.subList(gridMostLikedPodcasts.getColumnCount(), maxValue)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast.getValue0(), 1, podcast.getValue1().toString());

            this.gridMostLikedPodcasts.add(newPodcast, this.column++, this.row);
        }
    }

    void loadMostFollowedAuthors(boolean newLoad) throws IOException {
        clearIndexes(newLoad, gridMostFollowedAuthors);

        int maxValue = Math.min(this.mostFollowedAuthors.size(), (gridMostFollowedAuthors.getColumnCount() + this.authorsToLoadInGrid));
        for (Pair<Author, Integer> author : this.mostFollowedAuthors.subList(gridMostFollowedAuthors.getColumnCount(), maxValue)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

            AnchorPane newAuthor = fxmlLoader.load();
            AuthorPreviewController controller = fxmlLoader.getController();
            controller.setData(author.getValue0(), false, 1, author.getValue1().toString());

            this.gridMostFollowedAuthors.add(newAuthor, this.column++, this.row);
        }
    }

    @FXML
    void onClickShowBasedOnFollowedUsers(MouseEvent event) throws IOException {
        HomePageService homepageService = new HomePageService();
        this.noMoreBasedOnUsers = homepageService.loadMoreSuggested(this.podcastsBasedOnFollowedUsers, this.podcastsToRetrieve, 0);
        loadSuggestedOnFollowedUsers(false);

        if (podcastsBasedOnFollowedUsers.isEmpty()) {
            boxBasedOnUsers.setVisible(false);
            boxBasedOnUsers.setStyle("-fx-pref-height: 0");
            DialogManager.getInstance().createInformationAlert(this.mainPage, "Show more suggestions", "Suggestions can't be computed because you don't follow anyone.");
            Logger.info("No more suggested podcasts found");
        }
    }

    @FXML
    void onHoverBtnShow(MouseEvent event) {
        this.btnShowMoreSuggested.setStyle("-fx-background-color: skyblue; -fx-background-radius: 10;  -fx-background-insets: 0; -fx-border-color: #87c7ff; -fx-border-radius: 10");
    }

    @FXML
    void onExitedBtnShow(MouseEvent event) {
        this.btnShowMoreSuggested.setStyle("-fx-background-color: #5b9bd2; -fx-background-radius: 10; -fx-background-insets: 0; -fx-border-color: transparent; -fx-border-radius: 10");
    }

    /**** SCROLL EVENTS *****/
    @FXML
    void onScrollAuthorsBasedOnUsers(ScrollEvent event) throws IOException {
        // Hide left arrow
        leftArrowAuthorsBasedOnUser.setVisible(scrollSuggestedAuthors.getHvalue() != 0);
        // Hide right arrow
        rightArrowAuthorsBasedOnUser.setVisible(scrollSuggestedAuthors.getHvalue() != 1.0 && (gridSuggestedAuthors.getColumnCount() != authorsToLoadInGrid - 1));
        updateAuthorsBasedOnUserGrid();
    }

    void updateAuthorsBasedOnUserGrid() throws IOException {
        if (scrollSuggestedAuthors.getHvalue() >= 0.99) {
            Logger.info("Authors loaded and ready to be shown in the grid: " + (this.suggestedAuthors.size() - gridSuggestedAuthors.getColumnCount()));

            if ((this.suggestedAuthors.size() - gridSuggestedAuthors.getColumnCount()) == 0 && !this.noMoreSuggestedAuthors) {
                Logger.info("(Call to the service) Trying to load new " + this.authorsToRetrieve + " authors in memory");

                HomePageService homepageService = new HomePageService();
                this.noMoreSuggestedAuthors = homepageService.loadSuggestedAuthors(this.suggestedAuthors, this.authorsToRetrieve, this.suggestedAuthors.size());

                Logger.info("(End call service) Total authors loaded in memory: " + this.suggestedAuthors.size() + " | Authors available to be shown: " + (this.suggestedAuthors.size() - this.gridSuggestedAuthors.getColumnCount()));
                // Updating arrows
                if ((this.suggestedAuthors.size() - this.gridSuggestedAuthors.getColumnCount()) > 0)
                    rightArrowAuthorsBasedOnUser.setVisible(true);
                else
                    leftArrowAuthorsBasedOnUser.setVisible(false);
                loadSuggestedAuthorsBasedOnUserGrid(true);

            } else {
                Logger.info("Authors loaded in the grid: " + this.gridSuggestedAuthors.getColumnCount() + " | Authors in memory: " + this.suggestedAuthors.size());
                // Show authors already retrieved from the database
                loadSuggestedAuthorsBasedOnUserGrid(true);
            }
        }
    }

    @FXML
    void onScrollBasedOnFollowedUsers(ScrollEvent event) throws IOException {
        // Hide left arrow
        leftArrowPodcastsBasedOnUsers.setVisible(scrollPodcastsSuggestedForUsers.getHvalue() != 0);
        // Hide right arrow
        rightArrowPodcastsBasedOnUsers.setVisible(scrollPodcastsSuggestedForUsers.getHvalue() != 1.0 && (gridPodcastsSuggestedForUsers.getColumnCount() != podcastsToLoadInGrid - 1));
        updatePodcastsBasedOnUsersGrid();
    }

    void updatePodcastsBasedOnUsersGrid() throws IOException {
        if (scrollPodcastsSuggestedForUsers.getHvalue() >= 0.99) {
            Logger.info("Podcasts loaded and ready to be shown in the grid: " + (this.podcastsBasedOnFollowedUsers.size() - gridPodcastsSuggestedForUsers.getColumnCount()));

            if ((this.podcastsBasedOnFollowedUsers.size() - gridPodcastsSuggestedForUsers.getColumnCount()) == 0 && !this.noMoreBasedOnUsers) {
                Logger.info("(Call to the service) Trying to load new " + this.podcastsToRetrieve + " podcasts in memory");

                HomePageService homepageService = new HomePageService();
                this.noMoreBasedOnUsers = homepageService.loadMoreSuggested(this.podcastsBasedOnFollowedUsers, this.podcastsToRetrieve, this.podcastsBasedOnFollowedUsers.size());

                Logger.info("(End call service) Total podcasts loaded in memory: " + this.podcastsBasedOnFollowedUsers.size() + " | Podcasts available to be shown: " + (this.podcastsBasedOnFollowedUsers.size() - this.gridPodcastsSuggestedForUsers.getColumnCount()));
                // Updating arrows
                if ((this.podcastsBasedOnFollowedUsers.size() - this.gridPodcastsSuggestedForUsers.getColumnCount()) > 0)
                    rightArrowPodcastsBasedOnUsers.setVisible(true);
                else
                    leftArrowPodcastsBasedOnUsers.setVisible(false);
                loadSuggestedOnFollowedUsers(true);

            } else {
                Logger.info("Podcasts loaded in the grid: " + this.gridPodcastsSuggestedForUsers.getColumnCount() + " | Podcasts in memory: " + this.podcastsBasedOnFollowedUsers.size());
                // Show podcasts already retrieved from the database
                loadSuggestedOnFollowedUsers(true);
            }
        }
    }

    @FXML
    void onScrollBasedOnWatchlist(ScrollEvent event) throws IOException {
        // Hide left arrow
        leftArrowBasedOnWatchlist.setVisible(scrollPodcastsBasedOnWatchlist.getHvalue() != 0);
        // Hide right arrow
        rightArrowBasedOnWatchlist.setVisible(scrollPodcastsBasedOnWatchlist.getHvalue() != 1.0  && (gridPodcastsBasedOnWatchlist.getColumnCount() != podcastsToLoadInGrid - 1));
        updateBasedOnWatchlistGrid();
    }

    void updateBasedOnWatchlistGrid() throws IOException {
        if (scrollPodcastsBasedOnWatchlist.getHvalue() >= 0.99) {
            Logger.info("Podcasts loaded and ready to be shown in the grid: " + (this.basedOnWatchlist.size() - gridPodcastsBasedOnWatchlist.getColumnCount()));

            if ((this.basedOnWatchlist.size() - gridPodcastsBasedOnWatchlist.getColumnCount()) == 0 && !this.noMoreBasedOnWatchlist) {
                Logger.info("(Call to the service) Trying to load new " + this.podcastsToRetrieve + " podcasts in memory");

                HomePageService homepageService = new HomePageService();
                this.noMoreBasedOnWatchlist = homepageService.loadBasedOnWatchlist(this.basedOnWatchlist, this.podcastsToRetrieve, this.basedOnWatchlist.size());

                Logger.info("(End call service) Total podcasts loaded in memory: " + this.basedOnWatchlist.size() + " | Podcasts available to be shown: " + (this.basedOnWatchlist.size() - this.gridPodcastsBasedOnWatchlist.getColumnCount()));
                // Updating arrows
                if ((this.basedOnWatchlist.size() - this.gridPodcastsBasedOnWatchlist.getColumnCount()) > 0)
                    rightArrowBasedOnWatchlist.setVisible(true);
                else
                    rightArrowBasedOnWatchlist.setVisible(false);
                loadSuggestedBasedOnWatchlistGrid(true);

            } else {
                Logger.info("Podcasts loaded in the grid: " + this.gridPodcastsBasedOnWatchlist.getColumnCount() + " | Podcasts in memory: " + this.basedOnWatchlist.size());
                // Show podcasts already retrieved from the database
                loadSuggestedBasedOnWatchlistGrid(true);
            }
        }
    }

    @FXML
    void onScrollMostFollowed(ScrollEvent event) throws IOException {
        // Hide left arrow
        leftArrowMostFollowedAuthors.setVisible(scrollMostFollowedAuthors.getHvalue() != 0);
        // Hide right arrow
        rightArrowMostFollowedAuthors.setVisible(scrollMostFollowedAuthors.getHvalue() != 1.0 && (gridMostFollowedAuthors.getColumnCount() != authorsToLoadInGrid - 1));
        updateMostFollowedGrid();
    }

    void updateMostFollowedGrid() throws IOException {
        if (scrollMostFollowedAuthors.getHvalue() >= 0.99) {
            Logger.info("Authors loaded and ready to be shown in the grid: " + (this.mostFollowedAuthors.size() - gridMostFollowedAuthors.getColumnCount()));
            Logger.info("Authors loaded in the grid: " + this.gridMostFollowedAuthors.getColumnCount() + " | Authors in memory: " + this.mostFollowedAuthors.size());
            // Show podcasts already retrieved from the database
            loadMostFollowedAuthors(true);
        }
    }

    @FXML
    void onScrollMostLiked(ScrollEvent event) throws IOException {
        // Hide left arrow
        leftArrowMostLiked.setVisible(scrollMostLikedPodcasts.getHvalue() != 0);
        // Hide right arrow
        rightArrowMostLiked.setVisible(scrollMostLikedPodcasts.getHvalue() != 1.0 && (gridMostLikedPodcasts.getColumnCount() != podcastsToLoadInGrid - 1));
        updateMostLikedGrid();
    }

    void updateMostLikedGrid() throws IOException {
        if (scrollMostLikedPodcasts.getHvalue() >= 0.99) {
            Logger.info("Podcasts loaded and ready to be shown in the grid: " + (this.mostLikedPodcasts.size() - gridMostLikedPodcasts.getColumnCount()));
            Logger.info("Podcasts loaded in the grid: " + this.gridMostLikedPodcasts.getColumnCount() + " | Podcasts in memory: " + this.mostLikedPodcasts.size());
            // Show podcasts already retrieved from the database
            loadMostLikedPodcasts(true);
        }
    }

    @FXML
    void onScrollTopGenres(ScrollEvent event) throws IOException {
        // Hide left arrow
        leftArrowTopGenres.setVisible(scrollSuggestedForCategory.getHvalue() != 0);
        // Hide right arrow
        rightArrowTopGenres.setVisible(scrollSuggestedForCategory.getHvalue() != 1.0 && (gridSuggestedForCategory.getColumnCount() != podcastsToLoadInGrid - 1));
        updateTopGenresGrid();
    }

    void updateTopGenresGrid() throws IOException {
        if (scrollSuggestedForCategory.getHvalue() >= 0.99) {
            Logger.info("Podcasts loaded and ready to be shown in the grid: " + (this.topGenres.size() - gridSuggestedForCategory.getColumnCount()));

            if ((this.topGenres.size() - gridSuggestedForCategory.getColumnCount()) == 0 && !this.noMorePodcastsTopGenres) {
                Logger.info("(Call to the service) Trying to load new " + this.podcastsToRetrieve + " podcasts in memory");

                HomePageService homepageService = new HomePageService();
                this.noMorePodcastsTopGenres = homepageService.loadTopGenres(this.topGenres, this.podcastsToRetrieve, this.topGenres.size());

                Logger.info("(End call service) Total podcasts loaded in memory: " + this.topGenres.size() + " | Podcasts available to be shown: " + (this.topGenres.size() - this.gridSuggestedForCategory.getColumnCount()));
                // Updating arrows
                if ((this.topGenres.size() - this.gridSuggestedForCategory.getColumnCount()) > 0)
                    rightArrowTopGenres.setVisible(true);
                else
                    rightArrowTopGenres.setVisible(false);
                loadSuggestedBasedOnCategoryGrid(true);

            } else {
                Logger.info("Podcasts loaded in the grid: " + this.gridSuggestedForCategory.getColumnCount() + " | Podcasts in memory: " + this.topGenres.size());
                // Show podcasts already retrieved from the database
                loadSuggestedBasedOnCategoryGrid(true);
            }
        }
    }

    @FXML
    void onScrollTopRated(ScrollEvent event) throws IOException {
        // Hide left arrow
        leftArrowTopRated.setVisible(scrollTopRated.getHvalue() != 0);
        // Hide right arrow
        rightArrowTopRated.setVisible(scrollTopRated.getHvalue() != 1.0 && (gridTopRated.getColumnCount() != podcastsToLoadInGrid - 1));
        updateTopRatedGrid();
    }

    void updateTopRatedGrid() throws IOException {
        if (scrollTopRated.getHvalue() >= 0.99) {
            Logger.info("Podcasts loaded and ready to be shown in the grid: " + (this.topRated.size() - gridTopRated.getColumnCount()));
            Logger.info("Podcasts loaded in the grid: " + this.gridTopRated.getColumnCount() + " | Podcasts in memory: " + this.topRated.size());
            // Show podcasts already retrieved from the database
            loadTopRatedPodcasts(true);
        }
    }

    @FXML
    void onScrollWatchlist(ScrollEvent event) throws IOException {
        // Hide left arrow
        leftArrowWatchlist.setVisible(scrollWatchlist.getHvalue() != 0);
        // Hide right arrow
        rightArrowWatchlist.setVisible(scrollWatchlist.getHvalue() != 1.0 && (gridWatchlist.getColumnCount() != podcastsToLoadInGrid - 1));
        updateWatchlistGrid();
    }

    void updateWatchlistGrid() throws IOException {
        if (this.scrollWatchlist.getHvalue() >= 0.99) {
            Logger.info("Podcasts loaded and ready to be shown in the grid: " + (this.watchlist.size() - this.gridWatchlist.getColumnCount()));

            if ((this.watchlist.size() - this.gridWatchlist.getColumnCount()) == 0 && !this.noMorePodcastsWatchlist) {
                Logger.info("(Call to the service) Trying to load new " + this.podcastsToRetrieve + " podcasts in memory");

                HomePageService homepageService = new HomePageService();
                this.noMorePodcastsWatchlist = homepageService.loadWatchlist(this.watchlist, this.podcastsToRetrieve, this.watchlist.size());

                Logger.info("(End call service) Total podcasts loaded in memory: " + this.watchlist.size() + " | Podcasts available to be shown: " + (this.watchlist.size() - this.gridWatchlist.getColumnCount()));
                // Updating arrows
                if ((this.watchlist.size() - this.gridWatchlist.getColumnCount()) > 0)
                    rightArrowWatchlist.setVisible(true);
                else
                    rightArrowWatchlist.setVisible(false);
                loadWatchlistGrid(true);

            } else {
                Logger.info("Podcasts loaded in the grid: " + this.gridWatchlist.getColumnCount() + " | Podcasts in memory: " + this.watchlist.size());
                // Show podcasts already retrieved from the database
                loadWatchlistGrid(true);
            }
        }
    }

    public void initialize() throws IOException {
        HomePageService homepageService = new HomePageService();
        switch (this.actorType) {
            case "Author" -> {
                Author sessionActor = (Author) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an actor: " + sessionActor.getName());

                // Setting GUI parameters
                this.username.setText("Welcome " + sessionActor.getName() + "!");
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                this.actorPicture.setImage(image);

                // Homepage load as author
                homepageService.loadHomepageAsAuthor(this.topRated, this.mostLikedPodcasts, this.mostFollowedAuthors, this.authorsToRetrieve);
            }
            case "User" -> {
                User sessionActor = (User) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an user: " + sessionActor.getUsername());

                // Setting GUI params
                this.username.setText("Welcome " + sessionActor.getUsername() + "!");
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                this.actorPicture.setImage(image);

                // Homepage load as user
                // TODO: modificare limit
                homepageService.loadHomepageAsUser(this.topRated, this.mostLikedPodcasts, this.mostFollowedAuthors, this.watchlist, this.topGenres, this.basedOnWatchlist, this.suggestedAuthors, this.podcastsToRetrieve);
            }
            case "Admin" -> {
                Admin sessionActor = (Admin) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an administrator: " + sessionActor.getName());

                // Setting GUI params
                this.username.setText("Welcome " + sessionActor.getName() + "!");
                Image image = ImageCache.getImageFromLocalPath("/img/userPicture.png");
                this.actorPicture.setImage(image);

                // Homepage load as admin
                // TODO: sistemare il limit
                homepageService.loadHomepageAsAdmin(this.topRated, this.mostLikedPodcasts, this.mostFollowedAuthors, this.podcastsToRetrieve);
            }
            case "Unregistered" -> {
                this.username.setText("Welcome to MyPodcastDB!");
                Logger.info("I'm an unregistered user");

                // Disabling User Profile Page and Logout Button
                this.boxActorProfile.setVisible(false);
                this.boxActorProfile.setStyle("-fx-min-height: 0; -fx-pref-height: 0; -fx-min-width: 0; -fx-pref-width: 0;");

                // Homepage load as unregistered user
                // TODO: sistemare il limit
                homepageService.loadHomepageAsUnregistered(this.topRated, this.mostLikedPodcasts, this.mostFollowedAuthors, this.podcastsToRetrieve);
            }
            default -> Logger.error("Unidentified Actor Type");
        }

        // Showing grids available to every actor
        loadTopRatedPodcasts(false);
        loadMostLikedPodcasts(false);
        loadMostFollowedAuthors(false);

        if (actorType.equals("User")) {
            // Making visible the "Show more suggested" button for the user
            btnShowMoreSuggested.setVisible(true);
            btnShowMoreSuggested.setPrefHeight(Region.USE_COMPUTED_SIZE);

            boxBtnShowMoreSuggested.setVisible(true);
            boxBtnShowMoreSuggested.setPrefHeight(80);

            // Loading grids for registered users
            this.noMorePodcastsWatchlist = this.watchlist.size() < this.podcastsToRetrieve;
            loadWatchlistGrid(false);

            this.noMorePodcastsTopGenres = this.topGenres.size() < this.podcastsToRetrieve;
            loadSuggestedBasedOnCategoryGrid(false);

            this.noMoreBasedOnWatchlist = this.basedOnWatchlist.size() < this.podcastsToRetrieve;
            loadSuggestedBasedOnWatchlistGrid(false);

            this.noMoreSuggestedAuthors = this.suggestedAuthors.size() < this.authorsToRetrieve;
            loadSuggestedAuthorsBasedOnUserGrid(false);

            this.rightArrowWatchlist.setVisible(!(this.gridWatchlist.getColumnCount() < podcastsToLoadInGrid));
            this.rightArrowTopGenres.setVisible(!(this.gridSuggestedForCategory.getColumnCount() < podcastsToLoadInGrid));
            this.rightArrowBasedOnWatchlist.setVisible(!(this.gridPodcastsBasedOnWatchlist.getColumnCount() < podcastsToLoadInGrid));
            this.rightArrowAuthorsBasedOnUser.setVisible(!(this.gridSuggestedAuthors.getColumnCount() < authorsToLoadInGrid));
        }

        // Hiding the empty grids
        hideEmptyGrids();
        Logger.success("Homepage loading done!");
    }
}
