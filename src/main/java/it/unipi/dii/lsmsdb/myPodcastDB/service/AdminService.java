package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.QueryMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Pair;

import java.util.Date;
import java.util.List;

public class AdminService {

    //---------------- GIANLUCA ---------------------
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    //-----------------------------------------------

    //----------------- MATTEO ----------------------

    private PodcastMongo podcastMongo;
    private QueryMongo queryMongo;

    public AdminService() {
        this.podcastMongo = new PodcastMongo();
        this.queryMongo = new QueryMongo();
    }

    void updateAverageAgeOfUsersPerFavouriteCategory() {}

    void updatePodcastsWithHighestNumberOfReviews() {}

    void updateCountryWithHighestNumberOfPodcasts() {}

    void updateTopFavouriteCategory() {}

    void updateMostNumerousCategory() {}

    void updateMostAppreciatedCategory() {}

    public int updatePodcastsWithHighestAverageRating(int limit) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // get the updated values
        List<Pair<Podcast, Float>> results = this.podcastMongo.showPodcastsWithHighestAverageRating(limit);

        // check the result
        if (results != null && !results.isEmpty()) {
            // update the values in Mongo collection
            boolean resAdd = this.queryMongo.updatePodcastsWithHighestAverageRating(results, new Date());

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

    void updatePodcastsWithHighestAverageRatingInCountry() {}

    void updateMostFollowedAuthor() {}

    void updateMostLikedPodcast() {}

    //-----------------------------------------------
}
