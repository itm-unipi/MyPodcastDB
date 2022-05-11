package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.MongoCursor;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

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
                .append("createdAt", review.getCreatedAtAsString())
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
                String strCreatedAt = review.getString("createdAt").replace("T", " "). replace("Z", "");
                Date createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strCreatedAt);
                String authorName = review.getString("authorName");

                Review newReview = new Review(id, podcastId, authorName, title, content, rating, createdAt);
                return newReview;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public List<Review> findReviewsByPodcastId(String podcastId, int limit, String attributeToOrder, boolean ascending) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("review").find(eq("podcastId", new ObjectId(podcastId))).limit(limit).iterator()) {
            List<Review> reviews = new ArrayList<>();

            while (cursor.hasNext()) {
                Document review = cursor.next();

                // review attributes
                String id = review.getObjectId("_id").toString();
                String title = review.getString("title");
                String content = review.getString("content");
                int rating = review.getInteger("rating");
                String strCreatedAt = review.getString("createdAt").replace("T", " "). replace("Z", "");
                Date createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strCreatedAt);
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

    public List<Review> findReviewsByAuthorUsername(String authorUsername, int limit, String attributeToOrder, boolean ascending) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("review").find(eq("authorUsername", authorUsername)).limit(limit).iterator()) {
            List<Review> reviews = new ArrayList<>();

            while (cursor.hasNext()) {
                Document review = cursor.next();

                // review attributes
                String id = review.getObjectId("_id").toString();
                String podcastId = review.getObjectId("podcastId").toString();
                String title = review.getString("title");
                String content = review.getString("content");
                int rating = review.getInteger("rating");
                String strCreatedAt = review.getString("createdAt").replace("T", " "). replace("Z", "");
                Date createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strCreatedAt);

                Review newReview = new Review(id, podcastId, authorUsername, title, content, rating, createdAt);
                reviews.add(newReview);
            }

            /* / order by rating
            if (attributeToOrder.equals("rating")) {
                if (ascending) {
                    reviews.sort((Review r1, Review r2) -> {
                        if (r1.getRating() > r2.getRating())
                            return -1;
                        else if (r1.getRating() == r2.getRating())
                            return 0;
                        return 1;
                    });
                } else {
                    reviews.sort((Review r1, Review r2) -> {
                        if (r1.getRating() < r2.getRating())
                            return -1;
                        else if (r1.getRating() == r2.getRating())
                            return 0;
                        return 1;
                    });
                }
            } else if (attributeToOrder.equals("createdAt")) {
                if (ascending)
                    reviews.sort((Review r1, Review r2) -> r1.getCreatedAt().compareTo(r2.getCreatedAt()));
                else
                    reviews.sort((Review r1, Review r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()));
            }*/

            return reviews;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
