package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AuthorPreviewController {
    private Author author;

    @FXML
    private ImageView authorPicture;

    @FXML
    private VBox boxAuthorImage;

    @FXML
    private Button followAuthor;

    @FXML
    private Label nameAuthorFollowed;

    @FXML
    void changeBackgroundAuthor(MouseEvent event) {
        this.boxAuthorImage.setStyle("-fx-background-color: #dbe9fc; -fx-background-radius: 50px;");
    }

    @FXML
    void restoreBackgroundAuthor(MouseEvent event) {
        this.boxAuthorImage.setStyle("-fx-background-color: white; -fx-background-radius: 50px;");
    }

    @FXML
    void onClickAuthorName(MouseEvent event) throws IOException {
        Logger.info("Clicked on Author name: " + this.author.getName());
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.author.getName());
    }

    @FXML
    void onClickAuthorProfile(MouseEvent event) throws IOException {
        Logger.info("Clicked on Author Picture: " + this.author.getName());
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.author.getName());
    }

    @FXML
    void onClickFollowAuthor(MouseEvent event) {
        Logger.info("Clicked on Follow " + this.author.getName());
        // TODO: change button text from Follow to Unfollow and viceversa
    }

    @FXML
    void onMouseExitedFollowAuthor(MouseEvent event) {
        followAuthor.setStyle("-fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #c9c9c9; -fx-border-radius: 27px;");
    }

    @FXML
    void onMouseHoverFollowAuthor(MouseEvent event) {
        followAuthor.setStyle("-fx-background-color: #eaeaea; -fx-background-radius: 25px; -fx-border-color: #c9c9c9; -fx-border-radius: 27px;");
    }

    public void setData(Author author) {
        this.author = author;
        nameAuthorFollowed.setText(author.getName());

        Image image = ImageCache.getImageFromLocalPath(author.getPicturePath());
        authorPicture.setImage(image);
    }
}
