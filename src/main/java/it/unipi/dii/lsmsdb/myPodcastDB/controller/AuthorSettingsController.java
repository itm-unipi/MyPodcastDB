package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class AuthorSettingsController {

    private Author author;
    @FXML
    private TextField authorEmail;

    @FXML
    private TextField authorName;

    @FXML
    private PasswordField authorPassword;

    @FXML
    private PasswordField authorConfirmPassword;

    @FXML
    private ImageView imagePreview;

    private int counterImage;

    @FXML
    private DialogPane dialogPane;

    @FXML
    void nextAuthorPicture(MouseEvent event) {
        Logger.info("Next author picture");

        if (this.counterImage == 19)
            this.counterImage = 0; // MIN_NUMBER
        else
            ++this.counterImage;

        imagePreview.setImage(ImageCache.getImageFromLocalPath("/img/authors/author" + this.counterImage + ".png"));
    }

    @FXML
    void previousAuthorPicture(MouseEvent event) {
        Logger.info("Previous author picture");

        if (this.counterImage == 0)
            this.counterImage = 19; // MAX_NUMBER_IMAGE
        else
            --this.counterImage;

        imagePreview.setImage(ImageCache.getImageFromLocalPath("/img/authors/author" + this.counterImage + ".png"));
    }

    @FXML
    void deleteAccount(ActionEvent event) throws IOException {
        Logger.info("Delete account clicked!");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(dialogPane.getScene().getWindow());
        //alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Delete Account");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to delete this account?");
        alert.setGraphic(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            Logger.info("Delete account..");

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(dialogPane.getScene().getWindow());
            alert.setTitle("Delete Account");
            alert.setHeaderText(null);
            alert.setContentText("Account deleted successfully!");
            alert.setGraphic(null);;
            alert.showAndWait();
            StageManager.showPage(ViewNavigator.LOGIN.getPage());
            closeStage(event);
        } else {
            Logger.info("Operation aborted");
        }
    }

    public void setData(Author author) {
        this.author = author;
        this.counterImage = 0;

        authorName.setText(author.getName());
        authorEmail.setText(author.getEmail());
        authorPassword.setText(author.getPassword());
        authorConfirmPassword.setText(author.getPassword());
        imagePreview.setImage(ImageCache.getImageFromLocalPath(author.getPicturePath()));
    }

    @FXML
    void restoreBorderTextField(MouseEvent event) {
        ((TextField)event.getSource()).setStyle("-fx-border-radius: 4; -fx-border-color: transparent");
    }

    @FXML
    void restoreBorderPasswordField(MouseEvent event) {
        ((PasswordField)event.getSource()).setStyle("-fx-border-radius: 4; -fx-border-color: transparent");
    }

    @FXML
    void updatePersonalInfo(ActionEvent event) {
        System.out.println("Update applied");

        boolean emptyFields = false;

        if (authorName.getText().equals("")) {
            authorName.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            emptyFields = true;
        }

        if (authorEmail.getText().equals("")) {
            authorEmail.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            emptyFields = true;
        }

        if (authorPassword.getText().equals("")) {
            authorPassword.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            emptyFields = true;
        }

        if (authorConfirmPassword.getText().equals("")) {
            authorConfirmPassword.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            emptyFields = true;
        }

        if (!authorPassword.getText().equals(authorConfirmPassword.getText())) {
            Logger.info("password doesn't match");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Password Error");
            //alert.setHeaderText("Are u sure?");
            //alert.setContentText(null);
            alert.showAndWait();
            emptyFields = true;
        }

        if (!emptyFields) {
            this.author.setName(authorName.getText());
            this.author.setEmail(authorEmail.getText());
            this.author.setPassword(authorPassword.getText());
            this.author.setPicturePath("/img/authors/author" + this.counterImage + ".png");
            closeStage(event);
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        System.out.println("Cancel Operation");
        closeStage(event);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}
