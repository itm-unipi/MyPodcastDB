package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
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

    public Boolean loadPodcastPageForUsers(Podcast podcast, Boolean[] status) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        Boolean result = true;

        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcast.getId());
        if (foundPodcast == null) {
            Logger.error("Podcast requested not found");
            result = false;
        } else {
            // update the local object
            podcast.copy(foundPodcast);

            // get like and watchlater status
            String username = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
            status[0] = this.userNeo4j.checkUserWatchLaterPodcastExists(username, podcast.getId());
            status[1] = this.userNeo4j.checkUserLikesPodcastExists(username, podcast.getId());
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public Boolean loadPodcastPageForNotUser(Podcast podcast) {
        MongoManager.getInstance().openConnection();
        Boolean result = true;

        // search the podcast
        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcast.getId());
        if (foundPodcast == null) {
            Logger.error("Podcast requested not found");
            result = false;
        } else {
            podcast.copy(foundPodcast);
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public Boolean setWatchLater(String podcastId, Boolean newStatus) {
        Neo4jManager.getInstance().openConnection();

        // find if it has to add or delete the relation
        String username = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        Boolean result;
        if (newStatus)
            result = this.userNeo4j.addUserWatchLaterPodcast(username, podcastId);
        else
            result = this.userNeo4j.deleteUserWatchLaterPodcast(username, podcastId);

        // check the result of operation
        if (result) {
            if (newStatus)
                Logger.success("Added to watchlater");
            else
                Logger.success("Removed from watchlater");
        } else {
            Logger.error("Watchlater status not modified");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public Boolean setLike(String podcastId, Boolean newStatus) {
        Neo4jManager.getInstance().openConnection();

        // find if it has to add or delete the relation
        String username = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        Boolean result;
        if (newStatus)
            result = this.userNeo4j.addUserLikesPodcast(username, podcastId);
        else
            result = this.userNeo4j.deleteUserLikesPodcast(username, podcastId);

        // check the result of operation
        if (result) {
            if (newStatus)
                Logger.success("Liked podcast");
            else
                Logger.success("Unliked podcast");
        } else {
            Logger.error("Like status not modified");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public int updatePodcast(Podcast podcast) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // check if podcast exists
        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcast.getId());
        if (foundPodcast == null) {
            Logger.error("Podcast to update not found");
            result = 1;
        }

        // update podcast
        else {
            Boolean res = this.podcastMongo.updatePodcast(podcast);
            if (res) {
                Logger.success("Podcast updated");
            } else {
                // reset the podcast object to the old status (for interface update)
                Logger.error("Podcast not updated");
                podcast.copy(foundPodcast);
                result = 2;
            }
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int deletePodcast(String podcastId) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // check if podcast exists
        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcastId);
        if (foundPodcast == null) {
            Logger.error("Podcast to delete not found");
            result = 1;
        }

        // update podcast
        else {
            Boolean res = this.podcastMongo.deletePodcastById(podcastId);
            if (res) {
                Logger.success("Podcast deleted");
            } else {
                Logger.error("Podcast not deleted");
                result = 2;
            }
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int addEpisode(Podcast podcast, Episode episode) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // check if podcast exists
        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcast.getId());
        if (foundPodcast == null) {
            Logger.error("Episode's podcast not found");
            result = 1;
        }

        // check if episode already exists
        else if (podcast.getEpisodes().contains(episode)) {
            Logger.error("Episode already exists");
            result = 2;
        }

        // update podcast
        else {
            podcast.addEpisode(episode);
            Boolean res = this.podcastMongo.updatePodcast(podcast);
            if (res) {
                Logger.success("Episode added");
            } else {
                // reset the podcast object to the old status (for interface update)
                Logger.error("Episode not added");
                podcast.copy(foundPodcast);
                result = 3;
            }
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int deleteEpisode(Podcast podcast, Episode episode) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // check if podcast exists
        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcast.getId());
        if (foundPodcast == null) {
            Logger.error("Episode's podcast not found");
            result = 1;
        }

        // check if episode exists
        else if (!podcast.getEpisodes().contains(episode)) {
            Logger.success("Episode found");
            result = 2;
        }

        // update podcast
        else {
            Boolean res = this.podcastMongo.deleteEpisodeOfPodcast(podcast.getId(), episode.getName());
            if (res) {
                podcast.getEpisodes().remove(episode);
                Logger.success("Podcast deleted");
            } else {
                Logger.error("Podcast not deleted");
                result = 3;
            }
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    //-----------------------------------------------
}
