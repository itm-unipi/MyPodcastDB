package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomePageController {

    private User userPreview;

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
    private Label topCountryLabel;

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
    private GridPane gridTopCountry;

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
    private ScrollPane scrollTopCountry;

    @FXML
    private ScrollPane scrollTopRated;

    @FXML
    private ScrollPane scrollWatchlist;

    @FXML
    private VBox boxActorProfile;

    @FXML
    private VBox boxLogout;

    @FXML
    private VBox boxBasedOnAuthors;

    @FXML
    private VBox boxBasedOnUsers;

    @FXML
    private VBox boxPodcastsBasedOnWatchlist;

    @FXML
    private VBox boxSuggestedAuthors;

    @FXML
    private VBox boxTopCountry;

    @FXML
    private VBox boxTopGenres;

    @FXML
    private VBox boxTopRated;

    @FXML
    private VBox boxWatchlist;

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
            // TODO: alert
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
    void backTopCountry(MouseEvent event) {
        Logger.info("Clicked on back top country");
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
    void nextTopCountry(MouseEvent event) {
        Logger.info("Clicked on next top country");
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

        if (actorType.equals("Author")) {
            Author sessionActor = (Author)MyPodcastDB.getInstance().getSessionActor();
            Logger.info("I'm an actor: " + sessionActor.getName());

            // Setting GUI params
            this.username.setText("Welcome " + sessionActor.getName() + "!");
            Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
            actorPicture.setImage(image);

        } else if (actorType.equals("User")) {
            User sessionActor = (User)MyPodcastDB.getInstance().getSessionActor();
            Logger.info("I'm an user: " + sessionActor.getUsername());

            // Setting GUI params
            this.username.setText("Welcome " + sessionActor.getUsername() + "!");
            Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
            actorPicture.setImage(image);

        } else if (actorType.equals("Admin")) {
            Admin sessionActor = (Admin)MyPodcastDB.getInstance().getSessionActor();
            Logger.info("I'm an administrator: " + sessionActor.getName());

            // Setting GUI params
            this.username.setText("Welcome " + sessionActor.getName() + " (admin)!");
            Image image = ImageCache.getImageFromLocalPath("/img/userPicture.png");
            actorPicture.setImage(image);

        } else if (actorType.equals("Unregistered")) {
            this.username.setText("Welcome to MyPodcastDB!");
            Logger.info("I'm an unregistered user");

            // Disabling User Profile Page and Logout Button
            boxActorProfile.setVisible(false);
            boxActorProfile.setStyle("-fx-min-height: 0; -fx-pref-height: 0; -fx-min-width: 0; -fx-pref-width: 0;");

            boxLogout.setVisible(false);
            boxLogout.setStyle("-fx-min-height: 0; -fx-pref-height: 0; -fx-min-width: 0; -fx-pref-width: 0;");

        } else {
            Logger.error("Unidentified Actor Type");
        }

        /************************************************************************************/

        if (actorType.equals("User")) {

            /*********** WATCHLIST ***********/
            List<Podcast> previewList = new ArrayList<>();
            Podcast p1 = new Podcast("54eb342567c94dacfb2a3e50", "Scaling Global", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg");
            Podcast p2 = new Podcast("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg");
            Podcast p3 = new Podcast("061a68eb754c400eae8027d7", "Average O Podcast", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/54/e4/84/54e48471-6971-03c8-83f4-4f973dc2a8cb/mza_8686729233410161200.jpg/600x600bb.jpg");
            Podcast p4 = new Podcast("34e734b09246d17dc5d56f63", "Getting Smart Podcast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts115/v4/52/e3/25/52e325bd-e6ba-3899-b7b4-71e512a48472/mza_18046006527881111713.png/600x600bb.jpg");
            Podcast p5 = new Podcast("84baff1495bff70bb81bd016", "Sofra Sredom", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts115/v4/98/ca/c7/98cac700-4398-7489-100a-416ec28d6662/mza_15500803433364327137.jpg/600x600bb.jpg");
            Podcast p6 = new Podcast("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg");
            previewList.add(p1);
            previewList.add(p2);
            previewList.add(p3);
            previewList.add(p4);
            previewList.add(p5);
            previewList.add(p6);

            int row = 0;
            int column = 0;
            for (Podcast podcast : previewList){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                PodcastPreviewController controller = fxmlLoader.getController();
                controller.setData(podcast);

                gridWatchlist.add(newPodcast, column++, row);
            }

            /*********** SUGGESTED ON AUTHORS YOU FOLLOW ************/
            previewList.clear();
            p1 = new Podcast("84baff1495bff70bb81bd016", "Sofra Sredom", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts115/v4/98/ca/c7/98cac700-4398-7489-100a-416ec28d6662/mza_15500803433364327137.jpg/600x600bb.jpg");
            p2 = new Podcast("d79472fb8372d1adbf5dea69", "Willowbrook Church", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts113/v4/95/ce/df/95cedfb1-49a7-187a-b9c2-65f99b214ccd/mza_6194046548563981637.jpg/600x600bb.jpg");
            p3 = new Podcast("05faf4fccdf79b198b7a3407", "Can I be Funny?", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts115/v4/89/56/0e/89560e3c-0b96-0691-350e-eab2f443bf6d/mza_11121304875938507631.jpg/600x600bb.jpg");
            p4 = new Podcast("9aaae9ac725c3a586701abf4", "KTs Money Matters", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts126/v4/17/42/75/17427552-6eaa-cf22-6213-1774a2d424f7/mza_16549547877306014389.jpeg/600x600bb.jpg");
            p5 = new Podcast("ab3320eef1052aad807747ec", "Talking Disney Podcast", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts114/v4/3b/30/9c/3b309c73-aec5-ac96-60b9-34eba0218218/mza_7561584782270172307.jpg/600x600bb.jpg");
            p6 = new Podcast("b77ddff5e8d3df8aa39308ba", "Footnotes: A History Podcast", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/3b/b3/be/3bb3be3d-ebb0-e4e4-4311-1a7f9e43030a/mza_15641279120309904835.jpg/600x600bb.jpg");
            Podcast p7 = new Podcast("54eb342567c94dacfb2a3e50", "Scaling Global", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg");
            Podcast p8 = new Podcast("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg");
            previewList.add(p1);
            previewList.add(p2);
            previewList.add(p3);
            previewList.add(p4);
            previewList.add(p5);
            previewList.add(p6);
            previewList.add(p7);
            previewList.add(p8);

            row = 0;
            column = 0;
            for (Podcast podcast : previewList){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                PodcastPreviewController controller = fxmlLoader.getController();
                controller.setData(podcast);

                gridSuggestedForAuthor.add(newPodcast, column++, row);
            }

            /*********** SUGGESTED ON "CATEGORY" YOU LIKED ************/
            previewList.clear();
            p1 = new Podcast("84baff1495bff70bb81bd016", "Sofra Sredom", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts115/v4/98/ca/c7/98cac700-4398-7489-100a-416ec28d6662/mza_15500803433364327137.jpg/600x600bb.jpg");
            p2 = new Podcast("d79472fb8372d1adbf5dea69", "Willowbrook Church", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts113/v4/95/ce/df/95cedfb1-49a7-187a-b9c2-65f99b214ccd/mza_6194046548563981637.jpg/600x600bb.jpg");
            p3 = new Podcast("05faf4fccdf79b198b7a3407", "Can I be Funny?", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts115/v4/89/56/0e/89560e3c-0b96-0691-350e-eab2f443bf6d/mza_11121304875938507631.jpg/600x600bb.jpg");
            p4 = new Podcast("9aaae9ac725c3a586701abf4", "KTs Money Matters", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts126/v4/17/42/75/17427552-6eaa-cf22-6213-1774a2d424f7/mza_16549547877306014389.jpeg/600x600bb.jpg");
            p5 = new Podcast("ab3320eef1052aad807747ec", "Talking Disney Podcast", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts114/v4/3b/30/9c/3b309c73-aec5-ac96-60b9-34eba0218218/mza_7561584782270172307.jpg/600x600bb.jpg");
            p6 = new Podcast("b77ddff5e8d3df8aa39308ba", "Footnotes: A History Podcast", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/3b/b3/be/3bb3be3d-ebb0-e4e4-4311-1a7f9e43030a/mza_15641279120309904835.jpg/600x600bb.jpg");
            p7 = new Podcast("54eb342567c94dacfb2a3e50", "Scaling Global", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg");
            p8 = new Podcast("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg");
            previewList.add(p8);
            previewList.add(p5);
            previewList.add(p7);
            previewList.add(p1);
            previewList.add(p4);
            previewList.add(p6);
            previewList.add(p3);
            previewList.add(p8);

            row = 0;
            column = 0;
            for (Podcast podcast : previewList){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                PodcastPreviewController controller = fxmlLoader.getController();
                controller.setData(podcast);

                gridSuggestedForCategory.add(newPodcast, column++, row);
            }

            /*********** SUGGESTED ON USERS YOU FOLLOW ************/
            previewList.clear();
            p3 = new Podcast("05faf4fccdf79b198b7a3407", "Can I be Funny?", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts115/v4/89/56/0e/89560e3c-0b96-0691-350e-eab2f443bf6d/mza_11121304875938507631.jpg/600x600bb.jpg");
            p4 = new Podcast("9aaae9ac725c3a586701abf4", "KTs Money Matters", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts126/v4/17/42/75/17427552-6eaa-cf22-6213-1774a2d424f7/mza_16549547877306014389.jpeg/600x600bb.jpg");
            p5 = new Podcast("ab3320eef1052aad807747ec", "Talking Disney Podcast", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts114/v4/3b/30/9c/3b309c73-aec5-ac96-60b9-34eba0218218/mza_7561584782270172307.jpg/600x600bb.jpg");
            p6 = new Podcast("b77ddff5e8d3df8aa39308ba", "Footnotes: A History Podcast", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/3b/b3/be/3bb3be3d-ebb0-e4e4-4311-1a7f9e43030a/mza_15641279120309904835.jpg/600x600bb.jpg");
            previewList.add(p5);
            previewList.add(p4);
            previewList.add(p3);
            previewList.add(p6);
            previewList.add(p5);
            previewList.add(p6);

            row = 0;
            column = 0;
            for (Podcast podcast : previewList){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                PodcastPreviewController controller = fxmlLoader.getController();
                controller.setData(podcast);

                gridSuggestedForUser.add(newPodcast, column++, row);
            }

            /*********** TOP COUNTRY ************/
            topCountryLabel.setText("Top " + ((User)MyPodcastDB.getInstance().getSessionActor()).getCountry());
            previewList.clear();
            p3 = new Podcast("05faf4fccdf79b198b7a3407", "Can I be Funny?", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts115/v4/89/56/0e/89560e3c-0b96-0691-350e-eab2f443bf6d/mza_11121304875938507631.jpg/600x600bb.jpg");
            p4 = new Podcast("9aaae9ac725c3a586701abf4", "KTs Money Matters", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts126/v4/17/42/75/17427552-6eaa-cf22-6213-1774a2d424f7/mza_16549547877306014389.jpeg/600x600bb.jpg");
            p5 = new Podcast("ab3320eef1052aad807747ec", "Talking Disney Podcast", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts114/v4/3b/30/9c/3b309c73-aec5-ac96-60b9-34eba0218218/mza_7561584782270172307.jpg/600x600bb.jpg");
            p6 = new Podcast("b77ddff5e8d3df8aa39308ba", "Footnotes: A History Podcast", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/3b/b3/be/3bb3be3d-ebb0-e4e4-4311-1a7f9e43030a/mza_15641279120309904835.jpg/600x600bb.jpg");
            previewList.add(p5);
            previewList.add(p4);
            previewList.add(p3);
            previewList.add(p6);
            previewList.add(p5);
            previewList.add(p6);

            row = 0;
            column = 0;
            for (Podcast podcast : previewList){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                PodcastPreviewController controller = fxmlLoader.getController();
                controller.setData(podcast);

                gridTopCountry.add(newPodcast, column++, row);
            }

            /*********** SUGGEST BASED ON THE AUTHORS IN YOUR WATCHLIST ************/
            previewList.clear();
            p3 = new Podcast("05faf4fccdf79b198b7a3407", "Can I be Funny?", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts115/v4/89/56/0e/89560e3c-0b96-0691-350e-eab2f443bf6d/mza_11121304875938507631.jpg/600x600bb.jpg");
            p4 = new Podcast("9aaae9ac725c3a586701abf4", "KTs Money Matters", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts126/v4/17/42/75/17427552-6eaa-cf22-6213-1774a2d424f7/mza_16549547877306014389.jpeg/600x600bb.jpg");
            p5 = new Podcast("ab3320eef1052aad807747ec", "Talking Disney Podcast", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts114/v4/3b/30/9c/3b309c73-aec5-ac96-60b9-34eba0218218/mza_7561584782270172307.jpg/600x600bb.jpg");
            p6 = new Podcast("b77ddff5e8d3df8aa39308ba", "Footnotes: A History Podcast", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/3b/b3/be/3bb3be3d-ebb0-e4e4-4311-1a7f9e43030a/mza_15641279120309904835.jpg/600x600bb.jpg");
            previewList.add(p5);
            previewList.add(p4);
            previewList.add(p3);
            previewList.add(p6);
            previewList.add(p5);
            previewList.add(p6);

            row = 0;
            column = 0;
            for (Podcast podcast : previewList){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                PodcastPreviewController controller = fxmlLoader.getController();
                controller.setData(podcast);

                gridPodcastsBasedOnWatchlist.add(newPodcast, column++, row);
            }

            /*********** SUGGEST AUTHORS BASED ON USER YOU FOLLOW ************/
            List<Author> suggestedAuthors = new ArrayList<>();

            for (int j = 0; j < 14; j++){
                Author a = new Author();
                a.setName("Apple Inc. " + j);
                a.setPicturePath("/img/authorAnonymousPicture.png");
                suggestedAuthors.add(a);
            }

            row = 0;
            column = 0;
            for (Author a : suggestedAuthors) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

                AnchorPane newAuthor = fxmlLoader.load();
                AuthorPreviewController controller = fxmlLoader.getController();
                controller.setData(a, true);

                gridSuggestedAuthors.add(newAuthor, column++, row);
            }

            /*** VBOX TO HIDE TO THE USER ****/
            boxTopRated.setVisible(false);
            boxTopRated.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");
        }

        /************************************************************************************/

        if (actorType.equals("Admin") || actorType.equals("Author") || actorType.equals("Unregistered")) {

            /*********** TOP RATED ************/
            List<Podcast> topRated = new ArrayList<>();
            Podcast p3 = new Podcast("05faf4fccdf79b198b7a3407", "Can I be Funny?", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts115/v4/89/56/0e/89560e3c-0b96-0691-350e-eab2f443bf6d/mza_11121304875938507631.jpg/600x600bb.jpg");
            Podcast p4 = new Podcast("9aaae9ac725c3a586701abf4", "KTs Money Matters", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts126/v4/17/42/75/17427552-6eaa-cf22-6213-1774a2d424f7/mza_16549547877306014389.jpeg/600x600bb.jpg");
            Podcast p5 = new Podcast("ab3320eef1052aad807747ec", "Talking Disney Podcast", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts114/v4/3b/30/9c/3b309c73-aec5-ac96-60b9-34eba0218218/mza_7561584782270172307.jpg/600x600bb.jpg");
            Podcast p6 = new Podcast("b77ddff5e8d3df8aa39308ba", "Footnotes: A History Podcast", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/3b/b3/be/3bb3be3d-ebb0-e4e4-4311-1a7f9e43030a/mza_15641279120309904835.jpg/600x600bb.jpg");
            topRated.add(p5);
            topRated.add(p4);
            topRated.add(p3);
            topRated.add(p6);
            topRated.add(p5);
            topRated.add(p6);

            int row = 0;
            int column = 0;
            for (Podcast podcast : topRated){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                PodcastPreviewController controller = fxmlLoader.getController();
                controller.setData(podcast);

                gridTopRated.add(newPodcast, column++, row);
            }

            /*** VBOX TO HIDE TO THE USER/ADMIN ****/
            boxBasedOnAuthors.setVisible(false);
            boxBasedOnAuthors.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");

            boxBasedOnUsers.setVisible(false);
            boxBasedOnUsers.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");

            boxSuggestedAuthors.setVisible(false);
            boxSuggestedAuthors.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");

            boxWatchlist.setVisible(false);
            boxWatchlist.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");

            boxTopGenres.setVisible(false);
            boxTopGenres.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");

            boxTopCountry.setVisible(false);
            boxTopCountry.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");

            boxPodcastsBasedOnWatchlist.setVisible(false);
            boxPodcastsBasedOnWatchlist.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");
        }

        /******* MOST LIKED PODCASTS (AVAILABLE TO EVERYONE) ********/
        List<Podcast> previewList = new ArrayList<>();
        previewList.clear();
        Podcast p1 = new Podcast("54eb342567c94dacfb2a3e50", "Scaling Global", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg");
        Podcast p2 = new Podcast("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg");
        Podcast p3 = new Podcast("061a68eb754c400eae8027d7", "Average O Podcast", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/54/e4/84/54e48471-6971-03c8-83f4-4f973dc2a8cb/mza_8686729233410161200.jpg/600x600bb.jpg");
        Podcast p4 = new Podcast("34e734b09246d17dc5d56f63", "Getting Smart Podcast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts115/v4/52/e3/25/52e325bd-e6ba-3899-b7b4-71e512a48472/mza_18046006527881111713.png/600x600bb.jpg");
        Podcast p5 = new Podcast("84baff1495bff70bb81bd016", "Sofra Sredom", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts115/v4/98/ca/c7/98cac700-4398-7489-100a-416ec28d6662/mza_15500803433364327137.jpg/600x600bb.jpg");
        Podcast p6 = new Podcast("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg");
        previewList.add(p1);
        previewList.add(p2);
        previewList.add(p3);
        previewList.add(p4);
        previewList.add(p5);
        previewList.add(p6);

        int row = 0;
        int column = 0;
        for (Podcast podcast : previewList){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            PodcastPreviewController controller = fxmlLoader.getController();
            controller.setData(podcast);

            gridMostLikedPodcasts.add(newPodcast, column++, row);
        }

        /*********** MOST FOLLOWED AUTHORS (AVAILABLE TO EVERYONE) ************/
        List<Author> mostFollowedAuthors = new ArrayList<>();

        for (int j = 0; j < 14; j++){
            Author a = new Author();
            a.setName("Apple Inc. " + j);
            a.setPicturePath("/img/authorAnonymousPicture.png");
            mostFollowedAuthors.add(a);
        }

        row = 0;
        column = 0;
        for (Author a : mostFollowedAuthors) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

            AnchorPane newAuthor = fxmlLoader.load();
            AuthorPreviewController controller = fxmlLoader.getController();
            controller.setData(a, true);

            gridMostFollowedAuthors.add(newAuthor, column++, row);
        }
    }
}
