package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.MongoCursor;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class PodcastMongo {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    public void addPodcast(Podcast podcast) {

    }

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

    public List<Podcast> findPodcastByName(String name) {
        return null;
    }

    public List<Podcast> findPodcastByAuthorId(String authorId) {
        return null;
    }

    public List<Podcast> findPodcastByAuthorName(String authorName) {
        return null;
    }

    public List<Podcast> findPodcastByPrimaryCategory(String primaryCategory) {
        return null;
    }

    public List<Podcast> findPodcastByCategory(String category) {
        return null;
    }

    public void updatePodcast(Podcast podcast) {

    }

    public void deletePodcast(String id) {

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

    // ---------------------------------------------------------------------------------- //
}
