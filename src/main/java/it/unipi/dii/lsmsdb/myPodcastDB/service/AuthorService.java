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
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

public class AuthorService {

    //---------------- GIANLUCA ---------------------
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------

    private AuthorMongo authorMongoManager;
    private AuthorNeo4j authorNeo4jManager;
    private PodcastMongo podcastMongoManager;
    private PodcastNeo4j podcastNeo4jManager;
    private ReviewMongo reviewMongoManager;

    public AuthorService() {
        this.authorMongoManager = new AuthorMongo();
        this.authorNeo4jManager = new AuthorNeo4j();
        this.podcastMongoManager = new PodcastMongo();
        this.podcastNeo4jManager = new PodcastNeo4j();
        this.reviewMongoManager = new ReviewMongo();
    }

    public void loadAuthorOwnProfile(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        Logger.success("Retrieving followed authors");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        if (foundAuthor == null) {
            Logger.error("Author not found!");
        } else {
            author.copy(foundAuthor);

            List<Author> followedAuthor = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit, 0);
            for (Author a: followedAuthor)
                followed.add(new Pair<>(a, true));
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
    }

    public boolean loadAuthorProfile(Author author, List<Pair<Author, Boolean>> followed, int limit) {
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
            for (Author a: followedAuthor) {
                boolean following = authorNeo4jManager.findAuthorFollowsAuthor(((Author) (MyPodcastDB.getInstance().getSessionActor())).getName(), a.getName());
                followed.add(new Pair<>(a, following));
            }

            MongoManager.getInstance().closeConnection();
            Neo4jManager.getInstance().closeConnection();

            return followingAuthor;
        }
    }

    public boolean loadOwnFollowedAuthors(Author author, List<Pair<Author, Boolean>> followed, int limit, int skip) {
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

    public boolean loadFollowedAuthors(Author author, List<Pair<Author, Boolean>> followed, int limit, int skip) {
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

    public void followAuthor(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (authorNeo4jManager.addAuthorFollowsAuthor(((Author) (MyPodcastDB.getInstance().getSessionActor())).getName(), authorName))
            Logger.success("(Author) You started following " + authorName);
        else
            Logger.error("(Author) Error during the following operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public void unfollowAuthor(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (authorNeo4jManager.deleteAuthorFollowsAuthor(((Author) (MyPodcastDB.getInstance().getSessionActor())).getName(), authorName))
            Logger.success("(Author) You unfollowed " + authorName);
        else
            Logger.error("(Author) Error during the unfollowing operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public int updateAuthor(Author oldAuthor, Author newAuthor) {
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

    public int addPodcast(Podcast podcast) {
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

    public int deletePodcast(String podcastId) {
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

    public int deleteAccount() {
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

    public void search(String searchText, List<Podcast> podcastsMatch, List<Pair<Author, Boolean>> authorsMatch, List<Pair<User, Boolean>> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Searching for podcasts
        if (filters.getValue0()) {
            List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit, 0);
            if (podcasts != null)
                podcastsMatch.addAll(podcasts);
        }

        // Searching for authors
        if (filters.getValue1()) {
            List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, 0);
            for (Author authorFound : authors) {
                boolean followingAuthor = authorNeo4jManager.findAuthorFollowsAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getName(), authorFound.getName());
                authorsMatch.add(new Pair<>(authorFound, followingAuthor));
            }
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }

    public void loadHomepage(List<Triplet<Podcast, Float, Boolean>> topRated, List<Pair<Podcast, Integer>> mostLikedPodcasts, List<Triplet<Author, Integer, Boolean>> mostFollowedAuthors, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Loading Top Rated Podcasts
        List<Triplet<Podcast, Float, Boolean>> resultsTopRated = new ArrayList<>();
        resultsTopRated.add(new Triplet<>(new Podcast("9e3816413fb3430e714c91fc", "Run Amok with Rex and Chuck", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts115/v4/a6/e0/06/a6e006e3-869d-8d05-bdfd-f3bc7282b5f0/mza_5306896777310247561.jpg/600x600bb.jpg"), 4.0f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("fce2e20fe832f5f5e6148987", "Ingenious Basterds", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts3/v4/3d/16/af/3d16af60-2e1d-69bd-c3d3-b83bebdc0bd3/mza_5286960706166095957.jpg/600x600bb.jpg"), 5.0f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("ed70b4056b9415b7b43b4a52", "Strangers in Space", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts115/v4/5f/9d/d4/5f9dd483-a0fc-e81c-ce59-aeaea09be6c3/mza_8658599441396400725.jpg/600x600bb.jpg"), 4.7f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("35628503b2788fdcb7a1e145", "Orchard Recording Studio Podcast", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts113/v4/b9/3c/48/b93c4823-474e-edf5-c9ad-73025a01176e/mza_2537085863686121083.jpg/600x600bb.jpg"), 4.5f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("b8f759d95d3ed9d1e12f9430", "To The Batpoles! Batman 1966", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/f8/f8/a1/f8f8a158-24b8-091a-fc43-213739b4bbb1/mza_16028619290499021301.jpg/600x600bb.jpg"), 4.65f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("6b70d0098bb6c382ff41a274", "Hurtscast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts6/v4/e3/83/18/e38318f0-98c1-db5f-de31-a2d937401bbe/mza_2491539306600660338.jpg/600x600bb.jpg"), 4.87f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("35628503b2788fdcb7a1e145", "Orchard Recording Studio Podcast", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts113/v4/b9/3c/48/b93c4823-474e-edf5-c9ad-73025a01176e/mza_2537085863686121083.jpg/600x600bb.jpg"), 4.5f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("b8f759d95d3ed9d1e12f9430", "To The Batpoles! Batman 1966", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/f8/f8/a1/f8f8a158-24b8-091a-fc43-213739b4bbb1/mza_16028619290499021301.jpg/600x600bb.jpg"), 4.65f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("6b70d0098bb6c382ff41a274", "Hurtscast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts6/v4/e3/83/18/e38318f0-98c1-db5f-de31-a2d937401bbe/mza_2491539306600660338.jpg/600x600bb.jpg"), 4.87f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("35628503b2788fdcb7a1e145", "Orchard Recording Studio Podcast", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts113/v4/b9/3c/48/b93c4823-474e-edf5-c9ad-73025a01176e/mza_2537085863686121083.jpg/600x600bb.jpg"), 4.5f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("b8f759d95d3ed9d1e12f9430", "To The Batpoles! Batman 1966", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/f8/f8/a1/f8f8a158-24b8-091a-fc43-213739b4bbb1/mza_16028619290499021301.jpg/600x600bb.jpg"), 4.65f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("6b70d0098bb6c382ff41a274", "Hurtscast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts6/v4/e3/83/18/e38318f0-98c1-db5f-de31-a2d937401bbe/mza_2491539306600660338.jpg/600x600bb.jpg"), 4.87f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("9e3816413fb3430e714c91fc", "Run Amok with Rex and Chuck", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts115/v4/a6/e0/06/a6e006e3-869d-8d05-bdfd-f3bc7282b5f0/mza_5306896777310247561.jpg/600x600bb.jpg"), 4.0f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("fce2e20fe832f5f5e6148987", "Ingenious Basterds", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts3/v4/3d/16/af/3d16af60-2e1d-69bd-c3d3-b83bebdc0bd3/mza_5286960706166095957.jpg/600x600bb.jpg"), 5.0f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("ed70b4056b9415b7b43b4a52", "Strangers in Space", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts115/v4/5f/9d/d4/5f9dd483-a0fc-e81c-ce59-aeaea09be6c3/mza_8658599441396400725.jpg/600x600bb.jpg"), 4.7f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("35628503b2788fdcb7a1e145", "Orchard Recording Studio Podcast", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts113/v4/b9/3c/48/b93c4823-474e-edf5-c9ad-73025a01176e/mza_2537085863686121083.jpg/600x600bb.jpg"), 4.5f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("b8f759d95d3ed9d1e12f9430", "To The Batpoles! Batman 1966", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/f8/f8/a1/f8f8a158-24b8-091a-fc43-213739b4bbb1/mza_16028619290499021301.jpg/600x600bb.jpg"), 4.65f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("6b70d0098bb6c382ff41a274", "Hurtscast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts6/v4/e3/83/18/e38318f0-98c1-db5f-de31-a2d937401bbe/mza_2491539306600660338.jpg/600x600bb.jpg"), 4.87f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("35628503b2788fdcb7a1e145", "Orchard Recording Studio Podcast", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts113/v4/b9/3c/48/b93c4823-474e-edf5-c9ad-73025a01176e/mza_2537085863686121083.jpg/600x600bb.jpg"), 4.5f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("b8f759d95d3ed9d1e12f9430", "To The Batpoles! Batman 1966", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/f8/f8/a1/f8f8a158-24b8-091a-fc43-213739b4bbb1/mza_16028619290499021301.jpg/600x600bb.jpg"), 4.65f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("6b70d0098bb6c382ff41a274", "Hurtscast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts6/v4/e3/83/18/e38318f0-98c1-db5f-de31-a2d937401bbe/mza_2491539306600660338.jpg/600x600bb.jpg"), 4.87f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("35628503b2788fdcb7a1e145", "Orchard Recording Studio Podcast", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts113/v4/b9/3c/48/b93c4823-474e-edf5-c9ad-73025a01176e/mza_2537085863686121083.jpg/600x600bb.jpg"), 4.5f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("b8f759d95d3ed9d1e12f9430", "To The Batpoles! Batman 1966", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/f8/f8/a1/f8f8a158-24b8-091a-fc43-213739b4bbb1/mza_16028619290499021301.jpg/600x600bb.jpg"), 4.65f, false));
        resultsTopRated.add(new Triplet<>(new Podcast("6b70d0098bb6c382ff41a274", "Hurtscast", "https://is5-ssl.mzstatic.com/image/thumb/Podcasts6/v4/e3/83/18/e38318f0-98c1-db5f-de31-a2d937401bbe/mza_2491539306600660338.jpg/600x600bb.jpg"), 4.87f, false));

        if (resultsTopRated != null)
            topRated.addAll(resultsTopRated);

        // Loading Most Liked Podcasts
        List<Pair<Podcast, Integer>> resultsMostLikedPodcasts = new ArrayList<>();
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("956e4ed48c7856b7f57f71ad", "Into the Black", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts115/v4/82/e2/c1/82e2c132-2275-25e7-d0af-30b34110c892/mza_12455054422451763750.jpg/600x600bb.jpg"), 567));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("76f2901e32ba8672e3ee121b", "Bearded Ruckus", "https://is1-ssl.mzstatic.com/image/thumb/Podcasts123/v4/c1/0d/ee/c10dee1f-8d15-47cb-a9bf-28adcbd22ea7/mza_6387225195375893490.jpg/600x600bb.jpg"), 1767));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("416618a9d2bb7c74c7a5b779", "Podcast But Outside", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts116/v4/43/2e/4f/432e4f2a-3363-74fe-88c7-8a4e5866e419/mza_14697479480010952634.jpg/600x600bb.jpg"), 667));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("5bc82a037b1cd1e4c21ad4e6", "Sermons at St. Nicholas", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts123/v4/bc/26/61/bc2661c8-8cdb-c1e5-61cf-4929b6ba43a7/mza_7303017220217427746.jpg/600x600bb.jpg"), 555));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("f49f9d110c037f1f39a668fc", "HOLD UP! We need to talk", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts118/v4/16/71/de/1671de46-df8b-9430-5b79-156954a147fe/mza_979148902187170876.jpg/600x600bb.jpg"), 853));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("7f7f64a07837778030d8afc6", "Breaking Average", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/b5/8a/49/b58a49be-2621-a98b-f94a-9c4d62fc3a3c/mza_4437068013901004299.jpg/600x600bb.jpg"), 745));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("46f7ac981cdefd61b4f8683d", "Angry Dad Podcast", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts125/v4/df/9e/8f/df9e8f0b-a576-0897-1715-c7870a33f12d/mza_14276480442415215195.jpg/600x600bb.jpg"), 30));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("416618a9d2bb7c74c7a5b779", "Podcast But Outside", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts116/v4/43/2e/4f/432e4f2a-3363-74fe-88c7-8a4e5866e419/mza_14697479480010952634.jpg/600x600bb.jpg"), 667));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("5bc82a037b1cd1e4c21ad4e6", "Sermons at St. Nicholas", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts123/v4/bc/26/61/bc2661c8-8cdb-c1e5-61cf-4929b6ba43a7/mza_7303017220217427746.jpg/600x600bb.jpg"), 555));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("f49f9d110c037f1f39a668fc", "HOLD UP! We need to talk", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts118/v4/16/71/de/1671de46-df8b-9430-5b79-156954a147fe/mza_979148902187170876.jpg/600x600bb.jpg"), 853));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("7f7f64a07837778030d8afc6", "Breaking Average", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/b5/8a/49/b58a49be-2621-a98b-f94a-9c4d62fc3a3c/mza_4437068013901004299.jpg/600x600bb.jpg"), 745));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("46f7ac981cdefd61b4f8683d", "Angry Dad Podcast", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts125/v4/df/9e/8f/df9e8f0b-a576-0897-1715-c7870a33f12d/mza_14276480442415215195.jpg/600x600bb.jpg"), 30));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("416618a9d2bb7c74c7a5b779", "Podcast But Outside", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts116/v4/43/2e/4f/432e4f2a-3363-74fe-88c7-8a4e5866e419/mza_14697479480010952634.jpg/600x600bb.jpg"), 667));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("5bc82a037b1cd1e4c21ad4e6", "Sermons at St. Nicholas", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts123/v4/bc/26/61/bc2661c8-8cdb-c1e5-61cf-4929b6ba43a7/mza_7303017220217427746.jpg/600x600bb.jpg"), 555));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("f49f9d110c037f1f39a668fc", "HOLD UP! We need to talk", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts118/v4/16/71/de/1671de46-df8b-9430-5b79-156954a147fe/mza_979148902187170876.jpg/600x600bb.jpg"), 853));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("7f7f64a07837778030d8afc6", "Breaking Average", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/b5/8a/49/b58a49be-2621-a98b-f94a-9c4d62fc3a3c/mza_4437068013901004299.jpg/600x600bb.jpg"), 745));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("46f7ac981cdefd61b4f8683d", "Angry Dad Podcast", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts125/v4/df/9e/8f/df9e8f0b-a576-0897-1715-c7870a33f12d/mza_14276480442415215195.jpg/600x600bb.jpg"), 30));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("416618a9d2bb7c74c7a5b779", "Podcast But Outside", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts116/v4/43/2e/4f/432e4f2a-3363-74fe-88c7-8a4e5866e419/mza_14697479480010952634.jpg/600x600bb.jpg"), 667));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("5bc82a037b1cd1e4c21ad4e6", "Sermons at St. Nicholas", "https://is3-ssl.mzstatic.com/image/thumb/Podcasts123/v4/bc/26/61/bc2661c8-8cdb-c1e5-61cf-4929b6ba43a7/mza_7303017220217427746.jpg/600x600bb.jpg"), 555));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("f49f9d110c037f1f39a668fc", "HOLD UP! We need to talk", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts118/v4/16/71/de/1671de46-df8b-9430-5b79-156954a147fe/mza_979148902187170876.jpg/600x600bb.jpg"), 853));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("7f7f64a07837778030d8afc6", "Breaking Average", "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/b5/8a/49/b58a49be-2621-a98b-f94a-9c4d62fc3a3c/mza_4437068013901004299.jpg/600x600bb.jpg"), 745));
        resultsMostLikedPodcasts.add(new Pair<>(new Podcast("46f7ac981cdefd61b4f8683d", "Angry Dad Podcast", "https://is4-ssl.mzstatic.com/image/thumb/Podcasts125/v4/df/9e/8f/df9e8f0b-a576-0897-1715-c7870a33f12d/mza_14276480442415215195.jpg/600x600bb.jpg"), 30));

        if (resultsMostLikedPodcasts != null)
            mostLikedPodcasts.addAll(resultsMostLikedPodcasts);

        // Load Most Followed Author
        List<Pair<Author, Integer>> resultsMostFollowedAuthors = new ArrayList<>();
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Michael Colosi", "/img/authors/author17.png"), 1283));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Rick Girard", "/img/authors/author15.png"), 1154));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "DJ RAUL", "/img/authors/author2.png"), 1864));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Stoneface and Terminal", "/img/authors/author16.png"), 1246));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "BSGE After Hours", "/img/authors/author2.png"), 1092));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Uche", "/img/authors/author9.png"), 2021));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Andy Southern", "/img/authors/author7.png"), 1353));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Stoneface and Terminal", "/img/authors/author16.png"), 1246));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Cat Olive", "/img/authors/author2.png"), 1092));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "DC TV Podcasts", "/img/authors/author9.png"), 2021));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "tuesdaynightbull@gmail.com", "/img/authors/author7.png"), 1353));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Stoneface and Terminal", "/img/authors/author16.png"), 1246));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Intersection Education", "/img/authors/author2.png"), 1092));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Saint Iconic", "/img/authors/author9.png"), 2021));
        resultsMostFollowedAuthors.add(new Pair<>(new Author("", "Preface Podcast", "/img/authors/author1.png"), 1353));

        for (Pair<Author, Integer> author: resultsMostFollowedAuthors) {
            boolean following = authorNeo4jManager.findAuthorFollowsAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getName(), author.getValue0().getName());
            mostFollowedAuthors.add(new Triplet<>(author.getValue0(), author.getValue1(), following));
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }

    public boolean loadMorePodcasts(String searchText, List<Podcast> podcastsMatch, int limit, int skip) {
        MongoManager.getInstance().openConnection();
        boolean noMorePodcasts = false;

        List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit, skip);
        if (podcasts != null) {
            podcastsMatch.addAll(podcasts);
            noMorePodcasts = podcasts.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        return noMorePodcasts;
    }

    public boolean loadMoreAuthors(String searchText, List<Pair<Author, Boolean>> authorsMatch, int limit, int skip) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, skip);
        if (authors != null) {
            for (Author authorFound : authors) {
                boolean followingAuthor = authorNeo4jManager.findAuthorFollowsAuthor(((Author) MyPodcastDB.getInstance().getSessionActor()).getName(), authorFound.getName());
                authorsMatch.add(new Pair<>(authorFound, followingAuthor));
            }
            noMoreAuthors = authors.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();

        return noMoreAuthors;
    }
    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
