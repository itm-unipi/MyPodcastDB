package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
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

public class LoginService {

    //---------------- GIANLUCA ---------------------
    private UserMongo userMongoManager;
    private AuthorMongo authorMongoManager;
    private AdminMongo adminMongoManager;


    public LoginService(){
        userMongoManager = new UserMongo();
        authorMongoManager = new AuthorMongo();
        adminMongoManager = new AdminMongo();
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

    public int getAuthorLogin(Author author){
        int res = -1;
        MongoManager.getInstance().openConnection();
        Author newAuthor = authorMongoManager.findAuthorByName(author.getName());
        if(newAuthor == null)
            res = 1;
        else if(!author.getPassword().equals(newAuthor.getPassword()))
            res = 2;
        else {
            author.copy(newAuthor);
            res = 0;
        }

        MongoManager.getInstance().closeConnection();
        return res;
    }

    public int getAdminLogin(Admin admin){

        int res = -1;
        MongoManager.getInstance().openConnection();

        Admin newAdmin = adminMongoManager.findAdminByName(admin.getName());
        if(newAdmin == null)
            res = 1;
        else if(!admin.getPassword().equals(newAdmin.getPassword()))
            res = 2;
        else {
            admin.copy(newAdmin);
            res = 0;
        }

        MongoManager.getInstance().closeConnection();
        return res;
    }


}
