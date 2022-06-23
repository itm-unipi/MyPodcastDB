package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AdminDashboardService;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    private TextField adminEmailTextFiled;
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
    void exit(MouseEvent event) {
        closeStage(event);
    }

    @FXML
    void confirmButtonClick(MouseEvent event){

        String name = adminNameTextField.getText();
        String password = adminPwdTextFiled.getText();
        String rpPassword = adminRpPwdTextFiled.getText();
        String email = adminEmailTextFiled.getText();

        if( name.isEmpty() || password.isEmpty() || rpPassword.isEmpty() || email.isEmpty()) {
            Logger.error("Invalid inputs");
            DialogManager.getInstance().createErrorAlert(addAdminAnchorPane, "Invalid inputs");
            return;
        }
        if(name.indexOf('@') != -1){
            Logger.error("Username not valid [@]");
            DialogManager.getInstance().createErrorAlert(addAdminAnchorPane, "Username not valid [@]");
            return;
        }
        if(!password.equals(rpPassword)){
            Logger.error("Passwords not the same");
            DialogManager.getInstance().createErrorAlert(addAdminAnchorPane, "Password not the same");
            return;
        }
        if(email.indexOf('@') == -1){
            Logger.error("Invalid email");
            DialogManager.getInstance().createErrorAlert(addAdminAnchorPane, "Invalid email");
            return;
        }
        Admin admin = new Admin("",name, password, email);
        Logger.info(admin.toString());

        AdminDashboardService service = new AdminDashboardService();
        int res = service.addAdmin(admin);
        if(res == 0) {
            Logger.success("admin account created");
            DialogManager.getInstance().createInformationAlert(addAdminAnchorPane, "Account created");
            closeStage(event);
        }
        else if(res == 1){
            Logger.error("admin already exists");
            DialogManager.getInstance().createErrorAlert(addAdminAnchorPane, "Admin already exists");
        }
        else if(res == 2){
            Logger.error("admin with the same email already exists");
            DialogManager.getInstance().createErrorAlert(addAdminAnchorPane, "Admin with the same email already exists");
        }
        else{
            Logger.error("Adding admin on mongo failed");
            DialogManager.getInstance().createErrorAlert(addAdminAnchorPane, "Operation failed");
        }
    }

    @FXML
    void cancelButtonClick(MouseEvent event){
        Logger.info("cancel button clicked");
        closeStage(event);

    }

    @FXML
    void confirmButtonIn(MouseEvent event){
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
