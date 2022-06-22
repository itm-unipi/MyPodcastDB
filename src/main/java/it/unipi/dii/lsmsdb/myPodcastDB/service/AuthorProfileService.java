package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.FollowedAuthorCache;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.WatchlistCache;
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

import java.util.ArrayList;
import java.util.List;

public class AuthorProfileService {
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

    /******* User Service ********/
    public boolean loadAuthorProfileAsUser(Author author, List<Author> followedAuthorsByAuthor, int limit) {
        Logger.info("Load author profile as user");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        boolean loadResult;

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        if (foundAuthor == null) {
            Logger.error(author.getName() + " not found!");
            loadResult = false;
        } else {
            Logger.info("Author requested found: " + author.getName());
            author.copy(foundAuthor);

            // Getting the authors followed by the author visited
            List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, 0);
            if (followedAuthors != null)
                followedAuthorsByAuthor.addAll(followedAuthors);

            loadResult = true;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return loadResult;
    }

    public boolean loadAuthorProfileAsUnregistered(Author author, List<Author> followedAuthorsByAuthor, int limit) {
        Logger.info("Load author profile as unregistered user");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        boolean loadResult;

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        if (foundAuthor == null) {
            Logger.error(author.getName() + " not found!");
            loadResult = false;
        } else {
            Logger.info("Author requested found: " + author.getName());
            author.copy(foundAuthor);

            // Getting the authors followed by the author visited
            List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, 0);
            if (followedAuthors != null)
                followedAuthorsByAuthor.addAll(followedAuthors);

            loadResult = true;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return loadResult;
    }

    public boolean loadFollowedAuthorsAsUser(Author author, List<Author> followedAuthorsByAuthor, int limit, int skip) {
        Logger.info("Retrieving followed authors");
        Neo4jManager.getInstance().openConnection();

        // Getting the authors followed by the author visited
        List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, skip);
        if (followedAuthors != null)
            followedAuthorsByAuthor.addAll(followedAuthors);

        boolean noMoreAuthors = (followedAuthors == null || followedAuthors.size() < limit);
        Logger.info("No more authors: " + noMoreAuthors);

        Neo4jManager.getInstance().closeConnection();
        return noMoreAuthors;
    }

    public boolean loadFollowedAuthorsAsUnregistered(Author author, List<Author> followedAuthorsByAuthor, int limit, int skip) {
        Logger.info("Retrieving followed authors");
        Neo4jManager.getInstance().openConnection();

        List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, skip);
        if (followedAuthors != null)
            followedAuthorsByAuthor.addAll(followedAuthors);

        boolean noMoreAuthors = (followedAuthors == null || followedAuthors.size() < limit);
        Logger.info("No more authors: " + noMoreAuthors);

        Neo4jManager.getInstance().closeConnection();
        return noMoreAuthors;
    }

    public boolean followAuthorAsUser(Author author) {
        Neo4jManager.getInstance().openConnection();
        boolean result = userNeo4jManager.addUserFollowAuthor(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), author.getName());

        if (result) {
            Logger.success("(User) You started following " + author.getName());

            // Updating cache
            FollowedAuthorCache.addAuthor(author);
            Logger.info("Added in cache " + author);
        } else {
            Logger.error("(User) Error during the following operation");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public boolean unfollowAuthorAsUser(Author author) {
        Neo4jManager.getInstance().openConnection();
        boolean result = userNeo4jManager.deleteUserFollowAuthor(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), author.getName());

        if (result) {
            Logger.success("(User) You unfollowed " + author.getName());

            // Updating cache
            FollowedAuthorCache.removeAuthor(author.getName());
            Logger.info("Removed from cache " + author);
        } else {
            Logger.error("(User) Error during the unfollowing operation");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public boolean addPodcastInWatchlist(Podcast podcast) {
        Neo4jManager.getInstance().openConnection();
        boolean result = userNeo4jManager.addUserWatchLaterPodcast(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), podcast.getId());

        if (result) {
            Logger.success("Podcast added into the watchlist: " + podcast);

            // Updating cache
            WatchlistCache.addPodcast(podcast);
            Logger.info("Podcast added in cache " + podcast);
            Logger.info("Podcast in watchlist: " + WatchlistCache.getAllPodcastsInWatchlist().toString());
        } else {
            Logger.error("Error during the creation of the relationship");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public boolean removePodcastFromWatchlist(Podcast podcast) {
        Neo4jManager.getInstance().openConnection();
        boolean result = userNeo4jManager.deleteUserWatchLaterPodcast(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), podcast.getId());

        if (result) {
            Logger.success("Podcast removed from the watchlist: " + podcast);

            // Updating cache
            WatchlistCache.removePodcast(podcast.getId());
            Logger.info("Podcast removed from the cache ");
            Logger.info("Podcast in watchlist: " + WatchlistCache.getAllPodcastsInWatchlist().toString());
        } else {
            Logger.error("Error during the delete of the relationship");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    /****** Author Service ********/
    public boolean loadAuthorProfileAsPageOwner(Author author, List<Author> followedAuthorsByAuthor, int limit) {
        Logger.success("Load author profile as page owner");
        MongoManager.getInstance().openConnection();
        boolean loadResult;

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        if (foundAuthor == null) {
            Logger.error(author.getName() + " not found!");
            loadResult = false;
        } else {
            Logger.info("Author requested found: " + author.getName());
            author.copy(foundAuthor);

            // Followed authors are already in the cache if the author is the page owner
            List<Author> followedAuthors = FollowedAuthorCache.getAllFollowedAuthors();
            if (!followedAuthors.isEmpty())
                followedAuthorsByAuthor.addAll(followedAuthors);

            loadResult = true;
        }

        MongoManager.getInstance().closeConnection();
        return loadResult;
    }

    public boolean loadAuthorProfileAsAuthor(Author author, List<Author> followedAuthorsByAuthor, int limit) {
        Logger.info("Load author profile as author");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        boolean loadResult;

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        if (foundAuthor == null) {
            Logger.error(author.getName() + " not found!");
            loadResult = false;
        } else {
            Logger.info("Author requested found: " + author.getName());
            author.copy(foundAuthor);

            // Getting the authors followed by the author visited
            List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, 0);
            if (followedAuthors != null)
                followedAuthorsByAuthor.addAll(followedAuthors);

            loadResult = true;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return loadResult;
    }

    public boolean loadFollowedAuthorsAsAuthor(Author author, List<Author> followedAuthorsByAuthor, int limit, int skip) {
        Logger.info("Retrieving more followed authors");
        Neo4jManager.getInstance().openConnection();

        // Getting the authors followed by the author visited
        List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, skip);
        if (followedAuthors != null)
            followedAuthorsByAuthor.addAll(followedAuthors);

        boolean noMoreAuthors = (followedAuthors == null || followedAuthors.size() < limit);
        Logger.info("No more authors: " + noMoreAuthors);

        Neo4jManager.getInstance().closeConnection();
        return noMoreAuthors;
    }

    public boolean followAuthorAsAuthor(Author author) {
        Neo4jManager.getInstance().openConnection();
        boolean result = authorNeo4jManager.addAuthorFollowsAuthor(((Author) (MyPodcastDB.getInstance().getSessionActor())).getName(), author.getName());

        if (result) {
            Logger.success("(Author) You started following " + author.getName());

            // Updating cache
            FollowedAuthorCache.addAuthor(author);
            Logger.info("Added in cache " + author);
        } else {
            Logger.error("(Author) Error during the following operation");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public boolean unfollowAuthorAsAuthor(Author author) {
        Neo4jManager.getInstance().openConnection();
        boolean result = authorNeo4jManager.deleteAuthorFollowsAuthor(((Author) (MyPodcastDB.getInstance().getSessionActor())).getName(), author.getName());

        if (result) {
            Logger.success("(Author) You unfollowed " + author.getName());

            // Updating cache
            FollowedAuthorCache.removeAuthor(author.getName());
            Logger.info("Removed from cache " + author);
        } else {
            Logger.error("(Author) Error during the unfollowing operation");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public int updateAuthorAsAuthor(Author oldAuthor, Author newAuthor) {
        Logger.info("Update author service started");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        int updateResult = 0;

        if (!oldAuthor.getName().equals(newAuthor.getName()) && authorMongoManager.findAuthorByName(newAuthor.getName()) != null) {
            Logger.error("Author name already exists!");
            updateResult = -1;
        }

        if (!oldAuthor.getEmail().equals(newAuthor.getEmail()) && authorMongoManager.findAuthorByEmail(newAuthor.getEmail()) != null) {
            Logger.error("Email already associated to an account!");
            updateResult = updateResult - 2;
        }

        if (updateResult == 0) {
            if (!authorMongoManager.updateAuthor(newAuthor)) {
                Logger.error("Error during the update of the author on Mongo");
                updateResult = -4; // -3
            } else if (!authorNeo4jManager.updateAuthor(oldAuthor.getName(), newAuthor.getName(), newAuthor.getPicturePath())) {
                Logger.error("Error during the update of the author on Neo4J");
                updateResult = -5;
                updateAuthorRollback(updateResult, oldAuthor, newAuthor);
            } else if (!oldAuthor.getName().equals(newAuthor.getName())) {
                // if author name changed it is necessary to update all the podcasts of that author (if there are any)
                if (!podcastMongoManager.updateAllPodcasts(oldAuthor.getName(), newAuthor.getName())) {
                    updateResult = -6;
                    updateAuthorRollback(updateResult, oldAuthor, newAuthor);
                }

                if (updateResult == 0) {
                    // Updating information of the author object (java)
                    List<Podcast> podcasts = new ArrayList<>();
                    for (Podcast podcast : oldAuthor.getOwnPodcasts()) {
                        podcast.setAuthorName(newAuthor.getName());
                        podcasts.add(podcast);
                    }

                    newAuthor.setOwnPodcasts(podcasts);
                }
            }
        }

        if (updateResult == 0)
            Logger.success("(" + newAuthor.getName() + ") Author updated successfully!");

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return updateResult;
    }

    public int deleteAccountAsAuthor(Author author) {
        Logger.info("Deleting account as author");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        int deleteResult = 0;

        if (!authorMongoManager.deleteAuthorByName(author.getName())) {
            Logger.error(author.getName() + " deleted failed! (Mongo)");
            deleteResult = -1;
        } else if (!authorNeo4jManager.deleteAuthor(author.getName())) {
            Logger.error(author.getName() + " deleted failed! (Neo4J)");
            deleteResult = -2;
            deleteAuthorRollback(deleteResult, author);
        } else if (!podcastNeo4jManager.deletePodcastsOfAuthor(author.getName())) {
            // Delete podcasts on Neo4J. All the relationships will be deleted as well (detach mode).
            Logger.error("Error deleting podcasts of author on Neo4J");
            deleteResult = -3;
            deleteAuthorRollback(deleteResult, author);
        } else {
            // Delete podcasts of the author on Mongo
            int deletedPodcast = podcastMongoManager.deletePodcastsByAuthorName(author.getName());
            Logger.info("Deleted " + deletedPodcast + " podcasts!");

            if (deletedPodcast < 0) {
                deleteResult = -4;
                deleteAuthorRollback(deleteResult, author);
            } else {
                int deletedReviews = reviewMongoManager.deleteReviewsByAuthorUsername(author.getName());

                if (deletedReviews < 0) {
                    deleteResult = -5;
                    Logger.info("Erorr during the review's delete");
                } else {
                    Logger.info("Deleted " + deletedReviews + " reviews associated to " + author.getName() + "'s podcasts");
                }
            }
        }

        if (deleteResult == 0)
            Logger.success("(" + author.getName() + ") Author deleted successfully!");

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return deleteResult;
    }

    public int addPodcastAsAuthor(Podcast podcast, List<Podcast> reducedPodcast) {
        Logger.info("Adding a new podcast");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        int addResult = 0;

        // To avoid the creation a podcast with the same name
        boolean duplicate = false;
        for (Podcast pod : reducedPodcast) {
            if (pod.getName().equals(podcast.getName())) {
                duplicate = true;
                break;
            }
        }

        if (duplicate) {
            Logger.info("A podcast with the same name already exists.");
            addResult = -2;
        } else if (!podcastMongoManager.addPodcast(podcast)) {
            // Adding a new podcast on Mongo
            addResult = -1;
        } else if (!authorMongoManager.addPodcastToAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getId(), podcast)) {
            // Adding embedded podcast on Mongo
            addResult = -2;
            addPodcastRollback(addResult, podcast);
        } else if (!podcastNeo4jManager.addPodcast(podcast)) {
            // Adding the new podcast on Neo4J
            addResult = -3;
            addPodcastRollback(addResult, podcast);
        } else if (!podcastNeo4jManager.addPodcastCreatedByAuthor(podcast)) {
            // Adding "created by" relationship
            addResult = -4;
            addPodcastRollback(addResult, podcast);
        } else if (!podcastNeo4jManager.addPodcastBelongsToCategory(podcast, podcast.getPrimaryCategory())) {
            // Adding "belongs to" primary category relationship
            addResult = -5;
            addPodcastRollback(addResult, podcast);
        } else {
            // Adding secondary categories
            for (String category : podcast.getCategories()) {
                // To avoid duplicated relationship
                if (!category.equals(podcast.getPrimaryCategory())) {
                    if (!podcastNeo4jManager.addPodcastBelongsToCategory(podcast, category)) {
                        addResult = -6;
                        addPodcastRollback(addResult, podcast);
                        break;
                    }
                }
            }
        }

        if (addResult == 0)
            Logger.success("(" + podcast.getName() + ", " + podcast.getId() + ") Podcast added successfully!");

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return addResult;
    }

    public int deletePodcastAsAuthor(String podcastId) {
        Logger.info("Delete podcast as author");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        int deleteResult = 0;
        // Retrieving the entire podcast to perform an eventual rollback
        Podcast podcast = podcastMongoManager.findPodcastById(podcastId);

        if (podcast == null) {
            Logger.error("Podcast not found!");
            deleteResult = -1;
        } else if (!podcastMongoManager.deletePodcastById(podcastId)) {
            // Delete podcast from mongo
            Logger.error(podcastId + " deleted failed! (Mongo)");
            deleteResult = -2;
        } else if (!authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getName(), podcastId)) {
            // Update author embedded podcasts removing the podcast
            Logger.error("Error during the update of the embedded podcasts");
            deleteResult = -3;
            deletePodcastRollback(deleteResult, podcast);
        } else if (!podcastNeo4jManager.deletePodcastByPodcastId(podcastId)) {
            // Delete podcast from Neo4J (with all its relationships)
            Logger.error(podcastId + " deleted failed! (Neo4J)");
            deleteResult = -4;
            deletePodcastRollback(deleteResult, podcast);
        } else {
            // Delete reviews associated to that podcast
            int deletedReviews = reviewMongoManager.deleteReviewsByPodcastId(podcastId);
            Logger.info("Deleted " + deletedReviews + " reviews associated to podcastId " + podcastId);

            if (deletedReviews < 0) {
                Logger.error("Error during the delete of reviews");
                deleteResult = -5;
                deletePodcastRollback(deleteResult, podcast);
            }
        }

        if (deleteResult == 0)
            Logger.success(podcastId + " deleted successfully!");

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return deleteResult;
    }

    /****** Admin Service ******/
    public boolean loadAuthorProfileAsAdmin(Author author, List<Author> followedAuthorsByAuthor, int limit) {
        Logger.info("Load author profile as admin");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        boolean loadResult;

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        if (foundAuthor == null) {
            Logger.error(author.getName() + " not found!");
            loadResult = false;
        } else {
            Logger.info("Author requested found: " + author.getName());
            author.copy(foundAuthor);

            // Getting the authors followed by the author visited
            List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, 0);
            if (followedAuthors != null)
                followedAuthorsByAuthor.addAll(followedAuthors);

            loadResult = true;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return loadResult;
    }

    public boolean loadFollowedAuthorsAsAdmin(Author author, List<Author> followedAuthorsByAuthor, int limit, int skip) {
        Logger.info("Retrieving more followed authors");
        Neo4jManager.getInstance().openConnection();

        List<Author> followedAuthors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, skip);
        if (followedAuthors != null)
            followedAuthorsByAuthor.addAll(followedAuthors);

        boolean noMoreAuthors = (followedAuthors == null || followedAuthors.size() < limit);
        Logger.info("No more authors: " + noMoreAuthors);

        Neo4jManager.getInstance().closeConnection();
        return noMoreAuthors;
    }

    public int deleteAuthorAsAdmin(Author authorToDelete) {
        Logger.info("Deleting author account as admin");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        int deleteResult = 0;

        if (!authorMongoManager.deleteAuthorByName(authorToDelete.getName())) {
            Logger.error(authorToDelete.getName() + " deleted failed! (Mongo)");
            deleteResult = -1;
        } else if (!authorNeo4jManager.deleteAuthor(authorToDelete.getName())) {
            Logger.error(authorToDelete.getName() + " deleted failed! (Neo4J)");
            deleteResult = -2;
            deleteAuthorRollback(deleteResult, authorToDelete);
        } else if (!podcastNeo4jManager.deletePodcastsOfAuthor(authorToDelete.getName())) {
            // Delete podcasts on Neo4J. All the relationships will be deleted as well (detach mode).
            Logger.error("Error deleting podcasts of author on Neo4J");
            deleteResult = -3;
            deleteAuthorRollback(deleteResult, authorToDelete);
        } else {
            // Delete podcasts of the author on Mongo
            int deletedPodcast = podcastMongoManager.deletePodcastsByAuthorName(authorToDelete.getName());
            Logger.info("Deleted " + deletedPodcast + " podcasts!");

            if (deletedPodcast < 0) {
                deleteResult = -4;
                deleteAuthorRollback(deleteResult, authorToDelete);
            } else {
                int deletedReviews = reviewMongoManager.deleteReviewsByAuthorUsername(authorToDelete.getName());

                if (deletedReviews < 0) {
                    deleteResult = -5;
                    Logger.info("Error during the review's delete");
                } else {
                    Logger.success("Deleted " + deletedReviews + " reviews associated to " + authorToDelete.getName() + "'s podcasts");
                }
            }
        }

        if (deleteResult == 0)
            Logger.info("(" + authorToDelete.getName() + ") Author deleted successfully!");

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance(). closeConnection();
        return deleteResult;
    }

    /********* Rollback Functions *********/
    void updateAuthorRollback(int result, Author oldAuthor, Author newAuthor) {
        if (result <= -5) {
            Logger.info("Rollback due to the failure on Neo4J");
            authorMongoManager.updateAuthor(oldAuthor);
        }

        if (result <= -6) {
            Logger.info("Rollback due to the failure of the update podcast on Mongo");
            authorNeo4jManager.updateAuthor(newAuthor.getName(), oldAuthor.getName(), oldAuthor.getPicturePath());
        }
    }

    void deleteAuthorRollback(int result, Author author) {
        if (result <= -2) {
            Logger.info("Rollback due to the failure of the delete author on Neo4J");
            authorMongoManager.addAuthor(author);
        }

        if (result <= -3) {
            Logger.info("Rollback due to the failure of the podcasts' delete on Neo4J");
            authorNeo4jManager.addAuthor(author.getName(), author.getPicturePath());

            // Restoring "created by" relationships because they were deleted when the author was deleted on Neo4J
            if (result == -3) {
                for (Podcast podcast : author.getOwnPodcasts()) {
                    // Own podcasts list doesn't have the author name as parameter and it is necessary to restore the relationship on Neo4J.
                    podcast.setAuthorName(author.getName());
                    podcastNeo4jManager.addPodcastCreatedByAuthor(podcast);
                }
            }
        }

        if (result <= -4) {
            Logger.info("Rollback due to the failure of the podcasts' delete on Mongo");

            for (Podcast podcast : author.getOwnPodcasts()) {
                // Retrieving the entire podcast from Mongo in order to restore all the "created by" and "belongs to" relationships on Neo4J
                podcastMongoManager.findPodcastById(podcast.getId());

                // Restoring the podcast on neo4J
                podcastNeo4jManager.addPodcast(podcast);

                // Restoring the "created by" relationship
                podcastNeo4jManager.addPodcastCreatedByAuthor(podcast);

                // Restoring all the "belongs to" relationships
                podcastNeo4jManager.addPodcastBelongsToCategory(podcast, podcast.getPrimaryCategory());
                for (String category : podcast.getCategories()) {
                    podcastNeo4jManager.addPodcastBelongsToCategory(podcast, category);
                }
            }
        }

        if (result <= -5) {
            Logger.info("Rollback due to the failure of the reviews' delete on Mongo");
            authorMongoManager.addAuthor(author);
            authorNeo4jManager.addAuthor(author.getName(), author.getPicturePath());

            for (Podcast podcast : author.getOwnPodcasts()) {
                // Retrieving the entire podcast from Mongo in order to restore all the "created by" and "belongs to" relationships on Neo4J
                podcastMongoManager.findPodcastById(podcast.getId());

                // Restoring the podcast on neo4J
                podcastNeo4jManager.addPodcast(podcast);

                // Restoring the "created by" relationship
                podcastNeo4jManager.addPodcastCreatedByAuthor(podcast);

                // Restoring all the "belongs to" relationships
                podcastNeo4jManager.addPodcastBelongsToCategory(podcast, podcast.getPrimaryCategory());
                for (String category : podcast.getCategories()) {
                    podcastNeo4jManager.addPodcastBelongsToCategory(podcast, category);
                }
            }
        }
    }

    void addPodcastRollback(int result, Podcast podcast) {
        if (result <= -2) {
            Logger.info("Rollback due to the failure of the embedded podcast's add on Mongo");
            podcastMongoManager.deletePodcastById(podcast.getId());
        }

        if (result <= -3) {
            Logger.info("Rollback due to the failure of the podcast's add on Neo4J");
            authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getName(), podcast.getId());
        }

        if (result <= -4) {
            Logger.info("Rollback due to the failure of the \"created by\" author or \"belongs to\" category on Neo4J");
            podcastNeo4jManager.deletePodcastByPodcastId(podcast.getId());
        }
    }

    void deletePodcastRollback(int result, Podcast podcast) {
        if (result <= -3) {
            Logger.info("Rollback due to the failure of the embedded podcast's deleted on Mongo");
            podcastMongoManager.addPodcast(podcast);
        }

        if (result <= -4) {
            Logger.info("Rollback due to the failure of the podcast's delete on Neo4J");
            authorMongoManager.addPodcastToAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getId(), podcast);
        }

        if (result <= -5) {
            Logger.info("Rollback due to the failure of the reviews' delete on Mongo");
            // Adding podcast on Neo4J
            podcastNeo4jManager.addPodcast(podcast);

            // Restoring the "created by" relationship
            podcastNeo4jManager.addPodcastCreatedByAuthor(podcast);

            // Restoring all the "belongs to" relationships
            podcastNeo4jManager.addPodcastBelongsToCategory(podcast, podcast.getPrimaryCategory());
            for (String category : podcast.getCategories()) {
                podcastNeo4jManager.addPodcastBelongsToCategory(podcast, category);
            }
        }
    }
}
