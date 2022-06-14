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
        boolean result = userNeo4jManager.addUserFollowAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), author.getName());

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
        boolean result = userNeo4jManager.addUserWatchLaterPodcast(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), podcast.getId());

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
        boolean result = userNeo4jManager.deleteUserWatchLaterPodcast(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), podcast.getId());

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
        Logger.success("Retrieving followed authors");
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

            // Followed authors are already in the cache if the author is the page owner
            List<Author> followedAuthors = FollowedAuthorCache.getAllFollowedAuthors();
            if (!followedAuthors.isEmpty())
                followedAuthorsByAuthor.addAll(followedAuthors);

            loadResult = true;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return loadResult;
    }

    public boolean loadAuthorProfileAsAuthor(Author author, List<Author> followedAuthorsByAuthor, int limit) {
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
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
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
            if (!authorMongoManager.updateAuthor(newAuthor)) {
               Logger.error("Error during the update of the author on mongo");
               updateResult = -3;
            } else {
                if (!authorNeo4jManager.updateAuthor(oldAuthor.getName(), newAuthor.getName(), newAuthor.getPicturePath())) {
                    Logger.error("Error during the update of the author on neo4j");
                    updateResult = -4;
                    updateAuthorRollback(updateResult, oldAuthor, newAuthor);
                } else {
                    // if author name changed it is necessary to update all the podcasts of that author (if there are any)
                    if (!oldAuthor.getName().equals(newAuthor.getName())) {

                        // START WITHOUT UPDATE MANY
                        /*
                        List<Podcast> podcasts = new ArrayList<>();
                        for (Podcast podcast : oldAuthor.getOwnPodcasts()) {
                            podcast.setAuthorName(newAuthor.getName());
                            podcasts.add(podcast);

                            if (!podcastMongoManager.updatePodcast(podcast)) {
                                updateResult = -5;
                                updateAuthorRollback(updateResult, oldAuthor, newAuthor);
                                break;
                            }
                        }

                        if (updateResult == 0)
                            newAuthor.setOwnPodcasts(podcasts);
                        // END WITHOUT UPDATE MANY

                         */

                        // Start with update many:
                        List<Podcast> podcasts = new ArrayList<>();
                        for (Podcast podcast : oldAuthor.getOwnPodcasts()) {
                            podcast.setAuthorName(newAuthor.getName());
                            podcasts.add(podcast);
                        }

                        newAuthor.setOwnPodcasts(podcasts);

                        if (!podcastMongoManager.updateAllPodcasts(oldAuthor.getName(), newAuthor.getName())) {
                            updateResult = -5;
                            updateAuthorRollback(updateResult, oldAuthor, newAuthor);
                        }
                        // End with update many
                    }
                }
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return updateResult;
    }

    void updateAuthorRollback(int result, Author oldAuthor, Author newAuthor) {
        if (result <= -4) {
            Logger.info("Rollback - Undo operation because Neo4J failed");
            authorMongoManager.updateAuthor(oldAuthor);
        }

        if (result <= -5) {
            Logger.info("Rollback - Undo operation because podcast update on mongo failed");
            authorMongoManager.updateAuthor(oldAuthor);
            authorNeo4jManager.updateAuthor(newAuthor.getName(), oldAuthor.getName(), oldAuthor.getPicturePath());
        }
    }

    public int deleteAccountAsAuthor(Author author) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        int deleteResult = 0;

        if (!authorMongoManager.deleteAuthorByName(author.getName())) {
            Logger.error(author.getName() + " deleted failed! (Mongo)");
            deleteResult = -1;
        } else {
            if (!authorNeo4jManager.deleteAuthor(author.getName())) {
                Logger.error(author.getName() + " deleted failed! (Neo4J)");
                deleteResult = -2;
                deleteAuthorRollback(deleteResult, author);
            } else {
                // Delete podcasts on neo4j (all the relationships as well (detach mode))
                if (!podcastNeo4jManager.deletePodcastsOfAuthor(author.getName())) {
                    Logger.error("Error deleting podcast on Neo4J");
                    deleteResult = -3;
                    deleteAuthorRollback(deleteResult, author);
                } else {
                    // Delete podcasts of the author on mongo if the previous delete on Neo4J was completed successfully
                    int deletedPodcast = podcastMongoManager.deletePodcastsByAuthorName(author.getName());
                    Logger.info("Deleted " + deletedPodcast + " podcasts!");

                    if (deletedPodcast < 0) {
                        deleteResult = -3;
                        deleteAuthorRollback(deleteResult, author);
                    }
                }
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance(). closeConnection();
        return deleteResult;
    }

    void deleteAuthorRollback(int result, Author author) {
        if (result <= -2) {
            Logger.info("Rollback - adding the author on mongo because neo4j author delete failed");
            authorMongoManager.addAuthor(author);
        }

        if (result <= -3) {
            Logger.info("Rollback mongo and neo4j because delete of podcasts failed");

            authorMongoManager.addAuthor(author);

            authorNeo4jManager.addAuthor(author.getName(), author.getPicturePath());
            for (Podcast podcast: author.getOwnPodcasts()) {
                podcastNeo4jManager.addPodcastCreatedByAuthor(podcast);
            }
        }
    }

    public int addPodcastAsAuthor(Podcast podcast, List<Podcast> reducedPodcast) {
        Logger.info("Adding a new podcast");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        int addResult = 0;

        // To avoid the creation a podcast with the same name
        boolean duplicate = false;
        for (Podcast pod: reducedPodcast) {
            if (pod.getName().equals(podcast.getName())) {
                duplicate = true;
                break;
            }
        }

        if (duplicate) {
            Logger.info("A podcast with the same name already exists.");
            addResult = -2;
        } else {
            // Adding podcast to mongo
            if (!podcastMongoManager.addPodcast(podcast))
                addResult = -1;
            else {
                // Adding reduced podcast
                if (!authorMongoManager.addPodcastToAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getId(), podcast)) {
                    addResult = -2;
                    addPodcastRollback(addResult, podcast);
                } else {
                    // Adding podcast to Neo4j
                    if (!podcastNeo4jManager.addPodcast(podcast)) {
                        addResult = -3;
                        addPodcastRollback(addResult, podcast);
                    } else {
                        // Adding created by "author" -> "podcast"
                        if (!podcastNeo4jManager.addPodcastCreatedByAuthor(podcast)) {
                            addResult = -4;
                            addPodcastRollback(addResult, podcast);
                        } else {
                            // Adding belongs to "podcast" -> "category"
                            if (!podcastNeo4jManager.addPodcastBelongsToCategory(podcast, podcast.getPrimaryCategory())) {
                                addResult = -5;
                                addPodcastRollback(addResult, podcast);
                            } else {
                                // Adding secondary categories
                                for (String category : podcast.getCategories()) {
                                    // To avoid duplicated relationship
                                    if (!category.equals(podcast.getPrimaryCategory())) {
                                        if (!podcastNeo4jManager.addPodcastBelongsToCategory(podcast, category)) {
                                            addResult = -5;
                                            addPodcastRollback(addResult, podcast);
                                            break;
                                        }
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

    void addPodcastRollback(int result, Podcast podcast) {
        if (result <= -2) {
            Logger.info("Rollback - Deleting podcast on mongo because add reduced podcast failed");
            authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getName(), podcast.getId());
            podcastMongoManager.deletePodcastById(podcast.getId());
        }

        if (result <= -3) {
            Logger.info("Rollback - Deleting podcast and reduced podcast on mongo because neo4j failed");
            authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getName(), podcast.getId());
            podcastMongoManager.deletePodcastById(podcast.getId());
            podcastNeo4jManager.deletePodcastByPodcastId(podcast.getId());
        }

        if (result <= -4) {
            Logger.info("Rollback - Podcast created by author failed");
            authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getName(), podcast.getId());
            podcastMongoManager.deletePodcastById(podcast.getId());
            // Delete podcast uses DETACH keyword so every relationship will be removed as well
            podcastNeo4jManager.deletePodcastByPodcastId(podcast.getId());
        }

        if (result <= -5) {
            Logger.info("Rollback - Podcast belongs to category failed");
            authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getName(), podcast.getId());
            podcastMongoManager.deletePodcastById(podcast.getId());
            podcastNeo4jManager.deletePodcastByPodcastId(podcast.getId());
        }
    }

    public int deletePodcastAsAuthor(String podcastId) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        int deleteResult = 0;
        Podcast podcast = podcastMongoManager.findPodcastById(podcastId);

        if (podcast == null) {
            Logger.error("Podcast not found!");
            deleteResult = -1;
        } else {
            // Delete podcast from mongo
            if (!podcastMongoManager.deletePodcastById(podcastId)) {
                Logger.error(podcastId + " deleted failed! (Mongo)");
                deleteResult = -2;
            } else {
                // Update author embedded podcasts removing the podcast
                if (!authorMongoManager.deletePodcastOfAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getName(), podcastId)) {
                    Logger.error("Error during the update of the embedded podcasts");
                    deleteResult = -3;
                    deletePodcastRollback(deleteResult, podcast);
                } else {
                    // Delete podcast from Neo4J
                    if (!podcastNeo4jManager.deletePodcastByPodcastId(podcastId)) {
                        Logger.error(podcastId + " deleted failed! (Neo4J)");
                        deleteResult = -4;
                        deletePodcastRollback(deleteResult, podcast);
                    } else {
                        // Delete reviews associated to that podcast
                        int deletedReviews = reviewMongoManager.deleteReviewsByPodcastId(podcastId);
                        if (deletedReviews < 0) {
                            Logger.error("Error during the delete of reviews");
                            deleteResult = -5;
                            deletePodcastRollback(deleteResult, podcast);
                        } else {
                            Logger.info("Deleted " + deletedReviews + " reviews associated to podcastId " + podcastId);
                            Logger.success(podcastId + " deleted successfully!");
                        }
                    }
                }
            }
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return deleteResult;
    }

    void deletePodcastRollback(int result, Podcast podcast) {
        if (result <= -3) {
            Logger.info("Rollback - adding the podcast on mongo because embedded podcast failed");
            podcastMongoManager.addPodcast(podcast);
        }

        if (result <= -4) {
            Logger.info("Rollback mongo because delete on Neo4j failed");
            authorMongoManager.addPodcastToAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getId(), podcast);
        }

        if (result <= -5) {
            Logger.info("Rollback - adding the podcast on both databases because delete of reviews failed");
            podcastNeo4jManager.addPodcast(podcast);
        }
    }

    /****** Admin Service ******/
    public boolean loadAuthorProfileAsAdmin(Author author, List<Author> followedAuthorsByAuthor, int limit) {
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

    public int deletePodcastAsAdmin(String authorName, String podcastId) {
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
                    if (!authorMongoManager.deletePodcastOfAuthor(authorName, podcastId)) {
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

}
