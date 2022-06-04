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
    private Author author;
    @FXML
    private BorderPane MainPage;

    @FXML
    private Label authorName;

    @FXML
    private ImageView searchButton;

    @FXML
    private HBox authorButtons;

    @FXML
    private Button deleteAuthorButton;

    @FXML
    private Button btnFollowAuthor;

    private boolean followingAuthor;

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
    private GridPane gridAuthorPodcasts;

    @FXML
    private GridPane gridAuthorsFollowed;

    @FXML
    private ScrollPane scrollFollowedAuthors;

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

    /********* Author Operations (Add podcast, Update Author, Delete Author, Follow Author) ***********/
    @FXML
    void onClickBtnFollowAuthor(MouseEvent event) {
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author")) {
            AuthorService authorService = new AuthorService();

            if (btnFollowAuthor.getText().equals("Follow")) {
                authorService.followAuthor(StageManager.getObjectIdentifier());
                btnFollowAuthor.setText("Unfollow");
            } else {
                authorService.unfollowAuthor(StageManager.getObjectIdentifier());
                btnFollowAuthor.setText("Follow");
            }

        } else if (actorType.equals("User")) {
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
        Logger.info("Add podcast button clicked");

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

    /*****************************************************/

    public void initialize() throws IOException {
        // Load information about the actor of the session
        String actorType = MyPodcastDB.getInstance().getSessionType();
        this.followingAuthor = false;

        // Declaring object in order to contain author's information
        List<Pair<Author, Boolean>> followedAuthors = new ArrayList<>();
        this.author = new Author();

        switch (actorType) {
            case "Author" -> {
                Author sessionActor = (Author) MyPodcastDB.getInstance().getSessionActor();

                // Setting actor personal info
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                actorPicture.setImage(image);

                if (StageManager.getObjectIdentifier().equals(sessionActor.getName())) {
                    // Session author coincides with the author profile requested
                    AuthorService authorService = new AuthorService();
                    author.setName(sessionActor.getName());
                    authorService.loadAuthorOwnProfile(author, followedAuthors, 10);
                    // TODO: rimuovere dopo il rebase
                    ((Author)MyPodcastDB.getInstance().getSessionActor()).copy(author);

                    authorName.setText(sessionActor.getName());
                    authorFollowing.setText("Authors you follow");
                    podcastLabel.setText("Your podcasts");

                    // Hiding unnecessary button for the author
                    deleteAuthorButton.setVisible(false);
                    deleteAuthorButton.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                    btnFollowAuthor.setVisible(false);
                    btnFollowAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");

                } else {
                    // Author profile requested is different form the session author
                    AuthorService authorService = new AuthorService();
                    author.setName(StageManager.getObjectIdentifier());
                    this.followingAuthor = authorService.loadAuthorProfile(author, followedAuthors, 10);

                    // Setting GUI information about the author visited
                    authorName.setText(author.getName());
                    authorFollowing.setText("Authors followed by " + author.getName());
                    podcastLabel.setText("Podcasts");

                    if (this.followingAuthor)
                        btnFollowAuthor.setText("Unfollow");

                    // Hiding unnecessary button
                    deleteAuthorButton.setVisible(false);
                    deleteAuthorButton.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                    authorButtons.setVisible(false);
                    authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                }
            }
            case "User" -> {
                User sessionActor = (User) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an user: " + sessionActor.getUsername());

                // Setting user GUI parameters of the user
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                actorPicture.setImage(image);

                // Requesting the author's information from the database
                author.setName(StageManager.getObjectIdentifier());
                UserService userService = new UserService();
                this.followingAuthor = userService.loadAuthorProfileRegistered(author, followedAuthors, 10);

                // Setting GUI information about the author visited
                authorName.setText(author.getName());
                authorFollowing.setText("Authors followed by " + author.getName());
                podcastLabel.setText("Podcasts");

                if (this.followingAuthor)
                    btnFollowAuthor.setText("Unfollow");

                // Hiding unnecessary button for the user
                deleteAuthorButton.setVisible(false);
                deleteAuthorButton.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                authorButtons.setVisible(false);
                authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
            }
            case "Admin" -> {
                Admin sessionActor = (Admin) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an administrator: " + sessionActor.getName());

                // Setting GUI parameters of the admin
                Image image = ImageCache.getImageFromLocalPath("/img/userPicture.png");
                actorPicture.setImage(image);

                // Requesting the author's information from the database
                AdminService adminService = new AdminService();
                author.setName(StageManager.getObjectIdentifier());
                adminService.loadAuthorProfile(author, followedAuthors, 10);

                // Setting GUI information of the author visited
                authorName.setText(author.getName());
                authorFollowing.setText("Authors followed by " + author.getName());
                podcastLabel.setText("Podcasts");

                // Hiding unnecessary button for the admin
                btnFollowAuthor.setVisible(false);
                btnFollowAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                authorButtons.setVisible(false);
                authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
            }
            case "Unregistered" -> {
                Logger.info("I'm an unregistered user");

                // Requesting the author's information from the database
                author.setName(StageManager.getObjectIdentifier());
                UserService userService = new UserService();
                userService.loadAuthorProfileUnregistered(author, followedAuthors, 10);

                authorName.setText(author.getName());
                authorFollowing.setText("Authors followed by " + author.getName());
                podcastLabel.setText("Podcasts");

                // Hiding buttons
                boxActorProfile.setVisible(false);
                boxActorProfile.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0; -fx-start-margin: 0; -fx-end-margin: 0");
                deleteAuthorButton.setVisible(false);
                deleteAuthorButton.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                btnFollowAuthor.setVisible(false);
                btnFollowAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                authorButtons.setVisible(false);
                authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
            }
            default -> Logger.error("Unidentified Actor Type");
        }

        // Authors followed by the author
        int row = 0;
        int column = 0;
        for (Pair<Author, Boolean> followedAuthor: followedAuthors){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

            AnchorPane newFollowedAuthor = fxmlLoader.load();
            AuthorPreviewController controller = fxmlLoader.getController();
            controller.setData(followedAuthor.getValue0(), followedAuthor.getValue1());

            gridAuthorsFollowed.add(newFollowedAuthor, column++, row);
        }

        // Getting author podcasts
        row = 0;
        column = 0;
        for (Podcast podcast: author.getOwnPodcasts()){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorReducedPodcast.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            AuthorReducedPodcastController controller = fxmlLoader.getController();
            controller.setData(author.getId(), podcast.getId(), podcast.getName(), podcast.getReleaseDate(), podcast.getPrimaryCategory(), podcast.getArtworkUrl600());

            gridAuthorPodcasts.add(newPodcast, column, row++);
        }
    }
}

