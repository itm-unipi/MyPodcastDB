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

import java.util.*;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AdminMongo;

public class AdminDashboardService {

    private AdminMongo adminMongo;
    private PodcastMongo podcastMongo;
    private UserMongo userMongo;
    private QueryMongo queryMongo;
    private PodcastNeo4j podcastNeo4j;
    private AuthorNeo4j authorNeo4j;

    public AdminDashboardService() {
        this.adminMongo = new AdminMongo();
        this.podcastMongo = new PodcastMongo();
        this.userMongo = new UserMongo();
        this.queryMongo = new QueryMongo();
        this.podcastNeo4j = new PodcastNeo4j();
        this.authorNeo4j = new AuthorNeo4j();
    }

    public int addAdmin(Admin admin){

        int res = -1;
        MongoManager.getInstance().openConnection();
        //check if an admin with the same name already exists
        if(adminMongo.findAdminByName(admin.getName()) != null)
            res = 1;
        //check if an admin with the same email already exists
        else if(adminMongo.findAdminByEmail(admin.getEmail()) != null)
            res = 2;
        //adding new admin to mongo
        else if (!adminMongo.addAdmin(admin))
            res = 3;
        else
            res = 0;
        MongoManager.getInstance().closeConnection();
        return res;
    }

    public int updateAdmin(Admin oldAdmin, Admin newAdmin){

        int res = -1;

        //check if there is something to update
        if( oldAdmin.getName().equals(newAdmin.getName()) &&
            oldAdmin.getEmail().equals(newAdmin.getEmail()) &&
            oldAdmin.getPassword().equals(newAdmin.getPassword())
        )
            return 1;

        MongoManager.getInstance().openConnection();

        //check if an admin with the same name already exists
        if(!oldAdmin.getName().equals(newAdmin.getName()) && adminMongo.findAdminByName(newAdmin.getName()) != null)
            res = 2;
        //check if an admin with the same email already exists
        else if(!oldAdmin.getEmail().equals(newAdmin.getEmail()) && adminMongo.findAdminByEmail(newAdmin.getEmail()) != null)
            res = 3;
        //updating admin
        else if (!adminMongo.updateAdmin(newAdmin))
            res = 4;
        else
            res = 0;
        MongoManager.getInstance().closeConnection();

        if(res == 0)
            oldAdmin.copy(newAdmin);
        return res;
    }

    public int deleteAdmin(Admin admin){
        int res = -1;
        MongoManager.getInstance().openConnection();

        //check failure mongo operation
        if (!adminMongo.deleteAdminById(admin.getId()))
            res = 1;
        else
            res = 0;
        MongoManager.getInstance().closeConnection();

        return res;
    }

    public List<String> loadAdminPage(List<Pair<String, Float>> averageAgeOfUsersPerFavouriteCategory, List<Pair<Podcast, Integer>> podcastsWithHighestNumberOfReviews, List<Pair<String, Integer>> countryWithHighestNumberOfPodcasts, Triplet<List<String>, List<String>, List<String>> topFavouriteCategoriesPerGender, List<Pair<String, Integer>> mostNumerousCategories, List<Pair<String, Integer>> mostAppreciatedCategory) {
        Logger.info("Load Admin Page Service");

        MongoManager.getInstance().openConnection();
        List<String> updateTimes = new ArrayList<>();

        // load the statistics and their last update time
        String updateTime = this.queryMongo.getAverageAgeOfUsersPerFavouriteCategory(averageAgeOfUsersPerFavouriteCategory);
        updateTimes.add(updateTime);
        updateTime = this.queryMongo.getPodcastsWithHighestNumberOfReviews(podcastsWithHighestNumberOfReviews);
        updateTimes.add(updateTime);
        updateTime = this.queryMongo.getCountryWithHighestNumberOfPodcasts(countryWithHighestNumberOfPodcasts);
        updateTimes.add(updateTime);
        updateTime = this.queryMongo.getFavouriteCategoryForGender(topFavouriteCategoriesPerGender.getValue0(), topFavouriteCategoriesPerGender.getValue1(), topFavouriteCategoriesPerGender.getValue2());
        updateTimes.add(updateTime);
        updateTime = this.queryMongo.getMostNumerousCategory(mostNumerousCategories);
        updateTimes.add(updateTime);
        updateTime = this.queryMongo.getMostAppreciatedCategory(mostAppreciatedCategory);
        updateTimes.add(updateTime);

        // request last update time of the remaining heavy query
        String[] otherQueries = new String[] {"MostFollowedAuthor", "MostLikedPodcast", "PodcastsWithHighestAverageRating", "PodcastWithHighestAverageRatingPerCountry" };
        List<Pair<String, String>> requestedTimes = this.queryMongo.getUpdateTimes(otherQueries);
        String[] toAddTimes = new String[4];
        for (Pair<String, String> time : requestedTimes) {
            if (time.getValue0().equals("MostFollowedAuthor"))
                toAddTimes[0] = time.getValue1();
            else if (time.getValue0().equals("MostLikedPodcast"))
                toAddTimes[1] = time.getValue1();
            else if (time.getValue0().equals("PodcastsWithHighestAverageRating"))
                toAddTimes[2] = time.getValue1();
            else if (time.getValue0().equals("PodcastWithHighestAverageRatingPerCountry"))
                toAddTimes[3] = time.getValue1();
        }
        updateTimes.addAll(Arrays.stream(toAddTimes).toList());

        MongoManager.getInstance().closeConnection();
        Logger.success("Load Admin Page Service ended");

        return updateTimes;
    }

    public int updateAverageAgeOfUsersPerFavouriteCategory(Date updateTime, int limit) {
        Logger.info("Update Average Age of Users per Favourite Category Service");

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
        Logger.info("Update Podcasts with Highest Number of Reviews Service");

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
        Logger.info("Update Country with Highest Number of Podcasts Service");

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
        Logger.info("Update Favourite Category for Gender Service");

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
        Logger.info("Update Most Numerous Category Service");

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
        Logger.info("Update Most Appreciated Category Service");

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
        Logger.info("Update Podcasts with Highest Average Rating Service");

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
        Logger.info("Update Podcast with Highest Average Rating per Country Service");

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
        Logger.info("Update Most Followed Author Service");

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
        Logger.info("Update Most Liked Podcast Service");

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

    public boolean getUpdatedStatistic(int statisticId, Object statistic) {
        Logger.info("Get Updated Statistic Service");

        MongoManager.getInstance().openConnection();

        String result = null;
        switch(statisticId) {
            // Average Age Of Users Per Favourite Category
            case 0:
                result = this.queryMongo.getAverageAgeOfUsersPerFavouriteCategory((List<Pair<String, Float>>)statistic);
                break;

            // Podcasts With The Highest Number Of Reviews
            case 1:
                result = this.queryMongo.getPodcastsWithHighestNumberOfReviews((List<Pair<Podcast, Integer>>)statistic);
                break;

            // Country With The Highest Number Of Podcasts
            case 2:
                result = this.queryMongo.getCountryWithHighestNumberOfPodcasts((List<Pair<String, Integer>>)statistic);
                break;

            // Favourite Category For Gender
            case 3:
                result = this.queryMongo.getFavouriteCategoryForGender(((Triplet<List<String>, List<String>, List<String>>)statistic).getValue0(), ((Triplet<List<String>, List<String>, List<String>>)statistic).getValue1(), ((Triplet<List<String>, List<String>, List<String>>)statistic).getValue2());
                break;

            // Most Numerous Category
            case 4:
                result = this.queryMongo.getMostNumerousCategory((List<Pair<String, Integer>>)statistic);
                break;

            // Most Appreciated Category
            case 5:
                result = this.queryMongo.getMostAppreciatedCategory((List<Pair<String, Integer>>)statistic);
                break;
        }

        Neo4jManager.getInstance().closeConnection();

        if (result != null)
            Logger.success("Successfully got the Updated Statistic");
        else
            Logger.error("Failed to get the Updated Statistic");

        return result != null;
    }
}
