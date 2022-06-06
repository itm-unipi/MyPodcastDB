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
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorProfileController {
    private final Author author;

    private final String actorType;

    private final List<Pair<Author, Boolean>> followedAuthors;

    private boolean followingAuthor;

    private int row;

    private int column;

    @FXML
    private BorderPane MainPage;

    @FXML
    private Label authorName;


    @FXML
    private Tooltip tooltipAuthorName;

    @FXML
    private ImageView searchButton;

    @FXML
    private HBox authorButtons;


    @FXML
    private Button btnAddPodcast;

    @FXML
    private Button btnDeleteAuthor;

    @FXML
    private Button btnFollowAuthor;

    @FXML
    private Button btnSettings;

    @FXML
    private ImageView bin;

    @FXML
    private ImageView actorPicture;

    @FXML
    private ImageView logout;

    @FXML
    private VBox boxActorProfile;

    @FXML
    private VBox boxLogout;

    @FXML
    private TextField searchText;

    @FXML
    private ImageView home;

    @FXML
    private Label authorFollowing;

    @FXML
    private Label podcastLabel;

    @FXML
    private HBox boxFollowedAuthors;

    @FXML
    private VBox noFollowersFound;

    @FXML
    private VBox noPodcasts;

    @FXML
    private GridPane gridAuthorPodcasts;

    @FXML
    private GridPane gridAuthorsFollowed;

    @FXML
    private ScrollPane scrollFollowedAuthors;

    @FXML
    private ScrollPane scrollPodcasts;

    public AuthorProfileController() {
        this.actorType = MyPodcastDB.getInstance().getSessionType();
        this.followingAuthor = false;
        this.author = new Author();
        this.followedAuthors = new ArrayList<>();
    }

    /*********** Navigator Events (Profile, Home, Search) *************/
    @FXML
    void onClickActorProfile(MouseEvent event) throws IOException {
        switch (this.actorType) {
            case "Author" ->
                    StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), ((Author) MyPodcastDB.getInstance().getSessionActor()).getName());
            case "User" ->
                    StageManager.showPage(ViewNavigator.USERPAGE.getPage());
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
                // TODO: alert
            }
        }
    }

    @FXML
    void onClickHome(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }

    @FXML
    void onClickLogout(MouseEvent event) throws IOException {
        // Clear the session and exit
        MyPodcastDB.getInstance().setSession(null, null);
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    /********* Author Operations (Add podcast, Update Author, Delete Author, Follow Author) ***********/
    @FXML
    void onClickBtnFollowAuthor(MouseEvent event) {

        if (this.actorType.equals("Author")) {
            AuthorService authorService = new AuthorService();

            if (btnFollowAuthor.getText().equals("Follow")) {
                authorService.followAuthor(StageManager.getObjectIdentifier());
                btnFollowAuthor.setText("Unfollow");
            } else {
                authorService.unfollowAuthor(StageManager.getObjectIdentifier());
                btnFollowAuthor.setText("Follow");
            }

        } else if (this.actorType.equals("User")) {
            UserService userService = new UserService();

            if (btnFollowAuthor.getText().equals("Follow")) {
                userService.followAuthor(StageManager.getObjectIdentifier());
                btnFollowAuthor.setText("Unfollow");
            } else {
                userService.unfollowAuthor(StageManager.getObjectIdentifier());
                btnFollowAuthor.setText("Follow");
            }

        } else {
            Logger.error("Operation not allowed!");
        }
    }

    @FXML
    void addPodcast(MouseEvent event) throws IOException {

        BoxBlur blur = new BoxBlur(3, 3 , 3);
        MainPage.setEffect(blur);

        // Loading the fxml file of the popup dialog
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("AddPodcast.fxml"));
        DialogPane authorDialogPane = fxmlLoader.load();

        // Creating a Dialog Pane
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(authorDialogPane);
        dialog.initOwner(MainPage.getScene().getWindow());
        dialog.setTitle("Add new podcast");

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);

        dialog.showAndWait();
        MainPage.setEffect(null);
    }

    @FXML
    void settings(MouseEvent event) throws IOException {
        BoxBlur blur = new BoxBlur(3, 3 , 3);
        MainPage.setEffect(blur);

        // Loading the fxml file of the popup dialog
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorSettings.fxml"));

        DialogPane authorSettingsDialogPane = fxmlLoader.load();
        AuthorSettingsController settingsController = fxmlLoader.getController();

        // Pass the data of the author to the dialog pane
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(MainPage.getScene().getWindow());
        dialog.setDialogPane(authorSettingsDialogPane);
        dialog.setTitle("Settings");
        settingsController.setData((Author)MyPodcastDB.getInstance().getSessionActor(), authorName, actorPicture);

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.UNDECORATED);

        dialog.showAndWait();
        MainPage.setEffect(null);
    }

    @FXML
    void deleteAuthorByAdmin(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(MainPage.getScene().getWindow());
        alert.setTitle("Delete Account");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to delete this account?");
        alert.setGraphic(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {

            AdminService adminService = new AdminService();
            int deleteResult = adminService.deleteAuthor(author);

            if (deleteResult == 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(MainPage.getScene().getWindow());
                alert.setTitle("Delete Account");
                alert.setHeaderText(null);
                alert.setContentText("Account deleted successfully!");
                alert.setGraphic(null);;
                alert.showAndWait();

                StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
            } else {
                Logger.error("Error during the delete operation");

                alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(MainPage.getScene().getWindow());
                alert.setTitle("Delete Account Error");
                alert.setHeaderText(null);

                if (deleteResult == -1) {
                    alert.setContentText("Author don't exists!");
                } else {
                    // General message error
                    alert.setContentText("Something went wrong!");
                }

                alert.setGraphic(null);;
                alert.showAndWait();
            }

        } else {
            Logger.info("Operation aborted");
        }

    }

    /****************** Author Followed ******************/
    @FXML
    void nextFollowedAuthor(MouseEvent event) {
        Logger.info("next followed author");
        double scrollValue = 1;
        if (scrollFollowedAuthors.getHvalue() == 1.0)
            scrollValue = -1;
        scrollFollowedAuthors.setHvalue(scrollFollowedAuthors.getHvalue() + scrollValue);
    }

    @FXML
    void backFollowedAuthor(MouseEvent event) {
        Logger.info("back followed authors");
        double scrollValue = 1;
        if (scrollFollowedAuthors.getHvalue() == 0.0)
            scrollFollowedAuthors.setHvalue(1.0);
        else
            scrollFollowedAuthors.setHvalue(scrollFollowedAuthors.getHvalue() - scrollValue);
    }

    /********* BUTTONS HOVER AND MOUSE EXITED **********/

    @FXML
    void onHoverBtnDeleteAuthor(MouseEvent event) {
        btnDeleteAuthor.setStyle("-fx-background-color: white; -fx-text-fill: #5c5c5c; -fx-border-color: #555555; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
        bin.setStyle("-fx-blend-mode: multiply");
    }

    @FXML
    void onExitedBtnDeleteAuthor(MouseEvent event) {
        btnDeleteAuthor.setStyle("-fx-background-color:  #555555; -fx-text-fill: white; -fx-border-color:  #555555; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
        bin.setStyle("-fx-blend-mode: add");
    }

    @FXML
    void onHoverBtnFollowAuthor(MouseEvent event) {
        btnFollowAuthor.setStyle("-fx-background-color: #DA70D6; -fx-border-color: #DA70D6; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void onExitedBtnFollowAuthor(MouseEvent event) {
        btnFollowAuthor.setStyle("-fx-background-color: #db55e7; -fx-border-color: #db55e7; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void onHoverBtnAddPodcast(MouseEvent event) {
        btnAddPodcast.setStyle("-fx-background-color: whitesmoke; -fx-border-color: #bbbbbb; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void onExitedBtnAddPodcast(MouseEvent event) {
        btnAddPodcast.setStyle("-fx-background-color: white; -fx-border-color: #eaeaea; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void onHoverBtnSettings(MouseEvent event) {
        btnSettings.setStyle("-fx-background-color: whitesmoke; -fx-border-color: #bbbbbb; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void onExitedBtnSettings(MouseEvent event) {
        btnSettings.setStyle("-fx-background-color: white; -fx-border-color: #eaeaea; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    /******* LOAD GRIDS ******/
    void clearIndexes() {
        this.row = 0;
        this.column = 0;
    }

    // Load followed authors followed by the visited author
    void loadFollowedAuthors() throws IOException {
        clearIndexes();
        for (Pair<Author, Boolean> followedAuthor: this.followedAuthors) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

            AnchorPane newFollowedAuthor = fxmlLoader.load();
            AuthorPreviewController controller = fxmlLoader.getController();
            controller.setData(followedAuthor.getValue0(), followedAuthor.getValue1(), 0, null);

            this.gridAuthorsFollowed.add(newFollowedAuthor, this.column++, this.row);
        }
    }

    // Load author's podcasts
    void loadPodcasts() throws IOException {
        clearIndexes();
        for (Podcast podcast: this.author.getOwnPodcasts()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorReducedPodcast.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            AuthorReducedPodcastController controller = fxmlLoader.getController();
            controller.setData(author.getId(), podcast.getId(), podcast.getName(), podcast.getReleaseDate(), podcast.getPrimaryCategory(), podcast.getArtworkUrl600());

            this.gridAuthorPodcasts.add(newPodcast, this.column, this.row++);
        }
    }

    void hideEmptyGrids() {
        if (this.author.getOwnPodcasts().isEmpty()) {
            scrollPodcasts.setVisible(false);

            noPodcasts.setVisible(true);
            noPodcasts.setPrefHeight(Region.USE_COMPUTED_SIZE);
        }

        if (this.followedAuthors.isEmpty()) {
            boxFollowedAuthors.setVisible(false);

            noFollowersFound.setVisible(true);
            noFollowersFound.setPrefHeight(130);
        }
    }

    public void initialize() throws IOException {
        switch (this.actorType) {
            case "Author" -> {
                Author sessionActor = (Author) MyPodcastDB.getInstance().getSessionActor();

                // Setting actor personal info
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                actorPicture.setImage(image);

                AuthorService authorService = new AuthorService();
                if (StageManager.getObjectIdentifier().equals(sessionActor.getName())) {
                    // Session author coincides with the author profile requested
                    author.setName(sessionActor.getName());
                    authorService.loadAuthorOwnProfile(this.author, this.followedAuthors, 10);
                    // TODO: rimuovere dopo il rebase (è stato fatto solo perchè il login crea un utente di sessione fittizio)
                    ((Author)MyPodcastDB.getInstance().getSessionActor()).copy(this.author);

                    authorName.setText(sessionActor.getName());
                    tooltipAuthorName.setText(sessionActor.getName());
                    authorFollowing.setText("Authors you follow");
                    podcastLabel.setText("Your podcasts");

                    // Hiding unnecessary button for the author
                    btnDeleteAuthor.setVisible(false);
                    btnDeleteAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                    btnFollowAuthor.setVisible(false);
                    btnFollowAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");

                } else {
                    // Author profile requested is different form the session author
                    this.author.setName(StageManager.getObjectIdentifier());
                    this.followingAuthor = authorService.loadAuthorProfile(this.author, this.followedAuthors, 10);

                    // Setting GUI information about the author visited
                    authorName.setText(this.author.getName());
                    tooltipAuthorName.setText(this.author.getName());
                    authorFollowing.setText("Authors followed by " + this.author.getName());
                    podcastLabel.setText("Podcasts");

                    if (this.followingAuthor)
                        btnFollowAuthor.setText("Unfollow");

                    // Hiding unnecessary button
                    btnDeleteAuthor.setVisible(false);
                    btnDeleteAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                    authorButtons.setVisible(false);
                    authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                }
            }
            case "User" -> {
                User sessionActor = (User) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an user: " + sessionActor.getUsername());

                // Setting user GUI parameters of the user
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                this.actorPicture.setImage(image);

                // Requesting the author's information from the database
                this.author.setName(StageManager.getObjectIdentifier());
                UserService userService = new UserService();
                this.followingAuthor = userService.loadAuthorProfileRegistered(this.author, this.followedAuthors, 10);

                // Setting GUI information about the author visited
                this.authorName.setText(author.getName());
                this.tooltipAuthorName.setText(this.author.getName());
                this.authorFollowing.setText("Authors followed by " + this.author.getName());
                this.podcastLabel.setText("Podcasts");

                if (this.followingAuthor)
                    btnFollowAuthor.setText("Unfollow");

                // Hiding unnecessary button for the user
                btnDeleteAuthor.setVisible(false);
                btnDeleteAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                authorButtons.setVisible(false);
                authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
            }
            case "Admin" -> {
                Admin sessionActor = (Admin) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an administrator: " + sessionActor.getName());

                // Setting GUI parameters of the admin
                Image image = ImageCache.getImageFromLocalPath("/img/userPicture.png");
                this.actorPicture.setImage(image);

                // Requesting the author's information from the database
                AdminService adminService = new AdminService();
                this.author.setName(StageManager.getObjectIdentifier());
                adminService.loadAuthorProfile(this.author, this.followedAuthors, 10);

                // Setting GUI information of the author visited
                this.authorName.setText(this.author.getName());
                this.tooltipAuthorName.setText(this.author.getName());
                this.authorFollowing.setText("Authors followed by " + this.author.getName());
                this.podcastLabel.setText("Podcasts");

                // Hiding unnecessary button for the admin
                btnFollowAuthor.setVisible(false);
                btnFollowAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                authorButtons.setVisible(false);
                authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
            }
            case "Unregistered" -> {
                Logger.info("I'm an unregistered user");

                // Requesting the author's information from the database
                this.author.setName(StageManager.getObjectIdentifier());
                UserService userService = new UserService();
                userService.loadAuthorProfileUnregistered(this.author, this.followedAuthors, 10);

                this.authorName.setText(this.author.getName());
                this.tooltipAuthorName.setText(this.author.getName());
                this.authorFollowing.setText("Authors followed by " + this.author.getName());
                this.podcastLabel.setText("Podcasts");

                // Hiding buttons
                boxActorProfile.setVisible(false);
                boxActorProfile.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0; -fx-start-margin: 0; -fx-end-margin: 0");
                btnDeleteAuthor.setVisible(false);
                btnDeleteAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                btnFollowAuthor.setVisible(false);
                btnFollowAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                authorButtons.setVisible(false);
                authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
            }
            default -> Logger.error("Unidentified Actor Type");
        }

        // Load grids
        loadFollowedAuthors();
        loadPodcasts();

        // Hiding the empty grids
        hideEmptyGrids();
        Logger.success("Author's profile loading done!");
    }
}

