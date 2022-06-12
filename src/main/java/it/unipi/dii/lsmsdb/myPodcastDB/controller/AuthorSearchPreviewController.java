package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
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
    void onClickAuthor(MouseEvent event) throws IOException {
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

    public void setData(Author author) {
        this.author = author;
        this.authorFound.setText(author.getName());

        Image image = ImageCache.getImageFromLocalPath(author.getPicturePath());
        this.authorPicture.setImage(image);
    }
}

