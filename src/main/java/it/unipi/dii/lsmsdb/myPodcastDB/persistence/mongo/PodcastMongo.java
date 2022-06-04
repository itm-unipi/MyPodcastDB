package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.util.Arrays;
import java.util.*;
import java.util.Map.Entry;

import java.text.SimpleDateFormat;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.*;

public class PodcastMongo {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addPodcast(Podcast podcast) {

        MongoManager manager = MongoManager.getInstance();
        List<Document> episodes = new ArrayList<>();
        List<Document> reviews = new ArrayList<>();

        for (Episode episode : podcast.getEpisodes()) {
            Document newEpisode = new Document()
                    .append("episodeName", episode.getName())
                    .append("episodeDescription", episode.getDescription())
                    .append("episodeReleaseDate", episode.getReleaseDateAsString())
                    .append("episodeTimeMillis", episode.getTimeMillis());
            episodes.add(newEpisode);
        }

        for (Entry<String, Integer> review : podcast.getReviews()) {
            Document newReview = new Document()
                    .append("reviewId", new ObjectId(review.getKey()))
                    .append("rating", review.getValue());
            reviews.add(newReview);
        }

        Document newPodcast = new Document()
                .append("podcastName", podcast.getName())
                .append("authorId", new ObjectId(podcast.getAuthorId()))
                .append("authorName", podcast.getAuthorName())
                .append("artworkUrl60", podcast.getArtworkUrl60())
                .append("artworkUrl600", podcast.getArtworkUrl600())
                .append("contentAdvisoryRating", podcast.getContentAdvisoryRating())
                .append("country", podcast.getCountry())
                .append("primaryCategory", podcast.getPrimaryCategory())
                .append("categories", podcast.getCategories())
                .append("releaseDate", podcast.getReleaseDateAsString())
                .append("episodes", episodes)
                .append("reviews", reviews);

        try{
            manager.getCollection("podcast").insertOne(newPodcast);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        String newId = newPodcast.getObjectId("_id").toString();
        podcast.setId(newId);
        if( newId.isEmpty())
            return false;
        else
            return true;
            
    }

    // ---------- READ ---------- //

    public List<Podcast> searchPodcast(String textToSearch, int limit) {
        MongoManager manager = MongoManager.getInstance();

        List<Podcast> podcastMatch = new ArrayList<>();
        Bson filter = Filters.text(textToSearch);

        try (MongoCursor<Document> cursor = manager.getCollection("podcast").find(filter).limit(limit).iterator()) {
            while (cursor.hasNext()) {
                Document podcast = cursor.next();

                String id = podcast.getObjectId("_id").toString();
                String name = podcast.getString("podcastName");
                String authorId = podcast.getObjectId("authorId").toString();
                String authorName = podcast.getString("authorName");
                String artworkUrl600 = podcast.getString("artworkUrl600");
                String primaryCategory = podcast.getString("primaryCategory");
                String date = podcast.getString("releaseDate").replace("T", " "). replace("Z", "");
                Date releaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);

                Podcast podcastFound = new Podcast(id, name, releaseDate, artworkUrl600, primaryCategory);
                // AuthorId is needed for delete podcast when an admin is in the search page
                podcastFound.setAuthor(authorId, authorName);

                podcastMatch.add(podcastFound);
            }

            return podcastMatch;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Podcast findPodcastById(String podcastId) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("podcast").find(eq("_id", new ObjectId(podcastId))).iterator()) {
            if (cursor.hasNext()) {
                Document podcast = cursor.next();

                // podcast attributes
                String id = podcastId;
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
                Podcast newPodcast = new Podcast(id, name, authorId, authorName, artworkUrl60, artworkUrl600, contentAdvisoryRating, country, primaryCategory, categories, releaseDate);

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

    public List<Podcast> findPodcastsByName(String podcastName, int limit) {
        MongoManager manager = MongoManager.getInstance();
        List<Podcast> podcasts = new ArrayList<>();

        try (MongoCursor<Document> cursor = manager.getCollection("podcast").find(eq("podcastName", podcastName)).limit(limit).iterator()) {
            while (cursor.hasNext()) {
                Document podcast = cursor.next();

                // podcast attributes
                String id = podcast.getObjectId("_id").toString();
                String name = podcastName;
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
                Podcast newPodcast = new Podcast(id, name, authorId, authorName, artworkUrl60, artworkUrl600, contentAdvisoryRating, country, primaryCategory, categories, releaseDate);

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

                podcasts.add(newPodcast);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return podcasts;
    }

    public List<Podcast> findPodcastsByAuthorId(String podcastAuthorId, int limit) {
        MongoManager manager = MongoManager.getInstance();
        List<Podcast> podcasts = new ArrayList<>();

        try (MongoCursor<Document> cursor = manager.getCollection("podcast").find(eq("authorId", new ObjectId(podcastAuthorId))).limit(limit).iterator()) {
            while (cursor.hasNext()) {
                Document podcast = cursor.next();

                // podcast attributes
                String id = podcast.getObjectId("_id").toString();
                String name = podcast.getString("podcastName");
                String authorId = podcastAuthorId;
                String authorName = podcast.getString("authorName");
                String artworkUrl60 = podcast.getString("artworkUrl60");
                String artworkUrl600 = podcast.getString("artworkUrl600");
                String contentAdvisoryRating = podcast.getString("contentAdvisoryRating");
                String country = podcast.getString("country");
                String primaryCategory = podcast.getString("primaryCategory");
                List<String> categories = podcast.getList("categories", String.class);
                String date = podcast.getString("releaseDate").replace("T", " "). replace("Z", "");
                Date releaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                Podcast newPodcast = new Podcast(id, name, authorId, authorName, artworkUrl60, artworkUrl600, contentAdvisoryRating, country, primaryCategory, categories, releaseDate);

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

                podcasts.add(newPodcast);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return podcasts;
    }

    public List<Podcast> findPodcastsByAuthorName(String podcastAuthorName, int limit) {
        MongoManager manager = MongoManager.getInstance();
        List<Podcast> podcasts = new ArrayList<>();

        try (MongoCursor<Document> cursor = manager.getCollection("podcast").find(eq("authorName", podcastAuthorName)).limit(limit).iterator()) {
            while (cursor.hasNext()) {
                Document podcast = cursor.next();

                // podcast attributes
                String id = podcast.getObjectId("_id").toString();
                String name = podcast.getString("podcastName");
                String authorId = podcast.getObjectId("authorId").toString();
                String authorName = podcastAuthorName;
                String artworkUrl60 = podcast.getString("artworkUrl60");
                String artworkUrl600 = podcast.getString("artworkUrl600");
                String contentAdvisoryRating = podcast.getString("contentAdvisoryRating");
                String country = podcast.getString("country");
                String primaryCategory = podcast.getString("primaryCategory");
                List<String> categories = podcast.getList("categories", String.class);
                String date = podcast.getString("releaseDate").replace("T", " "). replace("Z", "");
                Date releaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                Podcast newPodcast = new Podcast(id, name, authorId, authorName, artworkUrl60, artworkUrl600, contentAdvisoryRating, country, primaryCategory, categories, releaseDate);

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

                podcasts.add(newPodcast);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return podcasts;
    }

    public List<Podcast> findPodcastsByPrimaryCategory(String podcastPrimaryCategory, int limit) {
        MongoManager manager = MongoManager.getInstance();
        List<Podcast> podcasts = new ArrayList<>();

        try (MongoCursor<Document> cursor = manager.getCollection("podcast").find(eq("primaryCategory", podcastPrimaryCategory)).limit(limit).iterator()) {
            while (cursor.hasNext()) {
                Document podcast = cursor.next();

                // podcast attributes
                String id = podcast.getObjectId("_id").toString();
                String name = podcast.getString("podcastName");
                String authorId = podcast.getObjectId("authorId").toString();
                String authorName = podcast.getString("authorName");
                String artworkUrl60 = podcast.getString("artworkUrl60");
                String artworkUrl600 = podcast.getString("artworkUrl600");
                String contentAdvisoryRating = podcast.getString("contentAdvisoryRating");
                String country = podcast.getString("country");
                String primaryCategory = podcastPrimaryCategory;
                List<String> categories = podcast.getList("categories", String.class);
                String date = podcast.getString("releaseDate").replace("T", " "). replace("Z", "");
                Date releaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                Podcast newPodcast = new Podcast(id, name, authorId, authorName, artworkUrl60, artworkUrl600, contentAdvisoryRating, country, primaryCategory, categories, releaseDate);

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

                podcasts.add(newPodcast);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return podcasts;
    }

    public List<Podcast> findPodcastsByCategory(String category, int limit) {
        MongoManager manager = MongoManager.getInstance();
        List<Podcast> podcasts = new ArrayList<>();

        try (MongoCursor<Document> cursor = manager.getCollection("podcast").find(eq("categories", category)).limit(limit).iterator()) {
            while (cursor.hasNext()) {
                Document podcast = cursor.next();

                // podcast attributes
                String id = podcast.getObjectId("_id").toString();
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
                Podcast newPodcast = new Podcast(id, name, authorId, authorName, artworkUrl60, artworkUrl600, contentAdvisoryRating, country, primaryCategory, categories, releaseDate);

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

                podcasts.add(newPodcast);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return podcasts;
    }

    // --------- UPDATE --------- //

    public boolean updatePodcast(Podcast podcast) {
        MongoManager manager = MongoManager.getInstance();

        try{
            Bson filter = eq("_id", new ObjectId(podcast.getId()));
            Bson updates = combine(
                    set("podcastName", podcast.getName()),
                    set("authorId", new ObjectId(podcast.getAuthorId())),
                    set("authorName", podcast.getAuthorName()),
                    set("artworkUrl60", podcast.getArtworkUrl60()),
                    set("artworkUrl600", podcast.getArtworkUrl600()),
                    set("contentAdvisoryRating", podcast.getContentAdvisoryRating()),
                    set("country", podcast.getCountry()),
                    set("primaryCategory", podcast.getPrimaryCategory()),
                    set("categories", podcast.getCategories()),
                    set("releaseDate", podcast.getReleaseDateAsString())
            );

            UpdateResult result = manager.getCollection("podcast").updateOne(filter, updates);

            return result.getModifiedCount() == 1;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public boolean addEpisodeToPodcast(String podcastId, Episode episode) {
        MongoManager manager = MongoManager.getInstance();

        Document newEpisode= new Document()
                .append("episodeName", episode.getName())
                .append("episodeDescription", episode.getDescription())
                .append("episodeReleaseDate", episode.getReleaseDateAsString())
                .append("episodeTimeMillis", episode.getTimeMillis());
        try {
            Bson filter = eq("_id", new ObjectId(podcastId));
            Bson update = push("episodes", newEpisode);
            UpdateResult result = manager.getCollection("podcast").updateOne(filter, update);

            return result.getModifiedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addReviewToPodcast(String podcastId, String reviewId, int rating) {
        MongoManager manager = MongoManager.getInstance();

        Document newReview = new Document()
                .append("reviewId", new ObjectId(reviewId))
                .append("rating", rating);
        try {
            Bson filter = eq("_id", new ObjectId(podcastId));
            Bson update = push("reviews", newReview);
            UpdateResult result = manager.getCollection("podcast").updateOne(filter, update);

            return result.getModifiedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------- DELETE --------- //

    public boolean deletePodcastById(String id) {
        MongoManager manager = MongoManager.getInstance();
        try{
            DeleteResult result = manager.getCollection("podcast").deleteOne(eq("_id", new ObjectId(id)));
            return result.getDeletedCount() == 1;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public int deletePodcastsByName(String name) {
        MongoManager manager = MongoManager.getInstance();
        try{
            DeleteResult result = manager.getCollection("podcast").deleteMany(eq("podcastName", name));
            return (int) result.getDeletedCount();
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public int deletePodcastsByAuthorId(String authorId) {
        MongoManager manager = MongoManager.getInstance();
        try{
            DeleteResult result = manager.getCollection("podcast").deleteMany(eq("authorId", new ObjectId(authorId)));
            return (int) result.getDeletedCount();
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public int deletePodcastsByAuthorName(String authorName) {
        MongoManager manager = MongoManager.getInstance();
        try{
            DeleteResult result = manager.getCollection("podcast").deleteMany(eq("authorName", authorName));
            return (int) result.getDeletedCount();
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public boolean deleteEpisodeOfPodcast(String podcastId, String episodeName) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("_id", new ObjectId(podcastId));
            Bson update = pull("episodes", new Document("episodeName", episodeName));
            UpdateResult result = manager.getCollection("podcast").updateMany(filter, update);

            return result.getModifiedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllEpisodesOfPodcast(String podcastId) {
        MongoManager manager = MongoManager.getInstance();

        try {
            // remove the review from reviews field
            Bson filter = eq("_id", new ObjectId(podcastId));
            Bson update = set("episodes", new ArrayList<>());
            UpdateResult result = manager.getCollection("podcast").updateMany(filter, update);

            // check the number of modified field
            return result.getModifiedCount() == 1;
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
            return result.getModifiedCount() == 1;
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
            return result.getModifiedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------------------------------------- //

    // ------------------------------- AGGREGATION QUERY -------------------------------- //

    public List<String> showCountriesWithHighestNumberOfPodcasts(int lim) {
        MongoManager manager = MongoManager.getInstance();

        /*db.podcasts.aggregate([
            { $group: { _id: { country: "$country" }, totalPodcasts: { $sum: 1 } } },
            { $sort: { totalPodcasts: -1 } },
            { $limit : 1 },
            { $project: { _id: 0, country: "$_id.country", totalPodcasts: "$totalPodcasts" } }
        ])*/

        try {
            Bson group = group("$country", sum("totalPodcasts", 1));
            Bson projectionsFields = fields(excludeId(), computed("country", "$_id"), computed("totalPodcasts", "$sum"));
            Bson projection = project(projectionsFields);
            Bson sort = sort(descending("totalPodcasts"));
            Bson limit = limit(lim);

            List<String> countries = new ArrayList<String>();
            for (Document doc :manager.getCollection("podcast").aggregate(Arrays.asList(group, sort, projection, limit)))
                countries.add(doc.getString("country"));

            if (countries.isEmpty())
                return null;

            return countries;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Triplet<String, String, Float>> showPodcastsWithHighestAverageRating(int limit) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson project = project(fields(
                    computed("name", "$podcastName"),
                    computed("meanRating", computed("$avg", "$reviews.rating")))
            );
            Bson sort = sort(Sorts.descending("meanRating"));
            Bson lim = limit(limit);

            List<Triplet<String, String, Float>> results = new ArrayList<>();
            for (Document result : manager.getCollection("podcast").aggregate(Arrays.asList(project, sort, lim))) {
                String id = result.getObjectId("_id").toString();
                String name = result.getString("name");
                double meanRating = result.getDouble("meanRating");
                Triplet<String, String, Float> record = new Triplet<>(id, name, (float)meanRating);
                results.add(record);
            }

            if (!results.isEmpty())
                return results;
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Quartet<String, String, String, Float>> showPodcastsWithHighestAverageRatingPerCountry(int limit) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson project = project(fields(
                    computed("name", "$podcastName"),
                    include("country"),
                    computed("meanRating", computed("$avg", "$reviews.rating")))
            );
            Bson sort = sort(Sorts.descending("meanRating"));
            Bson group = group("$country",
                    Accumulators.first("podcastId", "$_id"),
                    Accumulators.first("podcastName", "$name"),
                    Accumulators.first("meanRating", "$meanRating")
            );
            Bson lim = limit(limit);

            List<Quartet<String, String, String, Float>> results = new ArrayList<>();
            for (Document result : manager.getCollection("podcast").aggregate(Arrays.asList(project, sort, group, lim))) {
                String id = result.getObjectId("podcastId").toString();
                String name = result.getString("podcastName");
                String country = result.getString("_id");
                double meanRating = result.getDouble("meanRating");
                Quartet<String, String, String, Float> record = new Quartet<>(id, name, country, (float)meanRating);
                results.add(record);
            }

            if (!results.isEmpty())
                return results;
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Triplet<String, String, Integer>> showPodcastsWithHighestNumberOfReviews(int limit) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson project = project(fields(
                    computed("name", "$podcastName"),
                    computed("numReviews", computed("$size", "$reviews")))
            );
            Bson sort = sort(Sorts.descending("numReviews"));
            Bson lim = limit(limit);

            List<Triplet<String, String, Integer>> results = new ArrayList<>();
            for (Document result : manager.getCollection("podcast").aggregate(Arrays.asList(project, sort, lim))) {
                String id = result.getObjectId("_id").toString();
                String name = result.getString("name");
                int numReviews = result.getInteger("numReviews");
                Triplet<String, String, Integer> record = new Triplet<>(id, name, numReviews);
                results.add(record);
            }

            if (!results.isEmpty())
                return results;
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entry<String, Float>> showAuthorWithHighestAverageRating(int limit) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson project = project(fields(
                    include("_id"),
                    include("authorName"),
                    computed("rating", computed("$avg", "$reviews.rating"))
            ));
            Bson group = group(
                    "$authorName",
                    avg("rating", "$rating")
            );
            Bson sort = sort(descending("rating"));
            Bson limitRes = limit(limit);

            List<Document> results = manager.getCollection("podcast").aggregate(Arrays.asList(project, group, sort, limitRes)).into(new ArrayList<>());
            if(results.isEmpty())
                return null;

            List<Entry<String, Float>> authors = new ArrayList<>();
            for (Document author : results)
                authors.add(new AbstractMap.SimpleEntry<>(author.getString("_id"), (float)(double)author.getDouble("rating")));
            return authors;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------------------------------------------------------------------------- //
}
