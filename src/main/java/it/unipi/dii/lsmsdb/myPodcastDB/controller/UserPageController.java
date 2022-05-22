package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.PodcastPreview;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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
    private TextField userPageGenderTextField;

    @FXML
    private GridPane userPageLikedGrid;

    @FXML
    private TextField userPageUsernameTextField;

    @FXML
    private GridPane userPageWatchlistGrid;

    @FXML
    private ImageView userPageImage;

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
        for(int i = 0; i < 5; i++){
            test.add(p1);
            test.add(p2);
        }

        //fill textefileds and image
        userPageUsernameTextField.setText(user.getUsername());
        userPageCountryTextField.setText(user.getCountry());
        userPageFavGenreTextField.setText(user.getFavouriteGenre());
        userPageGenderTextField.setText(user.getGender());
        Image image = new Image(user.getPictureMedium());
        userPageImage.setImage(image);

        // fill the grid
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
    }

    @FXML
    void followButtonClick(MouseEvent event) {
        if(userPageFollowButton.getText().equals("FOLLOW"))
            userPageFollowButton.setText("UNFOLLOW");
        else
            userPageFollowButton.setText("FOLLOW");
    }
}
