package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

public class PodcastService {

    //---------------- GIANLUCA ---------------------
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    //-----------------------------------------------

    //----------------- MATTEO ----------------------

    private PodcastMongo podcastMongo;
    private UserNeo4j userNeo4j;

    public PodcastService() {
        this.podcastMongo = new PodcastMongo();
        this.userNeo4j = new UserNeo4j();
    }

    public void loadPodcastPageForUsers(Podcast podcast, Boolean watchlatered, Boolean liked) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcast.getId());
        podcast.copy(foundPodcast);

        String username = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        watchlatered = this.userNeo4j.checkUserWatchLaterPodcastExists(username, podcast.getId());
        liked = this.userNeo4j.checkUserLikesPodcastExists(username, podcast.getId());

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
    }

    public void loadPodcastPageForNotUser(Podcast podcast) {
        MongoManager.getInstance().openConnection();

        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcast.getId());
        podcast.copy(foundPodcast);

        MongoManager.getInstance().closeConnection();
    }

    public void setWatchLater(String podcastId, Boolean newStatus) {
        Neo4jManager.getInstance().openConnection();

        // find if it have to add or delete the relation
        String username = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        Boolean result;
        if (newStatus)
            result = this.userNeo4j.addUserWatchLaterPodcast(username, podcastId);
        else
            result = this.userNeo4j.deleteUserWatchLaterPodcast(username, podcastId);

        // check the result of operation
        if (result)
            Logger.success("Watchlater status modified");
        else
            Logger.error("Watchlater status not modified");

        Neo4jManager.getInstance().closeConnection();
    }

    public void setLike(String podcastId, Boolean newStatus) {
        Neo4jManager.getInstance().openConnection();

        // find if it have to add or delete the relation
        String username = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        Boolean result;
        if (newStatus)
            result = this.userNeo4j.addUserLikesPodcast(username, podcastId);
        else
            result = this.userNeo4j.deleteUserLikesPodcast(username, podcastId);

        // check the result of operation
        if (result)
            Logger.success("Like status modified");
        else
            Logger.error("Like status not modified");

        Neo4jManager.getInstance().closeConnection();
    }

    public void updatePodcast(Podcast podcast) {

    }

    public void deletePodcast(String podcastId) {

    }

    //-----------------------------------------------
}
