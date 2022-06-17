package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.ReviewPageService;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private ReviewPageService service;
    private ReviewPageController controller;

    @FXML
    void clickOnDeleteReview(MouseEvent event) throws IOException {
        // create the blur
        BoxBlur blur = new BoxBlur(3, 3 , 3);
        this.mainPage.setEffect(blur);

        // create the dialog
        boolean confirm = DialogManager.getInstance().createConfirmationAlert(this.mainPage, "Do you really want to delete this review?");
        if (confirm) {
            // delete review
            int result = this.service.deleteReview(this.review);

            // check the status and update the page
            if (result == 0) {
                this.controller.removeReviewFromLocalList(this.review);
            } else {
                DialogManager.getInstance().createErrorAlert(this.mainPage, "Failed to delete review");
            }
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
        this.authorName.setTextFill(Paint.valueOf("CornflowerBlue"));
    }

    public void setData(Review review, BorderPane mainPage, ReviewPageService service, ReviewPageController controller) {
        this.review = review;
        this.mainPage = mainPage;
        this.service = service;
        this.controller = controller;

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
        this.reviewDate.setText(dateAsString(review.getCreatedAt()));
        this.title.setText(review.getTitle());
        this.text.setText(review.getContent());

        // actor recognition
        String sessionType = MyPodcastDB.getInstance().getSessionType();
        if ((sessionType.equals("User") && ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername().equals(this.review.getAuthorUsername())) || sessionType.equals("Admin"))
            this.deleteReview.setVisible(true);
        else
            this.deleteReview.setVisible(false);

        // if is the owner hide the author name
        if (sessionType.equals("User") && ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername().equals(this.review.getAuthorUsername())) {
            this.authorNameWrapper.setVisible(false);
            this.authorNameWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");
        } else {
            this.yourNameWrapper.setVisible(false);
            this.yourNameWrapper.setStyle("-fx-min-width: 0; -fx-pref-width: 0; -fx-max-width: 0; -fx-min-height: 0; -fx-pref-height: 0; -fx-max-height: 0; -fx-padding: 0; -fx-margin: 0;");

            // unregistared can't go to user page
            if (sessionType.equals("Unregistered")) {
                this.authorName.setDisable(true);
                this.authorName.setTextFill(Paint.valueOf("Black"));
            }
        }

        // if the author was removed disable the name
        if (this.authorName.equals("Removed account")) {
            this.authorName.setText("Removed user");
            this.authorName.setDisable(true);
        }
    }

    private String dateAsString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateAsString = dateFormat.format(date);
        return dateAsString;
    }
}
