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
        if(newUser == null || !user.getPassword().equals(newUser.getPassword()))
            res = 1;
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

    public int loadUserPageProfile(User pageOwner, List<Podcast> wPodcasts, List<Podcast> lPodcasts, List<Author> followedAuthors, List<User> followedUsers, int limit ){

        int res = -1;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //load user info from mongo
        User user = userMongoManager.findUserByUsername(pageOwner.getUsername());
        if(user == null)
            res = 1;
        else {
            pageOwner.copy(user);
            //load podcasts in watchlist from neo4j
            List<Podcast> podcasts = podcastNeo4j.showPodcastsInWatchlist(pageOwner.getUsername(), limit);
            if(podcasts != null)
                wPodcasts.addAll(podcasts);

            //load liked podcasts
            podcasts = podcastNeo4j.showLikedPodcastsByUser(pageOwner.getUsername(), limit);
            if(podcasts != null)
                lPodcasts.addAll(podcasts);

            //load followed authors
            List<Author> authors = authorNeo4j.showFollowedAuthorsByUser(pageOwner.getUsername(), limit);
            if(authors != null)
                followedAuthors.addAll(authors);

            //load followed users
            List<User> users = userNeo4jManager.showFollowedUsers(user.getUsername(), limit);
            if(users != null)
                followedUsers.addAll(users);

            res = 0;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;
    }


    public int updateUserPageOwner(User oldUser, User newUser){
        int res = -1;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if oldUser exists in mongo
        if(userMongoManager.findUserByUsername(oldUser.getUsername()) == null)
            res = 1;
        //check if oldUser exists in neo4j
        else if(!userNeo4jManager.findUserByUsername(oldUser.getUsername()))
            res = 2;
        //check if a user with the new username already exists
        else if(!oldUser.getUsername().equals(newUser.getUsername()) && userMongoManager.findUserByUsername(newUser.getUsername()) != null)
            res = 3;
        //failure mongo operation from persistence
        else if(!userMongoManager.updateUser(newUser))
            res = 0;
        //failure neo4j operation from persistence
        else if(!userNeo4jManager.updateUser(oldUser.getUsername(), newUser.getUsername(), newUser.getPicturePath())){
            userMongoManager.updateUser(oldUser);
            res = 4;
        }
        else
            res = 0;
        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int checkFollowUser(String user1, String user2){
        int res = -1;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if user1 exists
        if(userMongoManager.findUserByUsername(user1) == null)
            res = 2;
        else if(!userNeo4jManager.findUserByUsername(user1))
            res = 3;
        //check if user2 exists
        else if(userMongoManager.findUserByUsername(user2) == null)
            res = 4;
        else if(!userNeo4jManager.findUserByUsername(user2))
            res = 5;
        //check if user1 follows user2
        else if(!userNeo4jManager.findUserFollowsUser(user1, user2))
            res = 1;
        else
            res = 0;
        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
        return res;

    }

    public int updateFollowUser(String user1, String user2, boolean op){
        int res = -1;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if user1 exists
        if(userMongoManager.findUserByUsername(user1) == null)
            res = 1;
        else if(!userNeo4jManager.findUserByUsername(user1))
            res = 2;
        //check if user1 exists
        else if(userMongoManager.findUserByUsername(user2) == null)
            res = 3;
        else if(!userNeo4jManager.findUserByUsername(user2))
            res = 4;
        else if(op) {
            //check the relation already exists
            if(userNeo4jManager.findUserFollowsUser(user1, user2))
                res = 5;
            else if(!userNeo4jManager.addUserFollowUser(user1, user2))
                res = 6;
            else
                res = 0;
        }
        else{
            //check the relation already not exists
            if(!userNeo4jManager.findUserFollowsUser(user1, user2))
                res = 7;
            else if(!userNeo4jManager.deleteUserFollowUser(user1, user2))
                res = 8;
            else
                res = 0;
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
        return res;
    }

    public int deleteUserPageOwner(User user){
        int res;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if user exists in mongo
        if(userMongoManager.findUserByUsername(user.getUsername()) == null)
            res = 1;
        //check if user exists in neo4j
        else if(!userNeo4jManager.findUserByUsername(user.getUsername()))
            res = 2;
        //check delete operation failure
        else if(!userMongoManager.deleteUserByUsername(user.getUsername()))
            res = 3;
        else if(!userNeo4jManager.deleteUser(user.getUsername())){
            userMongoManager.addUser(user);
            res =  4;
        }
        else
            res = 0;
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
