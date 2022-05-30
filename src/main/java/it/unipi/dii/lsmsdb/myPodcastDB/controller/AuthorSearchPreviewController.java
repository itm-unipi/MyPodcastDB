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
import javafx.scene.layout.HBox;

import java.io.IOException;

public class AuthorSearchPreviewController {
    private Author author;

    @FXML
    private Label authorFound;

    @FXML
    private ImageView authorPicture;

    @FXML
    private HBox boxAuthorPreview;

    @FXML
    private Button followAuthor;

    @FXML
    void onClickAuthor(MouseEvent event) throws IOException {
        Logger.info("Clicked on author " + this.author.getName());
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.author.getName());
    }

    @FXML
    void changeBackgroundAuthor(MouseEvent event) {
        this.boxAuthorPreview.setStyle("-fx-background-color: #eeeeee;");
    }

    @FXML
    void restoreBackgroundAuthor(MouseEvent event) {
        this.boxAuthorPreview.setStyle("-fx-background-color: transparent;");
    }

    /****** Events on follow button ******/
    @FXML
    void onClickFollowButton(MouseEvent event) {
        Logger.info("Clicked on Follow " + this.author.getName());
    }
    @FXML
    void onMouseExitedFollowButton(MouseEvent event) {
        followAuthor.setStyle("-fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #c9c9c9; -fx-border-radius: 27px;");
    }

    @FXML
    void onMouseHoverFollowButton(MouseEvent event) {
        followAuthor.setStyle("-fx-background-color: #eaeaea; -fx-background-radius: 25px; -fx-border-color: #c9c9c9; -fx-border-radius: 27px;");
    }

    public void setData(Author author) {
        this.author = author;
        authorFound.setText(author.getName());

        Image image = ImageCache.getImageFromLocalPath(author.getPicturePath());
        authorPicture.setImage(image);
    }
}

