package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.QueryMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.UserMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AdminService {

    //---------------- GIANLUCA ---------------------
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    //-----------------------------------------------

    //----------------- MATTEO ----------------------

    private PodcastMongo podcastMongo;
    private UserMongo userMongo;
    private QueryMongo queryMongo;
    private PodcastNeo4j podcastNeo4j;
    private AuthorNeo4j authorNeo4j;

    public AdminService() {
        this.podcastMongo = new PodcastMongo();
        this.userMongo = new UserMongo();
        this.queryMongo = new QueryMongo();
        this.podcastNeo4j = new PodcastNeo4j();
        this.authorNeo4j = new AuthorNeo4j();
    }

    public void loadAdminPage() {
        MongoManager.getInstance().openConnection();

        List<Pair<String, Float>> test = new ArrayList<>();
        String updateTime = this.queryMongo.getAverageAgeOfUsersPerFavouriteCategory(test);
        Logger.info(updateTime + " : " + test);

        MongoManager.getInstance().closeConnection();
    }

    public int updateAverageAgeOfUsersPerFavouriteCategory(Date updateTime, int limit) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // get the updated values
        List<Map.Entry<String, Float>> results = this.userMongo.showAverageAgeOfUsersPerFavouriteCategory(limit);

        // check the result
        if (results != null) {
            // update the values in Mongo collection
            boolean resAdd = this.queryMongo.updateAverageAgeOfUsersPerFavouriteCategory(results, updateTime);

            // check the result
            if (resAdd) {
                Logger.success("Query updated");
            } else {
                Logger.error("Update Mongo query collection failed");
                result = -2;
            }
        } else {
            Logger.error("Update query failed");
            result = -1;
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int updatePodcastsWithHighestNumberOfReviews(Date updateTime, int limit) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // get the updated values
        List<Pair<Podcast, Integer>> results = this.podcastMongo.showPodcastsWithHighestNumberOfReviews(limit);

        // check the result
        if (results != null) {
            // update the values in Mongo collection
            boolean resAdd = this.queryMongo.updatePodcastsWithHighestNumberOfReviews(results, updateTime);

            // check the result
            if (resAdd) {
                Logger.success("Query updated");
            } else {
                Logger.error("Update Mongo query collection failed");
                result = -2;
            }
        } else {
            Logger.error("Update query failed");
            result = -1;
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int updateCountryWithHighestNumberOfPodcasts(Date updateTime, int limit) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // get the updated values
        List<Pair<String, Integer>> results = this.podcastMongo.showCountriesWithHighestNumberOfPodcasts(limit);

        // check the result
        if (results != null) {
            // update the values in Mongo collection
            boolean resAdd = this.queryMongo.updateCountryWithHighestNumberOfPodcasts(results, updateTime);

            // check the result
            if (resAdd) {
                Logger.success("Query updated");
            } else {
                Logger.error("Update Mongo query collection failed");
                result = -2;
            }
        } else {
            Logger.error("Update query failed");
            result = -1;
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int updateFavouriteCategoryForGender(Date updateTime, int limit) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // get the updated values
        List<String> female = this.userMongo.showFavouriteCategoryForGender("male", limit);
        List<String> male = this.userMongo.showFavouriteCategoryForGender("female", limit);
        List<String> notBinary = this.userMongo.showFavouriteCategoryForGender("other", limit);

        // check the result
        if (female != null && male != null && notBinary != null) {
            // update the values in Mongo collection
            boolean resAdd = this.queryMongo.updateFavouriteCategoryForGender(male, female, notBinary, updateTime);

            // check the result
            if (resAdd) {
                Logger.success("Query updated");
            } else {
                Logger.error("Update Mongo query collection failed");
                result = -2;
            }
        } else {
            Logger.error("Update query failed");
            result = -1;
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int updateMostNumerousCategory(Date updateTime, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        int result = 0;

        // get the updated values
        List<Map.Entry<String, Integer>> results = this.podcastNeo4j.showMostNumerousCategories(limit);

        // check the result
        if (results != null) {
            // update the values in Mongo collection
            boolean resAdd = this.queryMongo.updateMostNumerousCategory(results, updateTime);

            // check the result
            if (resAdd) {
                Logger.success("Query updated");
            } else {
                Logger.error("Update Mongo query collection failed");
                result = -2;
            }
        } else {
            Logger.error("Update query failed");
            result = -1;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public int updateMostAppreciatedCategory(Date updateTime, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        int result = 0;

        // get the updated values
        List<Map.Entry<String, Integer>> results = this.podcastNeo4j.showMostAppreciatedCategories(limit);

        // check the result
        if (results != null) {
            // update the values in Mongo collection
            boolean resAdd = this.queryMongo.updateMostAppreciatedCategory(results, updateTime);

            // check the result
            if (resAdd) {
                Logger.success("Query updated");
            } else {
                Logger.error("Update Mongo query collection failed");
                result = -2;
            }
        } else {
            Logger.error("Update query failed");
            result = -1;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public int updatePodcastsWithHighestAverageRating(Date updateTime, int limit) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // get the updated values
        List<Pair<Podcast, Float>> results = this.podcastMongo.showPodcastsWithHighestAverageRating(limit);

        // check the result
        if (results != null) {
            // update the values in Mongo collection
            boolean resAdd = this.queryMongo.updatePodcastsWithHighestAverageRating(results, updateTime);

            // check the result
            if (resAdd) {
                Logger.success("Query updated");
            } else {
                Logger.error("Update Mongo query collection failed");
                result = -2;
            }
        } else {
            Logger.error("Update query failed");
            result = -1;
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int updatePodcastWithHighestAverageRatingPerCountry(Date updateTime, int limit) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // get the updated values
        List<Triplet<Podcast, String, Float>> results = this.podcastMongo.showPodcastsWithHighestAverageRatingPerCountry(limit);

        // check the result
        if (results != null) {
            // update the values in Mongo collection
            boolean resAdd = this.queryMongo.updatePodcastWithHighestAverageRatingPerCountry(results, updateTime);

            // check the result
            if (resAdd) {
                Logger.success("Query updated");
            } else {
                Logger.error("Update Mongo query collection failed");
                result = -2;
            }
        } else {
            Logger.error("Update query failed");
            result = -1;
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int updateMostFollowedAuthor(Date updateTime, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        int result = 0;

        // get the updated values
        List<Pair<Author, Integer>> results = this.authorNeo4j.showMostFollowedAuthor(limit);

        // check the result
        if (results != null) {
            // update the values in Mongo collection
            boolean resAdd = this.queryMongo.updateMostFollowedAuthor(results, updateTime);

            // check the result
            if (resAdd) {
                Logger.success("Query updated");
            } else {
                Logger.error("Update Mongo query collection failed");
                result = -2;
            }
        } else {
            Logger.error("Update query failed");
            result = -1;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public int updateMostLikedPodcast(Date updateTime, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        int result = 0;

        // get the updated values
        List<Map.Entry<Podcast, Integer>> results = this.podcastNeo4j.showMostLikedPodcasts(limit);

        // check the result
        if (results != null) {
            // update the values in Mongo collection
            boolean resAdd = this.queryMongo.updateMostLikedPodcast(results, updateTime);

            // check the result
            if (resAdd) {
                Logger.success("Query updated");
            } else {
                Logger.error("Update Mongo query collection failed");
                result = -2;
            }
        } else {
            Logger.error("Update query failed");
            result = -1;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    //-----------------------------------------------
}
