package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.ReviewMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.UserMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.AuthorNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.Neo4jManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.PodcastNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j.UserNeo4j;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserPageService {

    //---------------- GIANLUCA ---------------------
    private UserMongo userMongoManager;
    private UserNeo4j userNeo4jManager;
    private PodcastNeo4j podcastNeo4jManager;
    private AuthorNeo4j authorNeo4jManager;
    private ReviewMongo reviewMongoManager;

    public UserPageService(){
        userMongoManager = new UserMongo();
        userNeo4jManager = new UserNeo4j();
        podcastNeo4jManager = new PodcastNeo4j();
        authorNeo4jManager = new AuthorNeo4j();
        reviewMongoManager = new ReviewMongo();
    }


    public int loadUserPageProfile(
            String visitorType,
            String visitor,
            User pageOwner,
            List<Podcast> wPodcasts,
            List<Podcast> lPodcasts,
            List<Author> followedAuthors,
            List<User> followedUsers,
            List<String> wPodcastsByVisitor,
            List<String> lPodcastsByVisitor,
            List<String> followedAuthorsByVisitor,
            List<String> followedUsersByVisitor,
            int limitPodcast,
            int limitActor ){

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
            List<Podcast> podcasts = podcastNeo4jManager.showPodcastsInWatchlist(pageOwner.getUsername(), limitPodcast, 0);
            if(podcasts != null)
                wPodcasts.addAll(podcasts);

            //load liked podcasts from neo4j
            podcasts = podcastNeo4jManager.showLikedPodcastsByUser(pageOwner.getUsername(), limitPodcast, 0);
            if(podcasts != null)
                lPodcasts.addAll(podcasts);

            //load followed authors from neo4j
            List<Author> authors = authorNeo4jManager.showFollowedAuthorsByUser(pageOwner.getUsername(), limitActor, 0);
            if(authors != null)
                followedAuthors.addAll(authors);

            //load followed users from neo4j
            List<User> users = userNeo4jManager.showFollowedUsers(user.getUsername(), limitActor, 0);
            if(users != null)
                followedUsers.addAll(users);

            if(visitorType.equals("User") && !visitor.equals(pageOwner.getUsername())){

                List<String> list = new ArrayList<>();
                //load podcasts in the visitor's watchlist
                list = podcastNeo4jManager.showPodcastsInWatchlist(visitor);
                if(list != null)
                    wPodcastsByVisitor.addAll(list);

                //load liked podcasts by the visitor
                list = podcastNeo4jManager.showLikedPodcastsByUser(visitor);
                if(list != null)
                    lPodcastsByVisitor.addAll(list);

                //load followed authors by the visitors // TODO: da modificare con la cache
                List<Author> a = authorNeo4jManager.showFollowedAuthorsByUser(visitor);
                if(a != null)
                    for (Author author : a)
                        followedAuthorsByVisitor.add(author.getName());

                //load followed users by the visitors
                list = userNeo4jManager.showFollowedUsers(visitor);
                if(list != null)
                    followedUsersByVisitor.addAll(list);

                res = 0;
            }
            else if(visitorType.equals("Author")){

                //load followed authors by visitor
                List<Author> list = authorNeo4jManager.showFollowedAuthorsByAuthor(visitor);
                if(list != null)
                    for (Author author : list)
                        followedAuthorsByVisitor.add(author.getName());
                res = 0;

            }
            else
                res = 0;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int getMoreWatchlaterPodcasts(String pageOwner, List<Podcast> wPodcast, int limit){
        int res = -1;
        Neo4jManager.getInstance().openConnection();

        int skip = wPodcast.size();
        List<Podcast> podcasts = podcastNeo4jManager.showPodcastsInWatchlist(pageOwner, limit, skip);
        if(podcasts != null){
            wPodcast.addAll(podcasts);
            res = 0;
        }
        else
            res = 1;

        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int getMoreLikedPodcasts(String pageOwner, List<Podcast> lPodcast, int limit){
        int res = -1;
        Neo4jManager.getInstance().openConnection();

        int skip = lPodcast.size();
        List<Podcast> podcasts = podcastNeo4jManager.showLikedPodcastsByUser(pageOwner, limit, skip);
        if(podcasts != null){
            lPodcast.addAll(podcasts);
            res = 0;
        }
        else
            res = 1;

        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int getMoreFollowedAuthors(String pageOwner, List<Author> followedAuthors, int limit){
        int res = -1;
        Neo4jManager.getInstance().openConnection();

        int skip = followedAuthors.size();
        List<Author> authors = authorNeo4jManager.showFollowedAuthorsByUser(pageOwner, limit, skip);
        if(authors != null) {
            followedAuthors.addAll(authors);
            res = 0;
        }
        else
            res = 1;

        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int getMoreFollowedUsers(String pageOwner, List<User> followedUsers, int limit){
        int res = -1;
        Neo4jManager.getInstance().openConnection();

        int skip = followedUsers.size();
        List<User> users = userNeo4jManager.showFollowedUsers(pageOwner, limit, skip);
        if(users != null){
            followedUsers.addAll(users);
            res = 0;
        }
        else
            res = 1;

        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateUserPageOwner(User oldUser, User newUser) {
        int res = -1;

        //check if there is something to update
        if (oldUser.getUsername().equals(newUser.getUsername()) &&
                oldUser.getCountry().equals(newUser.getCountry()) &&
                oldUser.getGender().equals(newUser.getGender()) &&
                oldUser.getName().equals(newUser.getName()) &&
                oldUser.getSurname().equals(newUser.getSurname()) &&
                oldUser.getEmail().equals(newUser.getEmail()) &&
                oldUser.getAge() == newUser.getAge() &&
                oldUser.getPicturePath().equals(newUser.getPicturePath())
        )
            return 1;

        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if a user with the new username already exists
        if (!oldUser.getUsername().equals(newUser.getUsername()) && userMongoManager.findUserByUsername(newUser.getUsername()) != null)
            res = 2;
        //update user on mongo
        else if (!userMongoManager.updateUser(newUser))
            res = 3;
        //update user on neo4j
        else if (!userNeo4jManager.updateUser(oldUser.getUsername(), newUser.getUsername(), newUser.getPicturePath())) {
            userMongoManager.updateUser(oldUser);
            res = 4;
        }
        //update reviews' authorUsername written by the user
        else if (!oldUser.getUsername().equals(newUser.getUsername()) && reviewMongoManager.updateReviewsByAuthorUsername(oldUser.getUsername(), newUser.getUsername()) == -1){
            userMongoManager.updateUser(oldUser);
            userNeo4jManager.updateUser(newUser.getUsername(), oldUser.getUsername(), oldUser.getPicturePath());
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

        //check if user1 follows user2
        if(!userNeo4jManager.findUserFollowsUser(user1, user2))
            res = 1;
        else
            res = 0;
        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
        return res;

    }

    public int deleteUserPageOwner(User user){
        int res;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        if(!userMongoManager.deleteUserByUsername(user.getUsername()))
            res = 1;
        else if(!userNeo4jManager.deleteUser(user.getUsername())){
            userMongoManager.addUser(user);
            res =  2;
        }
        else if(reviewMongoManager.updateReviewsByAuthorUsername(user.getUsername(), "Removed account") == -1){
            userMongoManager.addUser(user);
            userNeo4jManager.addUser(user.getUsername(), user.getPicturePath());
            res = 3;
        }
        else
            res = 0;
        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateFollowUser(String user1, String user2, boolean toAdd){
        int res = -1;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        if(toAdd) {
            //check if the relation already exists
            if(userNeo4jManager.findUserFollowsUser(user1, user2))
                res = 1;
            else if(!userNeo4jManager.addUserFollowUser(user1, user2))
                res = 2;
            else
                res = 0;
        }
        else{
            //check if the relation already not exists
            if(!userNeo4jManager.findUserFollowsUser(user1, user2))
                res = 3;
            else if(!userNeo4jManager.deleteUserFollowUser(user1, user2))
                res = 4;
            else
                res = 0;
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
        return res;
    }

    public int updateWatchlist(String username, String podcastId, boolean toAdd){

        int res = -1;
        Neo4jManager.getInstance().openConnection();

        if(toAdd) {
            //check if watch later relation already exists
            if(userNeo4jManager.checkUserWatchLaterPodcastExists(username, podcastId))
                res = 1;
            //adding watch later relation
            else if (!userNeo4jManager.addUserWatchLaterPodcast(username, podcastId))
                res = 2;
            else
                res = 0;
        }
        else{
            //check if watch later relation already not exists
            if(!userNeo4jManager.checkUserWatchLaterPodcastExists(username, podcastId))
                res = 3;
            //removing watch later relation
            else if (!userNeo4jManager.deleteUserWatchLaterPodcast(username, podcastId))
                res = 4;
            else
                res = 0;
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateLiked(String username, String podcastId, boolean toAdd){

        int res = -1;
        Neo4jManager.getInstance().openConnection();

        if(toAdd) {
            //check if like relation already exists
            if(userNeo4jManager.checkUserLikesPodcastExists(username, podcastId))
                res = 1;
            //adding like relation
            else if (!userNeo4jManager.addUserLikesPodcast(username, podcastId))
                res = 2;
            else
                res = 0;
        }
        else{
            //check if like relation already not exists
            if(!userNeo4jManager.checkUserLikesPodcastExists(username, podcastId))
                res = 3;
            //removing relation
            else if (!userNeo4jManager.deleteUserLikesPodcast(username, podcastId))
                res = 4;
            else
                res = 0;
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateFollowedUser(String visitor, String username, boolean toAdd){

        int res = -1;
        Neo4jManager.getInstance().openConnection();

        if(toAdd) {
            //check if follow user relation already exists
            if(userNeo4jManager.findUserFollowsUser(visitor, username))
                res = 1;
            //adding follow user relation
            else if (!userNeo4jManager.addUserFollowUser(visitor, username))
                res = 2;
            else
                res = 0;
        }
        else{
            //check if follow user relation already not exists
            if(!userNeo4jManager.findUserFollowsUser(visitor, username))
                res = 3;
            //removing follow user relation
            else if (!userNeo4jManager.deleteUserFollowUser(visitor, username))
                res = 4;
            else
                res = 0;
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateFollowedAuthor(String visitorName, String visitorType, String author, boolean toAdd){

        int res = -1;
        Neo4jManager.getInstance().openConnection();

        if(visitorType.equals("User")) {
            if (toAdd) {
                //check if user follow author relation already exists
                if(userNeo4jManager.findUserFollowsAuthor(visitorName, author))
                    res = 1;
                //adding user follow author relation
                else if (!userNeo4jManager.addUserFollowAuthor(visitorName, author))
                    res = 2;
                else
                    res = 0;
            }
            else {
                //check if user follow author relation already not exists
                if(!userNeo4jManager.findUserFollowsAuthor(visitorName, author))
                    res = 3;
                //removing user follow author relation
                else if (!userNeo4jManager.deleteUserFollowAuthor(visitorName, author))
                    res = 4;
                else
                    res = 0;
            }
        }
        else if(visitorType.equals("Author")) {
            if (toAdd) {
                //check if author follow author relation already exists
                if(authorNeo4jManager.findAuthorFollowsAuthor(visitorName, author))
                    res = 5;
                //adding author follow author relation
                else if (!authorNeo4jManager.addAuthorFollowsAuthor(visitorName, author))
                    res = 6;
                else
                    res = 0;
            }
            else {
                //check if author follow author relation already not exists
                if(!authorNeo4jManager.findAuthorFollowsAuthor(visitorName, author))
                    res = 7;
                //removing author follow author relation
                else if (!authorNeo4jManager.deleteAuthorFollowsAuthor(visitorName, author))
                    res = 8;
                else
                    res = 0;
            }
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

}
