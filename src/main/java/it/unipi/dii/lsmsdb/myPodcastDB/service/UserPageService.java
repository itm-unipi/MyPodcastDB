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
        else if(!userNeo4jManager.findUserByUsername(pageOwner.getUsername()))
            res = 2;
        else {
            pageOwner.copy(user);
            //load podcasts in watchlist from neo4j
            List<Podcast> podcasts = podcastNeo4jManager.showPodcastsInWatchlist(pageOwner.getUsername(), limitPodcast, 0);
            if(podcasts != null)
                wPodcasts.addAll(podcasts);

            //load liked podcasts
            podcasts = podcastNeo4jManager.showLikedPodcastsByUser(pageOwner.getUsername(), limitPodcast, 0);
            if(podcasts != null)
                lPodcasts.addAll(podcasts);

            //load followed authors
            List<Author> authors = authorNeo4jManager.showFollowedAuthorsByUser(pageOwner.getUsername(), limitActor, 0);
            if(authors != null)
                followedAuthors.addAll(authors);

            //load followed users
            List<User> users = userNeo4jManager.showFollowedUsers(user.getUsername(), limitActor, 0);
            if(users != null)
                followedUsers.addAll(users);

            if(visitorType.equals("User") && !visitor.equals(pageOwner.getUsername())){

                if(!userNeo4jManager.findUserByUsername(visitor))
                    res = 3;
                else{
                    podcasts = podcastNeo4jManager.showPodcastsInWatchlist(visitor);
                    if(podcasts != null)
                        for(int i = 0; i < podcasts.size(); i++)
                            wPodcastsByVisitor.add(podcasts.get(i).getId());

                    podcasts = podcastNeo4jManager.showLikedPodcastsByUser(visitor);
                    if(podcasts != null)
                        for(int i = 0; i < podcasts.size(); i++)
                            lPodcastsByVisitor.add(podcasts.get(i).getId());

                    authors = authorNeo4jManager.showFollowedAuthorsByUser(visitor);
                    if(authors != null)
                        for(int i = 0; i < authors.size(); i++)
                            followedAuthorsByVisitor.add(authors.get(i).getName());

                    users = userNeo4jManager.showFollowedUsers(visitor);
                    if(users != null)
                        for(int i = 0; i < users.size(); i++)
                            followedUsersByVisitor.add(users.get(i).getUsername());

                    res = 0;
                }

            }
            else if(visitorType.equals("Author")){

                if(!authorNeo4jManager.findAuthorByName(visitor))
                    res = 4;
                else{
                    authors = authorNeo4jManager.showFollowedAuthorsByAuthor(visitor, 0, 0);
                    if(authors != null)
                        for(int i = 0; i < authors.size(); i++)
                            followedAuthorsByVisitor.add(authors.get(i).getName());
                    res = 0;
                }

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
        if(!userNeo4jManager.findUserByUsername(pageOwner))
            res = 2;
        else{
            int skip = wPodcast.size();
            List<Podcast> podcasts = podcastNeo4jManager.showPodcastsInWatchlist(pageOwner, limit, skip);
            if(podcasts != null){
                wPodcast.addAll(podcasts);
                res = 0;
            }
            else
                res = 1;
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int getMoreLikedPodcasts(String pageOwner, List<Podcast> lPodcast, int limit){
        int res = -1;
        Neo4jManager.getInstance().openConnection();
        if(!userNeo4jManager.findUserByUsername(pageOwner))
            res = 2;
        else{
            int skip = lPodcast.size();
            List<Podcast> podcasts = podcastNeo4jManager.showLikedPodcastsByUser(pageOwner, limit, skip);
            if(podcasts != null){
                lPodcast.addAll(podcasts);
                res = 0;
            }
            else
                res = 1;
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int getMoreFollowedAuthors(String pageOwner, List<Author> followedAuthors, int limit){
        int res = -1;
        Neo4jManager.getInstance().openConnection();
        if(!userNeo4jManager.findUserByUsername(pageOwner))
            res = 2;
        else{
            int skip = followedAuthors.size();
            List<Author> authors = authorNeo4jManager.showFollowedAuthorsByUser(pageOwner, limit, skip);
            if(authors != null) {
                followedAuthors.addAll(authors);
                res = 0;
            }
            else
                res = 1;
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int getMoreFollowedUsers(String pageOwner, List<User> followedUsers, int limit){
        int res = -1;
        Neo4jManager.getInstance().openConnection();
        if(!userNeo4jManager.findUserByUsername(pageOwner))
            res = 2;
        else{
            int skip = followedUsers.size();
            List<User> users = userNeo4jManager.showFollowedUsers(pageOwner, limit, skip);
            if(users != null){
                followedUsers.addAll(users);
                res = 0;
            }
            else
                res = 1;
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }


    public int updateUserPageOwner(User oldUser, User newUser){
        int res = -1;

        //check if there is something to update
        if(oldUser.getUsername().equals(newUser.getUsername()) &&
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

        //check if oldUser exists in mongo
        if(userMongoManager.findUserByUsername(oldUser.getUsername()) == null)
            res = 2;
            //check if oldUser exists in neo4j
        else if(!userNeo4jManager.findUserByUsername(oldUser.getUsername()))
            res = 3;
            //check if a user with the new username already exists
        else if(!oldUser.getUsername().equals(newUser.getUsername()) && userMongoManager.findUserByUsername(newUser.getUsername()) != null)
            res = 4;
            //failure mongo operation from persistence
        else if(!userMongoManager.updateUser(newUser))
            res = 5;
            //failure neo4j operation from persistence
        else if(!userNeo4jManager.updateUser(oldUser.getUsername(), newUser.getUsername(), newUser.getPicturePath())){
            userMongoManager.updateUser(oldUser);
            res = 6;
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
            //check if the relation already exists
            if(userNeo4jManager.findUserFollowsUser(user1, user2))
                res = 5;
            else if(!userNeo4jManager.addUserFollowUser(user1, user2))
                res = 6;
            else
                res = 0;
        }
        else{
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
        else if(reviewMongoManager.deleteReviewsByAuthorUsername(user.getUsername()) == 0){
            res = 0;
        }
        //else if(reviewMongoManager.deleteReviewsByPodcastId())
        else
            res = 0;
        MongoManager.getInstance().closeConnection();
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateWatchlist(String username, String podcastId, boolean ifInWatchlist){

        int res = -1;
        Neo4jManager.getInstance().openConnection();

        if(!userNeo4jManager.findUserByUsername(username))
            res = 1;
        else if(podcastNeo4jManager.findPodcastByPodcastId(podcastId) == null)
            res = 2;
        else if(ifInWatchlist) {
            if(userNeo4jManager.checkUserWatchLaterPodcastExists(username, podcastId))
                res = 3;
            else if (!userNeo4jManager.addUserWatchLaterPodcast(username, podcastId))
                res = 4;
            else
                res = 0;
        }
        else{
            if(!userNeo4jManager.checkUserWatchLaterPodcastExists(username, podcastId))
                res = 5;
            else if (!userNeo4jManager.deleteUserWatchLaterPodcast(username, podcastId))
                res = 6;
            else
                res = 0;
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateLiked(String username, String podcastId, boolean ifIsLiked){

        int res = -1;
        Neo4jManager.getInstance().openConnection();

        if(!userNeo4jManager.findUserByUsername(username))
            res = 1;
        else if(podcastNeo4jManager.findPodcastByPodcastId(podcastId) == null)
            res = 2;
        else if(ifIsLiked) {
            if(userNeo4jManager.checkUserLikesPodcastExists(username, podcastId))
                res = 3;
            else if (!userNeo4jManager.addUserLikesPodcast(username, podcastId))
                res = 4;
            else
                res = 0;
        }
        else{
            if(!userNeo4jManager.checkUserLikesPodcastExists(username, podcastId))
                res = 5;
            else if (!userNeo4jManager.deleteUserLikesPodcast(username, podcastId))
                res = 6;
            else
                res = 0;
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateFollowedUser(String visitor, String username, boolean ifIsFollowed){

        int res = -1;
        Neo4jManager.getInstance().openConnection();

        if(!userNeo4jManager.findUserByUsername(visitor))
            res = 1;
        else if(!userNeo4jManager.findUserByUsername(username))
            res = 2;
        else if(ifIsFollowed) {
            if(userNeo4jManager.findUserFollowsUser(visitor, username))
                res = 3;
            else if (!userNeo4jManager.addUserFollowUser(visitor, username))
                res = 4;
            else
                res = 0;
        }
        else{
            if(!userNeo4jManager.findUserFollowsUser(visitor, username))
                res = 5;
            else if (!userNeo4jManager.deleteUserFollowUser(visitor, username))
                res = 6;
            else
                res = 0;
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }

    public int updateFollowedAuthor(String visitorName, String visitorType, String author, boolean ifIsFollowed){

        int res = -1;
        Neo4jManager.getInstance().openConnection();

        if(visitorType.equals("User")) {
            if (!userNeo4jManager.findUserByUsername(visitorName))
                res = 1;
            else if (!authorNeo4jManager.findAuthorByName(author))
                res = 2;
            else if (ifIsFollowed) {
                if(userNeo4jManager.findUserFollowsAuthor(visitorName, author))
                    res = 3;
                else if (!userNeo4jManager.addUserFollowAuthor(visitorName, author))
                    res = 4;
                else
                    res = 0;
            }
            else {
                if(!userNeo4jManager.findUserFollowsAuthor(visitorName, author))
                    res = 5;
                else if (!userNeo4jManager.deleteUserFollowAuthor(visitorName, author))
                    res = 6;
                else
                    res = 0;
            }
        }
        else if(visitorType.equals("Author")) {
            if (!authorNeo4jManager.findAuthorByName(visitorName))
                res = 1;
            else if (!authorNeo4jManager.findAuthorByName(author))
                res = 2;
            else if (ifIsFollowed) {
                if(authorNeo4jManager.findAuthorFollowsAuthor(visitorName, author))
                    res = 3;
                else if (!authorNeo4jManager.addAuthorFollowsAuthor(visitorName, author))
                    res = 4;
                else
                    res = 0;
            }
            else {
                if(!authorNeo4jManager.findAuthorFollowsAuthor(visitorName, author))
                    res = 5;
                else if (!authorNeo4jManager.deleteAuthorFollowsAuthor(visitorName, author))
                    res = 6;
                else
                    res = 0;
            }
        }
        Neo4jManager.getInstance().closeConnection();
        return res;
    }


    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
