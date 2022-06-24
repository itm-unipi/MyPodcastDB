package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorProfileService;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
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
    @FXML
    private DialogPane dialogPane;

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
    private Button btnApply;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnDelete;

    @FXML
    private ImageView bin;

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
    void backAuthorPicture(MouseEvent event) {

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

    /******** BUTTONS HOVER AND MOUSE EXIT *********/

    @FXML
    void onExitedBtnApply(MouseEvent event) {
        btnApply.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: #4CAF50; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void onExitedBtnCancel(MouseEvent event) {
        btnCancel.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-border-color: #f44336; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void onExitedBtnDelete(MouseEvent event) {
        btnDelete.setStyle("-fx-background-color:  #555555; -fx-text-fill: white; -fx-border-color:  #555555; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
        bin.setStyle("-fx-blend-mode: add");
    }

    @FXML
    void onHoverBtnApply(MouseEvent event) {
        btnApply.setStyle("-fx-background-color: white; -fx-text-fill: #5c5c5c; -fx-border-color: #4CAF50; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void onHoverBtnCancel(MouseEvent event) {
        btnCancel.setStyle("-fx-background-color: white; -fx-text-fill: #5c5c5c; -fx-border-color: #f44336; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void onHoverBtnDelete(MouseEvent event) {
        btnDelete.setStyle("-fx-background-color: white; -fx-text-fill: #5c5c5c; -fx-border-color: #555555; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
        bin.setStyle("-fx-blend-mode: multiply");
    }

    /******************************/

    @FXML
    void deleteAccount(ActionEvent event) throws IOException {
        boolean result = DialogManager.getInstance().createConfirmationAlert(this.dialogPane, "Delete Account", "Do you really want to delete your account? You will lose all your podcasts.");

        if (result) {

            if (this.authorPassword.getText().equals(((Author)MyPodcastDB.getInstance().getSessionActor()).getPassword())) {
                AuthorProfileService authorProfileService = new AuthorProfileService();
                int deleteResult = authorProfileService.deleteAuthorAsAuthor(this.author);

                if (deleteResult == 0) {
                    DialogManager.getInstance().createInformationAlert(this.dialogPane, "Delete Account", "Account deleted successfully!");
                    closeStage(event);
                    StageManager.showPage(ViewNavigator.LOGIN.getPage());
                } else {
                    Logger.error("Error during the delete operation");
                    DialogManager.getInstance().createErrorAlert(this.dialogPane, "Delete Account", "Something went wrong! Please try again.");
                }
            } else {
                DialogManager.getInstance().createErrorAlert(this.dialogPane, "Delete Account - Incorrect Password", "Invalid password! Please try again.");
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
        } else if (authorName.getText().indexOf('@') > 0) {
            authorName.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            DialogManager.getInstance().createErrorAlert(dialogPane, "Update Personal Info", "Your name can't contain '@' character!");
            emptyFields = true;
        }

        if (authorEmail.getText().equals("")) {
            authorEmail.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            emptyFields = true;
        } else if (authorEmail.getText().indexOf('@') == -1) {
            authorEmail.setStyle("-fx-border-radius: 4; -fx-border-color: #ff7676");
            DialogManager.getInstance().createErrorAlert(dialogPane, "Update Personal Info", "Invalid email!");
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

            Author tempAuthor = new Author(author.getId(), authorName.getText(), password, authorEmail.getText(), "/img/authors/author" + this.counterImage + ".png");

            Logger.info("SESSION AUTHOR: " + oldAuthor);
            Logger.info("TEMP AUTHOR (to commit): " + tempAuthor);

            if (authorPassword.getText().equals(oldAuthor.getPassword())) {
                if (!(tempAuthor.getName().equals(oldAuthor.getName())
                        && tempAuthor.getId().equals(oldAuthor.getId())
                        && tempAuthor.getEmail().equals(oldAuthor.getEmail())
                        && authorNewPassword.getText().equals("")
                        && tempAuthor.getPicturePath().equals(oldAuthor.getPicturePath()))) {

                    AuthorProfileService authorProfileService = new AuthorProfileService();
                    int updateResult = authorProfileService.updateAuthorAsAuthor(oldAuthor, tempAuthor);

                    if (updateResult == 0) {
                        // Commit
                        this.author.setName(authorName.getText());
                        this.author.setEmail(authorEmail.getText());
                        this.author.setPassword(password);
                        this.author.setPicturePath(tempAuthor.getPicturePath());
                        MyPodcastDB.getInstance().setSession(this.author, "Author");

                        Logger.success("Author updated (and commited): " + this.author);

                        // Updating GUI
                        authorNameProfile.setText(this.author.getName());
                        actorPictureProfile.setImage(ImageCache.getImageFromLocalPath(this.author.getPicturePath()));
                        // Updating the stage object identifier to avoid unexpected errors
                        StageManager.setObjectIdentifier(authorName.getText());
                        DialogManager.getInstance().createInformationAlert(dialogPane, "Update Personal Info", "Personal info updated successfully!");
                        authorPassword.setText("");
                    } else if (updateResult == -1) {
                        // Resetting field that caused the error
                        authorName.setText(oldAuthor.getName());
                        DialogManager.getInstance().createErrorAlert(dialogPane, "Update Personal Info - Name Error", "This author name already exists!");
                    } else if (updateResult == -2) {
                        // Resetting field that caused the error
                        authorEmail.setText(oldAuthor.getEmail());
                        DialogManager.getInstance().createErrorAlert(dialogPane, "Update Personal Info - Email Error", "This email is already associated to an account!");
                    } else if (updateResult == -3) {
                        // Resetting field that caused the error
                        authorName.setText(oldAuthor.getName());
                        authorEmail.setText(oldAuthor.getEmail());
                        DialogManager.getInstance().createErrorAlert(dialogPane, "Update Personal Info - Error", "Name and email already associated to an account!");
                    } else if (updateResult <= -4) {
                        DialogManager.getInstance().createErrorAlert(dialogPane, "Update Personal Info - Error", "Something went wrong! Please try again.");
                    }

                } else {
                    DialogManager.getInstance().createInformationAlert(dialogPane, "Update Personal Info", "No changes found.");
                    closeStage(event);
                }
            }  else {
                // Resetting password field
                authorPassword.setText("");
                DialogManager.getInstance().createErrorAlert(dialogPane, "Update Personal Info - Incorrect Password", "Invalid password! Please try again.");
            }
        }
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

    void setData(Author author, Label authorNameLabel, ImageView actorPictureImage) {
        this.author = author;
        this.authorNameProfile = authorNameLabel;
        this.actorPictureProfile = actorPictureImage;
        this.counterImage = Integer.parseInt(author.getPicturePath().replaceAll("\\D+",""));

        authorName.setText(author.getName());
        authorEmail.setText(author.getEmail());
        authorPassword.setText("");
        authorNewPassword.setText("");
        imagePreview.setImage(ImageCache.getImageFromLocalPath(author.getPicturePath()));
        btnApply.requestFocus();
    }

}
