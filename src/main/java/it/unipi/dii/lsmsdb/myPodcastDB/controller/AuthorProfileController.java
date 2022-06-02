package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorService;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthorProfileController {
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
    private Button followAuthor;

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
        Logger.info("Settings button clicked");

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
        settingsController.setData((Author)MyPodcastDB.getInstance().getSessionActor());

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/logo.png"));
        stage.initStyle(StageStyle.UNDECORATED);

        Author old = new Author();
        old.setName(((Author)MyPodcastDB.getInstance().getSessionActor()).getName());
        old.setId(((Author)MyPodcastDB.getInstance().getSessionActor()).getId());
        old.setPassword(((Author)MyPodcastDB.getInstance().getSessionActor()).getPassword());
        old.setEmail(((Author)MyPodcastDB.getInstance().getSessionActor()).getEmail());
        old.setPicturePath(((Author)MyPodcastDB.getInstance().getSessionActor()).getPicturePath());
        Logger.info("OLD " + old);

        dialog.showAndWait();

        Author curr = (Author)MyPodcastDB.getInstance().getSessionActor();
        Logger.info("NEW " + curr);

        if (curr.getName().equals(old.getName()) && curr.getId().equals(old.getId()) && curr.getEmail().equals(old.getEmail())
                && curr.getPassword().equals(old.getPassword()) && curr.getPicturePath().equals(old.getPicturePath()))
            Logger.info("No changes");
        else {
            // TODO: Database update
            Logger.info("Query to update..");

            // Updating GUI
            authorName.setText(((Author) MyPodcastDB.getInstance().getSessionActor()).getName());
            actorPicture.setImage(ImageCache.getImageFromLocalPath(((Author) MyPodcastDB.getInstance().getSessionActor()).getPicturePath()));

            // Updating Session Object
            StageManager.setObjectIdentifier(((Author) MyPodcastDB.getInstance().getSessionActor()).getName());
        }

        MainPage.setEffect(null);
    }

    @FXML
    void deleteAuthorByAdmin(MouseEvent event) throws IOException {
        Logger.info("Deleting author");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(MainPage.getScene().getWindow());
        alert.setTitle("Delete Account");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to delete this account?");
        alert.setGraphic(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            Logger.info("Delete account..");

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(MainPage.getScene().getWindow());
            alert.setTitle("Delete Account");
            alert.setHeaderText(null);
            alert.setContentText("Account deleted successfully!");
            alert.setGraphic(null);;
            alert.showAndWait();

            // TODO: query to delete the author...

            StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());

        } else {
            Logger.info("Operation aborted");
        }

    }

    /**********************************************************/

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

    public void initialize() throws IOException {
        // TEST SERVICE
        AuthorService authorService = new AuthorService();
        Author diversamente = new Author();
        diversamente.setName("Kevin Pike");
        List<Author> followed = new ArrayList<>();
        authorService.loadAuthorOwnProfile(diversamente, followed, 10);
        Logger.info("TEST SERVICE:\n" + diversamente + "\n" + followed);

        // Load information about the actor of the session
        String actorType = MyPodcastDB.getInstance().getSessionType();

        switch (actorType) {
            case "Author" -> {
                Author sessionActor = (Author) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an author: " + sessionActor.getName());

                // Setting actor personal info
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                actorPicture.setImage(image);

                if (StageManager.getObjectIdentifier().equals(((Author) MyPodcastDB.getInstance().getSessionActor()).getName())) {
                    authorName.setText(sessionActor.getName());
                    authorFollowing.setText("Authors you follow");
                    podcastLabel.setText("Your podcasts");

                    // Hiding unnecessary button for the author
                    deleteAuthorButton.setVisible(false);
                    deleteAuthorButton.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                    followAuthor.setVisible(false);
                    followAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");

                } else {
                    // Hiding unnecessary button
                    deleteAuthorButton.setVisible(false);
                    deleteAuthorButton.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                    authorButtons.setVisible(false);
                    authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");

                    // QUERY
                    Author author = new Author();
                    author.setName(StageManager.getObjectIdentifier());
                    authorName.setText(author.getName());
                    authorFollowing.setText("Authors followed by " + author.getName());
                    podcastLabel.setText("Podcasts");
                }
            }
            case "User" -> {
                User sessionActor = (User) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an user: " + sessionActor.getUsername());

                // Setting actor stuff
                Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
                actorPicture.setImage(image);

                // Hiding unnecessary button for the user
                deleteAuthorButton.setVisible(false);
                deleteAuthorButton.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                authorButtons.setVisible(false);
                authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");

                Author a = new Author(); // Find using ObjectIdentifier
                a.setName(StageManager.getObjectIdentifier());
                authorName.setText(a.getName());
                authorFollowing.setText("Authors followed by " + a.getName());
                podcastLabel.setText("Podcasts");
            }
            case "Admin" -> {
                Admin sessionActor = (Admin) MyPodcastDB.getInstance().getSessionActor();
                Logger.info("I'm an administrator: " + sessionActor.getName());

                // Setting GUI params
                Image image = ImageCache.getImageFromLocalPath("/img/userPicture.png");
                actorPicture.setImage(image);

                // Hiding unnecessary button for the admin
                followAuthor.setVisible(false);
                followAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                authorButtons.setVisible(false);
                authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");

                // Find using ObjectIdentifier
                Author a = new Author();
                a.setName(StageManager.getObjectIdentifier());
                authorName.setText(a.getName());
                authorFollowing.setText("Authors followed by " + a.getName());
                podcastLabel.setText("Podcasts");

            }
            case "Unregistered" -> {
                Logger.info("I'm an unregistered user");

                // Hiding buttons
                deleteAuthorButton.setVisible(false);
                deleteAuthorButton.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                followAuthor.setVisible(false);
                followAuthor.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");
                authorButtons.setVisible(false);
                authorButtons.setStyle("-fx-pref-width: 0; -fx-min-width: 0; -fx-pref-height: 0; -fx-min-height: 0;");

                Author a = new Author(); // Find using ObjectIdentifier
                a.setName(StageManager.getObjectIdentifier());
                authorName.setText(a.getName());
                authorFollowing.setText("Authors followed by " + a.getName());

                podcastLabel.setText("Podcasts");
            }

            default -> Logger.error("Unidentified Actor Type");
        }

        // Authors Followed
        List<Author> authorsFollowed = new ArrayList<>();
        Author a1 = new Author();
        a1.setName("Apple Inc.");
        a1.setPicturePath("/img/authorAnonymousPicture.png");
        Author a2 = new Author();
        a2.setName("Gino Paolino");
        a2.setPicturePath("/img/test.jpg");
        authorsFollowed.add(a1);
        authorsFollowed.add(a2);

        int row = 0;
        int column = 0;
        for (Author author : authorsFollowed){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));

            AnchorPane newAuthor = fxmlLoader.load();
            AuthorPreviewController controller = fxmlLoader.getController();
            controller.setData(author);

            gridAuthorsFollowed.add(newAuthor, column++, row);
        }

        // Author Podcasts
        Author author = new Author();
        author.setName("Robespierre Janjaq");

        List<Podcast> previewList = new ArrayList<>();
        Podcast p1 = new Podcast("54eb342567c94dacfb2a3e50", "Scaling Global", new Date(), "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg", "Business");
        Podcast p2 = new Podcast("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", new Date(), "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg", "Roman");
        Podcast p3 = new Podcast("061a68eb754c400eae8027d7", "Average O Podcast", new Date(), "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/54/e4/84/54e48471-6971-03c8-83f4-4f973dc2a8cb/mza_8686729233410161200.jpg/600x600bb.jpg", "Chill");
        Podcast p4 = new Podcast("34e734b09246d17dc5d56f63", "Getting Smart Podcast", new Date(), "https://is5-ssl.mzstatic.com/image/thumb/Podcasts115/v4/52/e3/25/52e325bd-e6ba-3899-b7b4-71e512a48472/mza_18046006527881111713.png/600x600bb.jpg", "");
        Podcast p5 = new Podcast("84baff1495bff70bb81bd016", "Sofra Sredom", new Date(), "https://is4-ssl.mzstatic.com/image/thumb/Podcasts115/v4/98/ca/c7/98cac700-4398-7489-100a-416ec28d6662/mza_15500803433364327137.jpg/600x600bb.jpg", "Science");
        Podcast p6 = new Podcast("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", new Date(), "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg", "Moon");
        previewList.add(p1);
        previewList.add(p2);
        previewList.add(p3);
        previewList.add(p4);
        previewList.add(p5);
        previewList.add(p6);

        author.setOwnPodcasts(previewList);

        row = 0;
        column = 0;
        for (Podcast entry: author.getOwnPodcasts()){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorReducedPodcast.fxml"));

            // create new podcast element
            AnchorPane newPodcast = fxmlLoader.load();
            AuthorReducedPodcastController controller = fxmlLoader.getController();
            controller.setData(entry.getId(), entry.getName(), entry.getReleaseDate(), entry.getPrimaryCategory(), entry.getArtworkUrl600());

            // add new podcast to grid
            gridAuthorPodcasts.add(newPodcast, column, row++);
        }
    }
}

