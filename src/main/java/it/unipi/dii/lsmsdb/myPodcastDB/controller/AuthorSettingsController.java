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
import javafx.scene.text.Text;
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
        alert.setTitle("Delete Account");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to delete this account?");
        alert.setGraphic(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {

            if (authorPassword.getText().equals(((Author)MyPodcastDB.getInstance().getSessionActor()).getPassword())) {
                AuthorService authorService = new AuthorService();
                authorService.deleteAccount();

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

    public void setData(Author author, Label authorNameLabel, ImageView actorPictureImage) {
        this.author = author;
        this.authorNameProfile = authorNameLabel;
        this.actorPictureProfile = actorPictureImage;
        this.counterImage = 0;

        authorName.setText(author.getName());
        authorEmail.setText(author.getEmail());
        authorPassword.setText("");
        authorNewPassword.setText("");
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
            // Old Author
            Author oldAuthor = new Author();
            oldAuthor.copy((Author) MyPodcastDB.getInstance().getSessionActor());
            Logger.info("OLD AUTHOR: " + oldAuthor);

            // Temporary Author (to commit)
            String password;
            if (authorNewPassword.getText().equals(""))
                password = oldAuthor.getPassword();
            else
                password = authorNewPassword.getText();

            Author tempAuthor = new Author(author.getId(), authorName.getText(), password, authorEmail.getText(), "/img/authors/author" + this.counterImage + ".png");
            this.author.setPicturePath("/img/authors/author" + this.counterImage + ".png");
            Logger.info("TEMPORARY AUTHOR: " + tempAuthor);

            if (authorPassword.getText().equals(oldAuthor.getPassword())) {
                if (!(tempAuthor.getName().equals(oldAuthor.getName())
                        && tempAuthor.getId().equals(oldAuthor.getId())
                        && tempAuthor.getEmail().equals(oldAuthor.getEmail())
                        && authorNewPassword.getText().equals("")
                        && tempAuthor.getPicturePath().equals(oldAuthor.getPicturePath()))) {

                    AuthorService authorService = new AuthorService();
                    int updateResult = authorService.updateAuthor(oldAuthor, tempAuthor);

                    if (updateResult == 1) {
                        // Commit update
                        this.author.setName(authorName.getText());
                        this.author.setEmail(authorEmail.getText());
                        this.author.setPassword(authorNewPassword.getText());
                        this.author.setPicturePath("/img/authors/author" + this.counterImage + ".png");

                        // Updating GUI
                        authorNameProfile.setText(((Author) MyPodcastDB.getInstance().getSessionActor()).getName());
                        actorPictureProfile.setImage(ImageCache.getImageFromLocalPath(((Author) MyPodcastDB.getInstance().getSessionActor()).getPicturePath()));

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
            }
        }
    }

    @FXML
    void exit(ActionEvent event) {
        closeStage(event);
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
