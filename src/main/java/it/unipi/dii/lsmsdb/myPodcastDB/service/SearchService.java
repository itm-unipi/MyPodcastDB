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
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.UserMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Triplet;
import java.util.List;

public class SearchService {
    private final AuthorMongo authorMongoManager;
    private final AuthorNeo4j authorNeo4jManager;
    private final UserNeo4j userNeo4jManager;
    private final UserMongo userMongoManager;
    private final PodcastMongo podcastMongoManager;

    public SearchService() {
        this.authorMongoManager = new AuthorMongo();
        this.authorNeo4jManager = new AuthorNeo4j();
        this.userNeo4jManager = new UserNeo4j();
        this.userMongoManager = new UserMongo();
        this.podcastMongoManager = new PodcastMongo();
    }

    /******** USER SEARCH *********/
    public void searchAsUser(String searchText, List<Podcast> podcastsMatch, List<Author> authorsMatch, List<User> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
        Logger.info("Searching as Registered User");
        MongoManager.getInstance().openConnection();

        // Searching for podcasts
        if (filters.getValue0()) {
            List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit, 0);
            if (podcasts != null)
                podcastsMatch.addAll(podcasts);
        }

        // Searching for authors
        if (filters.getValue1()) {
            List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, 0);
            if (authors != null)
                authorsMatch.addAll(authors);
        }

        // Searching for users
        if (filters.getValue2()) {
            List<User> users = userMongoManager.searchUser(searchText, limit, 0);
            if (users != null)
                usersMatch.addAll(users);
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("Search finished");
    }

    public void searchAsUnregisteredUser(String searchText, List<Podcast> podcastsMatch, List<Author> authorsMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
        Logger.info("Searching as Unregistered User");
        MongoManager.getInstance().openConnection();

        // Searching for podcasts
        if (filters.getValue0()) {
            List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit, 0);
            if (podcasts != null)
                podcastsMatch.addAll(podcasts);
        }

        // Searching for authors
        if (filters.getValue1()) {
            List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, 0);
            if (authors != null)
                authorsMatch.addAll(authors);
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("Search finished");
    }

    public boolean loadMorePodcastsAsUser(String searchText, List<Podcast> podcastsMatch, int limit, int skip) {
        Logger.info("Loading more podcasts as registered user");
        MongoManager.getInstance().openConnection();

        boolean noMorePodcasts = false;

        List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit, skip);
        if (podcasts != null) {
            podcastsMatch.addAll(podcasts);
            noMorePodcasts = podcasts.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("More podcasts loaded");
        return noMorePodcasts;
    }

    public boolean loadMorePodcastsAsUnregistered(String searchText, List<Podcast> podcastsMatch, int limit, int skip) {
        Logger.info("Loading more podcasts as unregistered user");
        MongoManager.getInstance().openConnection();

        boolean noMorePodcasts = false;

        List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit, skip);
        if (podcasts != null) {
            podcastsMatch.addAll(podcasts);
            noMorePodcasts = podcasts.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("More podcasts loaded");
        return noMorePodcasts;
    }

    public boolean loadMoreAuthorsAsUser(String searchText, List<Author> authorsMatch, int limit, int skip) {
        Logger.info("Loading more users as registered user");
        MongoManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, skip);
        if (authors != null) {
            authorsMatch.addAll(authors);
            noMoreAuthors = authors.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("More users loaded");
        return noMoreAuthors;
    }

    public boolean loadMoreAuthorsAsUnregistered(String searchText, List<Author> authorsMatch, int limit, int skip) {
        Logger.info("Loading more authors as unregistered user");
        MongoManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, skip);
        if (authors != null) {
            authorsMatch.addAll(authors);
            noMoreAuthors = authors.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("More authors loaded");
        return noMoreAuthors;
    }

    public boolean loadMoreUsersAsUser(String searchText, List<User> usersMatch, int limit, int skip) {
        Logger.info("Loading more users as registered user");
        MongoManager.getInstance().openConnection();

        boolean noMoreUsers = false;

        List<User> users = userMongoManager.searchUser(searchText, limit, skip);
        if (users != null) {
            usersMatch.addAll(users);
            noMoreUsers = users.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("More users loaded");
        return noMoreUsers;
    }

    public boolean followUser(User user) {
        Neo4jManager.getInstance().openConnection();
        boolean result = userNeo4jManager.addUserFollowUser(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), user.getUsername());

        if (result) {
            Logger.success("(User) You started following " + user.getUsername());

            // Updating cache
            FollowedUserCache.addUser(user);
            Logger.info("Added in cache " + user);
        } else {
            Logger.error("(User) Error during the following operation");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
    }

    public boolean unfollowUser(User user) {
        Neo4jManager.getInstance().openConnection();
        boolean result = userNeo4jManager.deleteUserFollowUser(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), user.getUsername());

        if (result) {
            Logger.success("(User) You unfollowed " + user.getUsername());

            // Updating cache
            FollowedUserCache.removeUser(user.getUsername());
            Logger.info("Removed from cache " + user);
        } else {
            Logger.error("(User) Error during the unfollowing operation");
        }

        Neo4jManager.getInstance().closeConnection();
        return result;
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

    public int addPodcastInWatchlist(Podcast podcast) {
        Neo4jManager.getInstance().openConnection();
        int result = 0;

        if (WatchlistCache.addPodcast(podcast)) {
            if (!userNeo4jManager.addUserWatchLaterPodcast(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), podcast.getId()))
                result = 2;
        } else {
            Logger.info("Watchlist full!");
            result = 1;
        }

        if (result == 0) {
            Logger.success("Podcast added into the watchlist: " + podcast);
            Logger.info("Podcast added in cache " + podcast);
        } else {
            WatchlistCache.removePodcast(podcast.getId());
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

    /****** ADMIN SEARCH *******/
    public void searchAsAdmin(String searchText, List<Podcast> podcastsMatch, List<Author> authorsMatch, List<User> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
        Logger.info("Searching as Admin");
        MongoManager.getInstance().openConnection();

        // Searching for podcasts
        if (filters.getValue0()) {
            List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit, 0);
            if (podcasts != null)
                podcastsMatch.addAll(podcasts);
        }

        // Searching for authors
        if (filters.getValue1()) {
            List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, 0);
            if (authors != null)
                authorsMatch.addAll(authors);
        }

        // Searching for users
        if (filters.getValue2()) {
            List<User> users = userMongoManager.searchUser(searchText, limit, 0);
            if (users != null)
                usersMatch.addAll(users);
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("Search finished");
    }

    public boolean loadMorePodcastsAsAdmin(String searchText, List<Podcast> podcastsMatch, int limit, int skip) {
        Logger.info("Loading more podcasts as admin");
        MongoManager.getInstance().openConnection();

        boolean noMorePodcasts = false;

        List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit, skip);
        if (podcasts != null) {
            podcastsMatch.addAll(podcasts);
            noMorePodcasts = podcasts.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("More podcasts loaded");
        return noMorePodcasts;
    }

    public boolean loadMoreUsersAsAdmin(String searchText, List<User> usersMatch, int limit, int skip) {
        Logger.info("Loading more users as admin");
        MongoManager.getInstance().openConnection();

        boolean noMoreUsers = false;

        List<User> users = userMongoManager.searchUser(searchText, limit, skip);
        if (users != null) {
            usersMatch.addAll(users);
            noMoreUsers = users.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("More users loaded");
        return noMoreUsers;
    }

    public boolean loadMoreAuthorsAsAdmin(String searchText, List<Author> authorsMatch, int limit, int skip) {
        Logger.info("Loading more authors as admin");
        MongoManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, skip);
        if (authors != null) {
            authorsMatch.addAll(authors);
            noMoreAuthors = authors.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("More authors loaded");
        return noMoreAuthors;
    }

    /****** AUTHOR SEARCH *******/
    public void searchAsAuthor(String searchText, List<Podcast> podcastsMatch, List<Author> authorsMatch, List<User> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
        Logger.info("Searching as Author");
        MongoManager.getInstance().openConnection();

        // Searching for podcasts
        if (filters.getValue0()) {
            List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit, 0);
            if (podcasts != null)
                podcastsMatch.addAll(podcasts);
        }

        // Searching for authors
        if (filters.getValue1()) {
            List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, 0);
            if (authors != null)
                authorsMatch.addAll(authors);
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("Search finished");
    }

    public boolean loadMorePodcastsAsAuthor(String searchText, List<Podcast> podcastsMatch, int limit, int skip) {
        Logger.info("Loading more podcasts as author");
        MongoManager.getInstance().openConnection();

        boolean noMorePodcasts = false;

        List<Podcast> podcasts = podcastMongoManager.searchPodcast(searchText, limit, skip);
        if (podcasts != null) {
            podcastsMatch.addAll(podcasts);
            noMorePodcasts = podcasts.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("More podcasts loaded");
        return noMorePodcasts;
    }

    public boolean loadMoreAuthorsAsAuthor(String searchText, List<Author> authorsMatch, int limit, int skip) {
        Logger.info("Loading more authors as author");
        MongoManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, skip);
        if (authors != null) {
            authorsMatch.addAll(authors);
            noMoreAuthors = authors.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        Logger.info("More authors loaded");
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
}
