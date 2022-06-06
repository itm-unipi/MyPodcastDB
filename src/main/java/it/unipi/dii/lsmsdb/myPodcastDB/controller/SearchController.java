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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchController {
    private String actorType;

    private List<Podcast> podcastsMatch;

    private List<Pair<Author, Boolean>> authorsMatch;

    private List<Pair<User, Boolean>> usersMatch;

    private Triplet<Boolean, Boolean, Boolean> filters;

    @FXML
    private BorderPane MainPage;

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
    private VBox boxPodcastsFound;

    @FXML
    private VBox boxAuthorsFound;

    @FXML
    private VBox boxUsersFound;

    @FXML
    private Label noPodcastsText;

    @FXML
    private Label noAuthorsText;

    @FXML
    private Label noUsersFound;

    @FXML
    private CheckBox usersFilter;

    @FXML
    private CheckBox authorsFilter;

    @FXML
    private CheckBox podcastsFilter;

    public SearchController() {
        this.actorType = MyPodcastDB.getInstance().getSessionType();;
        this.podcastsMatch = new ArrayList<>();
        this.authorsMatch = new ArrayList<>();
        this.usersMatch = new ArrayList<>();
        this.filters = new Triplet<>(true, true, true);
    }

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

            clearData();
            this.filters = new Triplet<>(podcastsFilter.isSelected(), authorsFilter.isSelected(), usersFilter.isSelected());

            if (!filters.getValue0() && !filters.getValue1() && !filters.getValue2()) {
                Logger.error("No filter isn't allowed!");
                // TODO: alert
            } else {
                search(text);
                loadResult();
            }
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

                clearData();
                this.filters = new Triplet<>(podcastsFilter.isSelected(), authorsFilter.isSelected(), usersFilter.isSelected());

                if (!filters.getValue0() && !filters.getValue1() && !filters.getValue2()) {
                    Logger.error("No filter isn't allowed!");
                    // TODO: alert
                } else {
                    search(text);
                    loadResult();
                }

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

    void search(String text) {
        // Setting text in the GUI label
        this.searchingForText.setText("Searching for \"" + text + "\"");

        switch (this.actorType) {
            case "Author" -> {
                // Author search service
                AuthorService authorService = new AuthorService();
                authorService.search(text, podcastsMatch, authorsMatch, usersMatch, 8, filters);
            }
            case "User" -> {
                // Registered User search service
                UserService userService = new UserService();
                userService.searchRegistered(text, podcastsMatch, authorsMatch, usersMatch, 8, filters);
            }
            case "Admin" -> {
                // Admin search service
                AdminService adminService = new AdminService();
                adminService.search(text, podcastsMatch, authorsMatch, usersMatch, 8, filters);
            }
            case "Unregistered" -> {
                // Unregistered User Search Service
                UserService userService = new UserService();
                userService.searchUnregistered(text, podcastsMatch, authorsMatch, usersMatch, 8, filters);
            }
        }
    }

    void loadResult() throws IOException {

        /*********** PODCAST SEARCH ***********/
        if (!podcastsMatch.isEmpty()) {
            this.noPodcastsText.setVisible(false);
            this.noPodcastsText.setMinHeight(0);
            this.noPodcastsText.setPrefHeight(0);
            this.noPodcastsText.setMaxHeight(0);

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

        if (filters.getValue0()) {
            this.boxPodcastsFound.setVisible(true);
            this.boxPodcastsFound.setPrefHeight(Region.USE_COMPUTED_SIZE);
            this.podcastsFound.setText("Podcasts (" + podcastsMatch.size() + ")");
        }

        /*********** AUTHOR SEARCH ***********/
        if (!authorsMatch.isEmpty()) {
            this.noAuthorsText.setVisible(false);
            this.noAuthorsText.setMinHeight(0);
            this.noAuthorsText.setPrefHeight(0);
            this.noAuthorsText.setMaxHeight(0);

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

        if (filters.getValue1()) {
            this.boxAuthorsFound.setVisible(true);
            this.boxAuthorsFound.setPrefHeight(Region.USE_COMPUTED_SIZE);
            this.authorsFound.setText("Authors (" + authorsMatch.size() + ")");
        }

        /*********** USER SEARCH ***********/
        if (!actorType.equals("Author") && !actorType.equals("Unregistered")) {

            if (!usersMatch.isEmpty()) {
                this.noUsersFound.setVisible(false);
                this.noUsersFound.setMinHeight(0);
                this.noUsersFound.setPrefHeight(0);
                this.noUsersFound.setMaxHeight(0);

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

            if (filters.getValue2()) {
                this.boxUsersFound.setVisible(true);
                this.boxUsersFound.setPrefHeight(Region.USE_COMPUTED_SIZE);
                this.usersFound.setText("Users (" + usersMatch.size() + ")");
            }

        } else {
            this.boxUsersFound.setVisible(false);
        }
    }

    void clearData() {
        this.podcastsMatch.clear();
        this.authorsMatch.clear();
        this.usersMatch.clear();

        this.gridFoundUsers.getChildren().clear();
        this.gridFoundAuthors.getChildren().clear();
        this.gridFoundPodcasts.getChildren().clear();

        // Resetting visibility of some gui elements
        this.boxPodcastsFound.setVisible(false);
        this.boxPodcastsFound.setPrefHeight(0);

        this.noPodcastsText.setVisible(true);
        this.noPodcastsText.setPrefHeight(Region.USE_COMPUTED_SIZE);
        this.noPodcastsText.setMaxHeight(Region.USE_COMPUTED_SIZE);

        this.boxAuthorsFound.setVisible(false);
        this.boxAuthorsFound.setPrefHeight(0);

        this.noUsersFound.setVisible(true);
        this.noUsersFound.setPrefHeight(Region.USE_COMPUTED_SIZE);
        this.noUsersFound.setMaxHeight(Region.USE_COMPUTED_SIZE);

        this.boxUsersFound.setVisible(false);
        this.boxUsersFound.setPrefHeight(0);

        this.noAuthorsText.setVisible(true);
        this.noAuthorsText.setPrefHeight(Region.USE_COMPUTED_SIZE);
        this.noAuthorsText.setMaxHeight(Region.USE_COMPUTED_SIZE);
    }

    public void initialize() throws IOException {
        // Load information about the actor of the session

        switch (this.actorType) {
            case "Author" -> {
                Author sessionActor = (Author) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an actor: " + sessionActor.getName());

                // Setting GUI params
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                actorPicture.setImage(image);

                // Disabling users filter
                usersFilter.setVisible(false);
                usersFilter.setPrefWidth(0);

            }
            case "User" -> {
                User sessionActor = (User) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an user: " + sessionActor.getUsername());

                // Setting GUI params
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                actorPicture.setImage(image);

            }
            case "Admin" -> {
                Admin sessionActor = (Admin) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an administrator: " + sessionActor.getName());

                // Setting GUI params
                Image image = ImageCache.getImageFromLocalPath("/img/userPicture.png");
                actorPicture.setImage(image);

            }
            case "Unregistered" -> {
                Logger.info("I'm an unregistered user");

                // Disabling User Profile Page, Logout Button and Users Filter
                boxActorProfile.setVisible(false);
                boxActorProfile.setStyle("-fx-min-height: 0; -fx-pref-height: 0; -fx-min-width: 0; -fx-pref-width: 0;");
                boxLogout.setVisible(false);
                boxLogout.setStyle("-fx-min-height: 0; -fx-pref-height: 0; -fx-min-width: 0; -fx-pref-width: 0;");
                usersFilter.setVisible(false);
                usersFilter.setPrefWidth(0);
            }
            default -> Logger.error("Unidentified Actor Type");
        }

        search(StageManager.getObjectIdentifier());
        loadResult();
    }
}
