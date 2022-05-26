package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class AuthorPreviewController {
    private Author author;

    @FXML
    private Label authorFollowed;

    @FXML
    void onMouseExited(MouseEvent event) {
        followAuthor.setStyle("-fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #c9c9c9; -fx-border-radius: 27px;");
    }

    @FXML
    void onMouseHover(MouseEvent event) {
        followAuthor.setStyle("-fx-background-color: #eaeaea; -fx-background-radius: 25px; -fx-border-color: #c9c9c9; -fx-border-radius: 27px;");
    }

    @FXML
    private Button followAuthor;

    @FXML
    private VBox boxAuthorProfile;

    @FXML
    void changeBackgroundAuthor(MouseEvent event) {
        this.boxAuthorProfile.setStyle("-fx-background-color: #dbe9fc; -fx-background-radius: 50px;");
    }

    @FXML
    void restoreBackgroundAuthor(MouseEvent event) {
        this.boxAuthorProfile.setStyle("-fx-background-color: white; -fx-background-radius: 50px;");
    }

    @FXML
    void onClick(MouseEvent event) {
        Logger.info("Clicked on follow " + this.author.getName());
    }

    public void setData(Author author) {
        this.author = author;
        authorFollowed.setText(author.getName());
    }
}
