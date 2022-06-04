package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AuthorMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.ReviewMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import org.javatuples.Pair;
import java.util.List;

public class AdminService {

    //---------------- GIANLUCA ---------------------
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    private AuthorMongo authorMongoManager;
    private PodcastMongo podcastMongoManager;
    private PodcastNeo4j podcastNeo4jManager;
    private AuthorNeo4j authorNeo4jManager;
    private UserNeo4j userNeo4jManager;
    private ReviewMongo reviewMongoManager;

    public AdminService() {
        this.authorMongoManager = new AuthorMongo();
        this.authorNeo4jManager = new AuthorNeo4j();
        this.userNeo4jManager = new UserNeo4j();
        this.podcastMongoManager = new PodcastMongo();
        this.podcastNeo4jManager = new PodcastNeo4j();
        this.reviewMongoManager = new ReviewMongo();
    }

    public void loadAuthorProfile(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        author.copy(foundAuthor);

        // Getting the authors followed by the author visited
        List<Author> followedAuthor = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit);
        for (Author a: followedAuthor)
            followed.add(new Pair<>(a, false));

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
    }

    public int deleteAuthor(Author authorToDelete) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // TODO: rollback
        int deleteResult = 0;

        // Check if author exists
        if (authorMongoManager.findAuthorByName(authorToDelete.getName()) == null) {
            deleteResult = -1;
            Logger.error("Author don't found!");
        } else {
            // Delete podcasts of the author on mongo
            int deletedPodcast = podcastMongoManager.deletePodcastsByAuthorName(authorToDelete.getName());
            Logger.success("Deleted " + deletedPodcast + " podcasts!");

            if(deletedPodcast < 0) {
                deleteResult = -2;
                Logger.error("Podcasts don't deleted");
            } else {
                // Deleting author on both databases
                if (!(authorMongoManager.deleteAuthorByName(authorToDelete.getName())
                        && authorNeo4jManager.deleteAuthor(authorToDelete.getName())))  {
                    Logger.error(authorToDelete.getName() + " deleted failed!");
                    deleteResult = -3;
                } else {
                    Logger.success(authorToDelete.getName() + " deleted successfully!");

                    // Delete podcasts on neo4j
                    for (Podcast podcast : authorToDelete.getOwnPodcasts()) {
                        // Delete podcasts removes all the relationships as well (detach mode)
                        if (!podcastNeo4jManager.deletePodcastByPodcastId(podcast.getId())) {
                            Logger.error("Error deleting podcast neo4j");
                            deleteResult = -4;
                            break;
                        } else {
                            Logger.success("Deleted " + reviewMongoManager.deleteReviewsByPodcastId(podcast.getId()) + " reviews associated to " + podcast.getName() + " (" + podcast.getId() + ") ");
                        }
                    }
                }
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();

        return deleteResult;
    }

    public int deletePodcast(String authorId, String podcastId) {
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
