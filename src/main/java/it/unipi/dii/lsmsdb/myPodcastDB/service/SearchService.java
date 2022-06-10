package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AuthorMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.UserMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Triplet;
import java.util.List;

public class SearchService {
    private final AuthorMongo authorMongoManager;
    private final UserNeo4j userNeo4jManager;
    private final UserMongo userMongoManager;
    private final PodcastMongo podcastMongoManager;

    public SearchService() {
        this.authorMongoManager = new AuthorMongo();
        this.userNeo4jManager = new UserNeo4j();
        this.userMongoManager = new UserMongo();
        this.podcastMongoManager = new PodcastMongo();
    }

    /******** USER SEARCH *********/
    public void searchAsUser(String searchText, List<Podcast> podcastsMatch, List<Author> authorsMatch, List<User> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
        Logger.info("Searching as Registered User");
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
            if (authors != null)
                authorsMatch.addAll(authors);
        }

        // Searching for users
        if (filters.getValue2()) {
            List<User> users = userMongoManager.searchUser(searchText, limit, 0);
            if (users != null)
                usersMatch.addAll(users);
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }

    public void searchAsUnregisteredUser(String searchText, List<Podcast> podcastsMatch, List<Author> authorsMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
        Logger.info("Searching as Unregistered");
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
        return noMorePodcasts;
    }

    public boolean loadMoreAuthorsAsUser(String searchText, List<Author> authorsMatch, int limit, int skip) {
        Logger.info("Loading more users as registered user");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, skip);
        if (authors != null) {
            authorsMatch.addAll(authors);
            noMoreAuthors = authors.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
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
        return noMoreAuthors;
    }

    public boolean loadMoreUsersAsUser(String searchText, List<User> usersMatch, int limit, int skip) {
        Logger.info("Loading more users as registered user");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        boolean noMoreUsers = false;

        List<User> users = userMongoManager.searchUser(searchText, limit, skip);
        if (users != null) {
            usersMatch.addAll(users);
            noMoreUsers = users.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
        return noMoreUsers;
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
        return noMorePodcasts;
    }

    public boolean loadMoreUsersAsAdmin(String searchText, List<User> usersMatch, int limit, int skip) {
        Logger.info("Loading more users as admin");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        boolean noMoreUsers = false;

        List<User> users = userMongoManager.searchUser(searchText, limit, skip);
        if (users != null) {
            usersMatch.addAll(users);
            noMoreUsers = users.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
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
        return noMoreAuthors;
    }

    /****** AUTHOR SEARCH *******/
    public void searchAsAuthor(String searchText, List<Podcast> podcastsMatch, List<Author> authorsMatch, List<User> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
        Logger.info("Searching as Author");
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
            if (authors != null)
                authorsMatch.addAll(authors);
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
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
        return noMorePodcasts;
    }

    public boolean loadMoreAuthorsAsAuthor(String searchText, List<Author> authorsMatch, int limit, int skip) {
        Logger.info("Loading more authors as author");
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, skip);
        if (authors != null) {
            authorsMatch.addAll(authors);
            noMoreAuthors = authors.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
        return noMoreAuthors;
    }
}
