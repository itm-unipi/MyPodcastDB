package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.SearchService;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchController {
    private final String actorType;

    private int row;

    private int column;

    private List<Podcast> podcastsMatch;

    private List<Pair<Author, Boolean>> authorsMatch;

    private List<Pair<User, Boolean>> usersMatch;

    private Triplet<Boolean, Boolean, Boolean> filters;

    /*** Variables to manage the scroll behavior ****/
    private final int authorsToRetrieve;
    private final int authorsToLoadInGrid;
    private boolean noMoreAuthors;
    private final int podcastsToRetrieve;
    private final int podcastsToLoadInGrid;
    private boolean noMorePodcasts;
    private final int usersToRetrieve;
    private final int usersToLoadInGrid;
    private boolean noMoreUsers;

    /*************************/

    private int limit;

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
    private ScrollPane scrollFoundAuthors;

    @FXML
    private ScrollPane scrollFoundUsers;

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

        this.limit = 5;

        this.authorsToRetrieve = 5;
        this.authorsToLoadInGrid = 4;
        this.noMoreAuthors = false;

        this.podcastsToRetrieve = 5;
        this.podcastsToLoadInGrid = 4;
        this.noMorePodcasts = false;

        this.usersToRetrieve = 5;
        this.usersToLoadInGrid = 4;
        this.noMoreUsers = false;
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

            clearData();
            this.filters = new Triplet<>(podcastsFilter.isSelected(), authorsFilter.isSelected(), usersFilter.isSelected());

            if (!filters.getValue0() && !filters.getValue1() && !filters.getValue2()) {
                Logger.error("No filter isn't allowed!");
                // TODO: alert
            } else {
                search(text);
                loadResults();
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
                    loadResults();
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
    }

    @FXML
    void onClickLogout(MouseEvent event) throws IOException {
        // Clear the session and exit
        MyPodcastDB.getInstance().setSession(null, null);
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    /**********************************************************/

    void search(String text) {
        // Setting text in the GUI label
        this.searchingForText.setText("Searching for \"" + text + "\"");

        SearchService searchService = new SearchService();

        switch (this.actorType) {
            case "Author" -> {
                searchService.searchAsAuthor(text, podcastsMatch, authorsMatch, usersMatch, this.limit, filters);
            }
            case "User" -> {
                searchService.searchAsUser(text, podcastsMatch, authorsMatch, usersMatch, this.limit, filters);
            }
            case "Admin" -> {
                searchService.searchAsAdmin(text, podcastsMatch, authorsMatch, usersMatch, this.limit, filters);
            }
            case "Unregistered" -> {
                searchService.searchAsUnregisteredUser(text, podcastsMatch, authorsMatch, usersMatch, this.limit, filters);
            }
        }
    }

    void clearIndexes(boolean newLoad, GridPane grid) {
        if (newLoad) {
            this.row = grid.getRowCount();
        } else {
            this.row = 0;
        }

        this.column = 0;
    }

    void loadUsersFoundGrid(boolean newLoad) throws IOException {
        if (!usersMatch.isEmpty()) {
            this.noUsersFound.setVisible(false);
            this.noUsersFound.setMinHeight(0);
            this.noUsersFound.setPrefHeight(0);
            this.noUsersFound.setMaxHeight(0);

            clearIndexes(newLoad, gridFoundUsers);
            int maxValue = Math.min(this.usersMatch.size(), (this.row + this.usersToLoadInGrid));

            for (Pair<User, Boolean> user : this.usersMatch.subList(this.row, maxValue)) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("UserSearchPreview.fxml"));

                AnchorPane newUser = fxmlLoader.load();
                UserSearchPreviewController controller = fxmlLoader.getController();
                controller.setData(user.getValue0(), user.getValue1());

                gridFoundUsers.add(newUser, this.column, this.row++);
            }
        }

        // If filters include user the "user's Box" make it visible
        if (filters.getValue2()) {
            this.boxUsersFound.setVisible(true);
            this.boxUsersFound.setPrefHeight(Region.USE_COMPUTED_SIZE);
            this.usersFound.setText("Users (" + usersMatch.size() + ")");
        }
    }

    void loadPodcastsFoundGrid(boolean newLoad) throws IOException {
        if (!podcastsMatch.isEmpty()) {
            this.noPodcastsText.setVisible(false);
            this.noPodcastsText.setMinHeight(0);
            this.noPodcastsText.setPrefHeight(0);
            this.noPodcastsText.setMaxHeight(0);

            clearIndexes(newLoad, gridFoundPodcasts);

            int maxValue = Math.min(this.podcastsMatch.size(), (this.row + this.podcastsToLoadInGrid));

            for (Podcast entry : this.podcastsMatch.subList(this.row, maxValue)) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorReducedPodcast.fxml"));

                AnchorPane newPodcast = fxmlLoader.load();
                AuthorReducedPodcastController controller = fxmlLoader.getController();
                controller.setData(entry.getAuthorId(), entry.getId(), entry.getName(), entry.getReleaseDate(), entry.getPrimaryCategory(), entry.getArtworkUrl600());

                gridFoundPodcasts.add(newPodcast, this.column, this.row++);
            }
        }

        if (filters.getValue0()) {
            this.boxPodcastsFound.setVisible(true);
            this.boxPodcastsFound.setPrefHeight(Region.USE_COMPUTED_SIZE);
            this.podcastsFound.setText("Podcasts (" + podcastsMatch.size() + ")");
        }
    }

    void loadAuthorsFoundGrid(boolean newLoad) throws IOException {
        if (!this.authorsMatch.isEmpty()) {
            this.noAuthorsText.setVisible(false);
            this.noAuthorsText.setMinHeight(0);
            this.noAuthorsText.setPrefHeight(0);
            this.noAuthorsText.setMaxHeight(0);

            clearIndexes(newLoad, gridFoundAuthors);
            int maxValue = Math.min(this.authorsMatch.size(), (this.row + this.usersToLoadInGrid));

            for (Pair<Author, Boolean> author : this.authorsMatch.subList(this.row, maxValue)) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorSearchPreview.fxml"));

                AnchorPane newAuthor = fxmlLoader.load();
                AuthorSearchPreviewController controller = fxmlLoader.getController();
                controller.setData(author.getValue0(), author.getValue1());

                gridFoundAuthors.add(newAuthor, this.column, this.row++);
            }
        }

        if (filters.getValue1()) {
            this.boxAuthorsFound.setVisible(true);
            this.boxAuthorsFound.setPrefHeight(Region.USE_COMPUTED_SIZE);
            this.authorsFound.setText("Authors (" + authorsMatch.size() + ")");
        }
    }

    void loadResults() throws IOException {
        // Load all the grids
        this.noMorePodcasts = this.podcastsMatch.size() < this.podcastsToRetrieve;
        loadPodcastsFoundGrid(false);

        this.noMoreAuthors = this.authorsMatch.size() < this.authorsToRetrieve;
        loadAuthorsFoundGrid(false);

        if (this.actorType.equals("User") || this.actorType.equals("Admin")) {
            this.noMoreUsers = this.usersMatch.size() < this.usersToRetrieve;
            loadUsersFoundGrid(false);
        } else
            this.boxUsersFound.setVisible(false);
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

    /****** SCROLL EVENTS *********/
    @FXML
    void onScrollAuthors(ScrollEvent event) throws IOException {
        if (scrollFoundAuthors.getVvalue() == 1) {
            Logger.info("Authors loaded and ready to be shown in the grid: " + (this.authorsMatch.size() - gridFoundAuthors.getRowCount()));

            if ((this.authorsMatch.size() - gridFoundAuthors.getRowCount() == 0) && !this.noMoreAuthors) {
                Logger.info("(Call to the service) Trying to load new " + this.podcastsToRetrieve + " authors in memory");

                SearchService searchService = new SearchService();
                switch (this.actorType) {
                    case "Author" -> {
                        this.noMoreAuthors = searchService.loadMoreAuthorsAsAuthor(searchText.getText(), this.authorsMatch, this.authorsToRetrieve, this.authorsMatch.size());
                    }
                    case "User" -> {
                        this.noMoreAuthors = searchService.loadMoreAuthorsAsUser(searchText.getText(), this.authorsMatch, this.authorsToRetrieve, this.authorsMatch.size());
                    }
                    case "Admin" -> {
                        this.noMoreAuthors = searchService.loadMoreAuthorsAsAdmin(searchText.getText(), this.authorsMatch, this.authorsToRetrieve, this.authorsMatch.size());
                    }
                    case "Unregistered" -> {
                        this.noMoreAuthors = searchService.loadMoreAuthorsAsUnregistered(searchText.getText(), this.authorsMatch, this.authorsToRetrieve, this.authorsMatch.size());
                    }
                    default -> Logger.error("Unidentified Actor Type");
                }

                Logger.info("(End call service) Total authors loaded in memory: " + this.authorsMatch.size() + " | Authors available to be shown: " + (this.authorsMatch.size() - this.gridFoundAuthors.getRowCount()));
                loadAuthorsFoundGrid(true);

            } else {
                Logger.info("Authors loaded in the grid: " + this.gridFoundAuthors.getRowCount() + " | Authors in memory: " + this.authorsMatch.size());
                // Show authors already retrieved from the database
                loadAuthorsFoundGrid(true);
            }
        }
    }

    @FXML
    void onScrollPodcastsFound(ScrollEvent event) throws IOException {
        if (scrollFoundPodcasts.getVvalue() == 1) {
            Logger.info("Podcasts loaded and ready to be shown in the grid: " + (this.podcastsMatch.size() - gridFoundPodcasts.getRowCount()));

            if ((this.podcastsMatch.size() - gridFoundPodcasts.getRowCount()) == 0 && !this.noMorePodcasts) {
                Logger.info("(Call to the service) Trying to load new " + this.podcastsToRetrieve + " podcasts in memory");

                SearchService searchService = new SearchService();
                switch (this.actorType) {
                    case "Author" -> {
                        this.noMorePodcasts = searchService.loadMorePodcastsAsAuthor(searchText.getText(), this.podcastsMatch, this.podcastsToRetrieve, this.podcastsMatch.size());
                    }
                    case "User" -> {
                        this.noMorePodcasts = searchService.loadMorePodcastsAsUser(searchText.getText(), this.podcastsMatch, this.podcastsToRetrieve, this.podcastsMatch.size());
                    }
                    case "Admin" -> {
                        this.noMorePodcasts = searchService.loadMorePodcastsAsAdmin(searchText.getText(), this.podcastsMatch, this.podcastsToRetrieve, this.podcastsMatch.size());
                    }
                    case "Unregistered" -> {
                        this.noMorePodcasts = searchService.loadMorePodcastsAsUnregistered(searchText.getText(), this.podcastsMatch, this.podcastsToRetrieve, this.podcastsMatch.size());
                    }
                    default -> Logger.error("Unidentified Actor Type");
                }

                Logger.info("(End call service) Total podcasts loaded in memory: " + this.podcastsMatch.size() + " | Podcasts available to be shown: " + (this.podcastsMatch.size() - this.gridFoundPodcasts.getRowCount()));
                loadPodcastsFoundGrid(true);

            } else {
                Logger.info("Podcasts loaded in the grid: " + this.gridFoundPodcasts.getRowCount() + " | Podcasts in memory: " + this.podcastsMatch.size());
                // Show authors already retrieved from the database
                loadPodcastsFoundGrid(true);
            }
        }
    }

    @FXML
    void onScrollUsersFound(ScrollEvent event) throws IOException {
        if (scrollFoundUsers.getVvalue() == 1) {
            Logger.info("Users loaded and ready to be shown in the grid: " + (this.usersMatch.size() - gridFoundUsers.getRowCount()));

            if ((this.usersMatch.size() - gridFoundUsers.getRowCount()) == 0 && !this.noMoreUsers) {
                Logger.info("(Call to the service) Trying to load new " + this.usersToRetrieve + " users in memory");

                SearchService searchService = new SearchService();
                switch (this.actorType) {
                    case "User" -> {
                        this.noMoreUsers = searchService.loadMoreUsersAsUser(searchText.getText(), this.usersMatch, this.usersToRetrieve, this.usersMatch.size());
                    }
                    case "Admin" -> {
                        this.noMoreUsers = searchService.loadMoreUsersAsAdmin(searchText.getText(), this.usersMatch, this.usersToRetrieve, this.usersMatch.size());
                    }
                    default -> Logger.error("Unidentified Actor Type");
                }

                Logger.info("(End call service) Total users loaded in memory: " + this.usersMatch.size() + " | Users available to be shown: " + (this.usersMatch.size() - this.gridFoundUsers.getRowCount()));
                loadUsersFoundGrid(true);

            } else {
                Logger.info("Users loaded in the grid: " + this.gridFoundUsers.getRowCount() + " | Users in memory: " + this.usersMatch.size());
                // Show authors already retrieved from the database
                loadUsersFoundGrid(true);
            }
        }
    }

    public void initialize() throws IOException {
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

        searchText.setText(StageManager.getObjectIdentifier());
        search(searchText.getText());
        loadResults();
    }
}
