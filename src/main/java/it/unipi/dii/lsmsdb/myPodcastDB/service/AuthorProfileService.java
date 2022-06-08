package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AuthorMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.ReviewMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Pair;

import java.util.List;

public class AuthorProfileService {

    //---------------- GIANLUCA ---------------------
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------

    private AuthorMongo authorMongoManager;
    private AuthorNeo4j authorNeo4jManager;
    private PodcastMongo podcastMongoManager;
    private PodcastNeo4j podcastNeo4jManager;
    private ReviewMongo reviewMongoManager;
    private UserNeo4j userNeo4jManager;

    public AuthorProfileService() {
        this.authorMongoManager = new AuthorMongo();
        this.authorNeo4jManager = new AuthorNeo4j();
        this.podcastMongoManager = new PodcastMongo();
        this.podcastNeo4jManager = new PodcastNeo4j();
        this.reviewMongoManager = new ReviewMongo();
        this.userNeo4jManager = new UserNeo4j();
    }

    /******* USER ********/
    public boolean loadAuthorProfileAsUser(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        boolean followingAuthor;

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        if (foundAuthor == null) {
            Logger.error("Author not found!");
            return false;
        } else {
            author.copy(foundAuthor);

            // Checking if the user follows the visited author
            followingAuthor = userNeo4jManager.findUserFollowsAuthor(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), author.getName());

            // Getting the authors followed by the author visited
            List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, 0);
            if (followedAuthors != null) {
                for (Author followedAuthor : followedAuthors) {
                    boolean following = userNeo4jManager.findUserFollowsAuthor(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), followedAuthor.getName());
                    followed.add(new Pair<>(followedAuthor, following));
                }
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();

        return followingAuthor;
    }

    public void loadAuthorProfileAsUnregistered(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        if (foundAuthor == null) {
            Logger.error("Author not found!");
        } else {
            author.copy(foundAuthor);

            // Getting the authors followed by the author visited
            List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, 0);
            if (followedAuthors != null) {
                for (Author followedAuthor : followedAuthors)
                    followed.add(new Pair<>(followedAuthor, false));
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
    }

    public boolean loadFollowedAuthorsAsUser(Author author, List<Pair<Author, Boolean>> followed, int limit, int skip) {
        Logger.info("Retrieving followed authors");
        Neo4jManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, skip);
        if (followedAuthors != null) {
            for (Author followedAuthor: followedAuthors) {
                boolean following = userNeo4jManager.findUserFollowsAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), followedAuthor.getName());
                followed.add(new Pair<>(followedAuthor, following));
            }

            noMoreAuthors = followedAuthors.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();

        return noMoreAuthors;
    }

    public boolean loadFollowedAuthorsAsUnregistered(Author author, List<Pair<Author, Boolean>> followed, int limit, int skip) {
        Logger.info("Retrieving followed authors");
        Neo4jManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, skip);
        if (followedAuthors != null) {
            for (Author followedAuthor: followedAuthors)
                followed.add(new Pair<>(followedAuthor, false));

            noMoreAuthors = followedAuthors.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();

        return noMoreAuthors;
    }

    public void followAuthorAsUser(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (userNeo4jManager.addUserFollowAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), authorName))
            Logger.success("(User) You started following " + authorName);
        else
            Logger.error("(User) Error during the following operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public void unfollowAuthorAsUser(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (userNeo4jManager.deleteUserFollowAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), authorName))
            Logger.success("(User) You unfollowed " + authorName);
        else
            Logger.error("(User) Error during the unfollowing operation");

        Neo4jManager.getInstance().closeConnection();
    }

    /****** AUTHOR ********/

    public void loadAuthorProfileAsPageOwner(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        Logger.success("Retrieving followed authors");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        if (foundAuthor == null) {
            Logger.error("Author not found!");
        } else {
            author.copy(foundAuthor);

            List<Author> followedAuthor = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, 0);
            if (followedAuthor != null)
                for (Author a: followedAuthor)
                    followed.add(new Pair<>(a, true));
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
    }

    public boolean loadAuthorProfileAsAuthor(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        if (foundAuthor == null) {
            Logger.error("Author not found!");

            MongoManager.getInstance().closeConnection();
            Neo4jManager.getInstance().closeConnection();

            return false;
        } else {
            author.copy(foundAuthor);

            // Checking if the "session" author follows the requested author
            boolean followingAuthor = authorNeo4jManager.findAuthorFollowsAuthor(((Author) (MyPodcastDB.getInstance().getSessionActor())).getName(), foundAuthor.getName());

            // Getting the authors followed by the author requested
            List<Author> followedAuthor = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, 0);
            if (followedAuthor != null) {
                for (Author a: followedAuthor) {
                    boolean following = authorNeo4jManager.findAuthorFollowsAuthor(((Author) (MyPodcastDB.getInstance().getSessionActor())).getName(), a.getName());
                    followed.add(new Pair<>(a, following));
                }
            }

            MongoManager.getInstance().closeConnection();
            Neo4jManager.getInstance().closeConnection();

            return followingAuthor;
        }
    }

    public boolean loadFollowedAuthorsAsPageOwner(Author author, List<Pair<Author, Boolean>> followed, int limit, int skip) {
        Logger.info("Retrieving followed authors");
        Neo4jManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, skip);
        if (followedAuthors != null) {
            for (Author a : followedAuthors)
                followed.add(new Pair<>(a, true));
            noMoreAuthors = followedAuthors.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();

        return noMoreAuthors;
    }

    public boolean loadFollowedAuthorsAsAuthor(Author author, List<Pair<Author, Boolean>> followed, int limit, int skip) {
        Neo4jManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, skip);
        if (followedAuthors != null) {
            for (Author a : followedAuthors) {
                boolean following = authorNeo4jManager.findAuthorFollowsAuthor(((Author) (MyPodcastDB.getInstance().getSessionActor())).getName(), a.getName());
                followed.add(new Pair<>(a, following));
            }
            noMoreAuthors = followedAuthors.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();

        return noMoreAuthors;
    }

    public void followAuthorAsAuthor(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (authorNeo4jManager.addAuthorFollowsAuthor(((Author) (MyPodcastDB.getInstance().getSessionActor())).getName(), authorName))
            Logger.success("(Author) You started following " + authorName);
        else
            Logger.error("(Author) Error during the following operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public void unfollowAuthorAsAuthor(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (authorNeo4jManager.deleteAuthorFollowsAuthor(((Author) (MyPodcastDB.getInstance().getSessionActor())).getName(), authorName))
            Logger.success("(Author) You unfollowed " + authorName);
        else
            Logger.error("(Author) Error during the unfollowing operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public int updateAuthorAsAuthor(Author oldAuthor, Author newAuthor) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // TODO: check if author exists

        // 0: no update done
        int updateResult = 0;

        if (!oldAuthor.getName().equals(newAuthor.getName()) && authorMongoManager.findAuthorByName(newAuthor.getName()) != null) {
            Logger.error("Author name already exists!");
            updateResult = -1;
        }

        if (!oldAuthor.getEmail().equals(newAuthor.getEmail()) && authorMongoManager.findAuthorByEmail(newAuthor.getEmail()) != null) {
            Logger.error("Email already associated to an account!");
            updateResult = -2;
        }

        if (updateResult == 0) {
            if (authorMongoManager.updateAuthor(newAuthor)
                    && authorNeo4jManager.updateAuthor(oldAuthor.getName(), newAuthor.getName(), newAuthor.getPicturePath())) {

                if (!oldAuthor.getName().equals(newAuthor.getName())) {
                    List<Podcast> authorPodcasts = podcastMongoManager.findPodcastsByAuthorId(newAuthor.getId(), 0);
                    for (Podcast podcast : authorPodcasts) {
                        podcast.setAuthor(newAuthor.getId(), newAuthor.getName());
                        if (!podcastMongoManager.updatePodcast(podcast)) {
                            updateResult = -3;
                            break;
                        }
                    }
                }

                Logger.success("Author updated with success!");
                updateResult = 1;

            } else {
                Logger.error("Error during the update operation");
                updateResult = -4;
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();

        return updateResult;
    }

    public int deleteAccountAsAuthor() {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // TODO: rollback
        int deleteResult = 0;
        Author authorToDelete = (Author) MyPodcastDB.getInstance().getSessionActor();

        // Check if author exists
        if(authorMongoManager.findAuthorByName(authorToDelete.getName())==null) {
            deleteResult = -1;
            Logger.error("Author don't found!");
        } else {
            // Delete podcasts of the author on mongo
            int deletedPodcast = podcastMongoManager.deletePodcastsByAuthorName(authorToDelete.getName());
            Logger.success("Deleted " + deletedPodcast + " podcasts!");

            if (deletedPodcast < 0) {
                deleteResult = -2;
                Logger.error("Podcasts don't deleted");
            } else {
                // Deleting author on both databases
                if (!(authorMongoManager.deleteAuthorByName(authorToDelete.getName())
                        && authorNeo4jManager.deleteAuthor(authorToDelete.getName()))) {
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
        Neo4jManager.getInstance(). closeConnection();

        return deleteResult;
    }

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
    public void loadAuthorProfileAsAdmin(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        if (foundAuthor == null) {
            Logger.error("Author not found!");
        } else {
            author.copy(foundAuthor);

            // Getting the authors followed by the author visited
            List<Author> followedAuthor = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, 0);
            if (followedAuthor != null) {
                for (Author a : followedAuthor)
                    followed.add(new Pair<>(a, false));
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
    }

    public boolean loadFollowedAuthorsAsAdmin(Author author, List<Pair<Author, Boolean>> followed, int limit, int skip) {
        Logger.info("Retrieving followed authors");
        Neo4jManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, skip);
        if (followedAuthors != null) {
            for (Author followedAuthor: followedAuthors)
                followed.add(new Pair<>(followedAuthor, false));
            noMoreAuthors = followedAuthors.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();

        return noMoreAuthors;
    }

    public int deleteAuthorAsAdmin(Author authorToDelete) {
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
