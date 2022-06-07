package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.UserMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Pair;

import java.util.List;

public class UserService {

    //---------------- GIANLUCA ---------------------
    private UserMongo userMongoManager;
    private UserNeo4j userNeo4jManager;
    private PodcastNeo4j podcastNeo4j;
    private AuthorNeo4j authorNeo4j;

    public UserService(){
        userMongoManager = new UserMongo();
        userNeo4jManager = new UserNeo4j();
        podcastNeo4j = new PodcastNeo4j();
        authorNeo4j = new AuthorNeo4j();
    }

    public int getUserLogin(User user){

        int res = -1;
        MongoManager.getInstance().openConnection();

        User newUser = userMongoManager.findUserByUsername(user.getUsername());
        if(newUser == null)
            res = 1;
        else if(!user.getPassword().equals(newUser.getPassword()))
            res = 2;
        else {
            user.copy(newUser);
            res = 0;
        }
        MongoManager.getInstance().closeConnection();
        return res;
    }


    public int addUserSignUp(User user){

        int res = -1;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if a user with the same username already exists in mongo
        User newUser = userMongoManager.findUserByUsername(user.getUsername());
        if(newUser != null)
            res = 1;
        //check if a user with the same username already exists in neo4j
        else if(userNeo4jManager.findUserByUsername(user.getUsername()))
            res = 2;
        else {
            //failure mongo operation from persistence
            if(!userMongoManager.addUser(user)){
                res = 3;
            }
            //failure neo4j operation from persistence
            else if(!userNeo4jManager.addUser(user.getUsername(), user.getPicturePath())){
                userMongoManager.deleteUserByUsername(user.getUsername());
                res = 4;
            }
            else
                res = 0;
        }
        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;

    }

    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
