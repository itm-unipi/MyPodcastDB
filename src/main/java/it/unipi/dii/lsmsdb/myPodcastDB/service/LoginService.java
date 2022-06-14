package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.cache.FollowedAuthorCache;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.FollowedUserCache;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.WatchlistCache;
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
    private PodcastNeo4j podcastNeo4jManager;
    private UserNeo4j userNeo4jManager;
    private AuthorNeo4j authorNeo4jManager;


    public LoginService(){
        userMongoManager = new UserMongo();
        authorMongoManager = new AuthorMongo();
        adminMongoManager = new AdminMongo();
        podcastNeo4jManager = new PodcastNeo4j();
        userNeo4jManager = new UserNeo4j();
        authorNeo4jManager = new AuthorNeo4j();
    }

    public int getUserLogin(User user){

        int res;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if user exists
        User newUser;
        if(user.getUsername() != null && !user.getUsername().isEmpty())
            newUser = userMongoManager.findUserByUsername(user.getUsername());
        else
            newUser = userMongoManager.findUserByEmail(user.getEmail());
        if(newUser == null)
            res = 1;
        //check if user exists on neo4j
        else if(!userNeo4jManager.findUserByUsername(newUser.getUsername()))
            res = 2;
        //check if password is correct
        else if(!user.getPassword().equals(newUser.getPassword()))
            res = 3;
        else {
            user.copy(newUser);
            res = 0;
        }

        MongoManager.getInstance().closeConnection();

        if(res == 0){
            //load watchlist
            List<Podcast> podcasts = podcastNeo4jManager.showPodcastsInWatchlist(user.getUsername());
            if (podcasts != null) {
                WatchlistCache.addPodcastList(podcasts);
            }

            //load followed Authors
            List<Author> authors = authorNeo4jManager.showFollowedAuthorsByUser(user.getUsername());
            if (authors != null) {
                FollowedAuthorCache.addAuthorList(authors);
            }

            //load followed Users
            List<User> users = userNeo4jManager.showFollowedUsers(user.getUsername());
            if (users != null) {
                FollowedUserCache.addUserList(users);
            }
        }

        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int getAuthorLogin(Author author){
        int res;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if author exists on mongo
        Author newAuthor;
        if(author.getName() != null && !author.getName().isEmpty())
            newAuthor = authorMongoManager.findAuthorByName(author.getName());
        else
            newAuthor = authorMongoManager.findAuthorByEmail(author.getEmail());
        if(newAuthor == null)
            res = 1;
        //check if author exists on neo4j
        else if(!authorNeo4jManager.findAuthorByName(newAuthor.getName()))
            res = 2;
        //check if password is correct
        else if(!author.getPassword().equals(newAuthor.getPassword()))
            res = 3;
        else {
            author.copy(newAuthor);
            res = 0;
        }

        MongoManager.getInstance().closeConnection();

        if(res == 0){
            List<Author> authors = authorNeo4jManager.showFollowedAuthorsByAuthor(author.getName());
            if (authors != null) {
                FollowedAuthorCache.addAuthorList(authors);
            }
        }

        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int getAdminLogin(Admin admin){

        int res;
        MongoManager.getInstance().openConnection();

        //check if admin exists
        Admin newAdmin;
        if(admin.getName() != null && !admin.getName().isEmpty())
            newAdmin = adminMongoManager.findAdminByName(admin.getName());
        else
            newAdmin = adminMongoManager.findAdminByEmail(admin.getEmail());
        if(newAdmin == null)
            res = 1;
        //check if password is correct
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
