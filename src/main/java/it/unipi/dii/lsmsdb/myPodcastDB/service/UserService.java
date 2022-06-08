package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.*;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

public class UserService {

    //---------------- GIANLUCA ---------------------
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    private final AuthorMongo authorMongoManager;
    private final AuthorNeo4j authorNeo4jManager;
    private final UserNeo4j userNeo4jManager;
    private final PodcastNeo4j podcastNeo4jManager;

    public UserService() {
        this.authorMongoManager = new AuthorMongo();
        this.authorNeo4jManager = new AuthorNeo4j();
        this.userNeo4jManager = new UserNeo4j();
        this.podcastNeo4jManager = new PodcastNeo4j();
    }

    /******** USER ********/
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
