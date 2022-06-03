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
            if(!userMongoManager.addUser(user) || !userNeo4jManager.addUser(user.getUsername(), user.getPicturePath())){
                MongoManager.getInstance().closeConnection();
                Neo4jManager.getInstance().closeConnection();
                return false;
            }
            MongoManager.getInstance().closeConnection();
            Neo4jManager.getInstance().closeConnection();
            return true;
        }

    }

    public void loadUserPageProfile(User pageOwner, List<Podcast> wPodcasts, List<Podcast> lPodcasts, List<Author> followedAuthors, List<User> followedUsers, int limit ){
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //load user info from mongo
        User user = userMongoManager.findUserByUsername(pageOwner.getUsername());
        pageOwner.copy(user);

        //load podcasts in watchlist from neo4j
        List<Podcast> podcasts = podcastNeo4j.showPodcastsInWatchlist(pageOwner.getUsername(), limit);
        wPodcasts.addAll(podcasts);

        //load liked podcasts
        podcasts = podcastNeo4j.showLikedPodcastsByUser(pageOwner.getUsername(), limit);
        lPodcasts.addAll(podcasts);

        //load followed authors
        List<Author> authors = authorNeo4j.showFollowedAuthorsByUser(pageOwner.getUsername(), limit);
        followedAuthors.addAll(authors);

        //load followed users
        List<User> users = userNeo4jManager.showFollowedUsers(user.getUsername(), limit);
        followedUsers.addAll(users);

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
    }


        //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
