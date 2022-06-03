package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.*;
import it.unipi.dii.lsmsdb.myPodcastDB.service.UserService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
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
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserPageController {

    private User pageOwner;

    private boolean isFollowed;

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

    private int imageNumber;

    private String imagePath;

    private int maxUserImages = 30;




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
        double scrollValue = 0.125;
        if(userPageAuthorsScrollPane.getHvalue() == 1.0)
            return;
        userPageAuthorsScrollPane.setHvalue(userPageAuthorsScrollPane.getHvalue() + scrollValue);
    }

    @FXML
    void scrollAuthorsButtonLeftClick(MouseEvent event) {
        Logger.info("authors left button pressed");
        double scrollValue = 0.125;
        if(userPageAuthorsScrollPane.getHvalue() == 0.0)
            return;
        userPageAuthorsScrollPane.setHvalue(userPageAuthorsScrollPane.getHvalue() - scrollValue);
    }

    @FXML
    void scrollUsersButtonRightClick(MouseEvent event) {
        Logger.info("users right button pressed");
        double scrollValue = 0.125;
        if(userPageUsersScrollPane.getHvalue() == 1.0)
            return;
        userPageUsersScrollPane.setHvalue(userPageUsersScrollPane.getHvalue() + scrollValue);
    }

    @FXML
    void scrollUsersButtonLeftClick(MouseEvent event) {
        Logger.info("users left button pressed");
        double scrollValue = 0.125;
        if(userPageUsersScrollPane.getHvalue() == 0.0)
            return;
        userPageUsersScrollPane.setHvalue(userPageUsersScrollPane.getHvalue() - scrollValue);
    }

    @FXML
    void homeButtonClick(MouseEvent event) throws IOException {
        Logger.info("home button pressed");
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
        Logger.info("follow button pressed");
        if(isFollowed) {
            userPageFollowButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_50px.png"));
            isFollowed = false;
        }
        else {
            userPageFollowButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_52px.png"));
            isFollowed = true;
        }

    }

    @FXML
    void settingsButtonClick(MouseEvent event) {
        Logger.info("settings button clicked");
        enableTextFields(true);
        userPageSettingsButton.setVisible(false);
        userPageConfirmButton.setVisible(true);
        userPageCrossButton.setVisible(true);
        userPageDeleteButton.setVisible(true);
        imageButtonLeft.setVisible(true);
        imageButtonRight.setVisible(true);
        userPageUsernameTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageNameTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageSurnameTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageCountryTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageEmailTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageAgeTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageGenderTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        userPageFavGenreTextField.setStyle("-fx-border-radius: 25; -fx-background-radius: 25; -fx-background-color: #e0e0e0; -fx-border-color: #bcbcbc;");
        imageNumber = 0;
    }

    @FXML
    void crossButtonClick(MouseEvent event) {
        Logger.info("cross button clicked");
        enableTextFields(false);
        restoreTextFields();
        userPageSettingsButton.setVisible(true);
        userPageConfirmButton.setVisible(false);
        userPageCrossButton.setVisible(false);
        userPageDeleteButton.setVisible(false);
        imageButtonLeft.setVisible(false);
        imageButtonRight.setVisible(false);
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
        getDataFromTextFields();
        Logger.info(pageOwner.toString());
        userPageSettingsButton.setVisible(true);
        userPageConfirmButton.setVisible(false);
        userPageCrossButton.setVisible(false);
        userPageDeleteButton.setVisible(false);
        imageButtonLeft.setVisible(false);
        imageButtonRight.setVisible(false);
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
    void actorPageButtonClick(MouseEvent event) throws IOException {
        Logger.info("actor page button clicked");
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
        Logger.info("logout button clicked");
        MyPodcastDB.getInstance().setSession(null, null);
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void deleteButtonClick(MouseEvent event) throws  IOException{
        Logger.info("delete button clicked");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", new ButtonType("OK"),new ButtonType("CANCEL"));
        Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        alert.setTitle(null);
        alert.setHeaderText("Really Delete your account?");
        alert.setContentText(null);
        alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/error_100px.png")));
        alert.initOwner(userPageAnchorPane.getScene().getWindow());

        userPageAnchorPane.setEffect(new BoxBlur(3, 3, 3));
        Optional<ButtonType> result = alert.showAndWait();
        userPageAnchorPane.setEffect(null);


        if (result.get().getText().equals(alert.getButtonTypes().get(0).getText())){
            StageManager.showPage(ViewNavigator.LOGIN.getPage());
        } else {
            return;
        }

    }

    @FXML
    private void deleteButtonPressed(MouseEvent event){
        userPageDeleteButton.setStyle(
                "-fx-background-color: #bcbcbc;" +
                        "-fx-border-color:  #e0e0e0;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    private void deleteButtonReleased(MouseEvent event){
        userPageDeleteButton.setStyle(
                "-fx-background-color: #e0e0e0;" +
                        "-fx-border-color:  #bcbcbc;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    private void imageRightButtonClick(MouseEvent event){
        Logger.info("image right button clicked");
        if(imageNumber == maxUserImages - 1 )
            imageNumber = 0;
        else
            imageNumber += 1;

        imagePath = imagePath = "/img/users/user" + (Integer)imageNumber + ".png";
        userPageImage.setImage(ImageCache.getImageFromLocalPath(imagePath));
    }

    @FXML
    private void imageLeftButtonClick(MouseEvent event){

        Logger.info("image left button clicked");
        if(imageNumber == 0 )
            imageNumber = maxUserImages - 1;
        else
            imageNumber -= 1;

        imagePath = "/img/users/user" + (Integer)imageNumber + ".png";
        userPageImage.setImage(ImageCache.getImageFromLocalPath(imagePath));
    }


    /***********************/

    public void initialize() throws IOException {


        String sessionActorName = "";
        String actorType = MyPodcastDB.getInstance().getSessionType();
        if(actorType.equals("User"))
            sessionActorName = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();

        Logger.info("session user: " + actorType);

        String pageUsername = StageManager.getObjectIdentifier();

        List<Podcast> wPodcasts = new ArrayList<>();
        List<Podcast> lPodcasts = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        List<User> users = new ArrayList<>();
        pageOwner = new User();
        pageOwner.setUsername(pageUsername);
        //this.simulateServiceLayer(pageUsername, wPodcasts, lPodcasts, authors, users);
        UserService service = new UserService();
        service.loadUserPageProfile(pageOwner, wPodcasts, lPodcasts, authors, users, 10);

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

            Logger.info("owner mode");
            userPageFollowButton.setVisible(false);
            userPagePrivateArea.setVisible(true);
            userPageSettingsButton.setVisible(true);

        }
        else if(!actorType.equals("Admin")){
            Logger.info("visitor mode");
            isFollowed = false; //take it by service
            if(isFollowed)
                userPageFollowButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_52px.png"));
            else
                userPageFollowButton.setImage(ImageCache.getImageFromLocalPath("/img/Favorite_50px.png"));

            userPageFollowButton.setVisible(true);
            userPagePrivateArea.setVisible(false);
            userPageSettingsButton.setVisible(false);
            userPageDeleteButton.setVisible(false);
        }
        else{
            Logger.info("admin mode");
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
        userPageUsernameTextField.setText(pageOwner.getUsername());
        userPageCountryTextField.setText(pageOwner.getCountry());
        userPageFavGenreTextField.setText(pageOwner.getFavouriteGenre());
        userPageGenderTextField.setText(pageOwner.getGender());
        Image image = ImageCache.getImageFromLocalPath(pageOwner.getPicturePath());
        userPageImage.setImage(image);
        userPageNameTextField.setText(pageOwner.getName());
        userPageSurnameTextField.setText(pageOwner.getSurname());
        userPageAgeTextField.setText(((Integer)pageOwner.getAge()).toString());
        userPageEmailTextField.setText(pageOwner.getEmail());
        imagePath = pageOwner.getPicturePath();

        // fill the watchlist grid
        if(!wPodcasts.isEmpty()) {
            int row = 0;
            int column = 0;
            for (Podcast podcast : wPodcasts) {
                FXMLLoader watchListfxmlLoader = new FXMLLoader();
                watchListfxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreviewInUserPage.fxml"));

                // create new podcast element
                AnchorPane newPodcast = watchListfxmlLoader.load();
                PodcastPreviewInUserPageController watchListController = watchListfxmlLoader.getController();
                watchListController.setData(podcast);

                // add new podcast to grid
                this.userPageWatchlistGrid.add(newPodcast, column, row);
                column++;
            }
        }
        else{
            userPageWatchlistLabel.setText("Watchlist is empty");
            userPageWatchlistAreaHBox.setVisible(false);
            userPageWatchlistAreaHBox.setStyle("-fx-min-height: 0; -fx-pref-height: 0");
        }

        // fill the liked grid
        if(!lPodcasts.isEmpty()){
            int row = 0;
            int column = 0;
            for (Podcast podcast : lPodcasts) {
                FXMLLoader likedfxmlLoader = new FXMLLoader();
                likedfxmlLoader.setLocation(getClass().getClassLoader().getResource("PodcastPreviewInUserPage.fxml"));

                // create new podcast element
                AnchorPane newPodcast = likedfxmlLoader.load();
                PodcastPreviewInUserPageController likedController = likedfxmlLoader.getController();
                likedController.setData(podcast);

                // add new podcast to grid
                this.userPageLikedGrid.add(newPodcast, column, row);
                column++;
            }
        }
        else{
            userPageLikedLabel.setText("There are no liked podcasts");
            userPageLikedAreaHBox.setVisible(false);
            userPageLikedAreaHBox.setStyle("-fx-min-height: 0; -fx-pref-height: 0");
        }

        //fill the authors grid
        if(!authors.isEmpty()) {
            int row = 0;
            int column = 0;
            for (Author author : authors) {
                FXMLLoader authorfxmlLoader = new FXMLLoader();
                authorfxmlLoader.setLocation(getClass().getClassLoader().getResource("ActorPreview.fxml"));
                AnchorPane newAuthor = authorfxmlLoader.load();
                ActorPreviewController authorController = authorfxmlLoader.getController();
                authorController.setData(author);
                this.userPageAuthorsGrid.add(newAuthor, column, row);
                column++;
            }
        }
        else{
            userPageAuthorsLabel.setText("There are no followed authors");
            userPageAuthorsAreaHBox.setVisible(false);
            userPageAuthorsAreaHBox.setStyle("-fx-min-height: 0; -fx-pref-height: 0");
        }

        //fill the users grid
        if(!users.isEmpty()) {
            int row = 0;
            int column = 0;
            for (User u : users) {
                FXMLLoader userfxmlLoader = new FXMLLoader();
                userfxmlLoader.setLocation(getClass().getClassLoader().getResource("ActorPreview.fxml"));
                AnchorPane newUser = userfxmlLoader.load();
                ActorPreviewController actorController = userfxmlLoader.getController();
                actorController.setData(u);
                this.userPageUsersGrid.add(newUser, column, row);
                column++;
            }
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
        userPageCountryTextField.setEditable(value);
        userPageSurnameTextField.setEditable(value);
        userPageFavGenreTextField.setEditable(value);
        userPageEmailTextField.setEditable(value);
        userPageAgeTextField.setEditable(value);
        userPageGenderTextField.setEditable(value);
        userPageUsernameTextField.setPadding(new Insets(0,0,0,padding));
        userPageNameTextField.setPadding(new Insets(0,0,0,padding));
        userPageCountryTextField.setPadding(new Insets(0,0,0,padding));
        userPageSurnameTextField.setPadding(new Insets(0,0,0,padding));
        userPageFavGenreTextField.setPadding(new Insets(0,0,0,padding));
        userPageEmailTextField.setPadding(new Insets(0,0,0,padding));
        userPageAgeTextField.setPadding(new Insets(0,0,0,padding));
        userPageGenderTextField.setPadding(new Insets(0,0,0,padding));


    }

    void getDataFromTextFields(){

        pageOwner.setUsername(userPageUsernameTextField.getText());
        pageOwner.setName(userPageNameTextField.getText());
        pageOwner.setSurname(userPageSurnameTextField.getText());
        pageOwner.setAge(Integer.parseInt(userPageAgeTextField.getText()));
        pageOwner.setCountry(userPageCountryTextField.getText());
        pageOwner.setEmail(userPageEmailTextField.getText());
        pageOwner.setGender(userPageGenderTextField.getText());
        pageOwner.setFavouriteGenre(userPageFavGenreTextField.getText());
        pageOwner.setPicturePath(imagePath);

    }

    void restoreTextFields(){

        userPageUsernameTextField.setText(pageOwner.getUsername());
        userPageNameTextField.setText(pageOwner.getName());
        userPageSurnameTextField.setText(pageOwner.getSurname());
        userPageCountryTextField.setText(pageOwner.getCountry());
        userPageFavGenreTextField.setText(pageOwner.getFavouriteGenre());
        userPageAgeTextField.setText(((Integer)pageOwner.getAge()).toString());
        userPageGenderTextField.setText(pageOwner.getGender());
        userPageEmailTextField.setText(pageOwner.getEmail());
        userPageImage.setImage(ImageCache.getInstance().getImageFromLocalPath(pageOwner.getPicturePath()));
    }

}
