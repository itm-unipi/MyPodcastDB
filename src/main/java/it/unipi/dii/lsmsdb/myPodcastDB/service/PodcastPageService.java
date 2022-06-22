package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.LikedPodcastCache;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.WatchlistCache;
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

import java.util.ArrayList;
import java.util.List;

public class PodcastPageService {

    private PodcastMongo podcastMongo;
    private ReviewMongo reviewMongo;
    private AuthorMongo authorMongo;
    private PodcastNeo4j podcastNeo4j;
    private UserNeo4j userNeo4j;

    public PodcastPageService() {
        this.podcastMongo = new PodcastMongo();
        this.reviewMongo  = new ReviewMongo();
        this.authorMongo  = new AuthorMongo();
        this.podcastNeo4j = new PodcastNeo4j();
        this.userNeo4j = new UserNeo4j();
    }

    public Boolean loadPodcastPageForUsers(Podcast podcast, Boolean[] status) {
        Logger.info("Load Podcast Page Service for User");

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

            // check if like and watchlater are in cache
            boolean watchlaterInCache = false;
            boolean likeInCache = false;
            if (WatchlistCache.getPodcast(podcast.getId()) != null)
                watchlaterInCache = true;
            if (LikedPodcastCache.getPodcast(podcast.getId()) != null)
                likeInCache = true;

            Logger.info("watchlater in cache : " + watchlaterInCache);
            Logger.info("like in cache : " + likeInCache);

            // get like and watchlater status (from cache or from db)
            String username = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
            status[0] = watchlaterInCache || this.userNeo4j.checkUserWatchLaterPodcastExists(username, podcast.getId());
            status[1] = likeInCache || this.userNeo4j.checkUserLikesPodcastExists(username, podcast.getId());

            // update cache if needed
            if (!watchlaterInCache && status[0])
                WatchlistCache.addPodcast(podcast);
            if (!likeInCache && status[1])
                LikedPodcastCache.addPodcast(podcast);

            // check the result
            if (status[0] == null || status[1] == null) {
                Logger.error("Like and watchlater info not found");
                result = false;
            } else {
                Logger.success("Load Podcast Page Service for User ended");
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public Boolean loadPodcastPageForNotUser(Podcast podcast) {
        Logger.info("Load Podcast Page Service for not User");

        MongoManager.getInstance().openConnection();
        Boolean result = true;

        // search the podcast
        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcast.getId());
        if (foundPodcast == null) {
            Logger.error("Podcast requested not found");
            result = false;
        } else {
            Logger.success("Load Podcast Page Service for not User ended");
            podcast.copy(foundPodcast);
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public Boolean setWatchLater(Podcast podcast, Boolean newStatus) {
        Logger.info("Set Watchlater Service");

        Neo4jManager.getInstance().openConnection();

        // find if it has to add or delete the relation
        String username = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        Boolean result;
        if (newStatus)
            result = this.userNeo4j.addUserWatchLaterPodcast(username, podcast.getId());
        else
            result = this.userNeo4j.deleteUserWatchLaterPodcast(username, podcast.getId());

        // check the result of operation
        if (result) {
            if (newStatus) {
                // update cache
                WatchlistCache.addPodcast(podcast);
                Logger.success("Added to watchlater");
            } else {
                // update cache
                WatchlistCache.removePodcast(podcast.getId());
                Logger.success("Removed from watchlater");
            }
        } else {
            Logger.error("Watchlater status not modified");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public Boolean setLike(Podcast podcast, Boolean newStatus) {
        Logger.info("Set Like Service");

        Neo4jManager.getInstance().openConnection();

        // find if it has to add or delete the relation
        String username = ((User)MyPodcastDB.getInstance().getSessionActor()).getUsername();
        Boolean result;
        if (newStatus)
            result = this.userNeo4j.addUserLikesPodcast(username, podcast.getId());
        else
            result = this.userNeo4j.deleteUserLikesPodcast(username, podcast.getId());

        // check the result of operation
        if (result) {
            if (newStatus) {
                // update cache
                LikedPodcastCache.addPodcast(podcast);
                Logger.success("Liked podcast");
            } else {
                // update cache
                LikedPodcastCache.removePodcast(podcast.getId());
                Logger.success("Unliked podcast");
            }
        } else {
            Logger.error("Like status not modified");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public int updatePodcast(Podcast oldPodcast, Podcast newPodcast) {
        Logger.info("Update Podcast Service");

        MongoManager.getInstance().openConnection();
        int result = 0;

        // list for an eventual rollback of relations on Neo4J
        List<String> addedCategories = new ArrayList<>();
        List<String> removedCategories = new ArrayList<>();

        // check if is needed to update reduced podcast
        Boolean updateReduced = false;
        if (!oldPodcast.getName().equals(newPodcast.getName()) || !oldPodcast.getReleaseDate().equals(newPodcast.getReleaseDate()) || !oldPodcast.getPrimaryCategory().equals(newPodcast.getPrimaryCategory()) || !oldPodcast.getArtworkUrl600().equals(newPodcast.getArtworkUrl600())) {
            updateReduced = true;
        }

        // check if is needed to update Neo4J (update name/artwork/categories)
        Boolean updateNeo = false;
        if (!oldPodcast.getName().equals(newPodcast.getName()) || !oldPodcast.getArtworkUrl600().equals(newPodcast.getArtworkUrl600())) {
            updateNeo = true;
        }

        // check if is needed to update relations in Neo4J (categories)
        Boolean updateRelNeo = false;
        if (!oldPodcast.getPrimaryCategory().equals(newPodcast.getPrimaryCategory()) || !oldPodcast.getCategories().equals(newPodcast.getCategories())) {
            updateRelNeo = true;
        }

        // update podcast on mongo
        Boolean res = this.podcastMongo.updatePodcast(newPodcast);
        if (!res) {
            // reset the podcast object to the old status (for interface update)
            Logger.error("Podcast not updated on MongoDB");
            newPodcast.copy(oldPodcast);
            result = -1;
        } else {
            // update reduced podcast if needed
            if (updateReduced) {
                Boolean resUpRed = this.authorMongo.updatePodcastOfAuthor(newPodcast.getAuthorName(), newPodcast);
                if (!resUpRed) {
                    Logger.error("Reduced podcast not updated");
                    result = -2;
                }
            }

            // update Neo4J if needed
            if (result == 0 && (updateNeo || updateRelNeo)) {
                Neo4jManager.getInstance().openConnection();

                // update podcast in Neo4J if needed
                boolean resUpNeo = true;
                if (updateNeo)
                    resUpNeo = this.podcastNeo4j.updatePodcast(newPodcast);

                if (!resUpNeo) {
                    Logger.error("Podcast not updated on Neo4J");
                    result = -3;
                }

                // update relations in neo if needed
                if (result == 0 && updateRelNeo) {
                    // get the list of relation to add/remove
                    List<String> catToAdd = new ArrayList<>();
                    List<String> catToRem = new ArrayList<>();
                    getDifferencesBetweenCategories(oldPodcast, newPodcast, catToAdd, catToRem);

                    // add new categories
                    for (String category : catToAdd) {
                        boolean resAddRel = this.podcastNeo4j.addPodcastBelongsToCategory(newPodcast, category);

                        // check result
                        if (resAddRel) {
                            addedCategories.add(category);
                        } else {
                            result = -4;
                            break;
                        }
                    }

                    // remove old relations
                    if (result != 0) {
                        Logger.error("Failed to add a category on Neo4J");
                    } else {
                        // remove old categories
                        for (String category : catToRem) {
                            boolean resRemRel = this.podcastNeo4j.deletePodcastBelongsToCategory(newPodcast.getId(), category);

                            // check result
                            if (resRemRel) {
                                removedCategories.add(category);
                            } else {
                                result = -5;
                                break;
                            }
                        }

                        // check the result
                        if (result != 0) {
                            Logger.error("Failed to remove a category on Neo4J");
                        }
                    }
                }

                // if there are no error close the connection
                if (result == 0)
                    Neo4jManager.getInstance().closeConnection();
            }

            if (result == 0)
                Logger.success("Podcast updated");
        }

        // rollback if process failed
        if (result != 0) {
            rollbackUpdatePodcast(result, oldPodcast, updateReduced, addedCategories, removedCategories);

            // close Neo4J connection if needed
            if (updateNeo || updateRelNeo)
                Neo4jManager.getInstance().closeConnection();
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int deletePodcast(Podcast podcast) {
        Logger.info("Delete Podcast Service");

        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        int result = 0;

        // delete podcast entity in mongo
        Boolean resDelPod = this.podcastMongo.deletePodcastById(podcast.getId());
        if (!resDelPod) {
            Logger.error("Podcast not deleted");
            result = -1;
        }

        // remove reduced podcast from author
        else {
            Boolean resDelRedPod = this.authorMongo.deletePodcastOfAuthor(podcast.getAuthorName(), podcast.getId());
            if (!resDelRedPod) {
                Logger.error("Reduced podcast not deleted from author");
                result = -2;
            } else {
                // remove podcast entity from neo4j
                Boolean resDelNeo = this.podcastNeo4j.deletePodcastByPodcastId(podcast.getId());
                if (!resDelNeo) {
                    Logger.error("Podcast not deleted from Neo4J");
                    result = -3;
                } else {
                    // remove review of podcast
                    int resDelRew = this.reviewMongo.deleteReviewsByPodcastId(podcast.getId());
                    if (resDelRew == -1) {
                        Logger.error("Reviews of podcast not deleted");
                        result = -4;
                    } else {
                        Logger.success("Podcast deleted");
                    }
                }
            }
        }

        // rollback if process failed
        if (result != 0)
            rollbackDeletePodcast(result, podcast);

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public int addEpisode(Podcast podcast, Episode episode) {
        Logger.info("Add Episode Service");

        MongoManager.getInstance().openConnection();
        int result = 0;

        // check if episode already exists
        Boolean exists = false;
        for (Episode ep : podcast.getEpisodes()) {
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
                podcast.copy(podcast);
                result = -3;
            }
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int deleteEpisode(Podcast podcast, Episode episode) {
        Logger.info("Delete Episode Service");

        MongoManager.getInstance().openConnection();
        int result = 0;

        // check if episode exists
        if (!podcast.getEpisodes().contains(episode)) {
            Logger.success("Episode found");
            result = -1;
        }

        // update podcast
        else {
            Boolean res = this.podcastMongo.deleteEpisodeOfPodcast(podcast.getId(), episode.getName());
            if (res) {
                podcast.deleteEpisode(episode);
                Logger.success("Episode deleted");
            } else {
                Logger.error("Episode not deleted");
                result = -2;
            }
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    // utility that find the differences between the old and the new categories after the podcast update
    private void getDifferencesBetweenCategories(Podcast oldPodcast, Podcast newPodcast, List<String> catToAdd, List<String> catToRem) {
        // old list of category
        List<String> oldCategories = new ArrayList<>(oldPodcast.getCategories());
        if (!oldCategories.contains(oldPodcast.getPrimaryCategory()))
            oldCategories.add(oldPodcast.getPrimaryCategory());

        // old list of category
        List<String> newCategory = new ArrayList<>(newPodcast.getCategories());
        if (!newCategory.contains(newPodcast.getPrimaryCategory()))
            newCategory.add(newPodcast.getPrimaryCategory());

        // find the categories to add
        for (String category : newCategory)
            if (!oldCategories.contains(category))
                catToAdd.add(category);

        // find the categories to remove
        for (String category : oldCategories)
            if (!newCategory.contains(category))
                catToRem.add(category);

        Logger.info("Categories that have to be added : " + catToAdd);
        Logger.info("Categories that have to be removed : " + catToRem);
    }

    private void rollbackUpdatePodcast(int result, Podcast oldPodcast, boolean updateReduced, List<String> addedCategories, List<String> removedCategories) {
        // failed to remove a category
        if (result == -5) {
            // rollback all the removed categories before fail
            for (String category : removedCategories)
                this.podcastNeo4j.addPodcastBelongsToCategory(oldPodcast, category);
        }

        // failed to add a category
        if (result <= -4) {
            // rollback the podcast in Neo4J
            this.podcastNeo4j.updatePodcast(oldPodcast);

            // rollback all the added categories before fail
            for (String category : addedCategories)
                this.podcastNeo4j.deletePodcastBelongsToCategory(oldPodcast.getId(), category);
        }

        // failed to update podcast on Neo4J
        if (result <= -3 && updateReduced) {
            // rollback the reduced podcast in author
            this.authorMongo.updatePodcastOfAuthor(oldPodcast.getAuthorName(), oldPodcast);
        }

        // failed to update reduced podcast in author
        if (result <= -2) {
            // rollback the podcast
            this.podcastMongo.updatePodcast(oldPodcast);
        }
    }

    private void rollbackDeletePodcast(int result, Podcast podcast) {
        // failed to remove review of podcast
        if (result == -4) {
            // rollback the neo4j entity
            this.podcastNeo4j.addPodcast(podcast);

            // rollback all recoverable relationship
            this.podcastNeo4j.addPodcastCreatedByAuthor(podcast);
            this.podcastNeo4j.addPodcastBelongsToCategory(podcast, podcast.getPrimaryCategory());
            for (String cat : podcast.getCategories())
                this.podcastNeo4j.addPodcastBelongsToCategory(podcast, cat);
        }

        // failed to remove podcast entity from neo4j
        if (result <= -3) {
            // rollback the podcast embedded in author
            this.authorMongo.addPodcastToAuthor(podcast.getAuthorName(), podcast);
        }

        // failed to remove reduced podcast from author
        if (result <= -2) {
            // rollback the podcast
            this.podcastMongo.addPodcast(podcast);
        }
    }
}
