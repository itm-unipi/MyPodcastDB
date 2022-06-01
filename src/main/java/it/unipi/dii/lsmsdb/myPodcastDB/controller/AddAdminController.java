package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
            Logger.info("{ " +name + ", " + password + ", " + rpPassword + "}");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage stageInfo = (Stage)alert.getDialogPane().getScene().getWindow();
            stageInfo.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
            alert.setTitle("Information!");
            alert.setHeaderText("Account created");
            alert.setContentText(null);
            alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/info_80px.png")));
            alert.initOwner(addAdminAnchorPane.getScene().getWindow());

            addAdminAnchorPane.setEffect(new BoxBlur(3, 3, 3));
            alert.showAndWait();
            addAdminAnchorPane.setEffect(null);

            closeStage(event);
        }
        else{
            Logger.info("no new admin created, invalid inputs typed");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage stageErr = (Stage)alert.getDialogPane().getScene().getWindow();
            stageErr.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
            alert.setTitle("Error!");
            alert.setHeaderText("Invalid inputs");
            alert.setGraphic(new ImageView(ImageCache.getImageFromLocalPath("/img/error_100px.png")));
            alert.setContentText(null);
            alert.initOwner(addAdminAnchorPane.getScene().getWindow());

            addAdminAnchorPane.setEffect(new BoxBlur(3, 3, 3));
            alert.showAndWait();
            addAdminAnchorPane.setEffect(null);
        }
    }

    @FXML
    void cancelButtonClick(MouseEvent event){
        Logger.info("cancel button clicked");
        closeStage(event);

    }

    @FXML
    void confirmButtonPressed(MouseEvent event){
        confirmButton.setStyle(
                "-fx-background-color: #bcbcbc;" +
                        "-fx-border-color:  #e0e0e0;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );

    }

    @FXML
    void confirmButtonReleased(MouseEvent event){
        Logger.info("login butto released");
        confirmButton.setStyle(
                "-fx-background-color: #e0e0e0;" +
                        "-fx-border-color:  #bcbcbc;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    void cancelButtonPressed(MouseEvent event){
        cancelButton.setStyle(
                "-fx-background-color: #bcbcbc;" +
                        "-fx-border-color:  #e0e0e0;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );

    }

    @FXML
    void cancelButtonReleased(MouseEvent event){
        Logger.info("login butto released");
        cancelButton.setStyle(
                "-fx-background-color: #e0e0e0;" +
                        "-fx-border-color:  #bcbcbc;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    void closeStage(MouseEvent event){
        Logger.info("test clocked");
        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}
