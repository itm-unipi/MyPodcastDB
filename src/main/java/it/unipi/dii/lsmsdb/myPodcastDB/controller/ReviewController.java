package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ReviewController {

    @FXML
    private Label authorNameAndDate;

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
        Image star = new Image(getClass().getResourceAsStream("/img/star.png"));
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
        this.authorNameAndDate.setText(review.getAuthorUsername() + ", " + review.getCreatedAtAsString().replace("T", " ").replace("Z", ""));
        this.title.setText(review.getTitle());
        this.text.setText(review.getContent());
    }

}
