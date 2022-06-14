package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.FollowedAuthorCache;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.SearchService;
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
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AuthorSearchPreviewController {
    private Author author;

    private boolean isFollowing;

    private String actorType;

    @FXML
    private Label authorFound;

    @FXML
    private ImageView authorPicture;

    @FXML
    private HBox boxAuthorPreview;

    @FXML
    private Button btnFollowAuthor;

    @FXML
    private VBox boxFollowAuthor;

    @FXML
    void onClickAuthor(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), this.author.getName());
    }

    @FXML
    void onClickBtnFollowAuthor(MouseEvent event) {
        SearchService searchService = new SearchService();
        if (this.actorType.equals("Author")) {
            if (!this.isFollowing) {
                this.isFollowing = searchService.followAuthorAsAuthor(this.author);
                this.btnFollowAuthor.setText("Unfollow");
            } else {
                this.isFollowing = !searchService.unfollowAuthorAsAuthor(this.author);
                this.btnFollowAuthor.setText("Follow");
            }
        } else if (this.actorType.equals("User")) {

            if (!this.isFollowing) {
                this.isFollowing = searchService.followAuthorAsUser(this.author);
                this.btnFollowAuthor.setText("Unfollow");
            } else {
                this.isFollowing = !searchService.unfollowAuthorAsUser(this.author);
                this.btnFollowAuthor.setText("Follow");
            }
        } else {
            Logger.error("Operation not allowed!");
        }
    }

    @FXML
    void onHoverBtnFollowAuthor(MouseEvent event) {
        btnFollowAuthor.setStyle("-fx-pref-width: 85; -fx-background-color: #DA70D6; -fx-border-color: #DA70D6; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
    }

    @FXML
    void onExitedBtnFollowAuthor(MouseEvent event) {
        btnFollowAuthor.setStyle("-fx-pref-width: 85; -fx-background-color: #db55e7; -fx-border-color: #db55e7; -fx-background-insets: 0; -fx-background-radius: 4; -fx-border-radius: 4");
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
        this.actorType = MyPodcastDB.getInstance().getSessionType();

        Image image = ImageCache.getImageFromLocalPath(author.getPicturePath());
        this.authorPicture.setImage(image);

        if (actorType.equals("Admin")
                || actorType.equals("Unregistered")
                || (actorType.equals("Author") && this.author.getName().equals(((Author)MyPodcastDB.getInstance().getSessionActor()).getName())))
            this.boxFollowAuthor.setVisible(false);

        if (this.boxFollowAuthor.isVisible()) {
            this.isFollowing = (FollowedAuthorCache.getAuthor(author.getName()) != null);
            if (this.isFollowing)
                this.btnFollowAuthor.setText("Unfollow");
        }
    }
}

