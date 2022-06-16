package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.FollowedAuthorCache;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.FollowedUserCache;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.LikedPodcastCache;
import it.unipi.dii.lsmsdb.myPodcastDB.cache.WatchlistCache;
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
            int limitPodcast,
            int podcastRowSize
            ){

        int res;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();
        boolean ownerMode = false;

        //load user info from mongo (if it's the owner, the info are already in session)
        if(visitorType.equals("User") && pageOwner.getUsername().equals(visitor)) {
            User user = (User)MyPodcastDB.getInstance().getSessionActor();
            pageOwner.copy(user);
            ownerMode = true;
        }
        else{
            User user = userMongoManager.findUserByUsername(pageOwner.getUsername());
            if (user != null)
                pageOwner.copy(user);
            else
                pageOwner = null;

            ownerMode = false;
        }
        if(pageOwner == null)
            res = 1;
        else {
            List<Podcast> podcasts;
            List<Author> authors;
            List<User> users;

            //load podcasts in watchlist from neo4j
            if(ownerMode) {
                Logger.info("watchlist loaded from cache");
                wPodcasts.addAll(WatchlistCache.getAllPodcastsInWatchlist());
            }
            else {
                podcasts = podcastNeo4jManager.showPodcastsInWatchlist(pageOwner.getUsername());
                if (podcasts != null)
                    wPodcasts.addAll(podcasts);
            }

            //load liked podcasts from neo4j
            if(ownerMode && LikedPodcastCache.getAllLikedPodcasts().size() >= podcastRowSize) {
                Logger.info("Liked podcast loaded from cache");
                lPodcasts.addAll(LikedPodcastCache.getAllLikedPodcasts());
            }
            else {
                podcasts = podcastNeo4jManager.showLikedPodcastsByUser(pageOwner.getUsername(), limitPodcast, 0);
                if (podcasts != null) {
                    lPodcasts.addAll(podcasts);
                    if(ownerMode)
                        LikedPodcastCache.addPodcastList(podcasts);
                }
            }

            //load followed authors from neo4j
            if(ownerMode) {
                Logger.info("Followed Authors loaded from cache");
                followedAuthors.addAll(FollowedAuthorCache.getAllFollowedAuthors());
            }
            else {
                authors = authorNeo4jManager.showFollowedAuthorsByUser(pageOwner.getUsername());
                if (authors != null)
                    followedAuthors.addAll(authors);
            }

            //load followed users from neo4j
            if(ownerMode) {
                Logger.info("Followed users loaded from cache");
                followedUsers.addAll(FollowedUserCache.getAllFollowedUsers());
            }
            else {
                users = userNeo4jManager.showFollowedUsers(pageOwner.getUsername());
                if (users != null)
                    followedUsers.addAll(users);
            }
            /*if(visitorType.equals("User") && !ownerMode){


                //load podcasts in the visitor's watchlist
                if(WatchlistCache.getAllPodcastsInWatchlist().isEmpty()) {
                    podcasts = podcastNeo4jManager.showPodcastsInWatchlist(visitor);
                    if (podcasts != null)
                        WatchlistCache.addPodcastList(podcasts);
                }

                //load followed authors by the visitors
                if(FollowedAuthorCache.getAllFollowedAuthors().isEmpty()) {
                    authors = authorNeo4jManager.showFollowedAuthorsByUser(visitor);
                    if (authors != null)
                        FollowedAuthorCache.addAuthorList(authors);
                }

                //load followed users by the visitors
                if(FollowedUserCache.getAllFollowedUsers().isEmpty()) {
                    users = userNeo4jManager.showFollowedUsers(visitor);
                    if (users != null)
                        FollowedUserCache.addUserList(users);
                }

                res = 0;
            }
            else if(visitorType.equals("Author")){

                //load followed authors by visitor
                if(FollowedUserCache.getAllFollowedUsers().isEmpty()) {
                    List<Author> list = authorNeo4jManager.showFollowedAuthorsByAuthor(visitor);
                    if (list != null)
                        FollowedAuthorCache.addAuthorList(list);
                }
                res = 0;

            }
            else*/
                res = 0;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;
    }
    /*
    public int getMoreWatchlaterPodcasts(String pageOwner, List<Podcast> wPodcast, int limit){
        int res;
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
    */
    public int getMoreLikedPodcasts(String pageOwner, List<Podcast> lPodcast, int limit){
        int res;
        Neo4jManager.getInstance().openConnection();

        int skip = lPodcast.size();
        List<Podcast> podcasts = podcastNeo4jManager.showLikedPodcastsByUser(pageOwner, limit, skip);
        if(podcasts != null){
            lPodcast.addAll(podcasts);
            LikedPodcastCache.addPodcastList(podcasts);
            res = 0;
        }
        else
            res = 1;

        Neo4jManager.getInstance().closeConnection();
        return res;
    }
    /*
    public int getMoreFollowedAuthors(String pageOwner, List<Author> followedAuthors, int limit){
        int res;
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
        int res;
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
    */
    public int updateUserPageOwner(User oldUser, User newUser) {
        int res;

        //check if there is something to update
        if (oldUser.getUsername().equals(newUser.getUsername()) &&
                oldUser.getCountry().equals(newUser.getCountry()) &&
                oldUser.getGender().equals(newUser.getGender()) &&
                oldUser.getName().equals(newUser.getName()) &&
                oldUser.getSurname().equals(newUser.getSurname()) &&
                oldUser.getFavouriteGenre().equals(newUser.getFavouriteGenre()) &&
                oldUser.getEmail().equals(newUser.getEmail()) &&
                oldUser.getAge() == newUser.getAge() &&
                oldUser.getPicturePath().equals(newUser.getPicturePath()) &&
                oldUser.getPassword().equals(newUser.getPassword())
        )
            return 1;

        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if a user with the same username already exists
        if(!oldUser.getUsername().equals(newUser.getUsername()) && userMongoManager.findUserByUsername(newUser.getUsername()) != null)
            res = 2;
        //check if a user with the same email already exists
        else if(!oldUser.getEmail().equals(newUser.getEmail()) && userMongoManager.findUserByEmail(newUser.getEmail()) != null)
            res = 3;
        //update user on mongo
        else if (!userMongoManager.updateUser(newUser))
            res = 4;
        //update user on neo4j
        else if (!oldUser.getUsername().equals(newUser.getUsername()) && !userNeo4jManager.updateUser(oldUser.getUsername(), newUser.getUsername(), newUser.getPicturePath())) {
            userMongoManager.updateUser(oldUser);
            res = 5;
        }
        //update reviews' authorUsername written by the user
        //TODO farlo con un clco for per reviewid sfruttando le review embedded nello user
        else if (!oldUser.getUsername().equals(newUser.getUsername()) && reviewMongoManager.updateReviewsByAuthorUsername(oldUser.getUsername(), newUser.getUsername()) == -1){
            userMongoManager.updateUser(oldUser);
            userNeo4jManager.updateUser(newUser.getUsername(), oldUser.getUsername(), oldUser.getPicturePath());
            res = 6;
        }
        //update review embedded in podcast
        //TODO farlo sfruttando il podcastId nelle review embedded nello user
        else if(!oldUser.getUsername().equals(newUser.getUsername()) && false){
            userMongoManager.updateUser(oldUser);
            userNeo4jManager.updateUser(newUser.getUsername(), oldUser.getUsername(), oldUser.getPicturePath());
            reviewMongoManager.updateReviewsByAuthorUsername(newUser.getUsername(), oldUser.getUsername());
            res = 7;
        }
        else {
            MyPodcastDB.getInstance().setSession(newUser, "User");
            res = 0;
        }

        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int checkFollowUser(/*String user1,*/ User user2){
        int res;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        //check if user1 follows user2
        //if(!userNeo4jManager.findUserFollowsUser(user1, user2))
        if(FollowedUserCache.getUser(user2.getUsername()) == null)
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

        //delete user on mongo
        if(!userMongoManager.deleteUserByUsername(user.getUsername()))
            res = 1;
        //delete user on neo4j
        else if(!userNeo4jManager.deleteUser(user.getUsername())){
            userMongoManager.addUser(user);
            res =  2;
        }
        //update author username in reviews written by the removed user
        //TODO farlo con un clco for per reviewid sfruttando le review embedded nello user
        else if(reviewMongoManager.updateReviewsByAuthorUsername(user.getUsername(), "Removed account") == -1){
            userMongoManager.addUser(user);
            userNeo4jManager.addUser(user.getUsername(), user.getPicturePath());
            res = 3;
        }
        //update authore username in reviews embedded in podcast
        //TODO farlo sfruttando il podcastId nelle review embedded nello user
        else if(false){
            userMongoManager.addUser(user);
            userNeo4jManager.addUser(user.getUsername(), user.getPicturePath());
            reviewMongoManager.updateReviewsByAuthorUsername("Removed account", user.getUsername());
            res = 4;

        }
        else
            res = 0;
        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateFollowUser(String user1, User user2, boolean toAdd){
        int res;
        MongoManager.getInstance().openConnection();
        Neo4jManager.getInstance().openConnection();

        if(toAdd) {
            //check if the relation already exists
            if(userNeo4jManager.findUserFollowsUser(user1, user2.getUsername()))
                res = 1;
            else if(!userNeo4jManager.addUserFollowUser(user1, user2.getUsername()))
                res = 2;
            else {
                FollowedUserCache.addUser(user2);
                res = 0;
            }
        }
        else{
            //check if the relation already not exists
            if(!userNeo4jManager.findUserFollowsUser(user1, user2.getUsername()))
                res = 3;
            else if(!userNeo4jManager.deleteUserFollowUser(user1, user2.getUsername()))
                res = 4;
            else {
                FollowedUserCache.removeUser(user2.getUsername());
                res = 0;
            }
        }

        Neo4jManager.getInstance().closeConnection();
        MongoManager.getInstance().closeConnection();
        return res;
    }

    public int updateWatchlist(String username, Podcast podcast, boolean toAdd){

        int res;
        Neo4jManager.getInstance().openConnection();

        if(toAdd) {
            //check if watch later relation already exists
            if(userNeo4jManager.checkUserWatchLaterPodcastExists(username, podcast.getId()))
                res = 1;
            //check if the watchlist is full
            else if(!WatchlistCache.addPodcast(podcast))
                res = 2;
            //adding watch later relation
            else if (!userNeo4jManager.addUserWatchLaterPodcast(username, podcast.getId())) {
                res = 3;
                WatchlistCache.removePodcast(podcast.getId());
            }
            else
                res = 0;
        }
        else{
            //check if watch later relation already not exists
            if(!userNeo4jManager.checkUserWatchLaterPodcastExists(username, podcast.getId()))
                res = 4;
            //removing watch later relation
            else if (!userNeo4jManager.deleteUserWatchLaterPodcast(username, podcast.getId()))
                res = 5;
            else {
                WatchlistCache.removePodcast(podcast.getId());
                res = 0;
            }
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateLiked(String username, Podcast podcast, boolean toAdd){

        int res;
        Neo4jManager.getInstance().openConnection();

        if(toAdd) {
            //check if like relation already exists
            if(userNeo4jManager.checkUserLikesPodcastExists(username, podcast.getId()))
                res = 1;
            //adding like relation
            else if (!userNeo4jManager.addUserLikesPodcast(username, podcast.getId()))
                res = 2;
            else {
                LikedPodcastCache.addPodcast(podcast);
                res = 0;
            }
        }
        else{
            //check if like relation already not exists
            if(!userNeo4jManager.checkUserLikesPodcastExists(username, podcast.getId()))
                res = 3;
            //removing relation
            else if (!userNeo4jManager.deleteUserLikesPodcast(username, podcast.getId()))
                res = 4;
            else {
                LikedPodcastCache.removePodcast(podcast.getId());
                res = 0;
            }
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateFollowedUser(String visitor, User user, boolean toAdd){

        int res;
        Neo4jManager.getInstance().openConnection();

        if(toAdd) {
            //check if follow user relation already exists
            if(userNeo4jManager.findUserFollowsUser(visitor, user.getUsername()))
                res = 1;
            //adding follow user relation
            else if (!userNeo4jManager.addUserFollowUser(visitor, user.getUsername()))
                res = 2;
            else {
                FollowedUserCache.addUser(user);
                res = 0;
            }
        }
        else{
            //check if follow user relation already not exists
            if(!userNeo4jManager.findUserFollowsUser(visitor, user.getUsername()))
                res = 3;
            //removing follow user relation
            else if (!userNeo4jManager.deleteUserFollowUser(visitor, user.getUsername()))
                res = 4;
            else {
                FollowedUserCache.removeUser(user.getUsername());
                res = 0;
            }
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateFollowedAuthor(String visitorName, String visitorType, Author author, boolean toAdd){

        int res = -1;
        Neo4jManager.getInstance().openConnection();

        if(visitorType.equals("User")) {
            if (toAdd) {
                //check if user follow author relation already exists
                if(userNeo4jManager.findUserFollowsAuthor(visitorName, author.getName()))
                    res = 1;
                //adding user follow author relation
                else if (!userNeo4jManager.addUserFollowAuthor(visitorName, author.getName()))
                    res = 2;
                else {
                    FollowedAuthorCache.addAuthor(author);
                    res = 0;
                }
            }
            else {
                //check if user follow author relation already not exists
                if(!userNeo4jManager.findUserFollowsAuthor(visitorName, author.getName()))
                    res = 3;
                //removing user follow author relation
                else if (!userNeo4jManager.deleteUserFollowAuthor(visitorName, author.getName()))
                    res = 4;
                else {
                    FollowedAuthorCache.removeAuthor(author.getName());
                    res = 0;
                }
            }
        }
        else if(visitorType.equals("Author")) {
            if (toAdd) {
                //check if author follow author relation already exists
                if(authorNeo4jManager.findAuthorFollowsAuthor(visitorName, author.getName()))
                    res = 5;
                //adding author follow author relation
                else if (!authorNeo4jManager.addAuthorFollowsAuthor(visitorName, author.getName()))
                    res = 6;
                else {
                    FollowedAuthorCache.addAuthor(author);
                    res = 0;
                }
            }
            else {
                //check if author follow author relation already not exists
                if(!authorNeo4jManager.findAuthorFollowsAuthor(visitorName, author.getName()))
                    res = 7;
                //removing author follow author relation
                else if (!authorNeo4jManager.deleteAuthorFollowsAuthor(visitorName, author.getName()))
                    res = 8;
                else {
                    FollowedAuthorCache.removeAuthor(author.getName());
                    res = 0;
                }
            }
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

}
