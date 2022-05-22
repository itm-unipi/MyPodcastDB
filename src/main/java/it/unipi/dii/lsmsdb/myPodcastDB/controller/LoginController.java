package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class LoginController {

    @FXML
    private ImageView loginHomeButton;

    @FXML
    private Button loginLoginButton;

    @FXML
    private TextField loginPasswordTextField;

    @FXML
    private ImageView loginSearchButton;

    @FXML
    private TextField loginSearchTextField;

    @FXML
    private Button loginSugnUpButton;

    @FXML
    private TextField loginUsernameTextField;

    @FXML
    void loginLoginButtonClick(MouseEvent event) {

        String username = loginUsernameTextField.getText();
        String password = loginPasswordTextField.getText();
        String log = "Login pressed: (" + username + ", " + password +")";
        Logger.info(log);
    }

    @FXML
    void loginSignUpButtonClick(MouseEvent event) {
        Logger.info("SignUp pressed");

    }
}