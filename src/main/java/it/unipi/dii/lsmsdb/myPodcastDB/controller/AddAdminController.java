package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class AddAdminController {

    @FXML
    private AnchorPane AddAdminAnchorPane;

    @FXML
    private TextField adminNameTextField;

    @FXML
    private TextField adminPwdTextFiled;

    @FXML
    private TextField adminRpPwdTextFiled;

    public AnchorPane getAddAdminAnchorPane() {
        return AddAdminAnchorPane;
    }

    public TextField getAdminNameTextField() {
        return adminNameTextField;
    }

    public TextField getAdminPwdTextFiled() {
        return adminPwdTextFiled;
    }

    public TextField getAdminRpPwdTextFiled() {
        return adminRpPwdTextFiled;
    }
}
