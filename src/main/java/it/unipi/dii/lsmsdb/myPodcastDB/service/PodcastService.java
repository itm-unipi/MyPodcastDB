package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.*;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

public class PodcastService {

    //---------------- GIANLUCA ---------------------
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    private final AuthorMongo authorMongoManager;
    private final AuthorNeo4j authorNeo4jManager;
    private final UserNeo4j userNeo4jManager;
    private final UserMongo userMongoManager;
    private final PodcastMongo podcastMongoManager;
    private final PodcastNeo4j podcastNeo4jManager;
    private final ReviewMongo reviewMongoManager;

    public PodcastService() {
        this.authorMongoManager = new AuthorMongo();
        this.authorNeo4jManager = new AuthorNeo4j();
        this.userNeo4jManager = new UserNeo4j();
        this.userMongoManager = new UserMongo();
        this.podcastMongoManager = new PodcastMongo();
        this.podcastNeo4jManager = new PodcastNeo4j();
        this.reviewMongoManager = new ReviewMongo();
    }

    /******** AUTHOR *******/
    public int addPodcastAsAuthor(Podcast podcast) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        int addResult = 0;

        // Adding podcast to mongo
        if (!podcastMongoManager.addPodcast(podcast))
            addResult = -1;
        else {
            // Adding reduced podcast
            if (!authorMongoManager.addPodcastToAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getId(), podcast)) {
                addResult = -1;

                // Rollback
                Logger.info("Rollback");
                podcastMongoManager.deletePodcastById(podcast.getId());
            } else {
                // Adding podcast to Neo4j
                if (!podcastNeo4jManager.addPodcast(podcast)) {
                    addResult = -1;

                    Logger.info("Rollback");
                    authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getId(), podcast.getId());
                    podcastMongoManager.deletePodcastById(podcast.getId());
                } else {
                    // Adding created by "author" -> "podcast"
                    if (!podcastNeo4jManager.addPodcastCreatedByAuthor(podcast)) {
                        addResult = -1;

                        Logger.info("Rollback");
                        authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getId(), podcast.getId());
                        podcastMongoManager.deletePodcastById(podcast.getId());
                        podcastNeo4jManager.deletePodcastByPodcastId(podcast.getId());
                    } else {
                        // Adding belongs to "podcast" -> "category"
                        if (!podcastNeo4jManager.addPodcastBelongsToCategory(podcast, podcast.getPrimaryCategory())) {
                            addResult = -1;

                            Logger.info("Rollback");
                            authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getId(), podcast.getId());
                            podcastMongoManager.deletePodcastById(podcast.getId());
                            // Delete podcast uses DETACH keyword so every relationship will be removed as well
                            podcastNeo4jManager.deletePodcastByPodcastId(podcast.getId());
                        } else {
                            // Adding secondary categories
                            for (String category : podcast.getCategories()) {
                                // To avoid duplicated relationship
                                if (!category.equals(podcast.getPrimaryCategory())) {
                                    if (!podcastNeo4jManager.addPodcastBelongsToCategory(podcast, category)) {
                                        addResult = -1;

                                        Logger.info("Rollback");
                                        authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getId(), podcast.getId());
                                        podcastMongoManager.deletePodcastById(podcast.getId());
                                        podcastNeo4jManager.deletePodcastByPodcastId(podcast.getId());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();

        return addResult;
    }

    public int deletePodcastAsAuthor(String podcastId) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        int deleteResult = 0;

        if (podcastMongoManager.findPodcastById(podcastId) == null) {
            Logger.error("Podcast don't found!");
            deleteResult = -1;

        } else {
            // Delete podcast entity from both databases
            if (!(podcastMongoManager.deletePodcastById(podcastId) && podcastNeo4jManager.deletePodcastByPodcastId(podcastId))) {
                Logger.error(podcastId + " deleted failed!");
                deleteResult = -2;

            } else {
                // Delete reviews associated to that podcast
                int deletedReviews = reviewMongoManager.deleteReviewsByPodcastId(podcastId);
                Logger.info("Deleted " + deletedReviews + " reviews associated to podcastId " + podcastId);

                if (deletedReviews < 0) {
                    Logger.error("Error during the delete of reviews");
                    deleteResult = -3 ;

                } else {
                    // Update author embedded podcasts
                    if (!authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getId(), podcastId)) {
                        Logger.error("Error during the update of the embedded podcasts");
                        deleteResult = -4;
                    }
                }
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();

        return deleteResult;
    }

    /****** ADMIN ******/
    public int deletePodcastAsAdmin(String authorId, String podcastId) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        int deleteResult = 0;

        if (podcastMongoManager.findPodcastById(podcastId) == null) {
            Logger.error("Podcast don't found!");
            deleteResult = -1;

        } else {
            // Delete podcast entity from both databases
            if (!(podcastMongoManager.deletePodcastById(podcastId) && podcastNeo4jManager.deletePodcastByPodcastId(podcastId))) {
                Logger.error(podcastId + " deleted failed!");
                deleteResult = -2;

            } else {
                // Delete reviews associated to that podcast
                int deletedReviews = reviewMongoManager.deleteReviewsByPodcastId(podcastId);
                Logger.info("Deleted " + deletedReviews + " reviews associated to podcastId " + podcastId);

                if (deletedReviews < 0) {
                    Logger.error("Error during the delete of reviews");
                    deleteResult = -3 ;

                } else {
                    // Update author embedded podcasts
                    if (!authorMongoManager.deletePodcastOfAuthor(authorId, podcastId)) {
                        Logger.error("Error during the update of the embedded podcasts");
                        deleteResult = -4;
                    }
                }
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();

        return deleteResult;
    }
    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
