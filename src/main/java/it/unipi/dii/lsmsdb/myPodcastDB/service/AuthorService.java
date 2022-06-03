package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AuthorMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import org.javatuples.Pair;
import java.util.List;

public class AuthorService {

    //---------------- GIANLUCA ---------------------
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------

    private AuthorMongo authorMongoManager;
    private AuthorNeo4j authorNeo4jManager;

    public AuthorService() {
        this.authorMongoManager = new AuthorMongo();
        this.authorNeo4jManager = new AuthorNeo4j();
    }

    public void loadAuthorOwnProfile(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        author.copy(foundAuthor);

        List<Author> followedAuthor = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit);
        for (Author a: followedAuthor)
            followed.add(new Pair<>(a, true));

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
    }

    public boolean loadAuthorProfile(Author author, List<Pair<Author, Boolean>> followed, int limit) {
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        // Getting the author object from Mongo
        Author foundAuthor = authorMongoManager.findAuthorByName(author.getName());
        author.copy(foundAuthor);

        // Checking if the "session" author follows the requested author
        boolean followingAuthor = authorNeo4jManager.findAuthorFollowsAuthor(((Author)(MyPodcastDB.getInstance().getSessionActor())).getName(), foundAuthor.getName());

        // Getting the authors followed by the author requested
        List<Author> followedAuthor = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName(), limit);
        for (Author a: followedAuthor) {
            boolean following = authorNeo4jManager.findAuthorFollowsAuthor(((Author)(MyPodcastDB.getInstance().getSessionActor())).getName(), a.getName());
            followed.add(new Pair<>(a, following));
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();

        return followingAuthor;
    }

    public void followAuthor(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (authorNeo4jManager.addAuthorFollowsAuthor(((Author)(MyPodcastDB.getInstance().getSessionActor())).getName(), authorName))
            Logger.success("(Author) You started following " + authorName);
        else
            Logger.error("(Author) Error during the following operation");

        Neo4jManager.getInstance().closeConnection();
    }

    public void unfollowAuthor(String authorName) {
        Neo4jManager.getInstance().openConnection();

        if (authorNeo4jManager.deleteAuthorFollowsAuthor(((Author)(MyPodcastDB.getInstance().getSessionActor())).getName(), authorName))
            Logger.success("(Author) You unfollowed " + authorName);
        else
            Logger.error("(Author) Error during the unfollowing operation");

        Neo4jManager.getInstance().closeConnection();
    }

    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
