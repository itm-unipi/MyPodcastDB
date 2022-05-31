package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
        authorConfirmPassword.setText("");
        imagePreview.setImage(ImageCache.getImageFromLocalPath(author.getPicturePath()));
    }
}
