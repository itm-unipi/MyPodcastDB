package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.AuthorPreview;
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
    private ImageView userPageFollowButton;

    @FXML
    private ImageView  userPageWatchlistLeftButton;

    @FXML
    private ImageView  userPageWatchlistRightButton;

    @FXML
    private ImageView  userPageLikedRightButton;

    @FXML
    private ImageView  userPageLikedLeftButton;

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


    @FXML ScrollPane userPageAuthorsScrollPane;

    @FXML
    private GridPane userPageAuthorsGrid;

    @FXML
    private ImageView userPageAuthorsLeftButton;

    @FXML
    private ImageView userPageAuthorsRightButton;

    public void initialize() throws IOException {

        // service simulation
        User user = new User();
        user.setUsername("whitegoose14611");
        user.setAge(65);
        user.setGender("male");
        user.setPictureMedium("File:src/main/resources/images/user_100px.png");
        user.setFavouriteGenre("Music History");
        user.setCountry("Spain");
        List<PodcastPreview> test = new ArrayList<>();
        PodcastPreview p1 = new PodcastPreview("54eb342567c94dacfb2a3e50", "Scaling Global", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg");
        PodcastPreview p2 = new PodcastPreview("9852b276565c4f5eb9cdd999", "Speedway Soccer", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts116/v4/be/c4/51/bec45143-957a-c8ba-9af6-120578fd34f8/mza_14722049121013741560.jpg/600x600bb.jpg");
        PodcastPreview p3 = new PodcastPreview("ab3320eef1052aad807747ec", "Talking Disney Podcast", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts114/v4/3b/30/9c/3b309c73-aec5-ac96-60b9-34eba0218218/mza_7561584782270172307.jpg/60x60bb.jpg");
        List<AuthorPreview> authors = new ArrayList<>();
        AuthorPreview a1 = new AuthorPreview("ahy2bs89ha5c4f5eb9cddaaa", "Michael Colosi", "File:src/main/resources/images/user_male_96px.png" );
        AuthorPreview a2 = new AuthorPreview("ufsdfrt445efsge5srfsdffa", "Preface Podcast", "File:src/main/resources/images/User Female Skin Type 6_160px.png" );
        AuthorPreview a3 = new AuthorPreview("6sffgty6wefy742eerwetttt", "Apple Inc.", "File:src/main/resources/images/user_male_skin_type_3_80px.png" );



        for(int i = 0; i < 5; i++){
            test.add(p1);
            test.add(p2);
            test.add(p3);
            authors.add(a1);
            authors.add(a2);
            authors.add(a3);
        }

        //fill textfields and image
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

        row = 0;
        column = 0;

        for(AuthorPreview author : authors){
            FXMLLoader authorfxmlLoader = new FXMLLoader();
            authorfxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorPreview.fxml"));
            AnchorPane newAuthor = authorfxmlLoader.load();
            AuthorPreviewController authorController = authorfxmlLoader.getController();
            authorController.setData(author);
            this.userPageAuthorsGrid.add(newAuthor, column, row);
            column++;
        }

        userPageWatchlistScrollPane.setHvalue(0.0);
        userPageLikedScrollPane.setHvalue(0.0);
        userPageAuthorsScrollPane.setHvalue(0.0);
    }

    //click event

    @FXML
    void followButtonClick(MouseEvent event) {
        Logger.info("follow button pressed");
    }


    @FXML
    void scrollWatchlistButtonRightClick(MouseEvent event) {
        Logger.info("watchlist right button pressed");
        double scrollValue = 0.20;
        if(userPageWatchlistScrollPane.getHvalue() == 1.0)
            return;
        userPageWatchlistScrollPane.setHvalue(userPageWatchlistScrollPane.getHvalue() + scrollValue);
        //Logger.info(((Double)(userPageWatchlistScrollPane.getHvalue())).toString());
    }

    @FXML
    void scrollWatchlistButtonLeftClick(MouseEvent event) {
        Logger.info("watchlist left button pressed");
        double scrollValue = 0.20;
        if(userPageWatchlistScrollPane.getHvalue() == 0.0)
            return;
        userPageWatchlistScrollPane.setHvalue(userPageWatchlistScrollPane.getHvalue() - scrollValue);
        //Logger.info(((Double)(userPageWatchlistScrollPane.getHvalue())).toString());
    }

    @FXML
    void scrollLikedButtonRightClick(MouseEvent event) {
        Logger.info("liked right button pressed");
        double scrollValue = 0.20;
        if(userPageLikedScrollPane.getHvalue() == 1.0)
            return;
        userPageLikedScrollPane.setHvalue(userPageLikedScrollPane.getHvalue() + scrollValue);
    }

    @FXML
    void scrollLikedButtonLeftClick(MouseEvent event) {
        Logger.info("liked left button pressed");
        double scrollValue = 0.20;
        if(userPageLikedScrollPane.getHvalue() == 0.0)
            return;
        userPageLikedScrollPane.setHvalue(userPageLikedScrollPane.getHvalue() - scrollValue);
    }

    @FXML
    void scrollAuthorsButtonRightClick(MouseEvent event) {
        Logger.info("authors right button pressed");
        double scrollValue = 0.13;
        if(userPageAuthorsScrollPane.getHvalue() == 1.0)
            return;
        userPageAuthorsScrollPane.setHvalue(userPageAuthorsScrollPane.getHvalue() + scrollValue);
    }

    @FXML
    void scrollAuthorsButtonLeftClick(MouseEvent event) {
        Logger.info("authors left button pressed");
        double scrollValue = 0.13;
        if(userPageAuthorsScrollPane.getHvalue() == 0.0)
            return;
        userPageAuthorsScrollPane.setHvalue(userPageAuthorsScrollPane.getHvalue() - scrollValue);
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
    void scrollLikedRightIn(MouseEvent event) {
        userPageLikedRightButton.setFitHeight(31);
        userPageLikedRightButton.setFitWidth(31);
    }

    @FXML
    void scrollLikedRightOut(MouseEvent event) {
        userPageLikedRightButton.setFitHeight(30);
        userPageLikedRightButton.setFitWidth(30);
    }

    @FXML
    void scrollLikedLeftIn(MouseEvent event) {
        userPageLikedLeftButton.setFitHeight(31);
        userPageLikedLeftButton.setFitWidth(31);
    }

    @FXML
    void scrollLikedLeftOut(MouseEvent event) {
        userPageLikedLeftButton.setFitHeight(30);
        userPageLikedLeftButton.setFitWidth(30);
    }

    @FXML
    void scrollWatchlistRightIn(MouseEvent event) {
        userPageWatchlistRightButton.setFitHeight(31);
        userPageWatchlistRightButton.setFitWidth(31);
    }

    @FXML
    void scrollWatchlistRightOut(MouseEvent event) {
        userPageWatchlistRightButton.setFitHeight(30);
        userPageWatchlistRightButton.setFitWidth(30);
    }

    @FXML
    void scrollWatchlistLeftIn(MouseEvent event) {
        userPageWatchlistLeftButton.setFitHeight(31);
        userPageWatchlistLeftButton.setFitWidth(31);
    }

    @FXML
    void scrollWatchlistLeftOut(MouseEvent event) {
        userPageWatchlistLeftButton.setFitHeight(30);
        userPageWatchlistLeftButton.setFitWidth(30);
    }

    @FXML
    void scrollAuthorsLeftIn(MouseEvent event) {
        userPageAuthorsLeftButton.setFitHeight(31);
        userPageAuthorsLeftButton.setFitWidth(31);
    }

    @FXML
    void scrollAuthorsLeftOut(MouseEvent event) {
        userPageAuthorsLeftButton.setFitHeight(30);
        userPageAuthorsLeftButton.setFitWidth(30);
    }

    @FXML
    void scrollAuthorsRightIn(MouseEvent event) {
        userPageAuthorsRightButton.setFitHeight(31);
        userPageAuthorsRightButton.setFitWidth(31);
    }

    @FXML
    void scrollAuthorsRightOut(MouseEvent event) {
        userPageAuthorsRightButton.setFitHeight(30);
        userPageAuthorsRightButton.setFitWidth(30);
    }

    @FXML
    void followIn(MouseEvent event) {
        userPageFollowButton.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
    }

    @FXML
    void followOut(MouseEvent event) {
        userPageFollowButton.setStyle("-fx-font-weight: normal; -fx-cursor: hand;");
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
