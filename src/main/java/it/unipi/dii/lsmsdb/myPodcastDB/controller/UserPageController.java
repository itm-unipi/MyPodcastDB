package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.PodcastPreview;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserPageController {

    @FXML
    private ImageView homeButton;

    @FXML
    private ImageView searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField userPageCountryTextField;

    @FXML
    private TextField userPageFavGenreTextField;

    @FXML
    private Button userPageFollowButton;

    @FXML
    private ImageView  userPageWatchlistButton;

    @FXML
    private ImageView  userPageLikedButton;

    @FXML
    private TextField userPageGenderTextField;

    @FXML
    private GridPane userPageLikedGrid;

    @FXML
    private TextField userPageUsernameTextField;

    @FXML
    private GridPane userPageWatchlistGrid;

    @FXML
    private ScrollPane userPageWatchlistScrollPane;

    @FXML
    private ScrollPane userPageLikedScrollPane;

    @FXML
    private ImageView userPageImage;

    @FXML
    private GridPane userPageAuthorGrid;

    public void initialize() throws IOException {

        // service simulation
        User user = new User();
        user.setUsername("whitegoose14611");
        user.setAge(65);
        user.setGender("male");
        user.setPictureMedium("https://randomuser.me/api/portraits/med/men/11.jpg");
        user.setFavouriteGenre("Music History");
        user.setCountry("Spain");
        List<PodcastPreview> test = new ArrayList<>();
        PodcastPreview p1 = new PodcastPreview("54eb342567c94dacfb2a3e50", "Scaling Global", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg");
        PodcastPreview p2 = new PodcastPreview("9852b276565c4f5eb9cdd999", "Speedway Soccer", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts116/v4/be/c4/51/bec45143-957a-c8ba-9af6-120578fd34f8/mza_14722049121013741560.jpg/600x600bb.jpg");
        PodcastPreview p3 = new PodcastPreview("ab3320eef1052aad807747ec", "Talking Disney Podcast", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts114/v4/3b/30/9c/3b309c73-aec5-ac96-60b9-34eba0218218/mza_7561584782270172307.jpg/60x60bb.jpg");
        List<String> authors = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            test.add(p1);
            test.add(p2);
            test.add(p3);
            authors.add("Michael Colosi");
            authors.add("Preface Podcast");
            authors.add("Apple Inc.");
        }

        //fill textefileds and image
        userPageUsernameTextField.setText(user.getUsername());
        userPageCountryTextField.setText(user.getCountry());
        userPageFavGenreTextField.setText(user.getFavouriteGenre());
        userPageGenderTextField.setText(user.getGender());
        Image image = new Image(user.getPictureMedium());
        userPageImage.setImage(image);

        // fill the watchlist and liked grids
        int row = 0;
        int column = 0;
        for (PodcastPreview podcast : test) {
            FXMLLoader watchListfxmlLoader = new FXMLLoader();
            FXMLLoader likedfxmlLoader = new FXMLLoader();
            watchListfxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));
            likedfxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreview.fxml"));

            // create new podcast element
            AnchorPane newPodcast1 = watchListfxmlLoader.load();
            AnchorPane newPodcast2 = likedfxmlLoader.load();
            PodcastPreviewController watchListController = watchListfxmlLoader.getController();
            PodcastPreviewController likedController = likedfxmlLoader.getController();
            watchListController.setData(podcast);
            likedController.setData(podcast);

            // add new podcast to grid
            this.userPageWatchlistGrid.add(newPodcast1, column, row);
            this.userPageLikedGrid.add(newPodcast2, column, row);
            column++;
        }

        // fill the watchlist and liked grids
        int row2 = 0;
        int column2 = 0;
        for (String author : authors) {

            Label authorLabel = new Label();
            authorLabel.setText(author);
            authorLabel.setPrefWidth(230);
            authorLabel.setPrefHeight(20);
            authorLabel.setStyle("-fx-cursor: hand");
            authorLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getAuthorClick(author);
                }
            });
            authorLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    authorLabel.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
                }
            });
            authorLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    authorLabel.setStyle("-fx-font-weight: regular; -fx-cursor: hand;");
                }
            });

            // add new podcast to grid
            this.userPageAuthorGrid.add(authorLabel, column2, row2);
            row2++;
        }
    }

    //click event
    void getAuthorClick(String author){
        Logger.info("Author " + author + " pressed");
    }

    @FXML
    void followButtonClick(MouseEvent event) {
        if(userPageFollowButton.getText().equals("FOLLOW"))
            userPageFollowButton.setText("UNFOLLOW");
        else
            userPageFollowButton.setText("FOLLOW");
    }


    @FXML
    void scrollWatchlistButtonClick(MouseEvent event) {
        Logger.info("watchlist button pressed");
        double scrollValue = 0.16;
        if(userPageWatchlistScrollPane.getHvalue() == 1.0)
            scrollValue = -1;
        userPageWatchlistScrollPane.setHvalue(userPageWatchlistScrollPane.getHvalue() + scrollValue);
        //Logger.info(((Double)(userPageWatchlistScrollPane.getHvalue())).toString());
    }

    @FXML
    void scrollLikedButtonClick(MouseEvent event) {
        Logger.info("liked button pressed");
        double scrollValue = 0.16;
        if(userPageLikedScrollPane.getHvalue() == 1.0)
            scrollValue = -1;
        userPageLikedScrollPane.setHvalue(userPageLikedScrollPane.getHvalue() + scrollValue);
    }

    @FXML
    void homeButtonClick(MouseEvent event) throws IOException {
        Logger.info("home button pressed");
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void searchButtonClick(MouseEvent event) {
        Logger.info("Search: " + searchTextField.getText());
    }


    //hover
    @FXML
    void homeIn(MouseEvent event) {
        homeButton.setFitHeight(31);
        homeButton.setFitWidth(31);
    }

    @FXML
    void homeOut(MouseEvent event) {
        homeButton.setFitHeight(30);
        homeButton.setFitWidth(30);
    }

    @FXML
    void scrollLikedIn(MouseEvent event) {
        userPageLikedButton.setFitHeight(31);
        userPageLikedButton.setFitWidth(31);
    }

    @FXML
    void scrollLikedOut(MouseEvent event) {
        userPageLikedButton.setFitHeight(30);
        userPageLikedButton.setFitWidth(30);
    }

    @FXML
    void scrollWatchlistIn(MouseEvent event) {
        userPageWatchlistButton.setFitHeight(31);
        userPageWatchlistButton.setFitWidth(31);
    }

    @FXML
    void scrollWatchlistOut(MouseEvent event) {
        userPageWatchlistButton.setFitHeight(30);
        userPageWatchlistButton.setFitWidth(30);
    }

    @FXML
    void followIn(MouseEvent event) {
        userPageFollowButton.setStyle("-fx-font-weight: bold;");
    }

    @FXML
    void followOut(MouseEvent event) {
        userPageFollowButton.setStyle("-fx-font-weight: regular;");
    }


    @FXML
    void searchIn(MouseEvent event) {
        searchButton.setFitHeight(21);
        searchButton.setFitWidth(21);
    }

    @FXML
    void searchOut(MouseEvent event) {
        searchButton.setFitHeight(20);
        searchButton.setFitWidth(20);
    }
}
