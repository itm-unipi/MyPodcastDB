package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import it.unipi.dii.lsmsdb.myPodcastDB.model.User;

public class UserNeo4j {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addUser(User user) {
        return false;
    }

    public boolean addUserLikesPodcast(String username, String podcastId) {
        return false;
    }

    public boolean addUserWatchLaterPodcast(String username, String podcastId) {
        return false;
    }

    public boolean addUserFollowUser(String username, String userToFollow) {
        return false;
    }

    public boolean addUserFollowAuthor(String username, String author) {
        return false;
    }

    // ---------- READ ---------- //

    // TODO

    // --------- UPDATE --------- //

    public boolean updateUser(User user) {
        return false;
    }

    // --------- DELETE --------- //

    public boolean deleteUser(User user) {
        return false;
    }

    public boolean deleteUserLikesPodcast(String username, String podcastId) {
        return false;
    }

    public int deleteAllUserLikesPodcast(String username) {
        return -1;
    }

    public boolean deleteUserWatchLaterPodcast(String username, String podcastId) {
        return false;
    }

    public int deleteAllUserWatchLaterPodcast(String username) {
        return -1;
    }

    public boolean deleteUserFollowUser(String username, String userFollowed) {
        return false;
    }

    public int deleteAllUserFollowUser(String username) {
        return -1;
    }

    public boolean deleteUserFollowAuthor(String username, String author) {
        return false;
    }

    public int deleteAllUserFollowAuthor(String username, String author) {
        return -1;
    }

    // ---------------------------------------------------------------------------------- //

    // --------------------------------- GRAPH QUERY ------------------------------------ //

    // TODO: query per consigliare user???

    // ---------------------------------------------------------------------------------- //
}
