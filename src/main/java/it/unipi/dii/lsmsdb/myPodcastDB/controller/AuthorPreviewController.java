package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.AuthorPreview;
import it.unipi.dii.lsmsdb.myPodcastDB.model.PodcastPreview;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class AuthorPreviewController {

    private AuthorPreview authorPreview;

    @FXML
    private VBox authorContainer;

    @FXML
    private ImageView authorImage;

    @FXML
    private Tooltip authorToolTip;

    @FXML
    private Label authorName;

    public void setData(AuthorPreview author) {
        this.authorPreview = author;

        Image image = new Image(author.getImage());
        this.authorImage.setImage(image);
        this.authorName.setText(author.getName());
        this.authorToolTip.setText(author.getName());
    }

    @FXML
    void authorIn(MouseEvent event) {
        authorContainer.setStyle("-fx-background-color: f2f2f2; -fx-background-radius: 10;");
        authorName.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");
    }

    @FXML
    void authorOut(MouseEvent event) {
        authorContainer.setStyle("-fx-background-color: white");
        authorName.setStyle("-fx-font-size: 10; -fx-font-weight: bold");
    }

    @FXML
    void onClick(MouseEvent event) {
        Logger.info(authorPreview.getId() + " : " + this.authorPreview.getName());
    }

}
