package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class AuthorFollowedController {
    private Author author;

    @FXML
    private Label authorFollowed;

    @FXML
    void onMouseExited(MouseEvent event) {
        followAuthor.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 25px; -fx-border-color: #c9c9c9; -fx-border-radius: 27px;");
    }

    @FXML
    void onMouseHover(MouseEvent event) {
        followAuthor.setStyle("-fx-background-color: #eaeaea; -fx-background-radius: 25px; -fx-border-color: #c9c9c9; -fx-border-radius: 27px;");
    }

    @FXML
    private Button followAuthor;

    @FXML
    void onClick(MouseEvent event) {
        Logger.info("Clicked on follow " + this.author.getName());
    }

    public void setData(Author author) {
        this.author = author;
        authorFollowed.setText(author.getName());
    }
}
