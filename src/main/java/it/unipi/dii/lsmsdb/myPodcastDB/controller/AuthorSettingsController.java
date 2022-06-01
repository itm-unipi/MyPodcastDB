package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
    void updatePersonalInfo(ActionEvent event) {
        System.out.println("Update applied");

        Logger.info(imagePreview.getImage().getUrl());

        this.author.setName(authorName.getText());
        this.author.setEmail(authorEmail.getText());
        this.author.setPassword(authorPassword.getText());
        //this.author.setPicturePath(imagePreview.getImage().getUrl());

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
