package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorService;
import it.unipi.dii.lsmsdb.myPodcastDB.service.UserService;
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

    private boolean followed;

    @FXML
    private Label authorFound;

    @FXML
    private ImageView authorPicture;

    @FXML
    private HBox boxAuthorPreview;

    @FXML
    private Button btnFollowAuthor;

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
    void onClickBtnFollowAuthor(MouseEvent event) {
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author")) {
            AuthorService authorService = new AuthorService();

            if (btnFollowAuthor.getText().equals("Follow")) {
                authorService.followAuthor(author.getName());
                btnFollowAuthor.setText("Unfollow");
            } else {
                authorService.unfollowAuthor(author.getName());
                btnFollowAuthor.setText("Follow");
            }
        } else if (actorType.equals("User")) {
            UserService userService = new UserService();

            if (btnFollowAuthor.getText().equals("Follow")) {
                userService.followAuthor(author.getName());
                btnFollowAuthor.setText("Unfollow");
            } else {
                userService.unfollowAuthor(author.getName());
                btnFollowAuthor.setText("Follow");
            }

        } else {
            Logger.error("Operation not allowed!");
        }
    }
    @FXML
    void onMouseExitedFollowButton(MouseEvent event) {
        this.btnFollowAuthor.setStyle("-fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #c9c9c9; -fx-border-radius: 27px;");
    }

    @FXML
    void onMouseHoverFollowButton(MouseEvent event) {
        btnFollowAuthor.setStyle("-fx-background-color: #eaeaea; -fx-background-radius: 25px; -fx-border-color: #c9c9c9; -fx-border-radius: 27px;");
    }

    public void setData(Author author, boolean follow) {
        this.author = author;
        this.authorFound.setText(author.getName());

        Image image = ImageCache.getImageFromLocalPath(author.getPicturePath());
        this.authorPicture.setImage(image);

        // Disabling follow button for unregistered users, admin and if author founds himself
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Admin") || actorType.equals("Unregistered")
                || (actorType.equals("Author") && ((Author) MyPodcastDB.getInstance().getSessionActor()).getName().equals(author.getName()))) {
            this.btnFollowAuthor.setVisible(false);
        } else {
            this.followed = follow;
            if (followed)
                this.btnFollowAuthor.setText("Unfollow");
        }
    }
}

