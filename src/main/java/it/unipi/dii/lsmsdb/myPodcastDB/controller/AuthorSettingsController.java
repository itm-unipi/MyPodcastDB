package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorService;
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
import java.io.IOException;

public class AuthorSettingsController {
    private Author author;

    private Label authorNameProfile;

    private ImageView actorPictureProfile;

    @FXML
    private TextField authorEmail;

    @FXML
    private TextField authorName;

    @FXML
    private PasswordField authorPassword;

    @FXML
    private PasswordField authorNewPassword;

    @FXML
    private ImageView imagePreview;

    private int counterImage;

    @FXML
    private DialogPane dialogPane;

    /******* AUTHOR PICTURE SLIDE *******/

    @FXML
    void nextAuthorPicture(MouseEvent event) {

        if (this.counterImage == 19)
            this.counterImage = 0; // MIN_NUMBER_IMAGE
        else
            ++this.counterImage;

        imagePreview.setImage(ImageCache.getImageFromLocalPath("/img/authors/author" + this.counterImage + ".png"));
    }

    @FXML
    void previousAuthorPicture(MouseEvent event) {

        if (this.counterImage == 0)
            this.counterImage = 19; // MAX_NUMBER_IMAGE
        else
            --this.counterImage;

        imagePreview.setImage(ImageCache.getImageFromLocalPath("/img/authors/author" + this.counterImage + ".png"));
    }

    /********** RESET BORDER EMPTY FIELDS **********/

    @FXML
    void restoreBorderTextField(MouseEvent event) {
        ((TextField)event.getSource()).setStyle("-fx-border-radius: 4; -fx-border-color: transparent");
    }

    @FXML
    void restoreBorderPasswordField(MouseEvent event) {
        ((PasswordField)event.getSource()).setStyle("-fx-border-radius: 4; -fx-border-color: transparent");
    }

    /*********************************************/

    @FXML
    void deleteAccount(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(dialogPane.getScene().getWindow());
        alert.setTitle("Delete Account");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to delete this account?");
        alert.setGraphic(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {

            if (authorPassword.getText().equals(((Author)MyPodcastDB.getInstance().getSessionActor()).getPassword())) {
                AuthorService authorService = new AuthorService();
                int deleteResult = authorService.deleteAccount();

                if (deleteResult == 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(dialogPane.getScene().getWindow());
                    alert.setTitle("Delete Account");
                    alert.setHeaderText(null);
                    alert.setContentText("Account deleted successfully!");
                    alert.setGraphic(null);;
                    alert.showAndWait();

                    closeStage(event);
                    StageManager.showPage(ViewNavigator.LOGIN.getPage());
                } else {
                    Logger.error("Error during the delete operation");

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(dialogPane.getScene().getWindow());
                    alert.setTitle("Delete Account Error");
                    alert.setHeaderText(null);

                    if (deleteResult == -1) {
                        alert.setContentText("Author don't exists!");
                    } else {
                        // General message error
                        alert.setContentText("Something went wrong!");
                    }

                    alert.setGraphic(null);;
                    alert.showAndWait();
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogPane.getScene().getWindow());
                alert.setTitle("Delete Account: incorrect password");
                alert.setHeaderText(null);
                alert.setContentText("Invalid current password! Please try again.");
                alert.setGraphic(null);;
                alert.showAndWait();
            }
        } else {
            Logger.info("Operation aborted");
        }
    }

    @FXML
    void updatePersonalInfo(ActionEvent event) {
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

        if (!emptyFields) {
            // Author object that keeps the old information of the session author
            Author oldAuthor = new Author();
            oldAuthor.copy((Author) MyPodcastDB.getInstance().getSessionActor());

            // Temporary author with the new information to commit if there are no error
            String password;
            if (authorNewPassword.getText().equals(""))
                password = oldAuthor.getPassword();
            else
                password = authorNewPassword.getText();

            // TODO: bug in tempAuthor -> imagePreview.getImage().getURL()
            // "/img/authors/author" + this.counterImage + ".png" (this.counter is always 0 when the setData is called there will be always a change in the picutre path)
            // if the default img has not index 0
            Author tempAuthor = new Author(author.getId(), authorName.getText(), password, authorEmail.getText(), "/img/authors/author" + this.counterImage + ".png");

            Logger.info("SESSION AUTHOR: " + oldAuthor);
            Logger.info("TEMP AUTHOR (to commit): " + tempAuthor);

            if (authorPassword.getText().equals(oldAuthor.getPassword())) {
                if (!(tempAuthor.getName().equals(oldAuthor.getName())
                        && tempAuthor.getId().equals(oldAuthor.getId())
                        && tempAuthor.getEmail().equals(oldAuthor.getEmail())
                        && authorNewPassword.getText().equals("")
                        && tempAuthor.getPicturePath().equals(oldAuthor.getPicturePath()))) {

                    AuthorService authorService = new AuthorService();
                    int updateResult = authorService.updateAuthor(oldAuthor, tempAuthor);

                    if (updateResult == 1) {
                        // Commit
                        this.author.setName(authorName.getText());
                        this.author.setEmail(authorEmail.getText());
                        this.author.setPassword(password);
                        this.author.setPicturePath(tempAuthor.getPicturePath());

                        Logger.info("NEW AUTHOR (commited): " + this.author);

                        // Updating GUI
                        authorNameProfile.setText(this.author.getName());
                        actorPictureProfile.setImage(ImageCache.getImageFromLocalPath(this.author.getPicturePath()));

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.initOwner(dialogPane.getScene().getWindow());
                        alert.setTitle("Update Personal Info: Done");
                        alert.setHeaderText(null);
                        alert.setContentText("Settings updated successfully!");
                        alert.setGraphic(null);
                        alert.showAndWait();
                        closeStage(event);

                    } else if (updateResult == -1) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initOwner(dialogPane.getScene().getWindow());
                        alert.setTitle("Update Personal Info: Name Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Name already exists!");
                        alert.setGraphic(null);
                        alert.showAndWait();

                        // Resetting field that caused the error
                        authorName.setText(oldAuthor.getName());

                    } else if (updateResult == -2) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initOwner(dialogPane.getScene().getWindow());
                        alert.setTitle("Update Personal Info: Email Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Email is already associate to an account!");
                        alert.setGraphic(null);
                        alert.showAndWait();

                        // Resetting field that caused the error
                        authorEmail.setText(oldAuthor.getEmail());

                    } else if (updateResult == -4) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initOwner(dialogPane.getScene().getWindow());
                        alert.setTitle("Update Personal Info: Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Error during the update operation!");
                        alert.setGraphic(null);
                        alert.showAndWait();

                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(dialogPane.getScene().getWindow());
                    alert.setTitle("Update Personal Info");
                    alert.setHeaderText(null);
                    alert.setContentText("No changes found.");
                    alert.setGraphic(null);
                    alert.showAndWait();
                    closeStage(event);
                }
            }  else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogPane.getScene().getWindow());
                alert.setTitle("Update Personal Info: Invalid Password");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect Current Password!");
                alert.setGraphic(null);
                alert.showAndWait();

                // Resetting password field
                authorPassword.setText("");
            }
        }
    }

    public void setData(Author author, Label authorNameLabel, ImageView actorPictureImage) {
        this.author = author;
        this.authorNameProfile = authorNameLabel;
        this.actorPictureProfile = actorPictureImage;
        this.counterImage = Integer.parseInt(author.getPicturePath().replaceAll("\\D+",""));

        authorName.setText(author.getName());
        authorEmail.setText(author.getEmail());
        authorPassword.setText("");
        authorNewPassword.setText("");
        imagePreview.setImage(ImageCache.getImageFromLocalPath(author.getPicturePath()));
    }

    @FXML
    void exit(ActionEvent event) {
        closeStage(event);
    }

    @FXML
    void cancel(ActionEvent event) {
        closeStage(event);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
    }
}
