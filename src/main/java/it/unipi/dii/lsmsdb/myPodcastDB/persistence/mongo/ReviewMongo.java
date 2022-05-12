package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;


import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.combine;

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
        MongoManager manger = MongoManager.getInstance();

        try {
            Bson filter = eq("_id", new ObjectId(review.getId()));
            Bson update = combine(set("podcastId", new ObjectId(review.getPodcastId())),
                    set("authorUsername", review.getAuthorUsername()),
                    set("title", review.getTitle()),
                    set("content", review.getContent()),
                    set("rating", review.getRating()),
                    set("createdAt", review.getCreatedAtAsString())
            );

            UpdateResult result = manger.getCollection("review").updateOne(filter, update);
            return result.getMatchedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------- DELETE --------- //

    public boolean deleteReviewById(String reviewId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            DeleteResult result = manager.getCollection("review").deleteOne(eq("_id", new ObjectId(reviewId)));
            return result.getDeletedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int deleteReviewsByPodcastId(String podcastId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("podcastId", new ObjectId(podcastId));
            DeleteResult result = manager.getCollection("review").deleteMany(filter);

            return (int)result.getDeletedCount();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int deleteReviewsByAuthorUsername(String authorUsername) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("authorUsername", authorUsername);
            DeleteResult result = manager.getCollection("review").deleteMany(filter);

            return (int)result.getDeletedCount();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // ---------------------------------------------------------------------------------- //
}
