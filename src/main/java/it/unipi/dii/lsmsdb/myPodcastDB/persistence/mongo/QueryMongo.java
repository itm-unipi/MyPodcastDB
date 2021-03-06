package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Updates.set;

public class QueryMongo {

    private String dateAsString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateAsString = dateFormat.format(date);
        return dateAsString;
    }

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    // ---------- READ ---------- //

    public String getAverageAgeOfUsersPerFavouriteCategory(List<Pair<String, Float>> readValues) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("query").find(eq("queryName", "AverageAgeOfUsersPerFavouriteCategory")).iterator()) {
            if (cursor.hasNext()) {
                Document query = cursor.next();

                // get the update time
                String updateTime = dateAsString(query.getDate("lastUpdate"));

                // get all the results
                List<Document> results = query.getList("results", Document.class);
                for (Document result : results) {
                    String category = result.getString("category");
                    Float averageAge = (float)(double)result.getDouble("averageAge");
                    Pair<String, Float> r = new Pair<>(category, averageAge);
                    readValues.add(r);
                }

                return updateTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String getPodcastsWithHighestNumberOfReviews(List<Pair<Podcast, Integer>> readValues) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("query").find(eq("queryName", "PodcastsWithHighestNumberOfReviews")).iterator()) {
            if (cursor.hasNext()) {
                Document query = cursor.next();

                // get the update time
                String updateTime = dateAsString(query.getDate("lastUpdate"));

                // get all the results
                List<Document> results = query.getList("results", Document.class);
                for (Document result : results) {
                    String podcastId = result.getObjectId("podcastId").toString();
                    String podcastName = result.getString("podcastName");
                    String podcastArtwork = result.getString("podcastArtwork");
                    Podcast podcast = new Podcast(podcastId, podcastName, podcastArtwork);
                    int numReviews = result.getInteger("numReviews");
                    Pair<Podcast, Integer> r = new Pair<>(podcast, numReviews);
                    readValues.add(r);
                }

                return updateTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String getCountryWithHighestNumberOfPodcasts(List<Pair<String, Integer>> readValues) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("query").find(eq("queryName", "CountryWithHighestNumberOfPodcasts")).iterator()) {
            if (cursor.hasNext()) {
                Document query = cursor.next();

                // get the update time
                String updateTime = dateAsString(query.getDate("lastUpdate"));

                // get all the results
                List<Document> results = query.getList("results", Document.class);
                for (Document result : results) {
                    String country = result.getString("country");
                    int numPodcasts = result.getInteger("numPodcasts");
                    Pair<String, Integer> r = new Pair<>(country, numPodcasts);
                    readValues.add(r);
                }

                return updateTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String getFavouriteCategoryForGender(List<String> female, List<String> male, List<String> notBinary) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("query").find(eq("queryName", "FavouriteCategoryForGender")).iterator()) {
            if (cursor.hasNext()) {
                Document query = cursor.next();

                // get the update time
                String updateTime = dateAsString(query.getDate("lastUpdate"));

                // get the three lists
                female.addAll(query.getList("resultsFemale", String.class));
                male.addAll(query.getList("resultsMale", String.class));
                notBinary.addAll(query.getList("resultsNotBinary", String.class));

                return updateTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String getMostNumerousCategory(List<Pair<String, Integer>> readValues) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("query").find(eq("queryName", "MostNumerousCategory")).iterator()) {
            if (cursor.hasNext()) {
                Document query = cursor.next();

                // get the update time
                String updateTime = dateAsString(query.getDate("lastUpdate"));

                // get all the results
                List<Document> results = query.getList("results", Document.class);
                for (Document result : results) {
                    String category = result.getString("category");
                    int numPodcasts = result.getInteger("numPodcasts");
                    Pair<String, Integer> r = new Pair<>(category, numPodcasts);
                    readValues.add(r);
                }

                return updateTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String getMostAppreciatedCategory(List<Pair<String, Integer>> readValues) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("query").find(eq("queryName", "MostAppreciatedCategory")).iterator()) {
            if (cursor.hasNext()) {
                Document query = cursor.next();

                // get the update time
                String updateTime = dateAsString(query.getDate("lastUpdate"));

                // get all the results
                List<Document> results = query.getList("results", Document.class);
                for (Document result : results) {
                    String category = result.getString("category");
                    int numPodcasts = result.getInteger("numLikes");
                    Pair<String, Integer> r = new Pair<>(category, numPodcasts);
                    readValues.add(r);
                }

                return updateTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String getPodcastsWithHighestAverageRating(List<Pair<Podcast, Float>> readValues) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("query").find(eq("queryName", "PodcastsWithHighestAverageRating")).iterator()) {
            if (cursor.hasNext()) {
                Document query = cursor.next();

                // get the update time
                String updateTime = dateAsString(query.getDate("lastUpdate"));

                // get all the results
                List<Document> results = query.getList("results", Document.class);
                for (Document result : results) {
                    String podcastId = result.getObjectId("podcastId").toString();
                    String podcastName = result.getString("podcastName");
                    String podcastArtwork = result.getString("podcastArtwork");
                    Podcast podcast = new Podcast(podcastId, podcastName, podcastArtwork);
                    float meanRating = (float)(double)result.getDouble("meanRating");
                    Pair<Podcast, Float> r = new Pair<>(podcast, meanRating);
                    readValues.add(r);
                }

                return updateTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String getPodcastWithHighestAverageRatingPerCountry(List<Triplet<Podcast, String, Float>> readValues) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("query").find(eq("queryName", "PodcastWithHighestAverageRatingPerCountry")).iterator()) {
            if (cursor.hasNext()) {
                Document query = cursor.next();

                // get the update time
                String updateTime = dateAsString(query.getDate("lastUpdate"));

                // get all the results
                List<Document> results = query.getList("results", Document.class);
                for (Document result : results) {
                    String podcastId = result.getObjectId("podcastId").toString();
                    String podcastName = result.getString("podcastName");
                    String podcastArtwork = result.getString("podcastArtwork");
                    Podcast podcast = new Podcast(podcastId, podcastName, podcastArtwork);
                    String country = result.getString("country");
                    float meanRating = (float)(double)result.getDouble("meanRating");
                    Triplet<Podcast, String, Float> r = new Triplet<>(podcast, country, meanRating);
                    readValues.add(r);
                }

                return updateTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String getMostFollowedAuthor(List<Pair<Author, Integer>> readValues) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("query").find(eq("queryName", "MostFollowedAuthor")).iterator()) {
            if (cursor.hasNext()) {
                Document query = cursor.next();

                // get the update time
                String updateTime = dateAsString(query.getDate("lastUpdate"));

                // get all the results
                List<Document> results = query.getList("results", Document.class);
                for (Document result : results) {
                    String authorName = result.getString("authorName");
                    String picturePath = result.getString("picturePath");
                    Author author = new Author("", authorName, picturePath);
                    int followers = result.getInteger("followers");
                    Pair<Author, Integer> r = new Pair<>(author, followers);
                    readValues.add(r);
                }

                return updateTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String getMostLikedPodcast(List<Pair<Podcast, Integer>> readValues) {
        MongoManager manager = MongoManager.getInstance();

        try (MongoCursor<Document> cursor = manager.getCollection("query").find(eq("queryName", "MostLikedPodcast")).iterator()) {
            if (cursor.hasNext()) {
                Document query = cursor.next();

                // get the update time
                String updateTime = dateAsString(query.getDate("lastUpdate"));

                // get all the results
                List<Document> results = query.getList("results", Document.class);
                for (Document result : results) {
                    String podcastId = result.getObjectId("podcastId").toString();
                    String podcastName = result.getString("podcastName");
                    String podcastArtwork = result.getString("podcastArtwork");
                    Podcast podcast = new Podcast(podcastId, podcastName, podcastArtwork);
                    int numLikes = result.getInteger("numLikes");
                    Pair<Podcast, Integer> r = new Pair<>(podcast, numLikes);
                    readValues.add(r);
                }

                return updateTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public List<Pair<String, String>> getUpdateTimes(String[] queries) {
        MongoManager manager = MongoManager.getInstance();

        // create the filter based on requested queries
        List<Bson> queryNames = new ArrayList<>();
        for (String name : queries)
            queryNames.add(eq("queryName", name));
        Bson filter = or(queryNames);

        try (MongoCursor<Document> cursor = manager.getCollection("query").find(filter).iterator()) {
            List<Pair<String, String>> updateTimes = new ArrayList<>();
            while (cursor.hasNext()) {
                Document query = cursor.next();

                // get the update time
                String queryName = query.getString("queryName");
                String updateTime = dateAsString(query.getDate("lastUpdate"));
                updateTimes.add(new Pair<>(queryName, updateTime));
            }

            return updateTimes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --------- UPDATE --------- //

    public boolean updateAverageAgeOfUsersPerFavouriteCategory(List<Entry<String, Float>> newValues, Date updateTime) {
        MongoManager manager = MongoManager.getInstance();

        // create the results
        List<Document> results = new ArrayList<>();
        for (Entry<String, Float> value : newValues) {
            Document newResult = new Document()
                    .append("category", value.getKey())
                    .append("averageAge", value.getValue());
            results.add(newResult);
        }

        try {
            Bson filter = eq("queryName", "AverageAgeOfUsersPerFavouriteCategory");
            Bson update = combine(
                    set("queryName", "AverageAgeOfUsersPerFavouriteCategory"),
                    set("lastUpdate", updateTime),
                    set("results", results)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult result = manager.getCollection("query").updateOne(filter, update, options);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePodcastsWithHighestNumberOfReviews(List<Pair<Podcast, Integer>> newValues, Date updateTime) {
        MongoManager manager = MongoManager.getInstance();

        // create the results
        List<Document> results = new ArrayList<>();
        for (Pair<Podcast, Integer> value : newValues) {
            Document newResult = new Document()
                    .append("podcastId", new ObjectId(value.getValue0().getId()))
                    .append("podcastName", value.getValue0().getName())
                    .append("podcastArtwork", value.getValue0().getArtworkUrl600())
                    .append("numReviews", value.getValue1());
            results.add(newResult);
        }

        try {
            Bson filter = eq("queryName", "PodcastsWithHighestNumberOfReviews");
            Bson update = combine(
                    set("queryName", "PodcastsWithHighestNumberOfReviews"),
                    set("lastUpdate", updateTime),
                    set("results", results)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult result = manager.getCollection("query").updateOne(filter, update, options);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCountryWithHighestNumberOfPodcasts(List<Pair<String, Integer>> newValues, Date updateTime) {
        MongoManager manager = MongoManager.getInstance();

        // create the results
        List<Document> results = new ArrayList<>();
        for (Pair<String, Integer> value : newValues) {
            Document newResult = new Document()
                    .append("country", value.getValue0())
                    .append("numPodcasts", value.getValue1());
            results.add(newResult);
        }

        try {
            Bson filter = eq("queryName", "CountryWithHighestNumberOfPodcasts");
            Bson update = combine(
                    set("queryName", "CountryWithHighestNumberOfPodcasts"),
                    set("lastUpdate", updateTime),
                    set("results", results)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult result = manager.getCollection("query").updateOne(filter, update, options);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFavouriteCategoryForGender(List<String> female, List<String> male, List<String> notBinary, Date updateTime) {
        MongoManager manager = MongoManager.getInstance();

        try {
            Bson filter = eq("queryName", "FavouriteCategoryForGender");
            Bson update = combine(
                    set("queryName", "FavouriteCategoryForGender"),
                    set("lastUpdate", updateTime),
                    set("resultsFemale", female),
                    set("resultsMale", male),
                    set("resultsNotBinary", notBinary)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult result = manager.getCollection("query").updateOne(filter, update, options);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMostNumerousCategory(List<Entry<String, Integer>> newValues, Date updateTime) {
        MongoManager manager = MongoManager.getInstance();

        // create the results
        List<Document> results = new ArrayList<>();
        for (Entry<String, Integer> value : newValues) {
            Document newResult = new Document()
                    .append("category", value.getKey())
                    .append("numPodcasts", value.getValue());
            results.add(newResult);
        }

        try {
            Bson filter = eq("queryName", "MostNumerousCategory");
            Bson update = combine(
                    set("queryName", "MostNumerousCategory"),
                    set("lastUpdate", updateTime),
                    set("results", results)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult result = manager.getCollection("query").updateOne(filter, update, options);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMostAppreciatedCategory(List<Entry<String, Integer>> newValues, Date updateTime) {
        MongoManager manager = MongoManager.getInstance();

        // create the results
        List<Document> results = new ArrayList<>();
        for (Entry<String, Integer> value : newValues) {
            Document newResult = new Document()
                    .append("category", value.getKey())
                    .append("numLikes", value.getValue());
            results.add(newResult);
        }

        try {
            Bson filter = eq("queryName", "MostAppreciatedCategory");
            Bson update = combine(
                    set("queryName", "MostAppreciatedCategory"),
                    set("lastUpdate", updateTime),
                    set("results", results)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult result = manager.getCollection("query").updateOne(filter, update, options);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMostFollowedAuthor(List<Pair<Author, Integer>> newValues, Date updateTime) {
        MongoManager manager = MongoManager.getInstance();

        // create the results
        List<Document> results = new ArrayList<>();
        for (Pair<Author, Integer> value : newValues) {
            Document newResult = new Document()
                    .append("authorName", value.getValue0().getName())
                    .append("picturePath", value.getValue0().getPicturePath())
                    .append("followers", value.getValue1());
            results.add(newResult);
        }

        try {
            Bson filter = eq("queryName", "MostFollowedAuthor");
            Bson update = combine(
                    set("queryName", "MostFollowedAuthor"),
                    set("lastUpdate", updateTime),
                    set("results", results)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult result = manager.getCollection("query").updateOne(filter, update, options);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMostLikedPodcast(List<Entry<Podcast, Integer>> newValues, Date updateTime) {
        MongoManager manager = MongoManager.getInstance();

        // create the results
        List<Document> results = new ArrayList<>();
        for (Entry<Podcast, Integer> value : newValues) {
            Document newResult = new Document()
                    .append("podcastId", new ObjectId(value.getKey().getId()))
                    .append("podcastName", value.getKey().getName())
                    .append("podcastArtwork", value.getKey().getArtworkUrl600())
                    .append("numLikes", value.getValue());
            results.add(newResult);
        }

        try {
            Bson filter = eq("queryName", "MostLikedPodcast");
            Bson update = combine(
                    set("queryName", "MostLikedPodcast"),
                    set("lastUpdate", updateTime),
                    set("results", results)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult result = manager.getCollection("query").updateOne(filter, update, options);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePodcastsWithHighestAverageRating(List<Pair<Podcast, Float>> newValues, Date updateTime) {
        MongoManager manager = MongoManager.getInstance();

        // create the results
        List<Document> results = new ArrayList<>();
        for (Pair<Podcast, Float> value : newValues) {
            Document newResult = new Document()
                    .append("podcastId", new ObjectId(value.getValue0().getId()))
                    .append("podcastName", value.getValue0().getName())
                    .append("podcastArtwork", value.getValue0().getArtworkUrl600())
                    .append("meanRating", value.getValue1());
            results.add(newResult);
        }

        try {
            Bson filter = eq("queryName", "PodcastsWithHighestAverageRating");
            Bson update = combine(
                    set("queryName", "PodcastsWithHighestAverageRating"),
                    set("lastUpdate", updateTime),
                    set("results", results)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult result = manager.getCollection("query").updateOne(filter, update, options);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePodcastWithHighestAverageRatingPerCountry(List<Triplet<Podcast, String, Float>> newValues, Date updateTime) {
        MongoManager manager = MongoManager.getInstance();

        // create the results
        List<Document> results = new ArrayList<>();
        for (Triplet<Podcast, String, Float> value : newValues) {
            Document newResult = new Document()
                    .append("podcastId", new ObjectId(value.getValue0().getId()))
                    .append("podcastName", value.getValue0().getName())
                    .append("podcastArtwork", value.getValue0().getArtworkUrl600())
                    .append("country", value.getValue1())
                    .append("meanRating", value.getValue2());
            results.add(newResult);
        }

        try {
            Bson filter = eq("queryName", "PodcastWithHighestAverageRatingPerCountry");
            Bson update = combine(
                    set("queryName", "PodcastWithHighestAverageRatingPerCountry"),
                    set("lastUpdate", updateTime),
                    set("results", results)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult result = manager.getCollection("query").updateOne(filter, update, options);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------- DELETE --------- //

    // ---------------------------------------------------------------------------------- //
}
