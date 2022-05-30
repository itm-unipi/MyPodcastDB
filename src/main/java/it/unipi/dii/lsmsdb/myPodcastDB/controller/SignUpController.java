package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.JsonDecode;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalDate;

public class SignUpController {

    @FXML
    private DatePicker signUpAgeDatePicker;

    @FXML
    private ComboBox<String> signUpCoutryComboBox;

    @FXML
    private TextField signUpEmailTextField;

    @FXML
    private ComboBox<String> signUpFavGenreComboBox;

    @FXML
    private ChoiceBox<String> signUpGenderChoiceBox;

    @FXML
    private PasswordField signUpPasswordTextField;

    @FXML
    private PasswordField signUpRepPasswTextField;

    @FXML
    private Button signUpSignUpButton;

    @FXML
    private TextField signUpUsernameTextField;

    @FXML
    private TextField signupNameTextField;

    @FXML
    private TextField signupSurnameTextField;

    @FXML
    private ImageView signUpSwitchButton;

    @FXML
    private Label signUpLabelUser;

    @FXML
    private Label signUpLabelAuthor;

    @FXML
    private Label signUpLabelTitle;

    @FXML
    private HBox thirdLineInput;

    @FXML
    private HBox fourthLineInput;

    @FXML
    private HBox fifthLineInput;

    @FXML
    private Label signUpUsernameLabel;

    @FXML
    private ImageView signUpImage;

    @FXML
    private ImageView signUpLeftButton;

    @FXML
    private ImageView signUpRightButton;

    private String actorType;

    private int imageNumber;

    private int maxAuthorImages = 20;

    private int maxUserImages = 30;



    public void initialize() throws IOException,Exception {

        actorType = "User";
        imageNumber = 0;

        ObservableList<String> genderList = FXCollections.observableArrayList(
                "male",
                "famale",
                "other"
        );
        ObservableList<String> countryList = FXCollections.observableArrayList(JsonDecode.getInstance().getCountries());

        ObservableList<String> FavGenreList = FXCollections.observableArrayList(JsonDecode.getInstance().getCategories());
        signUpGenderChoiceBox.setItems(genderList);
        signUpCoutryComboBox.setItems(countryList);
        signUpFavGenreComboBox.setItems(FavGenreList);
        signUpCoutryComboBox.setVisibleRowCount(5);
        signUpFavGenreComboBox.setVisibleRowCount(5);

        signUpGenderChoiceBox.setValue("unknown");
        signUpCoutryComboBox.setValue("Unknown");
        signUpFavGenreComboBox.setValue("Unknown");
        signUpAgeDatePicker.setValue(LocalDate.now());
        signUpImage.setImage(ImageCache.getInstance().getImageFromLocalPath("/img/users/user0.png"));


    }
    @FXML
    void signUpSignUpButtonClick(MouseEvent event) throws IOException {

        Logger.info("Signup Button Clicked");
        if(actorType.equals("User")) {
            String username = signUpUsernameTextField.getText();
            String name = signupNameTextField.getText();
            String surname = signupSurnameTextField.getText();
            String email = signUpEmailTextField.getText();
            String password = signUpPasswordTextField.getText();
            String repPassword = signUpRepPasswTextField.getText();
            String gender = signUpGenderChoiceBox.getValue();
            String favGenre = signUpFavGenreComboBox.getValue();
            String country = signUpCoutryComboBox.getValue();
            LocalDate birthDate = signUpAgeDatePicker.getValue();
            String picturePath = "/img/users/user" + (Integer)imageNumber + ".png";

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || repPassword.isEmpty() || !password.equals(repPassword)) {
                Logger.error("invalid values");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid inputs");
                alert.setContentText("");
                alert.showAndWait();
                return;
            }

            int age = LocalDate.now().getYear() - birthDate.getYear();
            User user = new User("", username, password, name, surname, email, country, picturePath, favGenre, age, gender);
            Logger.info(user.toString());
            Author author = new Author("", name, password,email, picturePath);
            Logger.info(author.toString());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information!");
            alert.setHeaderText("Account created");
            alert.setContentText("");

            alert.showAndWait();
        }
        else{
            String name = signUpUsernameTextField.getText();
            String password = signUpPasswordTextField.getText();
            String repPassword = signUpRepPasswTextField.getText();
            String email = signUpEmailTextField.getText();
            String picturePath = "/img/users/user" + (Integer)imageNumber + ".png";

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repPassword.isEmpty() || !password.equals(repPassword)) {
                Logger.error("invalid values");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid inputs");
                alert.setContentText("");
                alert.showAndWait();
                return;
            }

            Author author = new Author("", name, password,email, picturePath);
            Logger.info(author.toString());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information!");
            alert.setHeaderText("Account created");
            alert.setContentText("");

            alert.showAndWait();
        }
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void signUpSignUpButtonPressed(MouseEvent event){
        Logger.info("sugnup button pressed");
        signUpSignUpButton.setStyle(
                "-fx-background-color: #bcbcbc;" +
                        "-fx-border-color:  #e0e0e0;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    void signUpSignUpButtonReleased(MouseEvent event){
        Logger.info("signup button released");
        signUpSignUpButton.setStyle(
                "-fx-background-color: #e0e0e0;" +
                        "-fx-border-color:  #bcbcbc;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    void signUpBackButtonClick(MouseEvent event) throws IOException {
        Logger.info("back button clicked");
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void signUpSwitchClicked(MouseEvent event) throws IOException{
        Logger.info("switch button clicked");
        if(actorType.equals("User")){
            signUpSwitchButton.setImage(ImageCache.getImageFromLocalPath("/img/switch_on_80px.png"));
            signUpLabelAuthor.setStyle("-fx-font-weight: bold;");
            signUpLabelUser.setStyle("-fx-font-weight: normal;");
            signUpLabelTitle.setText("Your Author Account");
            thirdLineInput.setVisible(false);
            fourthLineInput.setVisible(false);
            fifthLineInput.setVisible(false);
            signUpUsernameLabel.setText("Name");
            imageNumber = 0;
            actorType = "Author";
            signUpImage.setImage(ImageCache.getInstance().getImageFromLocalPath("/img/authors/author0.png"));

        }
        else{
            signUpSwitchButton.setImage(ImageCache.getImageFromLocalPath("/img/switch_off_80px.png"));
            signUpLabelAuthor.setStyle("-fx-font-weight: normal;");
            signUpLabelUser.setStyle("-fx-font-weight: bold;");
            signUpLabelTitle.setText("Your User Account");
            thirdLineInput.setVisible(true);
            fourthLineInput.setVisible(true);
            fifthLineInput.setVisible(true);
            signUpUsernameLabel.setText("Username");
            imageNumber = 0;
            actorType = "User";
            signUpImage.setImage(ImageCache.getInstance().getImageFromLocalPath("/img/users/user0.png"));
        }

    }

    @FXML
    void leftButtonClick(MouseEvent event){
        Logger.info("left button clicked");
        if(imageNumber == 0 && actorType.equals("User"))
            imageNumber = maxUserImages - 1;
        else if(imageNumber == 0 && actorType.equals("Author"))
            imageNumber = maxAuthorImages - 1;
        else
            imageNumber -= 1;

        String imagePath = "";
        if(actorType.equals("User"))
            imagePath = "/img/users/user" + (Integer)imageNumber + ".png";
        else
            imagePath = "/img/authors/author" + (Integer)imageNumber + ".png";

        signUpImage.setImage(ImageCache.getImageFromLocalPath(imagePath));

    }

    @FXML
    void rightButtonClick(MouseEvent event){
        Logger.info("right button clicked");
        if(actorType.equals("User") && imageNumber == maxUserImages - 1 )
            imageNumber = 0;
        else if(actorType.equals("Author") && imageNumber == maxAuthorImages - 1)
            imageNumber = 0;
        else
            imageNumber += 1;

        String imagePath = "";
        if(actorType.equals("User"))
            imagePath = "/img/users/user" + (Integer)imageNumber + ".png";
        else
            imagePath = "/img/authors/author" + (Integer)imageNumber + ".png";

        signUpImage.setImage(ImageCache.getImageFromLocalPath(imagePath));
    }

}