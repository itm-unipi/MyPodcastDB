package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;

public class ReviewController {

    @FXML
    private Label authorName;

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

    private Review review;

    public void setData(Review review) {
        this.review = review;

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
    }

    @FXML
    void clickOnReviewAuthor(MouseEvent event) throws IOException {
        Logger.info("Click on Review Author : " + this.review.getAuthorUsername());
        StageManager.showPage(ViewNavigator.USERPAGE.getPage(), this.review.getAuthorUsername());
    }

    @FXML
    void mouseOnReviewAuthor(MouseEvent event) {
        this.authorName.setTextFill(Color.color(0.388, 0.388, 0.4));
    }

    @FXML
    void mouseOutReviewAuthor(MouseEvent event) {
        this.authorName.setTextFill(Color.color(0.0, 0.0, 1.0));
    }
}
