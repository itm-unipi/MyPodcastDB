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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomePageController {
    private User userPreview;

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

    /*********** Navigator Events (Profile, Home, Search) *************/
    @FXML
    void onClickActorProfile(MouseEvent event) throws IOException {
        Logger.info("Actor profile clicked");
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author"))
            StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), ((Author)MyPodcastDB.getInstance().getSessionActor()).getName());
        else if (actorType.equals("User"))
            StageManager.showPage(ViewNavigator.USERPAGE.getPage(), ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername());
        else if (actorType.equals("Admin"))
            StageManager.showPage(ViewNavigator.ADMINDASHBOARD.getPage());
        else
            Logger.error("Unidentified Actor Type!");
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
        Logger.info(MyPodcastDB.getInstance().getSessionType() +  " Home Clicked");
    }

    @FXML
    void onClickLogout(MouseEvent event) throws IOException {
        Logger.info("Logout button clicked");
        // TODO: clear the session
        MyPodcastDB.getInstance().setSession(null, null);
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    /**********************************************************/

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
    void nextSuggestedCategory(MouseEvent event) {
        Logger.info("next suggested category");
        double scrollValue = 1;
        if (scrollSuggestedForCategory.getHvalue() == 1.0)
            scrollValue = -1;
        scrollSuggestedForCategory.setHvalue(scrollSuggestedForCategory.getHvalue() + scrollValue);
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
    void nextSuggestedUser(MouseEvent event) {
        Logger.info("next suggested user");
        double scrollValue = 1;
        if (scrollSuggestedForUser.getHvalue() == 1.0)
            scrollValue = -1;
        scrollSuggestedForUser.setHvalue(scrollSuggestedForUser.getHvalue() + scrollValue);
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

    /***************************************************/

    public void initialize() throws IOException {
        // Load information about the actor of the session
        String actorType = MyPodcastDB.getInstance().getSessionType();

        // Declaring lists in order to contain queries results
        List<Triplet<Podcast, Float, Boolean>> topRated = new ArrayList<>();
        List<Pair<Podcast, Integer>> mostLikedPodcasts = new ArrayList<>();
        List<Triplet<Author, Integer, Boolean>> mostFollowedAuthors = new ArrayList<>();

        // User lists
        List<Podcast> watchlist = new ArrayList<>();
        List<Podcast> topGenres = new ArrayList<>();
        List<Podcast> basedOnFriends = new ArrayList<>();
        List<Podcast> basedOnWatchlist = new ArrayList<>();
        List<Pair<Author, Boolean>> suggestedAuthors = new ArrayList<>();

        switch (actorType) {
            case "Author" -> {
                Author sessionActor = (Author) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an actor: " + sessionActor.getName());

                // Setting GUI parameters
                this.username.setText("Welcome " + sessionActor.getName() + "!");
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                actorPicture.setImage(image);

                // Homepage load as author
                AuthorService authorService = new AuthorService();
                authorService.loadHomepage(topRated, mostLikedPodcasts, mostFollowedAuthors, 5);
            }
            case "User" -> {
                User sessionActor = (User) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an user: " + sessionActor.getUsername());

                // Setting GUI params
                this.username.setText("Welcome " + sessionActor.getUsername() + "!");
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                actorPicture.setImage(image);

                // Homepage load as user
                UserService userService = new UserService();
                userService.loadHomepageRegistered(topRated, mostLikedPodcasts, mostFollowedAuthors, watchlist, topGenres, basedOnFriends, basedOnWatchlist, suggestedAuthors, 5);
            }
            case "Admin" -> {
                Admin sessionActor = (Admin) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an administrator: " + sessionActor.getName());

                // Setting GUI params
                this.username.setText("Welcome " + sessionActor.getName() + " (admin)!");
                Image image = ImageCache.getImageFromLocalPath("/img/userPicture.png");
                actorPicture.setImage(image);

                // Homepage load as admin
                AdminService adminService = new AdminService();
                adminService.loadHomepage(topRated, mostLikedPodcasts, mostFollowedAuthors, 5);
            }
            case "Unregistered" -> {
                this.username.setText("Welcome to MyPodcastDB!");
                Logger.info("I'm an unregistered user");

                // Disabling User Profile Page and Logout Button
                boxActorProfile.setVisible(false);
                boxActorProfile.setStyle("-fx-min-height: 0; -fx-pref-height: 0; -fx-min-width: 0; -fx-pref-width: 0;");

                // Homepage load as unregistered user
                UserService userService = new UserService();
                userService.loadHomepageUnregistered(topRated, mostLikedPodcasts, mostFollowedAuthors, 5);
            }
            default -> Logger.error("Unidentified Actor Type");
        }

        /************************************************************************************/

        if (actorType.equals("User")) {
            /*********** WATCHLIST ***********/
            int row = 0;
            int column = 0;

            for (Podcast podcast : watchlist){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                PodcastPreviewController controller = fxmlLoader.getController();
                controller.setData(podcast, 0, null);

                gridWatchlist.add(newPodcast, column++, row);
            }

            /*********** SUGGESTED ON "CATEGORY" YOU LIKED ************/
            row = 0;
            column = 0;

            for (Podcast podcast : topGenres){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                PodcastPreviewController controller = fxmlLoader.getController();
                controller.setData(podcast, 0, null);

                gridSuggestedForCategory.add(newPodcast, column++, row);
            }

            /*********** SUGGESTED ON USERS YOU FOLLOW ************/
            row = 0;
            column = 0;

            for (Podcast podcast : basedOnFriends){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                PodcastPreviewController controller = fxmlLoader.getController();
                controller.setData(podcast, 0, null);

                gridSuggestedForUser.add(newPodcast, column++, row);
            }

            /*********** SUGGEST BASED ON THE AUTHORS IN YOUR WATCHLIST ************/
            row = 0;
            column = 0;

            for (Podcast podcast : basedOnWatchlist){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                PodcastPreviewController controller = fxmlLoader.getController();
                controller.setData(podcast, 0, null);

                gridPodcastsBasedOnWatchlist.add(newPodcast, column++, row);
            }

            /*********** SUGGEST AUTHORS BASED ON USER YOU FOLLOW ************/
            row = 0;
            column = 0;
            for (Pair<Author, Boolean> author : suggestedAuthors) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

                AnchorPane newAuthor = fxmlLoader.load();
                AuthorPreviewController controller = fxmlLoader.getController();
                controller.setData(author.getValue0(), author.getValue1(), 0, null);

                gridSuggestedAuthors.add(newAuthor, column++, row);
            }
        }

        /******************************* TOP RATED PODCASTS *******************************/
        int row = 0;
        int column = 0;

        for (Triplet<Podcast, Float, Boolean> podcast : topRated){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            if (podcast.getValue2())
                controller.setData(podcast.getValue0(), 3, podcast.getValue1().toString());
            else
                controller.setData(podcast.getValue0(), 2, podcast.getValue1().toString());

            gridTopRated.add(newPodcast, column++, row);
        }

        /******* MOST LIKED PODCASTS (AVAILABLE TO EVERYONE) ********/
        row = 0;
        column = 0;

        for (Pair<Podcast, Integer> podcast : mostLikedPodcasts){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast.getValue0(), 1, podcast.getValue1().toString());

            gridMostLikedPodcasts.add(newPodcast, column++, row);
        }

        /*********** MOST FOLLOWED AUTHORS (AVAILABLE TO EVERYONE) ************/

        row = 0;
        column = 0;

        for (Triplet<Author, Integer, Boolean> author : mostFollowedAuthors) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

            AnchorPane newAuthor = fxmlLoader.load();
            AuthorPreviewController controller = fxmlLoader.getController();
            controller.setData(author.getValue0(), author.getValue2(), 1, author.getValue1().toString());

            gridMostFollowedAuthors.add(newAuthor, column++, row);
        }

        // Hide section if empty
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

        if (basedOnFriends.isEmpty()) {
            boxBasedOnUsers.setVisible(false);
            boxBasedOnUsers.setStyle("-fx-pref-height: 0");
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
}
