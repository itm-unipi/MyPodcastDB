package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
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
    private ChoiceBox<String> signUpCoutryChoiceBox;

    @FXML
    private TextField signUpEmailTextField;

    @FXML
    private ChoiceBox<String> signUpFavGenreChoiceBox;

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



    public void initialize() throws IOException {

        ObservableList<String> genderList = FXCollections.observableArrayList(
                "male",
                "famale",
                "other"
        );
        ObservableList<String> countryList = FXCollections.observableArrayList(
                "Italy",
                "France",
                "Germany",
                "Usa",
                "UK"
        );

        ObservableList<String> FavGenreList = FXCollections.observableArrayList(
                "Business",
                "Thriller",
                "Crime",
                "Love"

        );
        signUpGenderChoiceBox.setItems(genderList);
        signUpCoutryChoiceBox.setItems(countryList);
        signUpFavGenreChoiceBox.setItems(FavGenreList);

        signUpGenderChoiceBox.setValue("unknown");
        signUpCoutryChoiceBox.setValue("Unknown");
        signUpFavGenreChoiceBox.setValue("Unknown");
        signUpAgeDatePicker.setValue(LocalDate.now());


    }
    @FXML
    void signUpSignUpButtonClick(MouseEvent event) throws IOException {

        Logger.info("Signup Button Clicked");
        if(signUpLabelTitle.getText().equals("Your User Account")) {
            String username = signUpUsernameTextField.getText();
            String name = signupNameTextField.getText();
            String surname = signupSurnameTextField.getText();
            String email = signUpEmailTextField.getText();
            String password = signUpPasswordTextField.getText();
            String repPassword = signUpRepPasswTextField.getText();
            String gender = signUpGenderChoiceBox.getValue();
            String favGenre = signUpFavGenreChoiceBox.getValue();
            String country = signUpCoutryChoiceBox.getValue();
            LocalDate birthDate = signUpAgeDatePicker.getValue();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || repPassword.isEmpty() || !password.equals(repPassword)) {
                Logger.error("invalid values");
                return;
            }

            int age = LocalDate.now().getYear() - birthDate.getYear();
            User user = new User("", username, password, name, surname, email, country, "", "", favGenre, age, gender);
            Logger.info(user.toString());
        }
        else{
            String name = signUpUsernameTextField.getText();
            String password = signUpPasswordTextField.getText();
            String repPassword = signUpRepPasswTextField.getText();
            String email = signUpEmailTextField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repPassword.isEmpty() || !password.equals(repPassword)) {
                Logger.error("invalid values");
                return;
            }

            Author author = new Author("", name, password,email,"");
            Logger.info(author.toString());
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
        if(signUpLabelTitle.getText().equals("Your User Account")){
            signUpSwitchButton.setImage(new Image("File:src/main/resources/images/switch_on_80px.png"));
            signUpLabelAuthor.setStyle("-fx-font-weight: bold;");
            signUpLabelUser.setStyle("-fx-font-weight: normal;");
            signUpLabelTitle.setText("Your Author Account");
            thirdLineInput.setVisible(false);
            fourthLineInput.setVisible(false);
            fifthLineInput.setVisible(false);
            signUpUsernameLabel.setText("Name");

        }
        else{
            signUpSwitchButton.setImage(new Image("File:src/main/resources/images/switch_off_80px.png"));
            signUpLabelAuthor.setStyle("-fx-font-weight: normal;");
            signUpLabelUser.setStyle("-fx-font-weight: bold;");
            signUpLabelTitle.setText("Your User Account");
            thirdLineInput.setVisible(true);
            fourthLineInput.setVisible(true);
            fifthLineInput.setVisible(true);
            signUpUsernameLabel.setText("Username");
        }

    }

}