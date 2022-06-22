package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.FollowedAuthorCache;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.FollowedUserCache;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.WatchlistCache;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AuthorMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.QueryMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import java.util.ArrayList;
import java.util.List;

public class HomePageService {
    private final QueryMongo queryMongoManager;
    private final AuthorNeo4j authorNeo4jManager;
    private final UserNeo4j userNeo4jManager;
    private final PodcastNeo4j podcastNeo4jManager;

    public HomePageService() {
        this.queryMongoManager = new QueryMongo();
        this.authorNeo4jManager = new AuthorNeo4j();
        this.userNeo4jManager = new UserNeo4j();
        this.podcastNeo4jManager = new PodcastNeo4j();
    }

    /***** USER HOMEPAGE ******/
    public void loadHomepageAsUser(List<Triplet<Podcast, Float, Boolean>> topRated, List<Pair<Podcast, Integer>> mostLikedPodcasts, List<Pair<Author, Integer>> mostFollowedAuthors, List<Podcast> watchlist, List<Podcast> topGenres, List<Podcast> basedOnWatchlist, List<Author> suggestedAuthors, int limit) {
        Logger.info("Loading Homepage as Registered User");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Loading Top Rated in user's country
        List<Triplet<Podcast, String, Float>> resultsTopCountry = new ArrayList<>();
        this.queryMongoManager.getPodcastWithHighestAverageRatingPerCountry(resultsTopCountry);
        for (Triplet<Podcast, String, Float> entry: resultsTopCountry) {
            if (entry.getValue1().equals(((User) MyPodcastDB.getInstance().getSessionActor()).getCountry())) {
                topRated.add(new Triplet<>(entry.getValue0(), entry.getValue2(), true));
            }
        }

        // Loading Top Rated Podcasts
        List<Pair<Podcast, Float>> resultsTopRated = new ArrayList<>();
        this.queryMongoManager.getPodcastsWithHighestAverageRating(resultsTopRated);
        for (Pair<Podcast, Float> entry: resultsTopRated)
            topRated.add(new Triplet<>(entry.getValue0(), entry.getValue1(), false));

        // Loading Most Liked Podcasts
        this.queryMongoManager.getMostLikedPodcast(mostLikedPodcasts);

        // Load Most Followed Author
        this.queryMongoManager.getMostFollowedAuthor(mostFollowedAuthors);

        List<Podcast> podcasts;
        List<Author> authors;

        // Load podcasts in watchlist from cache
        Logger.info("Podcasts in watchlist loaded in cache: " + WatchlistCache.getAllPodcastsInWatchlist().toString());
        List<Podcast> podcastsInWatchlist = WatchlistCache.getAllPodcastsInWatchlist();
        if(!podcastsInWatchlist.isEmpty())
            watchlist.addAll(podcastsInWatchlist);

        // Load your top genres
        podcasts = podcastNeo4jManager.showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), limit, 0);
        if(podcasts != null)
            topGenres.addAll(podcasts);

        // Load podcasts based on author in user's watchlist
        podcasts = podcastNeo4jManager.showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist(((User)MyPodcastDB.getInstance().getSessionActor()), limit, 0);
        if(podcasts != null)
            basedOnWatchlist.addAll(podcasts);

        // Load suggested authors
        authors = authorNeo4jManager.showSuggestedAuthorsFollowedByFollowedUser(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), limit, 0);
        if (authors != null)
            suggestedAuthors.addAll(authors);

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }

    public void loadHomepageAsUnregistered(List<Triplet<Podcast, Float, Boolean>> topRated, List<Pair<Podcast, Integer>> mostLikedPodcasts, List<Pair<Author, Integer>> mostFollowedAuthors, int limit) {
        Logger.info("Loading Homepage as Unregistered");
        MongoManager.getInstance().openConnection();

        // Loading Top Rated Podcasts
        List<Pair<Podcast, Float>> resultsTopRated = new ArrayList<>();
        this.queryMongoManager.getPodcastsWithHighestAverageRating(resultsTopRated);

        for (Pair<Podcast, Float> value: resultsTopRated)
            topRated.add(new Triplet<>(value.getValue0(), value.getValue1(), false));

        // Loading Most Liked Podcasts
        this.queryMongoManager.getMostLikedPodcast(mostLikedPodcasts);

        // Load Most Followed Author
        this.queryMongoManager.getMostFollowedAuthor(mostFollowedAuthors);

        MongoManager.getInstance().closeConnection();
    }

    public boolean loadMoreSuggested(List<Podcast> basedOnFollowedUsers, int limit, int skip) {
        Logger.info("Retrieving podcasts based on your followed users");
        Neo4jManager.getInstance().openConnection();

        // Load podcasts liked by followed users
        List<Podcast> podcasts = podcastNeo4jManager.showSuggestedPodcastsLikedByFollowedUsers(((User)MyPodcastDB.getInstance().getSessionActor()), limit, skip);
        if(podcasts != null) {
            basedOnFollowedUsers.addAll(podcasts);
        }

        boolean noMorePodcasts = (podcasts == null || podcasts.size() < limit);
        Logger.info("No more podcasts: " + noMorePodcasts);

        Neo4jManager.getInstance().closeConnection();
        return noMorePodcasts;
    }

    public boolean loadTopGenres(List<Podcast> topGenres, int limit, int skip) {
        Logger.info("Retrieving top genres podcasts");
        Neo4jManager.getInstance().openConnection();

        List<Podcast> podcasts = podcastNeo4jManager.showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), limit, skip);
        if(podcasts != null) {
            topGenres.addAll(podcasts);
        }

        boolean noMorePodcastsTopGenres = (podcasts == null || podcasts.size() < limit);
        Logger.info("No more podcasts: " + noMorePodcastsTopGenres);

        Neo4jManager.getInstance().closeConnection();
        return noMorePodcastsTopGenres;
    }

    public boolean loadBasedOnWatchlist(List<Podcast> basedOnWatchlist, int limit, int skip) {
        Logger.info("Retrieving podcasts based on podcasts in watchlist");
        Neo4jManager.getInstance().openConnection();

        boolean noMorePodcasts = false;

        List<Podcast> podcasts = podcastNeo4jManager.showSuggestedPodcastsBasedOnAuthorsOfPodcastsInWatchlist(((User)MyPodcastDB.getInstance().getSessionActor()), limit, skip);
        if(podcasts != null) {
            basedOnWatchlist.addAll(podcasts);
            noMorePodcasts = podcasts.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();
        return noMorePodcasts;
    }

    public boolean loadSuggestedAuthors(List<Author> suggestedAuthors, int limit, int skip) {
        Logger.info("Retrieving suggested authors");
        Neo4jManager.getInstance().openConnection();

        List<Author> authors = authorNeo4jManager.showSuggestedAuthorsFollowedByFollowedUser(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), limit, skip);
        if (authors != null) {
            suggestedAuthors.addAll(authors);
        }

        boolean noMoreAuthors = (authors == null || authors.size() < limit);
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
            Logger.info("Added in cache");
            Logger.info("Authors in cache: " + FollowedAuthorCache.getAllFollowedAuthors().toString());
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
            Logger.info("Remove from cache");
            Logger.info("Authors in cache: " + FollowedAuthorCache.getAllFollowedAuthors().toString());
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

    /***** ADMIN HOMEPAGE ******/
    public void loadHomepageAsAdmin(List<Triplet<Podcast, Float, Boolean>> topRated, List<Pair<Podcast, Integer>> mostLikedPodcasts, List<Pair<Author, Integer>> mostFollowedAuthors, int limit) {
        Logger.info("Loading Homepage as Admin");
        MongoManager.getInstance().openConnection();

        // Loading Top Rated Podcasts
        List<Pair<Podcast, Float>> resultsTopRated = new ArrayList<>();
        this.queryMongoManager.getPodcastsWithHighestAverageRating(resultsTopRated);

        for (Pair<Podcast, Float> value: resultsTopRated)
            topRated.add(new Triplet<>(value.getValue0(), value.getValue1(), false));

        // Loading Most Liked Podcasts
        this.queryMongoManager.getMostLikedPodcast(mostLikedPodcasts);

        // Load Most Followed Author
        this.queryMongoManager.getMostFollowedAuthor(mostFollowedAuthors);

        MongoManager.getInstance().closeConnection();
    }

    /***** AUTHOR HOMEPAGE ******/
    public void loadHomepageAsAuthor(List<Triplet<Podcast, Float, Boolean>> topRated, List<Pair<Podcast, Integer>> mostLikedPodcasts, List<Pair<Author, Integer>> mostFollowedAuthors, int limit) {
        Logger.info("Loading homepage as author");
        MongoManager.getInstance().openConnection();

        // Loading Top Rated Podcasts
        List<Pair<Podcast, Float>> resultsTopRated = new ArrayList<>();
        this.queryMongoManager.getPodcastsWithHighestAverageRating(resultsTopRated);
        for (Pair<Podcast, Float> entry: resultsTopRated)
            topRated.add(new Triplet<>(entry.getValue0(), entry.getValue1(), false));

        // Loading Most Liked Podcasts
        this.queryMongoManager.getMostLikedPodcast(mostLikedPodcasts);

        // Load Most Followed Author
        this.queryMongoManager.getMostFollowedAuthor(mostFollowedAuthors);

        MongoManager.getInstance().closeConnection();
    }

    public boolean followAuthorAsAuthor(Author author) {
        Neo4jManager.getInstance().openConnection();
        boolean result = authorNeo4jManager.addAuthorFollowsAuthor(((Author) (MyPodcastDB.getInstance().getSessionActor())).getName(), author.getName());

        if (result) {
            Logger.success("(Author) You started following " + author.getName());

            // Updating cache
            FollowedAuthorCache.addAuthor(author);
            Logger.info("Added from cache");
            Logger.info("Authors in cache: " + FollowedAuthorCache.getAllFollowedAuthors().toString());
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
            Logger.info("Remove from cache");
            Logger.info("Authors in cache: " + FollowedAuthorCache.getAllFollowedAuthors().toString());
        } else {
            Logger.error("(Author) Error during the unfollowing operation");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }
}
