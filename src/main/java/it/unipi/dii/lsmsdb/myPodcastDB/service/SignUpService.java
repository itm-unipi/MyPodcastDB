package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AdminMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AuthorMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.UserMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import org.javatuples.Pair;

import java.util.List;

public class SignUpService {

    //---------------- GIANLUCA ---------------------
    private UserMongo userMongoManager;
    private UserNeo4j userNeo4jManager;
    private AuthorMongo authorMongoManager;
    private AuthorNeo4j authorNeo4jManager;

    public SignUpService(){
        userMongoManager = new UserMongo();
        userNeo4jManager = new UserNeo4j();
        authorMongoManager = new AuthorMongo();
        authorNeo4jManager = new AuthorNeo4j();
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
        //failure mongo operation
        else if(!userMongoManager.addUser(user))
            res = 3;
        //failure neo4j operation
        else if(!userNeo4jManager.addUser(user.getUsername(), user.getPicturePath())){
            userMongoManager.deleteUserByUsername(user.getUsername());
            res = 4;
        }
        else
            res = 0;

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;

    }

    public int addAuthorSignUp(Author author){

        int res = -1;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if author with the same name already exists in mongo
        if(authorMongoManager.findAuthorByName(author.getName()) != null)
            res = 1;
        //check if author with the same name already exists in neo4j
        else if(authorNeo4jManager.findAuthorByName(author.getName()))
            res = 2;
        //check failure mongo operation
        else if(!authorMongoManager.addAuthor(author))
                res = 3;
        //check failure neo4j operation
        else if(!authorNeo4jManager.addAuthor(author.getName(), author.getPicturePath())){
            authorMongoManager.deleteAuthorByName(author.getName());
            res = 4;
        }
        else
            res = 0;

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

}
