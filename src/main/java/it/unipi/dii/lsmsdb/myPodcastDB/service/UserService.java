package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.UserMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;

public class UserService {

    //---------------- GIANLUCA ---------------------
    private UserMongo userMongoManager;
    private UserNeo4j userNeo4jManager;

    public UserService(){
        userMongoManager = new UserMongo();
        userNeo4jManager = new UserNeo4j();
    }

    public boolean getUserLogin(User user){

        boolean res;
        MongoManager.getInstance().openConnection();

        User newUser = userMongoManager.findUserByUsername(user.getUsername());
        if(newUser == null || !user.getPassword().equals(newUser.getPassword()))
            res = false;
        else {
            user.copy(newUser);
            res = true;
        }
        MongoManager.getInstance().closeConnection();
        return res;
    }


    public boolean addUserSignUp(User user){

        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        User newUser = userMongoManager.findUserByUsername(user.getUsername());
        if(newUser != null){
            MongoManager.getInstance().closeConnection();
            Neo4jManager.getInstance().closeConnection();
            return false;
        }
        else {
            if(!userMongoManager.addUser(user)){
                MongoManager.getInstance().closeConnection();
                Neo4jManager.getInstance().closeConnection();
                return false;
            }
            if(!userNeo4jManager.addUser(user.getUsername(), user.getPicturePath())){
                MongoManager.getInstance().closeConnection();
                Neo4jManager.getInstance().closeConnection();
                return false;
            }
            MongoManager.getInstance().closeConnection();
            Neo4jManager.getInstance().closeConnection();
            return true;
        }

    }

    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
