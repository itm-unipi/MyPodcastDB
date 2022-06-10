package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
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
    private final AuthorMongo authorMongoManager;
    private final QueryMongo queryMongoManager;
    private final AuthorNeo4j authorNeo4jManager;
    private final UserNeo4j userNeo4jManager;
    private final PodcastNeo4j podcastNeo4jManager;

    public HomePageService() {
        this.authorMongoManager = new AuthorMongo();
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

        // Loading top rated in user's country
        List<Triplet<Podcast, String, Float>> resultsTopCountry = new ArrayList<>();
        this.queryMongoManager.getPodcastWithHighestAverageRatingPerCountry(resultsTopCountry);
        for (Triplet<Podcast, String, Float> entry: resultsTopCountry)
            if (entry.getValue1().equals(((User)MyPodcastDB.getInstance().getSessionActor()).getCountry()))
                topRated.add(new Triplet<>(entry.getValue0(), entry.getValue2(), true));

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

        // Load podcasts in watchlist
        podcasts = podcastNeo4jManager.showPodcastsInWatchlist(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), limit, 0);
        if(podcasts != null)
            watchlist.addAll(podcasts);

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
        Neo4jManager.getInstance().openConnection();

        // Loading Top Rated Podcasts
        List<Pair<Podcast, Float>> resultsTopRated = new ArrayList<>();
        this.queryMongoManager.getPodcastsWithHighestAverageRating(resultsTopRated);

        for (Pair<Podcast, Float> value: resultsTopRated)
            topRated.add(new Triplet<>(value.getValue0(), value.getValue1(), false));

        // Loading Most Liked Podcasts
        this.queryMongoManager.getMostLikedPodcast(mostLikedPodcasts);

        // Load Most Followed Author
        this.queryMongoManager.getMostFollowedAuthor(mostFollowedAuthors);

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }

    public boolean loadMoreSuggested(List<Podcast> basedOnFollowedUsers, int limit, int skip) {
        Logger.info("Retrieving podcasts based on your followed users");
        Neo4jManager.getInstance().openConnection();

        boolean noMorePodcasts = false;

        // Load podcasts liked by followed users
        List<Podcast> podcasts = podcastNeo4jManager.showSuggestedPodcastsLikedByFollowedUsers(((User)MyPodcastDB.getInstance().getSessionActor()), limit, skip);
        if(podcasts != null) {
            basedOnFollowedUsers.addAll(podcasts);
            noMorePodcasts = podcasts.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();

        return noMorePodcasts;
    }

    public boolean loadWatchlist(List<Podcast> watchlist, int limit, int skip) {
        Logger.info("Retrieving podcasts in watchlist");
        Neo4jManager.getInstance().openConnection();

        boolean noMorePodcastsWatchlist = false;

        List<Podcast> podcasts = podcastNeo4jManager.showPodcastsInWatchlist(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), limit, skip);
        if(podcasts != null) {
            watchlist.addAll(podcasts);
            noMorePodcastsWatchlist = podcasts.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();

        return noMorePodcastsWatchlist;
    }

    public boolean loadTopGenres(List<Podcast> topGenres, int limit, int skip) {
        Logger.info("Retrieving top genres podcasts");
        Neo4jManager.getInstance().openConnection();

        boolean noMorePodcastsTopGenres = false;

        List<Podcast> podcasts = podcastNeo4jManager.showSuggestedPodcastsBasedOnCategoryOfPodcastsUserLiked(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), limit, skip);
        if(podcasts != null) {
            topGenres.addAll(podcasts);
            noMorePodcastsTopGenres = podcasts.size() < limit;
        }

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

        boolean noMoreAuthors = false;

        List<Author> authors = authorNeo4jManager.showSuggestedAuthorsFollowedByFollowedUser(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), limit, skip);
        if (authors != null) {
            suggestedAuthors.addAll(authors);
            noMoreAuthors = authors.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();
        return noMoreAuthors;
    }

    /***** ADMIN HOMEPAGE ******/
    public void loadHomepageAsAdmin(List<Triplet<Podcast, Float, Boolean>> topRated, List<Pair<Podcast, Integer>> mostLikedPodcasts, List<Pair<Author, Integer>> mostFollowedAuthors, int limit) {
        Logger.info("Loading Homepage as Admin");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Loading Top Rated Podcasts
        List<Pair<Podcast, Float>> resultsTopRated = new ArrayList<>();
        this.queryMongoManager.getPodcastsWithHighestAverageRating(resultsTopRated);

        for (Pair<Podcast, Float> value: resultsTopRated)
            topRated.add(new Triplet<>(value.getValue0(), value.getValue1(), false));

        // Loading Most Liked Podcasts
        this.queryMongoManager.getMostLikedPodcast(mostLikedPodcasts);

        // Load Most Followed Author
        this.queryMongoManager.getMostFollowedAuthor(mostFollowedAuthors);

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }

    /***** AUTHOR HOMEPAGE ******/
    public void loadHomepageAsAuthor(List<Triplet<Podcast, Float, Boolean>> topRated, List<Pair<Podcast, Integer>> mostLikedPodcasts, List<Pair<Author, Integer>> mostFollowedAuthors, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Loading Top Rated Podcasts
        List<Pair<Podcast, Float>> resultsTopRated = new ArrayList<>();
        this.queryMongoManager.getPodcastsWithHighestAverageRating(resultsTopRated);
        for (Pair<Podcast, Float> entry: resultsTopRated)
            topRated.add(new Triplet<>(entry.getValue0(), entry.getValue1(), false));

        // Loading Most Liked Podcasts
        this.queryMongoManager.getMostLikedPodcast(mostLikedPodcasts);

        // Load Most Followed Author
        this.queryMongoManager.getMostFollowedAuthor(mostFollowedAuthors);

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }
}
