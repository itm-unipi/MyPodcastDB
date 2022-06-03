package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AdminService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddAdminController {

    @FXML
    private AnchorPane addAdminAnchorPane;

    @FXML
    private TextField adminNameTextField;

    @FXML
    private TextField adminPwdTextFiled;

    @FXML
    private TextField adminRpPwdTextFiled;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    public String getAdminName() {
        return adminNameTextField.getText();
    }

    public String getAdminPwd() {
        return adminPwdTextFiled.getText();
    }

    public String getAdminRpPwd() {
        return adminRpPwdTextFiled.getText();
    }


    @FXML
    void confirmButtonClick(MouseEvent event){

        String name = adminNameTextField.getText();
        String password = adminPwdTextFiled.getText();
        String rpPassword = adminRpPwdTextFiled.getText();

        if( !name.isEmpty() && !password.isEmpty() && !rpPassword.isEmpty() && password.equals(rpPassword) ) {

            Admin admin = new Admin("",name, password, "");
            Logger.info(admin.toString());

            AdminService service = new AdminService();
            if(service.addAdmin(admin)) {
                DialogManager.getInstance().createInformationAlert(addAdminAnchorPane, "Account created");
                closeStage(event);
            }
            else
                DialogManager.getInstance().createErrorAlert(addAdminAnchorPane, "Creating account failed");

        }
        else{
            Logger.info("no new admin created, invalid inputs typed");
            DialogManager.getInstance().createErrorAlert(addAdminAnchorPane, "Invalid ");
        }
    }

    @FXML
    void cancelButtonClick(MouseEvent event){
        Logger.info("cancel button clicked");
        closeStage(event);

    }

    @FXML
    void confirmButtonIn(MouseEvent event){
        Logger.info("login button pressed");
        confirmButton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #4CAF50;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: black;"
        );
    }

    @FXML
    void confirmButtonOut(MouseEvent event){
        Logger.info("login buttod released");
        confirmButton.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-border-color:  transparent;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: white;"
        );
    }

    @FXML
    void cancelButtonIn(MouseEvent event){
        Logger.info("sugnup butto pressed");
        cancelButton.setStyle(
                "-fx-background-color:  white;" +
                        "-fx-border-color:  #f4511e;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: black;"
        );
    }

    @FXML
    void cancelButtonOut(MouseEvent event){
        Logger.info("login button released");
        cancelButton.setStyle(
                "-fx-background-color:  #f4511e;" +
                        "-fx-border-color:  transparent;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: white;"
        );
    }

    void closeStage(MouseEvent event){
        Logger.info("test clocked");
        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}
