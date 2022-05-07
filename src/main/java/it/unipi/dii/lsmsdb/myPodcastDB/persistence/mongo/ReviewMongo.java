package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;

import java.util.List;

public class ReviewMongo {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addReview(Review review) {
        return false;
    }

    // ---------- READ ---------- //

    public Review findReviewById(String id) {
        return null;
    }

    public List<Review> findReviewsByPodcastId(String podcastId, int limit, String attributeToOrder, boolean ascending) {
        return null;
    }

    public List<Review> findReviewsByAuthorUsername(String authorUsername, int limit, String attributeToOrder, boolean ascending) {
        return null;
    }

    // --------- UPDATE --------- //

    public boolean updateReview(Review review) {
        return false;
    }

    // --------- DELETE --------- //

    public boolean deleteReviewById(String reviewId) {
        return false;
    }

    public int deleteReviewsByPodcastId(String podcastId) {
        return -1;
    }

    public int deleteReviewsByAuthorUsername(String authorUsername) {
        return -1;
    }

    // ---------------------------------------------------------------------------------- //
}
