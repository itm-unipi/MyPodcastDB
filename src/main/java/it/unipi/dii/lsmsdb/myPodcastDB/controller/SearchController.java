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
import org.javatuples.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchController {
    @FXML
    private ImageView home;

    @FXML
    private ImageView actorPicture;

    @FXML
    private ImageView logout;

    @FXML
    private VBox boxActorProfile;

    @FXML
    private VBox boxLogout;

    @FXML
    private ImageView searchButton;

    @FXML
    private TextField searchText;

    @FXML
    private Label searchingForText;

    @FXML
    private GridPane gridFoundAuthors;

    @FXML
    private GridPane gridFoundUsers;

    @FXML
    private GridPane gridFoundPodcasts;

    @FXML
    private ScrollPane scrollFoundPodcasts;

    @FXML
    private Label podcastsFound;

    @FXML
    private Label authorsFound;

    @FXML
    private Label usersFound;

    @FXML
    private VBox boxUsersFound;

    @FXML
    private Label noPodcastsText;

    @FXML
    private Label noAuthorsText;

    @FXML
    private Label noUsersFound;

    /*********** Navigator Events (Profile, Home, Search) *************/
    @FXML
    void onClickActorProfile(MouseEvent event) throws IOException {
        Logger.info("Actor profile clicked");
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author"))
            StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), ((Author)MyPodcastDB.getInstance().getSessionActor()).getName());
        else if (actorType.equals("User"))
            StageManager.showPage(ViewNavigator.USERPAGE.getPage());
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

    public void initialize() throws IOException {
        List<Podcast> podcastsMatch = new ArrayList<>();
        List<Pair<Author, Boolean>> authorsMatch = new ArrayList<>();
        List<Pair<User, Boolean>> usersMatch = new ArrayList<>();

        // Load information about the actor of the session
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author")) {
            Author sessionActor = (Author) MyPodcastDB.getInstance().getSessionActor();
            Logger.info("I'm an actor: " + sessionActor.getName());

            // Setting GUI params
            Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
            actorPicture.setImage(image);

            // Author search service
            AuthorService authorService = new AuthorService();
            authorService.search(StageManager.getObjectIdentifier(), podcastsMatch, authorsMatch, usersMatch, 15);

        } else if (actorType.equals("User")) {
            User sessionActor = (User) MyPodcastDB.getInstance().getSessionActor();
            Logger.info("I'm an user: " + sessionActor.getUsername());

            // Setting GUI params
            Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
            actorPicture.setImage(image);

            // Registered User search service
            UserService userService = new UserService();
            userService.searchRegistered(StageManager.getObjectIdentifier(), podcastsMatch, authorsMatch, usersMatch, 15);

        } else if (actorType.equals("Admin")) {
            Admin sessionActor = (Admin) MyPodcastDB.getInstance().getSessionActor();
            Logger.info("I'm an administrator: " + sessionActor.getName());

            // Setting GUI params
            Image image = ImageCache.getImageFromLocalPath("/img/userPicture.png");
            actorPicture.setImage(image);

            // Admin search service
            AdminService adminService = new AdminService();
            adminService.search(StageManager.getObjectIdentifier(), podcastsMatch, authorsMatch, usersMatch, 15);

        } else if (actorType.equals("Unregistered")) {
            Logger.info("I'm an unregistered user");

            // Disabling User Profile Page and Logout Button
            boxActorProfile.setVisible(false);
            boxActorProfile.setStyle("-fx-min-height: 0; -fx-pref-height: 0; -fx-min-width: 0; -fx-pref-width: 0;");

            boxLogout.setVisible(false);
            boxLogout.setStyle("-fx-min-height: 0; -fx-pref-height: 0; -fx-min-width: 0; -fx-pref-width: 0;");

            // Unregistered User Search Service
            UserService userService = new UserService();
            userService.searchUnregistered(StageManager.getObjectIdentifier(), podcastsMatch, authorsMatch, usersMatch, 15);

        } else {
            Logger.error("Unidentified Actor Type");
        }


        // Load word searched
        this.searchingForText.setText("Searching for \"" + StageManager.getObjectIdentifier() + "\"");

        if (podcastsMatch.isEmpty()) {
            this.gridFoundPodcasts.setVisible(false);
            this.noPodcastsText.setVisible(true);
        } else {
            this.noPodcastsText.setVisible(false);
            this.noPodcastsText.setStyle("-fx-min-height: 0; -fx-pref-height: 0");

            int row = 0;
            int column = 0;
            for (Podcast entry : podcastsMatch) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorReducedPodcast.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                AuthorReducedPodcastController controller = fxmlLoader.getController();
                controller.setData(entry.getAuthorId(), entry.getId(), entry.getName(), entry.getReleaseDate(), entry.getPrimaryCategory(), entry.getArtworkUrl600());

                gridFoundPodcasts.add(newPodcast, column, row++);
            }
        }

        this.podcastsFound.setText("Podcasts (" + podcastsMatch.size() + ")");

        /********************************************************************************/

        if (authorsMatch.isEmpty()) {
            this.gridFoundAuthors.setVisible(false);
            this.noAuthorsText.setVisible(true);
        } else {
            this.noAuthorsText.setVisible(false);
            this.noAuthorsText.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");

            int row = 0;
            int column = 0;
            for (Pair<Author, Boolean> author : authorsMatch) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorSearchPreview.fxml"));

                AnchorPane newAuthor = fxmlLoader.load();
                AuthorSearchPreviewController controller = fxmlLoader.getController();
                controller.setData(author.getValue0(), author.getValue1());

                gridFoundAuthors.add(newAuthor, column, row++);
            }
        }

        this.authorsFound.setText("Authors (" + authorsMatch.size() + ")");

        /********************************************************************************/
        // User found
        if (!actorType.equals("Author") && !actorType.equals("Unregistered")) {

            if (usersMatch.isEmpty()) {
                this.gridFoundUsers.setVisible(false);
                this.noUsersFound.setVisible(true);
            } else {
                this.noUsersFound.setVisible(false);
                this.noUsersFound.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");

                int row = 0;
                int column = 0;
                for (Pair<User, Boolean> user : usersMatch) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getClassLoader().getResource("UserSearchPreview.fxml"));

                    AnchorPane newUser = fxmlLoader.load();
                    UserSearchPreviewController controller = fxmlLoader.getController();
                    controller.setData(user.getValue0(), user.getValue1());

                    gridFoundUsers.add(newUser, column, row++);
                }
            }

            this.usersFound.setText("Users (" + usersMatch.size() + ")");
        } else {
            this.boxUsersFound.setVisible(false);
        }
    }
}
