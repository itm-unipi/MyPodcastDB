package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorProfileService;
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

    private final String actorType;

    @FXML
    private Label authorFound;

    @FXML
    private ImageView authorPicture;

    @FXML
    private HBox boxAuthorPreview;

    @FXML
    private Button btnFollowAuthor;

    public AuthorSearchPreviewController() {
        this.actorType = MyPodcastDB.getInstance().getSessionType();
    }

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

    /****** Events on follow button ******/
    @FXML
    void onClickBtnFollowAuthor(MouseEvent event) {
        AuthorProfileService authorProfileService = new AuthorProfileService();

        if (this.actorType.equals("Author")) {
            if (btnFollowAuthor.getText().equals("Follow")) {
                authorProfileService.followAuthorAsAuthor(author.getName());
                btnFollowAuthor.setText("Unfollow");
            } else {
                authorProfileService.unfollowAuthorAsAuthor(author.getName());
                btnFollowAuthor.setText("Follow");
            }
        } else if (this.actorType.equals("User")) {
            if (btnFollowAuthor.getText().equals("Follow")) {
                authorProfileService.followAuthorAsUser(author.getName());
                btnFollowAuthor.setText("Unfollow");
            } else {
                authorProfileService.unfollowAuthorAsUser(author.getName());
                btnFollowAuthor.setText("Follow");
            }

        } else {
            Logger.error("Operation not allowed!");
        }
    }
    @FXML
    void onMouseExitedFollowButton(MouseEvent event) {
        this.btnFollowAuthor.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #c9c9c9; -fx-border-radius: 10; -fx-background-insets: 0");
    }

    @FXML
    void onMouseHoverFollowButton(MouseEvent event) {
        btnFollowAuthor.setStyle("-fx-background-color: #eaeaea; -fx-background-radius: 10; -fx-border-color: #c9c9c9; -fx-border-radius: 10; -fx-background-insets: 0;");
    }

    public void setData(Author author, boolean follow) {
        this.author = author;
        this.authorFound.setText(author.getName());

        Image image = ImageCache.getImageFromLocalPath(author.getPicturePath());
        this.authorPicture.setImage(image);

        // Disabling follow button for unregistered users, admin and if author founds himself
        if (this.actorType.equals("Admin") || this.actorType.equals("Unregistered")
                || (this.actorType.equals("Author") && ((Author) MyPodcastDB.getInstance().getSessionActor()).getName().equals(this.author.getName()))) {
            this.btnFollowAuthor.setVisible(false);
        } else {
            if (follow)
                this.btnFollowAuthor.setText("Unfollow");
        }
    }
}

