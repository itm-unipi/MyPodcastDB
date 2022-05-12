package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class PodcastMongo {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addPodcast(Podcast podcast) {
        // podcast.setId(risultato.getObjectId("_id").toString());
        return false;
    }

    // ---------- READ ---------- //

    public Podcast findPodcastById(String id) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("podcast").find(eq("_id", new ObjectId(id))).iterator()) {
            if (cursor.hasNext()) {
                Document podcast = cursor.next();

                // podcast attributes
                String podcastId = id;
                String name = podcast.getString("podcastName");
                String authorId = podcast.getObjectId("authorId").toString();
                String authorName = podcast.getString("authorName");
                String artworkUrl60 = podcast.getString("artworkUrl60");
                String artworkUrl600 = podcast.getString("artworkUrl600");
                String contentAdvisoryRating = podcast.getString("contentAdvisoryRating");
                String country = podcast.getString("country");
                String primaryCategory = podcast.getString("primaryCategory");
                List<String> categories = podcast.getList("categories", String.class);
                String date = podcast.getString("releaseDate").replace("T", " "). replace("Z", "");
                Date releaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                Podcast newPodcast = new Podcast(podcastId, name, authorId, authorName, artworkUrl60, artworkUrl600, contentAdvisoryRating, country, primaryCategory, categories, releaseDate);

                // episodes
                List<Document> episodes = podcast.getList("episodes", Document.class);
                for (Document episode : episodes) {
                    String episodeName = episode.getString("episodeName");
                    String episodeDescription = episode.getString("episodeDescription");
                    String epDate = episode.getString("episodeReleaseDate").replace("T", " "). replace("Z", "");
                    Date episodeReleaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(epDate);
                    int episodeTimeMillis = episode.getInteger("episodeTimeMillis");

                    newPodcast.addEpisode(episodeName, episodeDescription, episodeReleaseDate, episodeTimeMillis);
                }

                // reviews
                List<Document> reviews = podcast.getList("reviews", Document.class);
                for (Document review : reviews) {
                    String reviewId = review.getObjectId("reviewId").toString();
                    int rating = review.getInteger("rating");

                    newPodcast.addReview(reviewId, rating);
                }

                return newPodcast;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public List<Podcast> findPodcastsByName(String name) {
        return null;
    }

    public List<Podcast> findPodcastsByAuthorId(String authorId) {
        return null;
    }

    public List<Podcast> findPodcastsByAuthorName(String authorName) {
        return null;
    }

    public List<Podcast> findPodcastsByPrimaryCategory(String primaryCategory) {
        return null;
    }

    public List<Podcast> findPodcastsByCategory(String category) {
        return null;
    }

    // --------- UPDATE --------- //

    public boolean updatePodcast(Podcast podcast) {
        return false;
    }

    public boolean addEpisodeToPodcast(String podcastId, String episodeName, String episodeDescription, Date episodeReleaseDate, int episodeTimeMillis) {
        return false;
    }

    public boolean addReviewToPodcast(String podcastId, String reviewId, int rating) {
        return false;
    }

    // --------- DELETE --------- //

    public boolean deletePodcastById(String id) {
        return false;
    }

    public int deletePodcastsByName(String name) {
        return -1;
    }

    public int deletePodcastsByAuthorId(String authorId) {
        return -1;
    }

    public int deletePodcastsByAuthorName(String authorName) {
        return -1;
    }

    public boolean deleteEpisodeOfPodcast(String podcastId, String episodeName) {
        return false;
    }

    public boolean deleteAllEpisodesOfPodcast(String podcastId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            // remove the review from reviews field
            Bson filter = eq("_id", new ObjectId(podcastId));
            Bson update = set("episodes", new ArrayList<>());
            UpdateResult result = manager.getCollection("podcast").updateMany(filter, update);

            // check the number of modified field
            if (result.getModifiedCount() == 1)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteReviewOfPodcast(String podcastId, String reviewId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            // remove the review from reviews field
            Bson filter = eq("_id", new ObjectId(podcastId));
            Bson update = pull("reviews", new Document("reviewId", new ObjectId(reviewId)));
            UpdateResult result = manager.getCollection("podcast").updateMany(filter, update);

            // check the number of modified field
            if (result.getModifiedCount() == 1)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllReviewsOfPodcast(String podcastId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            // remove the review from reviews field
            Bson filter = eq("_id", new ObjectId(podcastId));
            Bson update = set("reviews", new ArrayList<>());
            UpdateResult result = manager.getCollection("podcast").updateOne(filter, update);

            // check the number of modified field
            if (result.getModifiedCount() == 1)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------------------- //

    // ------------------------------- AGGREGATION QUERY -------------------------------- //

    public List<String> showCountriesWithHighestNumberOfPodcasts(int limit) {
        return null;
    }

    public List<Podcast> showPodcastsWithHighestAverageRating(int limit) {
        return null;
    }

    public List<Podcast> showPodcastsWithHighestAverageRatingPerCountry(int limit) {
        return null;
    }

    public List<Podcast> showPodcastsWithHighestNumberOfReviews(int limit) {
        return null;
    }

    public List<String> showAuthorWithHighestAverageRating(int limit) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
