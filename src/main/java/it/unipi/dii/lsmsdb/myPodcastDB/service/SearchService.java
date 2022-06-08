package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AuthorMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.UserMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Pair;
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
    public void searchAsUser(String searchText, List<Podcast> podcastsMatch, List<Pair<Author, Boolean>> authorsMatch, List<Pair<User, Boolean>> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
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
            if (authors != null) {
                for (Author authorFound : authors) {
                    boolean followingAuthor = userNeo4jManager.findUserFollowsAuthor(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), authorFound.getName());
                    authorsMatch.add(new Pair<>(authorFound, followingAuthor));
                }
            }
        }

        // Searching for users
        if (filters.getValue2()) {
            List<User> users = userMongoManager.searchUser(searchText, limit, 0);
            if (users != null) {
                for (User userFound : users) {
                    boolean followingUser = userNeo4jManager.findUserFollowsUser(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), userFound.getUsername());
                    usersMatch.add(new Pair<>(userFound, followingUser));
                }
            }
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }

    public void searchAsUnregisteredUser(String searchText, List<Podcast> podcastsMatch, List<Pair<Author, Boolean>> authorsMatch, List<Pair<User, Boolean>> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
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
            if (authors != null) {
                for (Author authorFound : authors)
                    authorsMatch.add(new Pair<>(authorFound, false));
            }
        }

        MongoManager.getInstance().closeConnection();
    }

    public boolean loadMorePodcastsAsUser(String searchText, List<Podcast> podcastsMatch, int limit, int skip) {
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

    public boolean loadMoreAuthorsAsUser(String searchText, List<Pair<Author, Boolean>> authorsMatch, int limit, int skip) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        boolean noMoreAuthors = false;

        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, skip);
        if (authors != null) {
            for (Author authorFound : authors) {
                boolean followingAuthor = userNeo4jManager.findUserFollowsAuthor(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), authorFound.getName());
                authorsMatch.add(new Pair<>(authorFound, followingAuthor));
            }
            noMoreAuthors = authors.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();

        return noMoreAuthors;
    }

    public boolean loadMoreAuthorsAsUnregistered(String searchText, List<Pair<Author, Boolean>> authorsMatch, int limit, int skip) {
        MongoManager.getInstance().openConnection();
        boolean noMoreAuthors = false;

        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, skip);
        if (authors != null) {
            for (Author authorFound : authors)
                authorsMatch.add(new Pair<>(authorFound, false));
            noMoreAuthors = authors.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        return noMoreAuthors;
    }

    public boolean loadMoreUsersAsUser(String searchText, List<Pair<User, Boolean>> usersMatch, int limit, int skip) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        boolean noMoreUsers = false;

        List<User> users = userMongoManager.searchUser(searchText, limit, skip);
        if (users != null) {
            for (User userFound : users) {
                boolean followingUser = userNeo4jManager.findUserFollowsUser(((User) MyPodcastDB.getInstance().getSessionActor()).getUsername(), userFound.getUsername());
                usersMatch.add(new Pair<>(userFound, followingUser));
            }
            noMoreUsers = users.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();

        return noMoreUsers;
    }

    public void followUser(String username) {
        Neo4jManager.getInstance().openConnection();

        if (userNeo4jManager.addUserFollowUser(((User) (MyPodcastDB.getInstance().getSessionActor())).getUsername(), username))
            Logger.success("(User) You started following " + username);
        else
            Logger.error("(User) Error during the following operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public void unfollowUser(String username) {
        Neo4jManager.getInstance().openConnection();

        if (userNeo4jManager.deleteUserFollowUser(((User) (MyPodcastDB.getInstance().getSessionActor())).getUsername(), username))
            Logger.success("(User) You started following " + username);
        else
            Logger.error("(User) Error during the following operation");

        Neo4jManager.getInstance().closeConnection();
    }

    /****** ADMIN SEARCH *******/

    public void searchAsAdmin(String searchText, List<Podcast> podcastsMatch, List<Pair<Author, Boolean>> authorsMatch, List<Pair<User, Boolean>> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
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
            for (Author authorFound : authors)
                authorsMatch.add(new Pair<>(authorFound, false));
        }

        // Searching for users
        if (filters.getValue2()) {
            List<User> users = userMongoManager.searchUser(searchText, limit, 0);
            for (User userFound : users)
                usersMatch.add(new Pair<>(userFound, false));
        }

        MongoManager.getInstance().closeConnection();
    }

    public boolean loadMorePodcastsAsAdmin(String searchText, List<Podcast> podcastsMatch, int limit, int skip) {
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

    public boolean loadMoreUsersAsAdmin(String searchText, List<Pair<User, Boolean>> usersMatch, int limit, int skip) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        boolean noMoreUsers = false;

        List<User> users = userMongoManager.searchUser(searchText, limit, skip);
        if (users != null) {
            for (User userFound : users)
                usersMatch.add(new Pair<>(userFound, false));
            noMoreUsers = users.size() < limit;
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();

        return noMoreUsers;
    }

    public boolean loadMoreAuthorsAsAdmin(String searchText, List<Pair<Author, Boolean>> authorsMatch, int limit, int skip) {
        MongoManager.getInstance().openConnection();
        boolean noMoreAuthors = false;

        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit, skip);
        if (authors != null) {
            for (Author authorFound : authors)
                authorsMatch.add(new Pair<>(authorFound, false));
            noMoreAuthors = authors.size() < limit;
        }

        MongoManager.getInstance().closeConnection();
        return noMoreAuthors;
    }

    /****** AUTHOR SEARCH *******/

    public void searchAsAuthor(String searchText, List<Podcast> podcastsMatch, List<Pair<Author, Boolean>> authorsMatch, List<Pair<User, Boolean>> usersMatch, int limit, Triplet<Boolean, Boolean, Boolean> filters) {
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

    public boolean loadMorePodcastsAsAuthor(String searchText, List<Podcast> podcastsMatch, int limit, int skip) {
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

    public boolean loadMoreAuthorsAsAuthor(String searchText, List<Pair<Author, Boolean>> authorsMatch, int limit, int skip) {
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
}
