package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;

public class ReviewController {

    @FXML
    private Button deleteReview;

    @FXML
    private Label authorName;

    @FXML
    private VBox authorNameWrapper;

    @FXML
    private Label reviewDate;

    @FXML
    private ImageView star1;

    @FXML
    private ImageView star2;

    @FXML
    private ImageView star3;

    @FXML
    private ImageView star4;

    @FXML
    private ImageView star5;

    @FXML
    private Text text;

    @FXML
    private Label title;

    @FXML
    private VBox yourNameWrapper;

    @FXML
    private Label yourReview;

    private Review review;
    private BorderPane mainPage;

    @FXML
    void clickOnDeleteReview(MouseEvent event) {
        // create the alert
        BoxBlur blur = new BoxBlur(3, 3 , 3);
        this.mainPage.setEffect(blur);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(this.mainPage.getScene().getWindow());
        alert.setTitle("Delete Podcast");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to delete this podcast?");
        alert.setGraphic(null);
        alert.showAndWait();

        // button handling
        if (alert.getResult() == ButtonType.OK) {
            Logger.info("Si");
        }

        this.mainPage.setEffect(null);
    }

    @FXML
    void clickOnReviewAuthor(MouseEvent event) throws IOException {
        Logger.info("Click on Review Author : " + this.review.getAuthorUsername());
        StageManager.showPage(ViewNavigator.USERPAGE.getPage(), this.review.getAuthorUsername());
    }

    @FXML
    void mouseOnReviewAuthor(MouseEvent event) {
        this.authorName.setCursor(Cursor.HAND);
        this.authorName.setTextFill(Color.color(0.388, 0.388, 0.4));
    }

    @FXML
    void mouseOutReviewAuthor(MouseEvent event) {
        this.authorName.setCursor(Cursor.DEFAULT);
        this.authorName.setTextFill(Color.color(0.0, 0.0, 1.0));
    }

    public void setData(Review review, BorderPane mainPage) {
        this.review = review;
        this.mainPage = mainPage;

        // review fields
        int rating = review.getRating();
        Image star = ImageCache.getImageFromLocalPath("/img/star.png");
        if (rating >= 1)
            this.star1.setImage(star);
        if (rating >= 2)
            this.star2.setImage(star);
        if (rating >= 3)
            this.star3.setImage(star);
        if (rating >= 4)
            this.star4.setImage(star);
        if (rating >= 5)
            this.star5.setImage(star);
        this.authorName.setText(review.getAuthorUsername());
        this.reviewDate.setText(review.getCreatedAtAsString().replace("T", " ").replace("Z", ""));
        this.title.setText(review.getTitle());
        this.text.setText(review.getContent());

        // actor recognition
        String sessionType = MyPodcastDB.getInstance().getSessionType();
        if (!sessionType.equals("Admin"))
            this.deleteReview.setVisible(false);

        // if is the owner hide the author name
        if (sessionType.equals("User") && ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername().equals(this.review.getAuthorUsername())) {
            this.authorNameWrapper.setVisible(false);
            this.authorNameWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");
        } else {
            this.yourNameWrapper.setVisible(false);
            this.yourNameWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");
        }
    }
}
