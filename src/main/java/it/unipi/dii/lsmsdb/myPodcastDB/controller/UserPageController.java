package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.*;
import it.unipi.dii.lsmsdb.myPodcastDB.service.UserPageService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.JsonDecode;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import org.javatuples.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.LongFunction;

public class UserPageController {

    @FXML
    private ImageView homeButton;

    @FXML
    private ImageView actorPageButton;

    @FXML
    private ImageView logoutPageButton;

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
    private ComboBox userPageCountryComboBox;

    @FXML
    private ImageView userPageCrossButton;

    @FXML
    private TextField userPageEmailTextField;

    @FXML
    private ComboBox userPageFavGenreComboBox;

    @FXML
    private ImageView userPageFollowButton;

    @FXML
    private ComboBox userPageGenderComboBox;

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

    @FXML
    private Button userPageDeleteButton;

    @FXML
    private Label userPageWatchlistLabel;

    @FXML
    private Label userPageLikedLabel;

    @FXML
    private Label userPageAuthorsLabel;

    @FXML
    private Label userPageUsersLabel;

    @FXML
    private HBox userPageWatchlistAreaHBox;

    @FXML
    private HBox userPageLikedAreaHBox;

    @FXML
    private HBox userPageAuthorsAreaHBox;

    @FXML
    private HBox userPageUsersAreaHBox;

    @FXML
    private AnchorPane userPageAnchorPane;

    @FXML
    private ImageView imageButtonRight;

    @FXML
    private ImageView imageButtonLeft;

    @FXML
    private Label userPageCountryLabel;

    @FXML
    private Label userPageFavGenreLabel;

    @FXML
    private Label userPageGenderLabel;

    private User pageOwner;

    private boolean isFollowed;

    private int imageNumber;

    private String imagePath;

    private int maxUserImages = 30;

    private List<Podcast> wPodcasts;
    private List<Podcast> lPodcasts;
    private List<Author> authors;
    private List<User> users;
    private List<String> wPodcastsByVisitor;
    private List<String> lPodcastsByVisitor;
    private List<String> authorsByVisitor;
    private List<String> usersByVisitor;

    private int podcastRowSize = 5;
    private int actorRowSize = 8;
    private int numberOfWpodcastsToAdd = 5; // it has to be <= podcastRowSize
    private int numberOfLpodcastsToAdd = 5; // it has to be <= podcastRowSize
    private int numberOfAuthorsToAdd = 8;   // it has to be <= actorRowSize
    private int numberOfUsersToAdd = 8;     // it has to be <= actorRowSize
    private int newRequestPodcast = 7;      //it has to be > podcastRowSize
    private int newRequestActor = 10;       //it has to be > actorRowSize
    private boolean getWbutton = false;
    private boolean getLbutton = false;
    private boolean getAbutton = false;
    private boolean getUbutton = false;
    UserPageService service = new UserPageService();



    /**************/

    //click event

    @FXML
    void scrollWatchlistButtonRightClick(MouseEvent event) throws IOException {
        Logger.info("Watchlist right button pressed");
        if(getWbutton){
            for(int i = 0; i < numberOfWpodcastsToAdd; i++) {
                int column = userPageWatchlistGrid.getColumnCount();
                if (column < wPodcasts.size()){
                    loadWatchlaterPodcast(false);
                    Logger.info("Loaded new podcast to watchlist from memory");
                }
                else{
                    getWpodcasts();
                    i--;
                }
            }
            userPageWatchlistRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getWbutton = false;
        }
        else if(numberOfWpodcastsToAdd != 0 && userPageWatchlistScrollPane.getHvalue() == 1.0){
            userPageWatchlistRightButton.setImage(ImageCache.getImageFromLocalPath("/img/add.png"));
            getWbutton = true;
        }
        else if(numberOfWpodcastsToAdd == 0 && userPageWatchlistScrollPane.getHvalue() == 1.0)
            userPageWatchlistRightButton.setVisible(false);


        double scrollValue = 1.0 / (userPageWatchlistGrid.getColumnCount() - podcastRowSize);
        userPageWatchlistLeftButton.setVisible(true);
        if(userPageWatchlistScrollPane.getHvalue() == 1.0)
            return;

        userPageWatchlistScrollPane.setHvalue(userPageWatchlistScrollPane.getHvalue() + scrollValue);
        //Logger.info(((Double)(userPageWatchlistScrollPane.getHvalue())).toString());
    }

    @FXML
    void scrollWatchlistButtonLeftClick(MouseEvent event) {
        Logger.info("Watchlist left button pressed");

        userPageWatchlistRightButton.setVisible(true);
        double scrollValue = 1.0 / (userPageWatchlistGrid.getColumnCount() - podcastRowSize);
        if(userPageWatchlistScrollPane.getHvalue() == 0.0) {
            userPageWatchlistLeftButton.setVisible(false);
            return;
        }
        if(getWbutton){
            userPageWatchlistRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getWbutton = false;
        }
        userPageWatchlistScrollPane.setHvalue(userPageWatchlistScrollPane.getHvalue() - scrollValue);
        //Logger.info(((Double)(userPageWatchlistScrollPane.getHvalue())).toString());
    }

    @FXML
    private void onWatchlistScroll(ScrollEvent event) throws IOException {

        if(wPodcasts.size() <= podcastRowSize)
            return;
        if(numberOfWpodcastsToAdd == 0 && userPageWatchlistScrollPane.getHvalue() == 1.0)
            userPageWatchlistRightButton.setVisible(false);
        else
            userPageWatchlistRightButton.setVisible(true);
        if(userPageWatchlistScrollPane.getHvalue() == 0.0)
            userPageWatchlistLeftButton.setVisible(false);
        else
            userPageWatchlistLeftButton.setVisible(true);

        if(numberOfWpodcastsToAdd == 0)
           return;

       if(!getWbutton && userPageWatchlistScrollPane.getHvalue() == 1.0) {
           userPageWatchlistRightButton.setImage(ImageCache.getImageFromLocalPath("/img/add.png"));
           getWbutton = true;
       }
       else if(getWbutton && userPageWatchlistScrollPane.getHvalue() < 1.0) {
            userPageWatchlistRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getWbutton = false;
        }

    }


    @FXML
    void scrollLikedButtonRightClick(MouseEvent event) throws IOException {
        Logger.info("Liked right button pressed");


        if(getLbutton){
            for(int i = 0; i < numberOfLpodcastsToAdd; i++) {
                int column = userPageLikedGrid.getColumnCount();
                if(column < lPodcasts.size()) {
                    loadLikedPodcast(false);
                    Logger.info("Loaded new podcast to watchlist from memory");
                }
                else{
                    getLpodcasts();
                    i--;
                }
            }
            userPageLikedRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getLbutton = false;
        }
        else if(numberOfLpodcastsToAdd != 0 && userPageLikedScrollPane.getHvalue() == 1.0){
            userPageLikedRightButton.setImage(ImageCache.getImageFromLocalPath("/img/add.png"));
            getLbutton = true;
        }
        else if(numberOfLpodcastsToAdd == 0 && userPageLikedScrollPane.getHvalue() == 1.0)
            userPageLikedRightButton.setVisible(false);

        double scrollValue = 1.0 / (userPageLikedGrid.getColumnCount() - podcastRowSize);
        userPageLikedLeftButton.setVisible(true);
        if(userPageLikedScrollPane.getHvalue() == 1.0)
            return;

        userPageLikedScrollPane.setHvalue(userPageLikedScrollPane.getHvalue() + scrollValue);
    }

    @FXML
    void scrollLikedButtonLeftClick(MouseEvent event) {
        Logger.info("Liked left button pressed");

        double scrollValue = 1.0 / (userPageLikedGrid.getColumnCount() - podcastRowSize);
        userPageLikedRightButton.setVisible(true);
        if(userPageLikedScrollPane.getHvalue() == 0.0){
            userPageLikedLeftButton.setVisible(false);
            return;
        }
        if(getLbutton){
            userPageLikedRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getLbutton = false;
        }
        userPageLikedScrollPane.setHvalue(userPageLikedScrollPane.getHvalue() - scrollValue);
    }

    @FXML
    private void onLikedScroll(ScrollEvent event) throws IOException {

        if(lPodcasts.size() <= podcastRowSize)
            return;

        if(numberOfLpodcastsToAdd == 0 && userPageLikedScrollPane.getHvalue() == 1.0)
            userPageLikedRightButton.setVisible(false);
        else
            userPageLikedRightButton.setVisible(true);
        if(userPageLikedScrollPane.getHvalue() == 0.0)
            userPageLikedLeftButton.setVisible(false);
        else
            userPageLikedLeftButton.setVisible(true);

        if(numberOfLpodcastsToAdd == 0)
            return;
        if(!getLbutton && userPageLikedScrollPane.getHvalue() == 1.0) {
            userPageLikedRightButton.setImage(ImageCache.getImageFromLocalPath("/img/add.png"));
            getLbutton = true;
        }
        else if(getLbutton && userPageLikedScrollPane.getHvalue() < 1.0) {
            userPageLikedRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getLbutton = false;
        }

    }
    @FXML
    void scrollAuthorsButtonRightClick(MouseEvent event) throws IOException {
        Logger.info("Authors right button pressed");
        if (getAbutton) {
            for (int i = 0; i < numberOfAuthorsToAdd; i++) {
                int column = userPageAuthorsGrid.getColumnCount();
                if (column < authors.size()) {
                    loadAuthor(false);
                    Logger.info("Loaded new podcast to watchlist from memory");
                }
                else if (getAbutton) {
                    getAuthors();
                    i--;
                }
            }
            userPageAuthorsRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getAbutton = false;
        } else if (numberOfAuthorsToAdd != 0 && userPageAuthorsScrollPane.getHvalue() == 1.0){
            userPageAuthorsRightButton.setImage(ImageCache.getImageFromLocalPath("/img/add.png"));
            getAbutton = true;
        }
        else if(numberOfAuthorsToAdd == 0 && userPageAuthorsScrollPane.getHvalue() == 1.0)
            userPageAuthorsRightButton.setVisible(false);

        double scrollValue = 1.0 / (userPageAuthorsGrid.getColumnCount() - actorRowSize);
        userPageAuthorsLeftButton.setVisible(true);
        if(userPageAuthorsScrollPane.getHvalue() == 1.0)
            return;

        userPageAuthorsScrollPane.setHvalue(userPageAuthorsScrollPane.getHvalue() + scrollValue);
    }

    @FXML
    void scrollAuthorsButtonLeftClick(MouseEvent event) {
        Logger.info("Authors left button pressed");

        double scrollValue = 1.0 / (userPageAuthorsGrid.getColumnCount() - actorRowSize);
        userPageAuthorsRightButton.setVisible(true);
        if(userPageAuthorsScrollPane.getHvalue() == 0.0) {
            userPageAuthorsLeftButton.setVisible(false);
            return;
        }
        if(getAbutton){
            userPageAuthorsRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getAbutton = false;
        }

        userPageAuthorsScrollPane.setHvalue(userPageAuthorsScrollPane.getHvalue() - scrollValue);
    }

    @FXML
    private void onAuthorsScroll(ScrollEvent event) throws IOException {

        if(authors.size() <= actorRowSize)
            return;

        if(numberOfAuthorsToAdd == 0 && userPageAuthorsScrollPane.getHvalue() == 1.0)
            userPageAuthorsRightButton.setVisible(false);
        else
            userPageAuthorsRightButton.setVisible(true);
        if(userPageAuthorsScrollPane.getHvalue() == 0.0)
            userPageAuthorsLeftButton.setVisible(false);
        else
            userPageAuthorsLeftButton.setVisible(true);

        if(numberOfAuthorsToAdd == 0)
            return;
        if(!getAbutton && userPageAuthorsScrollPane.getHvalue() == 1.0) {
            userPageAuthorsRightButton.setImage(ImageCache.getImageFromLocalPath("/img/add.png"));
            getAbutton = true;
        }
        else if(getAbutton && userPageAuthorsScrollPane.getHvalue() < 1.0) {
            userPageAuthorsRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getAbutton = false;
        }

    }
    @FXML
    void scrollUsersButtonRightClick(MouseEvent event) throws IOException {
        Logger.info("Users right button pressed");
        if(getUbutton){
            for(int i = 0; i < numberOfUsersToAdd; i++) {
                int column = userPageUsersGrid.getColumnCount();
                if(column < users.size()) {
                    loadUser(false);
                    Logger.info("Loaded new podcast to watchlist from memory");
                }
                else if(getUbutton) {
                    getUsers();
                    i--;
                }
            }
            userPageUsersRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getUbutton = false;
        }
        else if(numberOfUsersToAdd != 0 && userPageUsersScrollPane.getHvalue() == 1.0){
            userPageUsersRightButton.setImage(ImageCache.getImageFromLocalPath("/img/add.png"));
            getUbutton = true;
        }
        else if(numberOfUsersToAdd == 0 && userPageUsersScrollPane.getHvalue() == 1.0)
            userPageUsersRightButton.setVisible(false);

        double scrollValue = 1.0 / (userPageUsersGrid.getColumnCount() - actorRowSize);
        userPageUsersLeftButton.setVisible(true);
        if(userPageUsersScrollPane.getHvalue() == 1.0)
            return;
        userPageUsersScrollPane.setHvalue(userPageUsersScrollPane.getHvalue() + scrollValue);
    }

    @FXML
    void scrollUsersButtonLeftClick(MouseEvent event) {
        Logger.info("Users left button pressed");

        double scrollValue = 1.0 / (userPageUsersGrid.getColumnCount() - actorRowSize);
        userPageUsersRightButton.setVisible(true);
        if(userPageUsersScrollPane.getHvalue() == 0.0) {
            userPageUsersLeftButton.setVisible(false);
            return;
        }
        if(getUbutton){
            userPageUsersRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getUbutton = false;
        }

        userPageUsersScrollPane.setHvalue(userPageUsersScrollPane.getHvalue() - scrollValue);
    }

    @FXML
    private void onUsersScroll(ScrollEvent event) throws IOException {

        if(users.size() <= actorRowSize)
            return;

        if(numberOfUsersToAdd == 0 && userPageUsersScrollPane.getHvalue() == 1.0)
            userPageUsersRightButton.setVisible(false);
        else
            userPageUsersRightButton.setVisible(true);
        if(userPageUsersScrollPane.getHvalue() == 0.0)
            userPageUsersLeftButton.setVisible(false);
        else
            userPageUsersLeftButton.setVisible(true);

        if(numberOfUsersToAdd == 0)
            return;
        if(!getUbutton && userPageUsersScrollPane.getHvalue() == 1.0) {
            userPageUsersRightButton.setImage(ImageCache.getImageFromLocalPath("/img/add.png"));
            getUbutton = true;
        }
        else if(getUbutton && userPageUsersScrollPane.getHvalue() < 1.0) {
            userPageUsersRightButton.setImage(ImageCache.getImageFromLocalPath("/img/forward_32px.png"));
            getUbutton = false;
        }

    }
    @FXML
    void homeButtonClick(MouseEvent event) throws IOException {
        Logger.info("Home button pressed");
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }

    @FXML
    void searchButtonClick(MouseEvent event) throws IOException {

        if(searchTextField.getText().isEmpty()){
            Logger.error("Field cannot be empty!");
            return;
        }

        Logger.info("Search: " + searchTextField.getText());
        StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchTextField.getText());
    }

    @FXML
    private void onKeyPressedInSearchBar(KeyEvent event) throws IOException {

        if(!event.getCode().equals(KeyCode.ENTER))
            return;

        if(searchTextField.getText().isEmpty()){
            Logger.error("Field cannot be empty!");
            return;
        }

        Logger.info("Search: " + searchTextField.getText());
        StageManager.showPage(ViewNavigator.SEARCH.getPage(), searchTextField.getText());

    }

    @FXML
    void followButtonClick(MouseEvent event) {
        Logger.info("Follow button pressed");
        String owner = pageOwner.getUsername();
        String visitor = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        int res = -1;
        if(isFollowed) {
            res = service.updateFollowUser(visitor, owner, false);
            if(res == 0) {
                userPageFollowButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_50px.png"));
                isFollowed = false;
            }
        }
        else{
            res = service.updateFollowUser(visitor, owner, true);
            if(res == 0) {
                userPageFollowButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_52px.png"));
                isFollowed = true;
            }
        }
        String logMsg = "";
        String dialogMsg = "";
        switch(res){
            case 0:
                Logger.success("Update follow relation success");
                break;
            case 1:
                logMsg = "Visitor account not exists on mongo";
                dialogMsg = "Your account not exists";
                break;
            case 2:
                logMsg = "Visitor account not exists on neo4j";
                dialogMsg = "Your account not exists";
                break;
            case 3:
                logMsg = "Owner not exists on mongo";
                dialogMsg = "User not exists";
                break;
            case 4:
                logMsg = "Owner not exists on neo4j";
                dialogMsg = "User not exists";
                break;
            case 5:
                logMsg = "Follow relation already exists";
                dialogMsg = "You already followed user";
                break;
            case 6:
                logMsg = "Adding following relation failed";
                dialogMsg = "Operation failed";
                break;
            case 7:
                logMsg = "Follow relation already not exists";
                dialogMsg = "Operation failed";
                break;
            case 8:
                logMsg = "Removing following relation failed";
                dialogMsg = "Operation failed";
                break;
            case -1:
                logMsg = "Unknown error";
                dialogMsg = "Unknown error";
                break;
        }

        if(res > 0 || res == -1){
            Logger.error(logMsg);
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, dialogMsg);
        }
    }

    @FXML
    void settingsButtonClick(MouseEvent event) {
        Logger.info("Settings button clicked");
        enableTextFields(true);
        userPageSettingsButton.setVisible(false);
        userPageConfirmButton.setVisible(true);
        userPageCrossButton.setVisible(true);
        imageButtonLeft.setVisible(true);
        imageButtonRight.setVisible(true);
        userPageUsernameTextField.setStyle("-fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: white;");
        userPageNameTextField.setStyle("-fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: white;");
        userPageSurnameTextField.setStyle("-fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: white;");
        userPageEmailTextField.setStyle("-fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: white;");
        userPageAgeTextField.setStyle("-fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: white;");
        imageNumber = 0;
    }

    @FXML
    void crossButtonClick(MouseEvent event) {
        Logger.info("Cross button clicked");
        enableTextFields(false);
        restoreTextFields();
        userPageSettingsButton.setVisible(true);
        userPageConfirmButton.setVisible(false);
        userPageCrossButton.setVisible(false);
        imageButtonLeft.setVisible(false);
        imageButtonRight.setVisible(false);
        userPageUsernameTextField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        userPageNameTextField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        userPageSurnameTextField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        userPageEmailTextField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        userPageAgeTextField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");

    }

    @FXML
    void confirmButtonClick(MouseEvent event) {

        Logger.info("Confirm button clicked");
        User newUser = getDataFromTextFields();

        if(newUser.getUsername().isEmpty() || newUser.getEmail().isEmpty() || (Integer)newUser.getAge() < 0){
            Logger.error("Invalid inputs typed");
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, "invalid inputs");
            return;
        }
        Logger.info(newUser.toString());
        int res = service.updateUserPageOwner(pageOwner, newUser);
        String logMsg = "";
        String dialogMsg = "";
        switch(res){
            case 0 :
                Logger.success("Updating user success");
                break;
            case 1 :
                Logger.success("No operation needed");
                break;
            case 2 :
                logMsg = "User not exists on mongo";
                dialogMsg = "Updating failed";
                break;
            case 3 :
                logMsg = "User not exists on neo4j";
                dialogMsg = "Updating failed";
                break;
            case 4 :
                logMsg = "User with the same username already exists";
                dialogMsg = "Username already in use";
                break;
            case 5 :
                logMsg = "Mongo operation failed";
                dialogMsg = "Updating failed";
                break;
            case 6 :
                logMsg = "Neo4j operation failed";
                dialogMsg = "Updating failed";
                break;
            case -1:
                logMsg = "Unknown error";
                dialogMsg = "Unknown error";
                break;

        }
        if(res > 1 || res == -1){
            Logger.error(logMsg);
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, dialogMsg);
            return;
        }

        enableTextFields(false);
        pageOwner.copy(newUser);
        MyPodcastDB.getInstance().setSession(pageOwner, "User");
        actorPageButton.setImage(ImageCache.getImageFromLocalPath(pageOwner.getPicturePath()));
        userPageSettingsButton.setVisible(true);
        userPageConfirmButton.setVisible(false);
        userPageCrossButton.setVisible(false);
        imageButtonLeft.setVisible(false);
        imageButtonRight.setVisible(false);
        userPageUsernameTextField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        userPageNameTextField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        userPageSurnameTextField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        userPageEmailTextField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        userPageAgeTextField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
        userPageCountryLabel.setText(pageOwner.getCountry());
        userPageGenderLabel.setText(pageOwner.getGender());
        userPageFavGenreLabel.setText(pageOwner.getFavouriteGenre());

    }

    @FXML
    void actorPageButtonClick(MouseEvent event) throws IOException {
        Logger.info("Actor page button clicked");
        String actorType = MyPodcastDB.getInstance().getSessionType();
        if(actorType.equals("User"))
            StageManager.showPage(ViewNavigator.USERPAGE.getPage(), ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername());
        else if(actorType.equals("Author"))
            StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), ((Author)MyPodcastDB.getInstance().getSessionActor()).getId());
        else if (actorType.equals("Admin"))
            StageManager.showPage(ViewNavigator.ADMINDASHBOARD.getPage());
        else
            Logger.error("Unidentified Actor Type");
    }

    @FXML
    void logoutButtonClick(MouseEvent event) throws IOException{
        Logger.info("Logout button clicked");
        MyPodcastDB.getInstance().setSession(null, null);
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void deleteButtonClick(MouseEvent event) throws  IOException{
        Logger.info("Delete button clicked");

        if(DialogManager.getInstance().createConfirmationAlert(userPageAnchorPane, "Really Delete your account?")) {
            int res = service.deleteUserPageOwner(pageOwner);
            String logMsg = "";
            String dialogMsg = "";
            switch(res){
                case 0:
                    Logger.success("Delete account success");
                    break;
                case 1:
                    logMsg = "User not exists on mongo";
                    dialogMsg = "Your account not exists";
                    break;
                case 2:
                    logMsg = "User not exists on neo4j";
                    dialogMsg = "Your account not exists";
                    break;
                case 3:
                    logMsg = "Delete operation failed on mongo";
                    dialogMsg = "Operation failed";
                    break;
                case 4:
                    logMsg = "Delete operation failed on neo4j";
                    dialogMsg = "Operation failed";
                    break;
                case -1:
                    logMsg = "Unknown error";
                    dialogMsg = "Unknown error";
                    break;
            }
            if(res > 0 || res == -1){
                Logger.error(logMsg);
                DialogManager.getInstance().createErrorAlert(userPageAnchorPane, dialogMsg);
            }
            else
                StageManager.showPage(ViewNavigator.LOGIN.getPage());
        }

    }

    @FXML
    private void deleteButtonIn(MouseEvent event){
        userPageDeleteButton.setStyle(
                "-fx-background-color:  white;" +
                        "-fx-border-color:  #f4511e;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: black;"
        );
    }

    @FXML
    private void deleteButtonOut(MouseEvent event){
        userPageDeleteButton.setStyle(
                "-fx-background-color:  #f4511e;" +
                        "-fx-border-color:  transparent;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: white;"
        );
    }

    @FXML
    private void imageRightButtonClick(MouseEvent event){
        Logger.info("Image right button clicked");
        if(imageNumber == maxUserImages - 1 )
            imageNumber = 0;
        else
            imageNumber += 1;

        imagePath = imagePath = "/img/users/user" + (Integer)imageNumber + ".png";
        userPageImage.setImage(ImageCache.getImageFromLocalPath(imagePath));
    }

    @FXML
    private void imageLeftButtonClick(MouseEvent event){

        Logger.info("Image left button clicked");
        if(imageNumber == 0 )
            imageNumber = maxUserImages - 1;
        else
            imageNumber -= 1;

        imagePath = "/img/users/user" + (Integer)imageNumber + ".png";
        userPageImage.setImage(ImageCache.getImageFromLocalPath(imagePath));
    }


    /***********************/

    public void initialize() throws IOException, Exception {


        String sessionActorName = "";
        String actorType = MyPodcastDB.getInstance().getSessionType();
        if(actorType.equals("User"))
            sessionActorName = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        else if(actorType.equals("Author"))
            sessionActorName = ((Author)MyPodcastDB.getInstance().getSessionActor()).getName();

        Logger.info("Session user: " + actorType);

        String pageUsername = StageManager.getObjectIdentifier();

        wPodcasts = new ArrayList<>();
        lPodcasts = new ArrayList<>();
        authors = new ArrayList<>();
        users = new ArrayList<>();
        wPodcastsByVisitor = new ArrayList<>();
        lPodcastsByVisitor = new ArrayList<>();
        authorsByVisitor = new ArrayList<>();
        usersByVisitor = new ArrayList<>();
        pageOwner = new User();
        ObservableList<String> countryList = FXCollections.observableArrayList(JsonDecode.getInstance().getCountries());
        ObservableList<String> categoryList = FXCollections.observableArrayList(JsonDecode.getInstance().getCategories());
        ObservableList<String> genderList =FXCollections.observableArrayList("male", "famale", "other");
        userPageCountryComboBox.setItems(countryList);
        userPageFavGenreComboBox.setItems(categoryList);
        userPageGenderComboBox.setItems(genderList);
        userPageCountryComboBox.setVisibleRowCount(5);
        userPageFavGenreComboBox.setVisibleRowCount(5);
        userPageCountryComboBox.setEditable(false);
        userPageFavGenreComboBox.setEditable(false);
        userPageGenderComboBox.setEditable(false);
        pageOwner.setUsername(pageUsername);
        //this.simulateServiceLayer(pageUsername, wPodcasts, lPodcasts, authors, users);
        int res = service.loadUserPageProfile(
                actorType,
                sessionActorName,
                pageOwner, wPodcasts, lPodcasts, authors, users,
                wPodcastsByVisitor, lPodcastsByVisitor, authorsByVisitor, usersByVisitor,
                newRequestPodcast, newRequestActor
                );
        Logger.info(usersByVisitor.toString());
        if(res == 0)
            Logger.success("Load user profile success");
        else if(res == 1){
            Logger.error("User not exists on mongo");
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, "User not exists");
            return;
        }
        else if(res == 2){
            Logger.error("User not exists on neo4j");
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, "User not exists");
            return;
        }
        else if(res == 3){
            Logger.error("User visitor not exists on neo4j");
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, "Your user account not exists");
            return;
        }
        else if(res == 4){
            Logger.error("Author visitor not exists on neo4j");
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, "Your author account not exists");
            return;
        }
        else{
            Logger.error("Unknown error");
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, "Unknown error");
            return;
        }

        if(actorType.equals("User"))
            actorPageButton.setImage(
                    ImageCache.getImageFromLocalPath(
                            ((User)MyPodcastDB.getInstance().getSessionActor()).getPicturePath()
                    )
            );
        else if(actorType.equals("Author"))
            actorPageButton.setImage(
                    ImageCache.getImageFromLocalPath(
                            ((Author)MyPodcastDB.getInstance().getSessionActor()).getPicturePath()
                    )
            );

        if(actorType.equals("User") && pageOwner.getUsername().equals(sessionActorName)) {

            Logger.info("Owner mode");
            userPageFollowButton.setVisible(false);
            userPagePrivateArea.setVisible(true);
            userPageSettingsButton.setVisible(true);

        }
        else if (actorType.equals("User")){
            Logger.info("User visitor mode");
            int result = service.checkFollowUser(sessionActorName, pageOwner.getUsername());
            String logMsg = "";
            String dialogMsg = "";
            switch (res){
                case 0 :
                case 1 :
                    Logger.success("Check relation success");
                    break;
                case 2 :
                    logMsg = "Your account not exists on mongo";
                    dialogMsg = "Your account not exists";
                    break;
                case 3 :
                    logMsg = "Your account not exists on neo4j";
                    dialogMsg = "Your account not exists";
                    break;
                case 4 :
                    logMsg = "User not exists on mongo";
                    dialogMsg = "User not exists";
                    break;
                case 5 :
                    logMsg = "User not exists on neo4j";
                    dialogMsg = "User not exists";
                    break;
                case -1:
                    logMsg = "Unknown error";
                    dialogMsg = "Unknown error";
                    break;
            }
            if(res == 0 || res == -1) {
                userPageFollowButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_52px.png"));
                isFollowed = true;
            }
            else if(res == 1) {
                userPageFollowButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_50px.png"));
                isFollowed = false;
            }
            else if (res == -1){
                Logger.error("Unknown error");
                DialogManager.getInstance().createErrorAlert(userPageAnchorPane, "unknown error");
                return;
            }
            else{
                Logger.error(logMsg);
                DialogManager.getInstance().createErrorAlert(userPageAnchorPane, dialogMsg);
            }

            userPageFollowButton.setVisible(true);
            userPagePrivateArea.setVisible(false);
            userPageSettingsButton.setVisible(false);
            userPageDeleteButton.setVisible(false);
        }
        else if(actorType.equals("Author")){
            Logger.info("Author visitor mode");

            userPageFollowButton.setVisible(false);
            userPagePrivateArea.setVisible(false);
            userPageSettingsButton.setVisible(false);
            userPageDeleteButton.setVisible(false);
        }
        else{
            Logger.info("Admin mode");
            userPageFollowButton.setVisible(false);
            userPagePrivateArea.setVisible(true);
            userPageSettingsButton.setVisible(false);
            userPageDeleteButton.setVisible(true);
        }

        userPageConfirmButton.setVisible(false);
        userPageCrossButton.setVisible(false);
        imageButtonLeft.setVisible(false);
        imageButtonRight.setVisible(false);

        //fill textFields and image
        restoreTextFields();

        // fill the watchlist grid
        if(!wPodcasts.isEmpty()) {
            loadWatchlaterPodcast(true);
            for(int i = 1; i < wPodcasts.size() && i <= podcastRowSize; i++)
                loadWatchlaterPodcast(false);
        }
        else{
            userPageWatchlistLabel.setText("Watchlist is empty");
            userPageWatchlistAreaHBox.setVisible(false);
            userPageWatchlistAreaHBox.setStyle("-fx-min-height: 0; -fx-pref-height: 0");
        }

        // fill the liked grid
        if(!lPodcasts.isEmpty()){
            loadLikedPodcast(true);
            for(int i = 1; i < lPodcasts.size() && i <= podcastRowSize; i++)
                loadLikedPodcast(false);
        }
        else{
            userPageLikedLabel.setText("There are no liked podcasts");
            userPageLikedAreaHBox.setVisible(false);
            userPageLikedAreaHBox.setStyle("-fx-min-height: 0; -fx-pref-height: 0");
        }

        //fill the authors grid
        if(!authors.isEmpty()) {
            loadAuthor(true);
           for(int i = 1; i < authors.size() && i <= actorRowSize; i++)
               loadAuthor(false);
        }
        else{
            userPageAuthorsLabel.setText("There are no followed authors");
            userPageAuthorsAreaHBox.setVisible(false);
            userPageAuthorsAreaHBox.setStyle("-fx-min-height: 0; -fx-pref-height: 0");
        }

        //fill the users grid
        if(!users.isEmpty()) {
            loadUser(true);
            for(int i = 1; i < users.size() && i <= actorRowSize; i++)
                loadUser(false);
        }
        else{
            userPageUsersLabel.setText("There are no followed users");
            userPageUsersAreaHBox.setVisible(false);
            userPageUsersAreaHBox.setStyle("-fx-min-height: 0; -fx-pref-height: 0");
        }

        userPageWatchlistScrollPane.setHvalue(0.0);
        userPageLikedScrollPane.setHvalue(0.0);
        userPageAuthorsScrollPane.setHvalue(0.0);
        userPageUsersScrollPane.setHvalue(0.0);
        if(wPodcasts.size() <= podcastRowSize)
            userPageWatchlistRightButton.setVisible(false);
        userPageWatchlistLeftButton.setVisible(false);
        if(lPodcasts.size() <= podcastRowSize)
            userPageLikedRightButton.setVisible(false);
        userPageLikedLeftButton.setVisible(false);
        if(authors.size() <= actorRowSize)
            userPageAuthorsRightButton.setVisible(false);
        userPageAuthorsLeftButton.setVisible(false);
        if(users.size() <= actorRowSize)
            userPageUsersRightButton.setVisible(false);
        userPageUsersLeftButton.setVisible(false);
        userPageCountryComboBox.setVisible(false);
        userPageGenderComboBox.setVisible(false);
        userPageFavGenreComboBox.setVisible(false);
    }


    void simulateServiceLayer(String usernamePage, List<Podcast> wPodcasts, List<Podcast> lPodcasts, List<Author> authors, List<User> users){

        // service simulation
        pageOwner.setUsername(usernamePage);
        pageOwner.setAge(65);
        pageOwner.setGender("male");
        pageOwner.setPicturePath("File:src/main/resources/img/user_100px.png");
        pageOwner.setFavouriteGenre("Music History");
        pageOwner.setCountry("Spain");
        pageOwner.setEmail("paologiacomini@example.com");
        pageOwner.setName("Paolo");
        pageOwner.setSurname("Giacomini");
        pageOwner.setPicturePath("/img/users/user5.png");
        isFollowed = false;

        Podcast p1 = new Podcast("54eb342567c94dacfb2a3e50", "Scaling Global", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg");
        Podcast p2 = new Podcast("9852b276565c4f5eb9cdd999", "Speedway Soccer", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts116/v4/be/c4/51/bec45143-957a-c8ba-9af6-120578fd34f8/mza_14722049121013741560.jpg/600x600bb.jpg");
        Podcast p3 = new Podcast("ab3320eef1052aad807747ec", "Talking Disney Podcast", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts114/v4/3b/30/9c/3b309c73-aec5-ac96-60b9-34eba0218218/mza_7561584782270172307.jpg/600x600bb.jpg");

        Author a1 = new Author("ahy2bs89ha5c4f5eb9cddaaa", "Michael Colosi", "File:src/main/resources/img/user_male_96px.png" );
        Author a2 = new Author("ufsdfrt445efsge5srfsdffa", "Preface Podcast", "File:src/main/resources/img/User Female Skin Type 6_160px.png" );
        Author a3 = new Author("6sffgty6wefy742eerwetttt", "Apple Inc.", "File:src/main/resources/img/user_male_skin_type_3_80px.png" );

        User u1 = new User("Paolo Lupini", "File:src/main/resources/img/account_50px.png");
        User u2 = new User("Chiara Proietti", "File:src/main/resources/img/clever_woman_50px.png");
        User u3 = new User("Claudio Giuseppe", "File:src/main/resources/img/business_man_with_beard_50px.png");


        for(int i = 0; i < 5; i++){
            wPodcasts.add(p1);
            wPodcasts.add(p2);
            wPodcasts.add(p3);
            lPodcasts.add(p1);
            lPodcasts.add(p2);
            lPodcasts.add(p3);
            authors.add(a1);
            authors.add(a2);
            authors.add(a3);
            users.add(u1);
            users.add(u2);
            users.add(u3);
        }

        //test empty lists
        //wPodcasts.clear();
        //lPodcasts.clear();
        //authors.clear();
        //users.clear();


    }

    void enableTextFields(boolean value){
        int padding;
        if(value)
            padding = 1;
        else
            padding = 0;
        userPageUsernameTextField.setEditable(value);
        userPageNameTextField.setEditable(value);
        userPageSurnameTextField.setEditable(value);
        userPageEmailTextField.setEditable(value);
        userPageAgeTextField.setEditable(value);
        userPageUsernameTextField.setPadding(new Insets(0,0,0,padding));
        userPageNameTextField.setPadding(new Insets(0,0,0,padding));
        userPageGenderComboBox.setPadding(new Insets(0,0,0,padding));
        userPageSurnameTextField.setPadding(new Insets(0,0,0,padding));
        userPageGenderComboBox.setPadding(new Insets(0,0,0,padding));
        userPageEmailTextField.setPadding(new Insets(0,0,0,padding));
        userPageAgeTextField.setPadding(new Insets(0,0,0,padding));
        userPageGenderComboBox.setPadding(new Insets(0,0,0,padding));
        userPageCountryComboBox.setVisible(value);
        userPageGenderComboBox.setVisible(value);
        userPageFavGenreComboBox.setVisible(value);
        userPageCountryLabel.setVisible(!value);
        userPageGenderLabel.setVisible(!value);
        userPageFavGenreLabel.setVisible(!value);


    }

    User getDataFromTextFields(){

        User newUser = new User();
        newUser.copy(pageOwner);
        newUser.setUsername(userPageUsernameTextField.getText());
        newUser.setName(userPageNameTextField.getText());
        newUser.setSurname(userPageSurnameTextField.getText());
        newUser.setAge(Integer.parseInt(userPageAgeTextField.getText()));
        newUser.setCountry(userPageCountryComboBox.getValue().toString());
        newUser.setEmail(userPageEmailTextField.getText());
        newUser.setGender(userPageGenderComboBox.getValue().toString());
        newUser.setFavouriteGenre(userPageFavGenreComboBox.getValue().toString());
        newUser.setPicturePath(imagePath);
        return newUser;
    }

    void restoreTextFields(){

        userPageUsernameTextField.setText(pageOwner.getUsername());
        userPageNameTextField.setText(pageOwner.getName());
        userPageSurnameTextField.setText(pageOwner.getSurname());
        userPageCountryComboBox.setValue(pageOwner.getCountry());
        userPageFavGenreComboBox.setValue(pageOwner.getFavouriteGenre());
        userPageAgeTextField.setText(((Integer)pageOwner.getAge()).toString());
        userPageGenderComboBox.setValue(pageOwner.getGender());
        userPageEmailTextField.setText(pageOwner.getEmail());
        userPageImage.setImage(ImageCache.getInstance().getImageFromLocalPath(pageOwner.getPicturePath()));
        imagePath = pageOwner.getPicturePath();
        userPageFavGenreLabel.setText(pageOwner.getFavouriteGenre());
        userPageCountryLabel.setText(pageOwner.getCountry());
        userPageGenderLabel.setText(pageOwner.getGender());
    }

    void loadWatchlaterPodcast(boolean first) throws IOException {

        int row = 0;
        int column;
        if(first)
            column = 0;
        else
            column = this.userPageWatchlistGrid.getColumnCount();

        Podcast podcast = wPodcasts.get(column);

        FXMLLoader likedfxmlLoader = new FXMLLoader();
        likedfxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreviewInUserPage.fxml"));

        // create new podcast element
        AnchorPane newPodcast = likedfxmlLoader.load();
        PodcastPreviewInUserPageController whatchlistController = likedfxmlLoader.getController();

        boolean isInWatchlist = false;
        boolean isLiked = false;
        if(MyPodcastDB.getInstance().getSessionType().equals("User") && !((User)(MyPodcastDB.getInstance().getSessionActor())).getUsername().equals(pageOwner.getUsername())){
            if(wPodcastsByVisitor.contains(podcast.getId()))
                isInWatchlist = true;
            if(lPodcastsByVisitor.contains(podcast.getId()))
                isLiked = true;

            whatchlistController.setData(userPageAnchorPane, podcast,((User)(MyPodcastDB.getInstance().getSessionActor())).getUsername(), isInWatchlist, isLiked);
        }
        else
            whatchlistController.setData(userPageAnchorPane, podcast, MyPodcastDB.getInstance().getSessionType());

        // add new podcast to grid
        this.userPageWatchlistGrid.add(newPodcast, column, row);
    }

    void loadLikedPodcast(boolean first) throws IOException {
        int row = 0;
        int column;
        if(first)
            column = 0;
        else
            column = userPageLikedGrid.getColumnCount();

        if(column == lPodcasts.size())
            return;
        Podcast podcast = lPodcasts.get(column);
        FXMLLoader likedfxmlLoader = new FXMLLoader();
        likedfxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreviewInUserPage.fxml"));

        // create new podcast element
        AnchorPane newPodcast = likedfxmlLoader.load();
        PodcastPreviewInUserPageController likedController = likedfxmlLoader.getController();

        boolean isInWatchlist = false;
        boolean isLiked = false;
        if(MyPodcastDB.getInstance().getSessionType().equals("User") && !((User)(MyPodcastDB.getInstance().getSessionActor())).getUsername().equals(pageOwner.getUsername())){
            if(wPodcastsByVisitor.contains(podcast.getId()))
                isInWatchlist = true;
            if(lPodcastsByVisitor.contains(podcast.getId()))
                isLiked = true;

            likedController.setData(userPageAnchorPane, podcast, ((User)(MyPodcastDB.getInstance().getSessionActor())).getUsername(), isInWatchlist, isLiked);
        }
        else
            likedController.setData(userPageAnchorPane, podcast, MyPodcastDB.getInstance().getSessionType());

        // add new podcast to grid
        this.userPageLikedGrid.add(newPodcast, column, row);
    }

    void loadAuthor(boolean first) throws IOException {
        int row = 0;
        int column;
        if(first)
            column = 0;
        else
            column = userPageAuthorsGrid.getColumnCount();
        Author author = authors.get(column);
        FXMLLoader authorfxmlLoader = new FXMLLoader();
        authorfxmlLoader.setLocation(getClass().getClassLoader().getResource("ActorPreview.fxml"));
        AnchorPane newAuthor = authorfxmlLoader.load();
        ActorPreviewController authorController = authorfxmlLoader.getController();

        boolean isFollowed = false;
        if(MyPodcastDB.getInstance().getSessionType().equals("Author") ||
                (MyPodcastDB.getInstance().getSessionType().equals("User") && !((User)(MyPodcastDB.getInstance().getSessionActor())).getUsername().equals(pageOwner.getUsername()))){
            if(authorsByVisitor.contains(author.getName()))
                isFollowed = true;

            if(MyPodcastDB.getInstance().getSessionType().equals("Author"))
                authorController.setData(userPageAnchorPane, author, "Author", ((Author)(MyPodcastDB.getInstance().getSessionActor())).getName(), isFollowed);
            else
                authorController.setData(userPageAnchorPane, author, "User", ((User)(MyPodcastDB.getInstance().getSessionActor())).getUsername(), isFollowed);
        }
        else
            authorController.setData(userPageAnchorPane,author, MyPodcastDB.getInstance().getSessionType());
        this.userPageAuthorsGrid.add(newAuthor, column, row);
        column++;
    }

    void loadUser(boolean first) throws IOException {
        int row = 0;
        int column;
        if(first)
            column = 0;
        else
            column = userPageUsersGrid.getColumnCount();

        User user = users.get(column);
        FXMLLoader userfxmlLoader = new FXMLLoader();
        userfxmlLoader.setLocation(getClass().getClassLoader().getResource("ActorPreview.fxml"));
        AnchorPane newUser = userfxmlLoader.load();
        ActorPreviewController actorController = userfxmlLoader.getController();
        boolean isFollowed = false;
        if(MyPodcastDB.getInstance().getSessionType().equals("User") && !((User)(MyPodcastDB.getInstance().getSessionActor())).getUsername().equals(pageOwner.getUsername())){
            if(usersByVisitor.contains(user.getUsername()))
                isFollowed = true;

            actorController.setData(userPageAnchorPane, user, ((User)(MyPodcastDB.getInstance().getSessionActor())).getUsername(), isFollowed);
        }
        else
            actorController.setData(userPageAnchorPane, user, MyPodcastDB.getInstance().getSessionType());
        this.userPageUsersGrid.add(newUser, column, row);

    }

    public void getWpodcasts(){
        int res = service.getMoreWatchlaterPodcasts(pageOwner.getUsername(), wPodcasts, newRequestPodcast);
        if(res == 2){
            Logger.error("User not exists on neo4j");
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, "User account not exists");
        }
        else if(res == 1){
            Logger.info("No podcasts to show");
            numberOfWpodcastsToAdd = 0;
        }
        else if(res == 0)
            Logger.success("New Podcasts loaded");
        else
            Logger.error("Unknown error");
    }

    public void getLpodcasts(){
        int res = service.getMoreLikedPodcasts(pageOwner.getUsername(), lPodcasts, newRequestPodcast);
        if(res == 2){
            Logger.error("User not exists on neo4j");
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, "User account not exists");
        }
        else if(res == 1){
            Logger.info("No podcasts to show");
            numberOfLpodcastsToAdd = 0;
        }
        else if(res == 0)
            Logger.success("New Podcasts loaded");
        else
            Logger.error("Unknown error");
    }

    public void getAuthors(){
        int res = service.getMoreFollowedAuthors(pageOwner.getUsername(), authors, newRequestActor);
        if(res == 2){
            Logger.error("User not exists on neo4j");
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, "User account not exists");
        }
        else if(res == 1){
            Logger.info("No authors to show");
            numberOfAuthorsToAdd = 0;
        }
        else if(res == 0)
            Logger.success("New authors loaded");
        else
            Logger.error("Unknown error");
    }

    public void getUsers(){
        int res = service.getMoreFollowedUsers(pageOwner.getUsername(), users, newRequestActor);
        if(res == 2){
            Logger.error("User not exists on neo4j");
            DialogManager.getInstance().createErrorAlert(userPageAnchorPane, "User account not exists");
        }
        else if(res == 1){
            Logger.info("No users to show");
            numberOfUsersToAdd = 0;
        }
        else if(res == 0)
            Logger.success("New users loaded");
        else
            Logger.error("Unknown error");
    }
}
