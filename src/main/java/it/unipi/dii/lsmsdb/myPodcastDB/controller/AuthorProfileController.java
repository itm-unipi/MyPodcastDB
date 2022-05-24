package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.PodcastPreview;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorProfileController {

    @FXML
    private GridPane gridAuthorPodcasts;

    @FXML
    private Label authorName;

    @FXML
    private ImageView userPicture;

    @FXML
    void backSuggestedCategory(MouseEvent event) {

    }

    @FXML
    void nextSuggestedCategory(MouseEvent event) {

    }

    @FXML
    void userProfile(MouseEvent event) {
        Logger.info("User profile clicked");
        //StageManager.showPage(ViewNavigator.USERPROFILE.getPage());
    }

    public void initialize() throws IOException {
        Author a = new Author();
        a.setName("Robespierre");
        authorName.setText(a.getName());

        // Most Liked Podcasts
        List<PodcastPreview> mostLikedPodcast = new ArrayList<>();
        PodcastPreview p1 = new PodcastPreview("54eb342567c94dacfb2a3e50", "Scaling Global", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg");
        PodcastPreview p2 = new PodcastPreview("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg");
        PodcastPreview p3 = new PodcastPreview("061a68eb754c400eae8027d7", "Average O Podcast", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/54/e4/84/54e48471-6971-03c8-83f4-4f973dc2a8cb/mza_8686729233410161200.jpg/600x600bb.jpg");
        PodcastPreview p4 = new PodcastPreview("34e734b09246d17dc5d56f63", "Getting Smart Podcast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts115/v4/52/e3/25/52e325bd-e6ba-3899-b7b4-71e512a48472/mza_18046006527881111713.png/600x600bb.jpg");
        PodcastPreview p5 = new PodcastPreview("84baff1495bff70bb81bd016", "Sofra Sredom", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts115/v4/98/ca/c7/98cac700-4398-7489-100a-416ec28d6662/mza_15500803433364327137.jpg/600x600bb.jpg");
        PodcastPreview p6 = new PodcastPreview("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg");
        mostLikedPodcast.add(p1);
        mostLikedPodcast.add(p2);
        mostLikedPodcast.add(p3);
        mostLikedPodcast.add(p4);
        mostLikedPodcast.add(p5);
        mostLikedPodcast.add(p6);

        int row = 0;
        int column = 0;
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

