package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorProfileService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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

    private final List<Pair<Author, Boolean>> followedAuthorsByAuthor;

    // List of following authors of the session actor
    private List<String> following;

    private boolean followingAuthor;

    private int row;

    private int column;

    /*** Variables to manage the scroll behavior ****/
    private final int podcastsToLoadInGrid;
    private final int authorsToRetrieve;
    private final int authorsToLoadInGrid;
    private boolean noMoreAuthors;
    /***********************************************/

    @FXML
    private BorderPane mainPage;

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
    private ImageView leftArrow;

    @FXML
    private ImageView rightArrow;

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
        this.followedAuthorsByAuthor = new ArrayList<>();
        this.following = new ArrayList<>();

        // Authors retrieved from the databases in one request (corresponds to the "limit")
        this.authorsToRetrieve = 16;
        // Authors to add to the grid at each "scroll finished" (taken from the "authors in memory" retrieved)
        this.authorsToLoadInGrid = 8;
        // Used to avoid useless call to the services
        this.noMoreAuthors = false;

        this.podcastsToLoadInGrid = 5;
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

    /********* Author Operations (Add podcast, Update Author, Delete Author, Follow Author) ***********/
    @FXML
    void onClickBtnFollowAuthor(MouseEvent event) {
        AuthorProfileService authorService = new AuthorProfileService();

        if (this.actorType.equals("Author")) {

            if (btnFollowAuthor.getText().equals("Follow")) {
                authorService.followAuthorAsAuthor(StageManager.getObjectIdentifier());
                btnFollowAuthor.setText("Unfollow");
            } else {
                authorService.unfollowAuthorAsAuthor(StageManager.getObjectIdentifier());
                btnFollowAuthor.setText("Follow");
            }

        } else if (this.actorType.equals("User")) {

            if (btnFollowAuthor.getText().equals("Follow")) {
                authorService.followAuthorAsUser(StageManager.getObjectIdentifier());
                btnFollowAuthor.setText("Unfollow");
            } else {
                authorService.unfollowAuthorAsUser(StageManager.getObjectIdentifier());
                btnFollowAuthor.setText("Follow");
            }

        } else {
            Logger.error("Operation not allowed!");
        }
    }

    @FXML
    void addPodcast(MouseEvent event) throws IOException {
        BoxBlur blur = new BoxBlur(3, 3 , 3);
        mainPage.setEffect(blur);

        // Loading the fxml file of the popup dialog
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("AddPodcast.fxml"));
        DialogPane authorDialogPane = fxmlLoader.load();

        AddPodcastController addPodcastController = fxmlLoader.getController();
        addPodcastController.setData(mainPage, this.author);

        // Creating a Dialog Pane
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(authorDialogPane);
        dialog.initOwner(mainPage.getScene().getWindow());
        dialog.setTitle("Add new podcast");

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);

        dialog.showAndWait();
        mainPage.setEffect(null);
    }

    @FXML
    void settings(MouseEvent event) throws IOException {
        BoxBlur blur = new BoxBlur(3, 3 , 3);
        mainPage.setEffect(blur);

        // Loading the fxml file of the popup dialog
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorSettings.fxml"));

        DialogPane authorSettingsDialogPane = fxmlLoader.load();
        AuthorSettingsController settingsController = fxmlLoader.getController();

        // Pass the data of the author to the dialog pane
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPage.getScene().getWindow());
        dialog.setDialogPane(authorSettingsDialogPane);
        dialog.setTitle("Settings");
        settingsController.setData((Author)MyPodcastDB.getInstance().getSessionActor(), authorName, actorPicture);

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.UNDECORATED);

        dialog.showAndWait();
        mainPage.setEffect(null);
    }

    @FXML
    void deleteAuthorByAdmin(MouseEvent event) throws IOException {
        boolean result = DialogManager.getInstance().createConfirmationAlert(mainPage, "Delete Account", "Do you really want to delete this account?");

        if (result) {
            AuthorProfileService authorProfileService = new AuthorProfileService();
            int deleteResult = authorProfileService.deleteAuthorAsAdmin(author);

            if (deleteResult == 0) {
                DialogManager.getInstance().createInformationAlert(mainPage, "Delete Account", "Account delete successfully!");
                StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
            } else {
                Logger.error("Error during the delete operation");

                String alertText;
                if (deleteResult == -1) {
                    alertText = "Author don't exists!";
                } else {
                    // General message error
                    alertText = "Something went wrong! Please try again.";
                }

                DialogManager.getInstance().createErrorAlert(mainPage, "Delete Account Failed", alertText);
            }
        } else {
            Logger.info("Operation aborted");
        }
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
    void clearIndexes(boolean newFollowedAuthors, boolean newPodcasts) {
        if (newPodcasts) {
            this.row = this.gridAuthorPodcasts.getRowCount() - 1;
            this.column = 0;
        } else if (newFollowedAuthors) {
            this.row = 0;
            this.column = this.gridAuthorsFollowed.getColumnCount();
        } else {
            this.row = 0;
            this.column = 0;
        }
    }

    // Load followed authors followed by the visited author
    void loadFollowedAuthorsByAuthor(boolean newLoad) throws IOException {
        clearIndexes(newLoad, false);

        int maxValue = Math.min(this.followedAuthorsByAuthor.size(), (gridAuthorsFollowed.getColumnCount() + this.authorsToLoadInGrid));
        for (Pair<Author, Boolean> followedAuthor: this.followedAuthorsByAuthor.subList(gridAuthorsFollowed.getColumnCount(), maxValue)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

            AnchorPane newFollowedAuthor = fxmlLoader.load();
            AuthorPreviewController controller = fxmlLoader.getController();
            controller.setData(followedAuthor.getValue0(), followedAuthor.getValue1(), 0, null);

            this.gridAuthorsFollowed.add(newFollowedAuthor, this.column++, this.row);
        }
    }

    // Load author's podcasts
    void loadPodcasts(boolean newPodcasts) throws IOException {
        clearIndexes(false, newPodcasts);

        int maxValue  = Math.min(this.author.getOwnPodcasts().size(), (this.row + this.podcastsToLoadInGrid));
        for (Podcast podcast: this.author.getOwnPodcasts().subList(this.row, maxValue)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorReducedPodcast.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            AuthorReducedPodcastController controller = fxmlLoader.getController();
            controller.setData(podcast, mainPage);

            this.gridAuthorPodcasts.add(newPodcast, this.column, this.row++);
        }
    }

    void hideEmptyGrids() {
        if (this.author.getOwnPodcasts().isEmpty()) {
            scrollPodcasts.setVisible(false);

            noPodcasts.setVisible(true);
            noPodcasts.setPrefHeight(Region.USE_COMPUTED_SIZE);
        }

        if (this.followedAuthorsByAuthor.isEmpty()) {
            boxFollowedAuthors.setVisible(false);

            noFollowersFound.setVisible(true);
            noFollowersFound.setPrefHeight(130);
        }
    }

    /******* SCROLL EVENTS ******/
    @FXML
    void onScrollGridPane(ScrollEvent event) throws IOException {
        // Hide left arrow
        leftArrow.setVisible(scrollFollowedAuthors.getHvalue() != 0);

        // Hide right arrow
        rightArrow.setVisible(scrollFollowedAuthors.getHvalue() != 1.0);

        /*
        if (scrollFollowedAuthors.getHvalue() == 1) {
            //Logger.info("Authors loaded and ready to be shown in the grid: " + (this.followedAuthors.size() - gridAuthorsFollowed.getColumnCount()));

            if (this.followedAuthors.size() - this.gridAuthorsFollowed.getColumnCount() == 0) {
                rightArrow.setImage(ImageCache.getImageFromLocalPath("/img/add.png"));
            } else {
                updateAuthorsGrid();
            }

        }
        */

        updateAuthorsGrid();
    }

    void updateAuthorsGrid() throws IOException {
        if (scrollFollowedAuthors.getHvalue() == 1) {
            Logger.info("Authors loaded and ready to be shown in the grid: " + (this.followedAuthorsByAuthor.size() - gridAuthorsFollowed.getColumnCount()));

            if ((this.followedAuthorsByAuthor.size() - gridAuthorsFollowed.getColumnCount()) == 0 && !this.noMoreAuthors) {
                Logger.info("(Call to the service) Trying to load new " + this.authorsToRetrieve + " authors in memory");

                AuthorProfileService authorProfileService = new AuthorProfileService();
                switch (this.actorType) {
                    case "Author" -> {

                        // Need to distinguish if the session author is also the visited author
                        if (StageManager.getObjectIdentifier().equals(this.author.getName())) {
                            this.noMoreAuthors = authorProfileService.loadFollowedAuthorsAsPageOwner(this.author, this.followedAuthorsByAuthor, this.authorsToRetrieve, this.followedAuthorsByAuthor.size());
                        } else {
                            this.noMoreAuthors = authorProfileService.loadFollowedAuthorsAsAuthor(this.author, this.followedAuthorsByAuthor, this.following, this.authorsToRetrieve, this.followedAuthorsByAuthor.size());
                        }
                    }
                    case "User" -> {
                        this.noMoreAuthors = authorProfileService.loadFollowedAuthorsAsUser(this.author, this.followedAuthorsByAuthor, this.following, this.authorsToRetrieve, this.followedAuthorsByAuthor.size());
                    }
                    case "Admin" -> {
                        this.noMoreAuthors = authorProfileService.loadFollowedAuthorsAsAdmin(this.author, this.followedAuthorsByAuthor, this.authorsToRetrieve, this.followedAuthorsByAuthor.size());
                    }
                    case "Unregistered" -> {
                        this.noMoreAuthors = authorProfileService.loadFollowedAuthorsAsUnregistered(this.author, this.followedAuthorsByAuthor, this.authorsToRetrieve, this.followedAuthorsByAuthor.size());
                    }
                    default -> Logger.error("Unidentified Actor Type");
                }

                Logger.info("(End call service) Total authors loaded in memory: " + this.followedAuthorsByAuthor.size() + " | Authors available to be shown: " + (this.followedAuthorsByAuthor.size() - this.gridAuthorsFollowed.getColumnCount()));
                loadFollowedAuthorsByAuthor(true);

            } else {
                Logger.info("Authors loaded in the grid: " + this.gridAuthorsFollowed.getColumnCount() + " | Authors in memory: " + this.followedAuthorsByAuthor.size());
                // Show authors already retrieved from the database
                loadFollowedAuthorsByAuthor(true);
            }
        }
    }

    void setupScrollArrows() {

        if (this.followedAuthorsByAuthor.size() >= this.authorsToLoadInGrid) {
            this.rightArrow.setVisible(true);
            Logger.info("There are authors to load");
        } else {
            // All authors are loaded
            this.rightArrow.setVisible(false);
            Logger.info("(Disabling right arrow) No other authors to load");
        }

        this.leftArrow.setVisible(false);
    }

    @FXML
    void nextFollowedAuthor(MouseEvent event) throws IOException {
        /*
        double scrollValue = 1.0 / (gridAuthorsFollowed.getColumnCount() - this.authorToLoadInGrid + 1.0);
        scrollFollowedAuthors.setHvalue(scrollFollowedAuthors.getHvalue() + scrollValue);

        leftArrow.setVisible(true);

        updateAuthorsGrid();

        if (scrollFollowedAuthors.getHvalue() == 1.0)
            rightArrow.setVisible(false);
        else
            rightArrow.setVisible(true);

        updateAuthorsGrid();
        */
    }

    @FXML
    void backFollowedAuthor(MouseEvent event) throws IOException {
        /*
        double scrollValue = 1.0 / (gridAuthorsFollowed.getColumnCount() - this.authorToLoadInGrid + 1.0);
        scrollFollowedAuthors.setHvalue(scrollFollowedAuthors.getHvalue() - scrollValue);

        rightArrow.setVisible(true);

        updateAuthorsGrid();

        if (scrollFollowedAuthors.getHvalue() == 0)
            leftArrow.setVisible(false);
        else
            leftArrow.setVisible(true);
         */
    }

    @FXML
    void onScrollPodcasts(ScrollEvent event) throws IOException {
        if (scrollPodcasts.getVvalue() == 1) {
            Logger.info("Podcasts loaded and ready to be shown in the grid: " + (this.author.getOwnPodcasts().size() - gridAuthorPodcasts.getRowCount()));
            Logger.info("Podcasts loaded in the grid: " + this.gridAuthorPodcasts.getRowCount() + " | Podcasts in memory: " + this.author.getOwnPodcasts().size());
            loadPodcasts(true);
        }
    }

    /********************/

    void redirect() throws IOException {
        // Redirect to the homepage if the author doesn't exist
        DialogManager.getInstance().createErrorAlert(mainPage, "404 Not found", "Sorry, the requested author is not available!");
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }

    public void initialize() throws IOException {
        AuthorProfileService authorProfileService = new AuthorProfileService();

        switch (this.actorType) {
            case "Author" -> {
                Author sessionActor = (Author) MyPodcastDB.getInstance().getSessionActor();

                // Setting actor personal info
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                actorPicture.setImage(image);

                if (StageManager.getObjectIdentifier().equals(sessionActor.getName())) {
                    // Session author coincides with the author profile requested
                    author.setName(sessionActor.getName());

                    boolean result = authorProfileService.loadAuthorProfileAsPageOwner(this.author, this.followedAuthorsByAuthor, this.authorsToRetrieve);
                    if (!result) {
                        Platform.runLater(() -> {
                            try {
                                redirect();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

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

                    boolean result = authorProfileService.loadAuthorProfileAsAuthor(this.author, this.followedAuthorsByAuthor, this.following, this.authorsToRetrieve);
                    if (!result) {
                        Platform.runLater(() -> {
                            try {
                                redirect();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    // Checking if the session actor follows the visited author (this not count for the author in his own profile)
                    this.followingAuthor = this.following.contains(StageManager.getObjectIdentifier());

                    if (this.followingAuthor)
                        btnFollowAuthor.setText("Unfollow");

                    // Setting GUI information about the author visited
                    authorName.setText(this.author.getName());
                    tooltipAuthorName.setText(this.author.getName());
                    authorFollowing.setText("Authors followed by " + this.author.getName());
                    podcastLabel.setText("Podcasts");

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

                boolean result = authorProfileService.loadAuthorProfileAsUser(this.author, this.followedAuthorsByAuthor, this.following, this.authorsToRetrieve);
                if (!result) {
                    Platform.runLater(() -> {
                        try {
                            redirect();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

                // Checking if the session actor follows the visited author (this not count for the author in his own profile)
                this.followingAuthor = this.following.contains(StageManager.getObjectIdentifier());

                if (this.followingAuthor)
                    btnFollowAuthor.setText("Unfollow");

                // Setting GUI information about the author visited
                this.authorName.setText(author.getName());
                this.tooltipAuthorName.setText(this.author.getName());
                this.authorFollowing.setText("Authors followed by " + this.author.getName());
                this.podcastLabel.setText("Podcasts");

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
                this.author.setName(StageManager.getObjectIdentifier());

                boolean result = authorProfileService.loadAuthorProfileAsAdmin(this.author, this.followedAuthorsByAuthor, this.authorsToRetrieve);
                if (!result) {
                    Platform.runLater(() -> {
                        try {
                            redirect();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

                // Checking if the session actor follows the visited author (this not count for the author in his own profile)
                this.followingAuthor = this.following.contains(StageManager.getObjectIdentifier());

                if (this.followingAuthor)
                    btnFollowAuthor.setText("Unfollow");

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

                boolean result = authorProfileService.loadAuthorProfileAsUnregistered(this.author, this.followedAuthorsByAuthor, this.authorsToRetrieve);
                if (!result) {
                    Platform.runLater(() -> {
                        try {
                            redirect();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

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
        this.noMoreAuthors = this.followedAuthorsByAuthor.size() < this.authorsToRetrieve;
        loadFollowedAuthorsByAuthor(false);
        loadPodcasts(false);

        // Hiding the empty grids
        hideEmptyGrids();

        // Setting scrollbar arrows
        setupScrollArrows();

        Logger.success("Author's profile loading done!");
    }
}

