package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.SignUpService;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.JsonDecode;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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

    @FXML
    private AnchorPane signUpAnchorPane;

    private String actorType;

    private int imageNumber;

    private SignUpService service = new SignUpService();

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

        signUpGenderChoiceBox.setValue("Unknown");
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

            Date dateOfBirth = Date.from(Instant.from(birthDate.atStartOfDay(ZoneId.systemDefault())));

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || repPassword.isEmpty() || !password.equals(repPassword)) {
                Logger.error("Invalid values");
                DialogManager.getInstance().createErrorAlert(signUpAnchorPane, "Invalid values");
                return;
            }
            else if(username.equals("Removed user")){
                Logger.error("username not valid");
                DialogManager.getInstance().createErrorAlert(signUpAnchorPane, "Invalid username");
            }

            User user = new User("", username, password, name, surname, email, country, picturePath, favGenre, dateOfBirth, gender);
            Logger.info(user.toString());

            int res = service.addUserSignUp(user);
            String logMsg = "";
            String dialogMsg = "";
            switch (res){
                case 0:
                    Logger.success("User account created");
                    DialogManager.getInstance().createInformationAlert(signUpAnchorPane, "User account created");
                    break;
                case 1:
                    logMsg = "A user with the same username already exists on mongo";
                    dialogMsg = "A user with the same username already exists";
                    break;
                case 2:
                    logMsg = "A user with the same username already exists on neo4j";
                    dialogMsg = "A user with the same username already exists";
                    break;
                case 3:
                    logMsg = "Operation failed on mongo";
                    dialogMsg = "Operation failed";
                    break;
                case 4:
                    logMsg = "Operation failed on neo4j";
                    dialogMsg = "Operation failed";
                    break;
                case -1:
                    logMsg = "Unknown error";
                    dialogMsg = "Unknown error";
                    break;
            }

            if(res > 0 || res == -1){
                Logger.error(logMsg);
                DialogManager.getInstance().createErrorAlert(signUpAnchorPane, dialogMsg);
                return;
            }

        }
        else{
            String name = signUpUsernameTextField.getText();
            String password = signUpPasswordTextField.getText();
            String repPassword = signUpRepPasswTextField.getText();
            String email = signUpEmailTextField.getText();
            String picturePath = "/img/authors/author" + (Integer)imageNumber + ".png";

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repPassword.isEmpty() || !password.equals(repPassword)) {
                Logger.error("Invalid values");
                DialogManager.getInstance().createErrorAlert(signUpAnchorPane, "Invalid values");
                return;
            }

            Author author = new Author("", name, password, email, picturePath);
            Logger.info(author.toString());

            int res = service.addAuthorSignUp(author);
            String logMsg = "";
            String dialogMsg = "";
            switch (res){
                case 0:
                    Logger.success("Author account created");
                    DialogManager.getInstance().createInformationAlert(signUpAnchorPane, "Author account created");
                    break;
                case 1:
                    logMsg = "An author with the same name already exists on mongo";
                    dialogMsg = "A author with the same name already exists";
                    break;
                case 2:
                    logMsg = "An author with the same name already exists on neo4j";
                    dialogMsg = "An author with the same name already exists";
                    break;
                case 3:
                    logMsg = "Operation failed on mongo";
                    dialogMsg = "Operation failed";
                    break;
                case 4:
                    logMsg = "Operation failed on neo4j";
                    dialogMsg = "Operation failed";
                    break;
                case -1:
                    logMsg = "Unknown error";
                    dialogMsg = "Unknown error";
                    break;
            }

            if(res > 0 || res == -1){
                Logger.error(logMsg);
                DialogManager.getInstance().createErrorAlert(signUpAnchorPane, dialogMsg);
                return;
            }

        }
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void signUpSignUpButtonIn(MouseEvent event){
        Logger.info("Signup button pressed");
        signUpSignUpButton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #4CAF50;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: black;"
        );
    }

    @FXML
    void signUpSignUpButtonOut(MouseEvent event){
        Logger.info("Signup button released");
        signUpSignUpButton.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-border-color:  transparent;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: white;"
        );
    }

    @FXML
    void signUpBackButtonClick(MouseEvent event) throws IOException {
        Logger.info("Back button clicked");
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    @FXML
    void signUpSwitchClicked(MouseEvent event) throws IOException{
        Logger.info("Switch button clicked");
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
        Logger.info("Left button clicked");
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
        Logger.info("Right button clicked");
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