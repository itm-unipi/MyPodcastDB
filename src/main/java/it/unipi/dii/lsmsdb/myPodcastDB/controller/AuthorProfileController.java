package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
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
import org.javatuples.Triplet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthorProfileController {
    @FXML
    private Label authorName;

    @FXML
    private ImageView searchButton;

    @FXML
    private ImageView actorPicture;

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
        // Load information about the actor of the session
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author")) {
            Author sessionActor = (Author)MyPodcastDB.getInstance().getSessionActor();
            Logger.info("I'm an actor: " + sessionActor.getName());

            // Setting actor personal info
            Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
            actorPicture.setImage(image);

            if (StageManager.getObjectIdentifier().equals(((Author)MyPodcastDB.getInstance().getSessionActor()).getName())) {
                authorName.setText(sessionActor.getName());
                authorFollowing.setText("Authors you follow");
                podcastLabel.setText("Your podcasts");
            } else {
                // QUERY
                Author author = new Author();
                author.setName(StageManager.getObjectIdentifier());
                authorName.setText(author.getName());
                authorFollowing.setText("Authors followed by " + author.getName());
                podcastLabel.setText("Podcasts");
            }

        } else if (actorType.equals("User")) {
            User sessionActor = (User)MyPodcastDB.getInstance().getSessionActor();
            Logger.info("I'm an user: " + sessionActor.getUsername());

            // Setting actor stuff
            Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
            actorPicture.setImage(image);

            Author a = new Author(); // Find using ObjectIdentifier
            a.setName(StageManager.getObjectIdentifier());
            authorName.setText(a.getName());
            authorFollowing.setText("Authors followed by " + a.getName());

            podcastLabel.setText("Your podcasts");

        } else
            Logger.error("Unidentified Actor Type");

        // Authors Followed
        List<Author> authorsFollowed = new ArrayList<>();
        Author a1 = new Author();
        a1.setName("Apple Inc.");
        a1.setPicturePath("/img/authorAnonymousePicture.png");
        Author a2 = new Author();
        a2.setName("Gino Paolino");
        a2.setPicturePath("/img/test.png");
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

