package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || repPassword.isEmpty() || !password.equals(repPassword)){
            Logger.error("invalid values");
            return;
        }

        int age = LocalDate.now().getYear()-birthDate.getYear();
        User user = new User("", username, password, name, surname, email, country, "", "", favGenre, age, gender);
        Logger.info(user.toString());
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

}