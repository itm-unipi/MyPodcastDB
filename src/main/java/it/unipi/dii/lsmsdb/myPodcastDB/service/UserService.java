package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.*;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Pair;

import java.util.List;

public class UserService {

    //---------------- GIANLUCA ---------------------
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    private AuthorMongo authorMongoManager;
    private AuthorNeo4j authorNeo4jManager;
    private UserNeo4j userNeo4jManager;
    private UserMongo userMongoManager;
    private PodcastMongo podcastMongoManager;

    public UserService() {
        this.authorMongoManager = new AuthorMongo();
        this.authorNeo4jManager = new AuthorNeo4j();
        this.userNeo4jManager = new UserNeo4j();
        this.userMongoManager = new UserMongo();
        this.podcastMongoManager = new PodcastMongo();
    }

    /***** Loading author profile **********/
    public boolean loadAuthorProfileRegistered(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        author.copy(foundAuthor);

        // Checking if the user follows the visited author
        boolean followingAuthor = userNeo4jManager.findUserFollowsAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), author.getName());

        // Getting the authors followed by the author visited
        List<Author> followedAuthor = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit);
        for (Author a: followedAuthor) {
            boolean following = userNeo4jManager.findUserFollowsAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), a.getName());
            followed.add(new Pair<>(a, following));
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();

        return followingAuthor;
    }

    public void loadAuthorProfileUnregistered(Author author, List<Pair<Author, Boolean>> followed, int limit) {
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

    /******+ Follow Author ********/
    public void followAuthor(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (userNeo4jManager.addUserFollowAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), authorName))
            Logger.success("(User) You started following " + authorName);
        else
            Logger.error("(User) Error during the following operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public void unfollowAuthor(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (userNeo4jManager.deleteUserFollowAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), authorName))
            Logger.success("(User) You unfollowed " + authorName);
        else
            Logger.error("(User) Error during the unfollowing operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public void searchRegistered(String searchText, List<Podcast> podcastsMatch, List<Pair<Author, Boolean>> authorsMatch, List<Pair<User, Boolean>> usersMatch, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Searching for podcasts
        podcastsMatch.addAll(podcastMongoManager.searchPodcast(searchText, limit));

        // Searching for authors
        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit);
        for (Author authorFound: authors) {
            boolean followingAuthor = userNeo4jManager.findUserFollowsAuthor(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), authorFound.getName());
            authorsMatch.add(new Pair<>(authorFound, followingAuthor));
        }

        Logger.info("Authors found: " + authorsMatch);

        // Searching for users
        List<User> users = userMongoManager.searchUser(searchText, limit);
        for (User userFound: users) {
            boolean followingUser = userNeo4jManager.findUserFollowsUser(((User)MyPodcastDB.getInstance().getSessionActor()).getUsername(), userFound.getUsername());
            usersMatch.add(new Pair<>(userFound, followingUser));
        }

        Logger.info("Users found: " + usersMatch);

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
    }

    public void searchUnregistered(String searchText, List<Podcast> podcastsMatch, List<Pair<Author, Boolean>> authorsMatch, List<Pair<User, Boolean>> usersMatch, int limit) {
        MongoManager.getInstance().openConnection();

        // Searching for podcasts
        podcastsMatch.addAll(podcastMongoManager.searchPodcast(searchText, limit));

        // Searching for authors
        List<Author> authors = authorMongoManager.searchAuthor(searchText, limit);
        for (Author authorFound: authors)
            authorsMatch.add(new Pair<>(authorFound, false));

        MongoManager.getInstance().closeConnection();
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

    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
