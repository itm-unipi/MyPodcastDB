package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Episode;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AuthorMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.ReviewMongo;
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
    private ReviewMongo reviewMongo;
    private AuthorMongo authorMongo;
    private PodcastNeo4j podcastNeo4j;
    private UserNeo4j userNeo4j;

    public PodcastService() {
        this.podcastMongo = new PodcastMongo();
        this.reviewMongo  = new ReviewMongo();
        this.authorMongo  = new AuthorMongo();
        this.podcastNeo4j = new PodcastNeo4j();
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
            result = -1;
        }

        // update podcast
        else {
            // check if is needed to update reduced podcast
            Boolean updateReduced = false;
            if (!foundPodcast.getName().equals(podcast.getName()) || !foundPodcast.getReleaseDate().equals(podcast.getReleaseDate()) || !foundPodcast.getPrimaryCategory().equals(podcast.getPrimaryCategory()) || !foundPodcast.getArtworkUrl600().equals(podcast.getArtworkUrl600())) {
                updateReduced = true;
            }

            // check if is needed to update Neo4J
            Boolean updateNeo = false;
            if (!foundPodcast.getName().equals(podcast.getName()) || !foundPodcast.getArtworkUrl600().equals(podcast.getArtworkUrl600())) {
                updateNeo = true;
            }

            // update podcast on mongo
            Boolean res = this.podcastMongo.updatePodcast(podcast);
            if (!res) {
                // reset the podcast object to the old status (for interface update)
                Logger.error("Podcast not updated on MongoDB");
                podcast.copy(foundPodcast);
                result = -2;
            } else {
                // update reduced podcast if needed
                if (updateReduced) {
                    int index = 0;
                    Boolean resUpRed = this.authorMongo.updatePodcastOfAuthor(podcast.getAuthorId(), index, podcast.getId(), podcast.getName(), podcast.getReleaseDateAsString(), podcast.getPrimaryCategory(), podcast.getArtworkUrl600());
                    if (!resUpRed) {
                        Logger.error("Reduced podcast not updated");
                        result = -3;
                    }
                }

                // update Neo4J if needed
                if (result == 0 && updateNeo) {
                    Neo4jManager.getInstance().openConnection();
                    Boolean resUpNeo = this.podcastNeo4j.updatePodcast(podcast.getId(), podcast.getName(), podcast.getArtworkUrl600());
                    if (!resUpNeo) {
                        Logger.error("Podcast not updated on Neo4J");
                        result = -4;
                    }
                    Neo4jManager.getInstance().closeConnection();
                }

                if (result == 0)
                    Logger.success("Podcast updated");
            }
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int deletePodcast(Podcast podcast) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        int result = 0;

        // check if podcast exists
        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcast.getId());
        if (foundPodcast == null || this.podcastNeo4j.findPodcastByPodcastId(podcast.getId()) == null) {
            Logger.error("Podcast to delete not found in all databases");
            result = -1;
        }

        // delete podcast
        else {
            // delete podcast entity in mongo
            Boolean resDelPod = this.podcastMongo.deletePodcastById(podcast.getId());
            if (!resDelPod) {
                Logger.error("Podcast not deleted");
                result = -2;
            }

            // remove reduced podcast from author
            else {
                Boolean resDelRedPod = this.authorMongo.deletePodcastOfAuthor(podcast.getAuthorId(), podcast.getId());
                if (!resDelRedPod) {
                    Logger.error("Reduced podcast not deleted from author");
                    result = -3;
                } else {
                    // remove podcast entity from neo4j
                    Boolean resDelNeo = this.podcastNeo4j.deletePodcastByPodcastId(podcast.getId());
                    if (!resDelNeo) {
                        Logger.error("Podcast not deleted from Neo4J");
                        result = -4;
                    } else {
                        // remove review of podcast
                        int resDelRew = this.reviewMongo.deleteReviewsByPodcastId(podcast.getId());
                        if (resDelRew == -1) {
                            Logger.error("Reviews of podcast not deleted");
                            result = -5;
                        } else {
                            Logger.success("Podcast deleted");
                        }
                    }
                }
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public int addEpisode(Podcast podcast, Episode episode) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // check if podcast exists
        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcast.getId());
        if (foundPodcast == null) {
            Logger.error("Episode's podcast not found");
            result = -1;
        } else {
            // check if episode already exists
            Boolean exists = false;
            for (Episode ep : foundPodcast.getEpisodes()) {
                if (ep.getName().equals(episode.getName())) {
                    exists = true;
                    break;
                }
            }

            // the episode exists
            if (exists) {
                Logger.error("Episode already exists");
                result = -2;
            }

            // update podcast
            else {
                Boolean res = this.podcastMongo.addEpisodeToPodcast(podcast.getId(), episode);
                if (res) {
                    Logger.success("Episode added");
                    podcast.addEpisode(episode);
                } else {
                    // reset the podcast object to the old status (for interface update)
                    Logger.error("Episode not added");
                    podcast.copy(foundPodcast);
                    result = -3;
                }
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
            result = -1;
        }

        // check if episode exists
        else if (!podcast.getEpisodes().contains(episode)) {
            Logger.success("Episode found");
            result = -2;
        }

        // update podcast
        else {
            Boolean res = this.podcastMongo.deleteEpisodeOfPodcast(podcast.getId(), episode.getName());
            if (res) {
                podcast.getEpisodes().remove(episode);
                Logger.success("Podcast deleted");
            } else {
                Logger.error("Podcast not deleted");
                result = -3;
            }
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    //-----------------------------------------------
}
