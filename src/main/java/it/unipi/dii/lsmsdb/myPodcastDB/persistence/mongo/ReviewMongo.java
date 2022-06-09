package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewMongo {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addReview(Review review) {
        MongoManager manager = MongoManager.getInstance();

        Document newReview = new Document()
                .append("podcastId", new ObjectId(review.getPodcastId()))
                .append("title", review.getTitle())
                .append("content", review.getContent())
                .append("rating", review.getRating())
                .append("createdAt", review.getCreatedAt())
                .append("authorUsername", review.getAuthorUsername());

        try {
            manager.getCollection("review").insertOne(newReview);
            review.setId(newReview.getObjectId("_id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // ---------- READ ---------- //

    public Review findReviewById(String id) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("review").find(eq("_id", new ObjectId(id))).iterator()) {
            if (cursor.hasNext()) {
                Document review = cursor.next();

                // review attributes
                String podcastId = review.getObjectId("podcastId").toString();
                String title = review.getString("title");
                String content = review.getString("content");
                int rating = review.getInteger("rating");
                Date createdAt = review.getDate("createdAt");
                String authorName = review.getString("authorUsername");

                Review newReview = new Review(id, podcastId, authorName, title, content, rating, createdAt);
                return newReview;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public List<Review> findReviewsByPodcastId(String podcastId, int skip, int limit, String attributeToOrder, boolean ascending) {
        MongoManager manager = MongoManager.getInstance();

        try {
            MongoCursor<Document> cursor;

            // order by attribute specified
            if (attributeToOrder.equals("createdAt")) {
                if (ascending) {
                    cursor = manager.getCollection("review").find(eq("podcastId", new ObjectId(podcastId)))
                            .sort(ascending("createdAt"))
                            .skip(skip).limit(limit).iterator();
                } else {
                    cursor = manager.getCollection("review").find(eq("podcastId", new ObjectId(podcastId)))
                            .sort(descending("createdAt"))
                            .skip(skip).limit(limit).iterator();
                }
            } else if (attributeToOrder.equals("rating")) {
                if (ascending) {
                    cursor = manager.getCollection("review").find(eq("podcastId", new ObjectId(podcastId)))
                            .sort(ascending("rating"))
                            .skip(skip).limit(limit).iterator();
                } else {
                    cursor = manager.getCollection("review").find(eq("podcastId", new ObjectId(podcastId)))
                            .sort(descending("rating"))
                            .skip(skip).limit(limit).iterator();
                }
            } else {
                cursor = manager.getCollection("review").find(eq("podcastId", new ObjectId(podcastId))).limit(limit).iterator();
            }

            List<Review> reviews = new ArrayList<>();

            while (cursor.hasNext()) {
                Document review = cursor.next();

                // review attributes
                String id = review.getObjectId("_id").toString();
                String title = review.getString("title");
                String content = review.getString("content");
                int rating = review.getInteger("rating");
                Date createdAt = review.getDate("createdAt");
                String authorUsername = review.getString("authorUsername");

                Review newReview = new Review(id, podcastId, authorUsername, title, content, rating, createdAt);
                reviews.add(newReview);
            }

            return reviews;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Review findSpecificReviewByAuthorName(String podcastId, String authorUsername) {
        MongoManager manager = MongoManager.getInstance();

        Bson filter = and(eq("podcastId", new ObjectId(podcastId)), eq("authorUsername", authorUsername));
        try (MongoCursor<Document> cursor = manager.getCollection("review").find(filter).iterator()) {
            if (cursor.hasNext()) {
                Document review = cursor.next();

                // review attributes
                String id = review.getObjectId("_id").toString();
                String title = review.getString("title");
                String content = review.getString("content");
                int rating = review.getInteger("rating");
                Date createdAt = review.getDate("createdAt");

                Review newReview = new Review(id, podcastId, authorUsername, title, content, rating, createdAt);
                return newReview;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public List<Review> findReviewsByAuthorUsername(String authorUsername, int limit, String attributeToOrder, boolean ascending) {
        MongoManager manager = MongoManager.getInstance();

        try {
            MongoCursor<Document> cursor;

            // order by attribute specified
            if (attributeToOrder.equals("createdAt")) {
                if (ascending) {
                    cursor = manager.getCollection("review").find(eq("authorUsername", authorUsername))
                            .sort(ascending("createdAt"))
                            .limit(limit).iterator();
                } else {
                    cursor = manager.getCollection("review").find(eq("authorUsername", authorUsername))
                            .sort(descending("createdAt"))
                            .limit(limit).iterator();
                }
            } else if (attributeToOrder.equals("rating")) {
                if (ascending) {
                    cursor = manager.getCollection("review").find(eq("authorUsername", authorUsername))
                            .sort(ascending("rating"))
                            .limit(limit).iterator();
                } else {
                    cursor = manager.getCollection("review").find(eq("authorUsername", authorUsername))
                            .sort(descending("rating"))
                            .limit(limit).iterator();
                }
            } else {
                cursor = manager.getCollection("review").find(eq("authorUsername", authorUsername)).limit(limit).iterator();
            }

            List<Review> reviews = new ArrayList<>();

            while (cursor.hasNext()) {
                Document review = cursor.next();

                // review attributes
                String id = review.getObjectId("_id").toString();
                String podcastId = review.getObjectId("podcastId").toString();
                String title = review.getString("title");
                String content = review.getString("content");
                int rating = review.getInteger("rating");
                Date createdAt = review.getDate("createdAt");

                Review newReview = new Review(id, podcastId, authorUsername, title, content, rating, createdAt);
                reviews.add(newReview);
            }

            return reviews;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
                    set("createdAt", review.getCreatedAt())
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
