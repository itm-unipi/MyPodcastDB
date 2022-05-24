package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.PodcastPreview;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorProfileController {

    @FXML
    private Label authorName;

    @FXML
    private Label authorFollowing;

    @FXML
    private GridPane gridAuthorPodcasts;

    @FXML
    private GridPane gridAuthorsFollowed;

    @FXML
    private ScrollPane scrollFollowedAuthors;

    @FXML
    private ImageView userPicture;

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

    @FXML
    void userProfile(MouseEvent event) {
        Logger.info("User profile clicked");
        //StageManager.showPage(ViewNavigator.USERPROFILE.getPage());
    }

    public void initialize() throws IOException {
        // Temp author (profile)
        Author a = new Author();
        a.setName("Robespierre");
        authorName.setText(a.getName());
        authorFollowing.setText("Authors followed by " + a.getName());

        // Authors Followed
        List<Author> authorsFollowed = new ArrayList<>();
        Author a1 = new Author();
        a1.setName("Apple Inc.");
        Author a2 = new Author();
        a2.setName("Gino Paolino");
        authorsFollowed.add(a1);
        authorsFollowed.add(a2);

        int row = 0;
        int column = 0;
        for (Author author : authorsFollowed){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorFollowed.fxml"));

            AnchorPane newAuthor = fxmlLoader.load();
            AuthorFollowedController controller = fxmlLoader.getController();
            controller.setData(author);

            gridAuthorsFollowed.add(newAuthor, column++, row);
        }

        // Author Podcasts
        List<PodcastPreview> mostLikedPodcast = new ArrayList<>();
        PodcastPreview p1 = new PodcastPreview("54eb342567c94dacfb2a3e50", "Scaling Global", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg");
        PodcastPreview p2 = new PodcastPreview("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg");
        mostLikedPodcast.add(p1);
        mostLikedPodcast.add(p2);

        row = 0;
        column = 0;
        for (PodcastPreview podcast : mostLikedPodcast){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorReducedPodcast.fxml"));

            // create new podcast element
            AnchorPane newPodcast = fxmlLoader.load();
            AuthorReducedPodcastController controller = fxmlLoader.getController();
            //controller.setData(podcast);

            // add new podcast to grid
            gridAuthorPodcasts.add(newPodcast, column, row++);
        }

    }

}

