package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.User;

import java.util.List;
import java.util.Map.Entry;

public class UserMongo {

    // ------------------------------- CRUD OPERATION ----------------------------------- //

    // --------- CREATE --------- //

    public boolean addUser(User user) {
        // user.setId(risultato.getObjectId("_id").toString());
        return false;
    }

    // ---------- READ ---------- //

    public User findUserById(String id) {
        return null;
    }

    public User findUserByUsername(String username) {
        return null;
    }

    public User findUserByEmail(String email) {
        return null;
    }

    public List<User> findUsersByCountry(String country) {
        return null;
    }

    // --------- UPDATE --------- //

    public boolean updateUser(User user) {
        return false;
    }

    // --------- DELETE --------- //

    public boolean deleteUserById(String id) {
        return false;
    }

    public boolean deleteUserByUsername(String username) {
        return false;
    }

    // ---------------------------------------------------------------------------------- //

    // ------------------------------- AGGREGATION QUERY -------------------------------- //

    public List<Entry<String, Float>> showAverageAgeOfUsersPerFavouriteCategory(int limit) {
        return null;
    }

    public List<Entry<String, Float>> showAverageAgeOfUsersPerCountry(int limit) {
        return null;
    }

    public List<Entry<String, Integer>> showNumberOfUsersPerCountry(int limit) {
        return null;
    }

    public String showFavouriteCategoryForGender(String gender) {
        return null;
    }

    // ---------------------------------------------------------------------------------- //
}
