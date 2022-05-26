package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

import java.io.IOException;

public class LoginController {


    @FXML
    private Button loginLoginButton;

    @FXML
    private TextField loginPasswordTextField;


    @FXML
    private Button loginSignUpButton;

    @FXML
    private TextField loginUsernameTextField;

    @FXML
    void loginLoginButtonClick(MouseEvent event) throws IOException {

        String username = loginUsernameTextField.getText();
        String password = loginPasswordTextField.getText();

        if( username.isEmpty() || password.isEmpty()) {
            Logger.error("Login clicked: invalid values");
            return;
        }

        String log = "Login clicked: (" + username + ", " + password +")";
        Logger.info(log);
        StageManager.showPage(ViewNavigator.USERPAGE.getPage());

    }

    @FXML
    void loginLoginButtonPressed(MouseEvent event){
        Logger.info("login buttod pressed");
        loginLoginButton.setStyle(
                "-fx-background-color: #bcbcbc;" +
                        "-fx-border-color:  #e0e0e0;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    void loginLoginButtonReleased(MouseEvent event){
        Logger.info("login buttod released");
        loginLoginButton.setStyle(
                "-fx-background-color: #e0e0e0;" +
                        "-fx-border-color:  #bcbcbc;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    void loginSignUpButtonPressed(MouseEvent event){
        Logger.info("sugnup butto pressed");
        loginSignUpButton.setStyle(
                "-fx-background-color: #bcbcbc;" +
                        "-fx-border-color:  #e0e0e0;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    void loginSgnUpButtonReleased(MouseEvent event){
        Logger.info("login butto released");
        loginSignUpButton.setStyle(
                "-fx-background-color: #e0e0e0;" +
                        "-fx-border-color:  #bcbcbc;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }


    @FXML
    void loginSignUpButtonClick(MouseEvent event) throws IOException {
        Logger.info("SignUp clicked");
        StageManager.showPage(ViewNavigator.SIGNUP.getPage());

    }
}