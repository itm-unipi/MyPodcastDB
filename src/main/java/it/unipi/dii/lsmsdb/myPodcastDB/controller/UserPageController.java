package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.*;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class UserPageController {

    private Hashtable<String, String> cacheData;
    @FXML
    private ImageView homeButton;

    @FXML
    private ImageView searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField userPageAgeTextField;

    @FXML
    private GridPane userPageAuthorsGrid;

    @FXML
    private ImageView userPageAuthorsLeftButton;

    @FXML
    private ImageView userPageAuthorsRightButton;

    @FXML
    private ScrollPane userPageAuthorsScrollPane;

    @FXML
    private ImageView userPageConfirmButton;

    @FXML
    private TextField userPageCountryTextField;

    @FXML
    private ImageView userPageCrossButton;

    @FXML
    private TextField userPageEmailTextField;

    @FXML
    private TextField userPageFavGenreTextField;

    @FXML
    private ImageView userPageFollowButton;

    @FXML
    private TextField userPageGenderTextField;

    @FXML
    private ImageView userPageImage;

    @FXML
    private GridPane userPageLikedGrid;

    @FXML
    private ImageView userPageLikedLeftButton;

    @FXML
    private ImageView userPageLikedRightButton;

    @FXML
    private ScrollPane userPageLikedScrollPane;

    @FXML
    private TextField userPageNameTextField;

    @FXML
    private TextField userPageSurnameTextField;

    @FXML
    private TextField userPageUsernameTextField;

    @FXML
    private GridPane userPageUsersGrid;

    @FXML
    private ImageView userPageUsersLeftButton;

    @FXML
    private ImageView userPageUsersRightButton;

    @FXML
    private ScrollPane userPageUsersScrollPane;

    @FXML
    private GridPane userPageWatchlistGrid;

    @FXML
    private ImageView userPageWatchlistLeftButton;

    @FXML
    private ImageView userPageWatchlistRightButton;

    @FXML
    private ScrollPane userPageWatchlistScrollPane;

    @FXML
    private Pane userPagePrivateArea;

    @FXML
    private ImageView userPageSettingsButton;


    /**************/

    //click event

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
    void scrollUsersButtonRightClick(MouseEvent event) {
        Logger.info("users right button pressed");
        double scrollValue = 0.13;
        if(userPageUsersScrollPane.getHvalue() == 1.0)
            return;
        userPageUsersScrollPane.setHvalue(userPageUsersScrollPane.getHvalue() + scrollValue);
    }

    @FXML
    void scrollUsersButtonLeftClick(MouseEvent event) {
        Logger.info("users left button pressed");
        double scrollValue = 0.13;
        if(userPageUsersScrollPane.getHvalue() == 0.0)
            return;
        userPageUsersScrollPane.setHvalue(userPageUsersScrollPane.getHvalue() - scrollValue);
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

    @FXML
    void followButtonClick(MouseEvent event) {
        Logger.info("follow button pressed");
        if(!userPagePrivateArea.visibleProperty().getValue()) {
            userPageFollowButton.setImage(new Image("File:src/main/resources/images/heart_24px.png"));
            userPagePrivateArea.setVisible(true);
            userPageSettingsButton.setVisible(true);
            userPageConfirmButton.setVisible(false);
            userPageCrossButton.setVisible(false);
        }
        else {
            userPageFollowButton.setImage(new Image("File:src/main/resources/images/following_30px.png"));
            userPagePrivateArea.setVisible(false);
            userPageSettingsButton.setVisible(false);
            userPageConfirmButton.setVisible(false);
            userPageCrossButton.setVisible(false);
        }
    }

    @FXML
    void settingsButtonClick(MouseEvent event) {
        Logger.info("settings button clicked");
        enableTextFields(true);
        cacheData = getDataFromTextFields();
        userPageSettingsButton.setVisible(false);
        userPageConfirmButton.setVisible(true);
        userPageCrossButton.setVisible(true);
        userPageUsernameTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageNameTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageSurnameTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageCountryTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageEmailTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageAgeTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageGenderTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageFavGenreTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
    }

    @FXML
    void crossButtonClick(MouseEvent event) {
        Logger.info("cross button clicked");
        enableTextFields(false);
        restoreTextFields(cacheData);
        userPageSettingsButton.setVisible(true);
        userPageConfirmButton.setVisible(false);
        userPageCrossButton.setVisible(false);
        userPageUsernameTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageNameTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageSurnameTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageCountryTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageEmailTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageAgeTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageGenderTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageFavGenreTextField.setStyle("-fx-background-color: white; -fx-border-color: white");

    }

    @FXML
    void confirmButtonClick(MouseEvent event) {
        Logger.info("confirm button clicked");
        enableTextFields(false);
        cacheData = getDataFromTextFields();
        Logger.info(cacheData.toString());
        userPageSettingsButton.setVisible(true);
        userPageConfirmButton.setVisible(false);
        userPageCrossButton.setVisible(false);
        userPageUsernameTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageNameTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageSurnameTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageCountryTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageEmailTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageAgeTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageGenderTextField.setStyle("-fx-background-color: white; -fx-border-color: white");
        userPageFavGenreTextField.setStyle("-fx-background-color: white; -fx-border-color: white");

    }


    /***********************/

    public void initialize() throws IOException {

        User user = new User();
        List<PodcastPreview> podcasts = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        List<User> users = new ArrayList<>();

        this.simulateServiceLayer(user, podcasts, authors, users);

        //fill textfields and image
        userPageUsernameTextField.setText(user.getUsername());
        userPageCountryTextField.setText(user.getCountry());
        userPageFavGenreTextField.setText(user.getFavouriteGenre());
        userPageGenderTextField.setText(user.getGender());
        Image image = new Image(user.getPictureMedium());
        userPageImage.setImage(image);
        userPageNameTextField.setText(user.getName());
        userPageSurnameTextField.setText(user.getSurname());
        userPageAgeTextField.setText(((Integer)user.getAge()).toString());
        userPageEmailTextField.setText(user.getEmail());

        // fill the watchlist and liked grids
        int row = 0;
        int column = 0;
        for (PodcastPreview podcast : podcasts) {
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

        //fill the authors grid
        row = 0;
        column = 0;
        for(Author author : authors){
            FXMLLoader authorfxmlLoader = new FXMLLoader();
            authorfxmlLoader.setLocation(getClass().getClassLoader().getResource("ActorPreview.fxml"));
            AnchorPane newAuthor = authorfxmlLoader.load();
            ActorPreviewController authorController = authorfxmlLoader.getController();
            authorController.setData(author);
            this.userPageAuthorsGrid.add(newAuthor, column, row);
            column++;
        }

        //fill the users grid
        row = 0;
        column = 0;
        for(User u : users){
            FXMLLoader userfxmlLoader = new FXMLLoader();
            userfxmlLoader.setLocation(getClass().getClassLoader().getResource("ActorPreview.fxml"));
            AnchorPane newUser = userfxmlLoader.load();
            ActorPreviewController actorController = userfxmlLoader.getController();
            actorController.setData(u);
            this.userPageUsersGrid.add(newUser, column, row);
            column++;
        }

        userPageWatchlistScrollPane.setHvalue(0.0);
        userPageLikedScrollPane.setHvalue(0.0);
        userPageAuthorsScrollPane.setHvalue(0.0);
        userPageUsersScrollPane.setHvalue(0.0);
        userPagePrivateArea.setVisible(false);
        userPageSettingsButton.setVisible(false);
        userPageConfirmButton.setVisible(false);
        userPageCrossButton.setVisible(false);
    }

    void simulateServiceLayer(User user, List<PodcastPreview> podcasts, List<Author> authors, List<User> users){

        // service simulation
        user.setUsername("whitegoose14611");
        user.setAge(65);
        user.setGender("male");
        user.setPictureMedium("File:src/main/resources/images/user_100px.png");
        user.setFavouriteGenre("Music History");
        user.setCountry("Spain");
        user.setEmail("whitegoose14611@example.com");
        user.setName("Paolo");
        user.setSurname("Giacomini");

        PodcastPreview p1 = new PodcastPreview("54eb342567c94dacfb2a3e50", "Scaling Global", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg");
        PodcastPreview p2 = new PodcastPreview("9852b276565c4f5eb9cdd999", "Speedway Soccer", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts116/v4/be/c4/51/bec45143-957a-c8ba-9af6-120578fd34f8/mza_14722049121013741560.jpg/600x600bb.jpg");
        PodcastPreview p3 = new PodcastPreview("ab3320eef1052aad807747ec", "Talking Disney Podcast", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts114/v4/3b/30/9c/3b309c73-aec5-ac96-60b9-34eba0218218/mza_7561584782270172307.jpg/60x60bb.jpg");

        Author a1 = new Author("ahy2bs89ha5c4f5eb9cddaaa", "Michael Colosi", "File:src/main/resources/images/user_male_96px.png" );
        Author a2 = new Author("ufsdfrt445efsge5srfsdffa", "Preface Podcast", "File:src/main/resources/images/User Female Skin Type 6_160px.png" );
        Author a3 = new Author("6sffgty6wefy742eerwetttt", "Apple Inc.", "File:src/main/resources/images/user_male_skin_type_3_80px.png" );

        User u1 = new User("Paolo Lupini", "File:src/main/resources/images/account_50px.png");
        User u2 = new User("Chiara Proietti", "File:src/main/resources/images/clever_woman_50px.png");
        User u3 = new User("Claudio Giuseppe", "File:src/main/resources/images/business_man_with_beard_50px.png");


        for(int i = 0; i < 5; i++){
            podcasts.add(p1);
            podcasts.add(p2);
            podcasts.add(p3);
            authors.add(a1);
            authors.add(a2);
            authors.add(a3);
            users.add(u1);
            users.add(u2);
            users.add(u3);
        }

    }

    void enableTextFields(boolean value){
        userPageUsernameTextField.setEditable(value);
        userPageNameTextField.setEditable(value);
        userPageCountryTextField.setEditable(value);
        userPageSurnameTextField.setEditable(value);
        userPageFavGenreTextField.setEditable(value);
        userPageEmailTextField.setEditable(value);
        userPageAgeTextField.setEditable(value);
        userPageGenderTextField.setEditable(value);

    }

    Hashtable<String, String> getDataFromTextFields(){
        Hashtable<String, String> list = new Hashtable<>();
        list.put("username", userPageUsernameTextField.getText());
        list.put("name", userPageNameTextField.getText());
        list.put("surname", userPageSurnameTextField.getText());
        list.put("age", userPageAgeTextField.getText());
        list.put("country", userPageCountryTextField.getText());
        list.put("email", userPageEmailTextField.getText());
        list.put("gender", userPageGenderTextField.getText());
        list.put("favGenre", userPageFavGenreTextField.getText());

        return list;
    }

    void restoreTextFields(Hashtable<String, String> list){

        userPageUsernameTextField.setText(list.get("username"));
        userPageNameTextField.setText(list.get("name"));
        userPageSurnameTextField.setText(list.get("surname"));
        userPageCountryTextField.setText(list.get("country"));
        userPageFavGenreTextField.setText(list.get("favGenre"));
        userPageAgeTextField.setText(list.get("age"));
        userPageGenderTextField.setText(list.get("gender"));
        userPageEmailTextField.setText(list.get("email"));
    }

}
