package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
            Logger.error("Login pressed: invalid values");
            return;
        }

        String log = "Login pressed: (" + username + ", " + password +")";
        Logger.info(log);
        StageManager.showPage(ViewNavigator.USERPAGE.getPage());
    }

    @FXML
    void loginSignUpButtonClick(MouseEvent event) throws IOException {
        Logger.info("SignUp pressed");
        StageManager.showPage(ViewNavigator.SIGNUP.getPage());

    }
}