package it.unipi.dii.lsmsdb.myPodcastDB.cache;

import it.unipi.dii.lsmsdb.myPodcastDB.model.User;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class FollowedUserCache {
    private static FollowedUserCache followedUserCache;
    private Hashtable<String, User> htUser;

    public FollowedUserCache() { this.htUser = new Hashtable<>(); }

    public static List<User> getAllFollowedUsers() {
        return new ArrayList<>(getInstance().htUser.values());
    }

    public static User getUser(String username) {
        return  getInstance().htUser.get(username);
    }

    public static void addUser(User user) {
        getInstance().htUser.put(user.getUsername(), user);
    }

    public static void addUserList(List<User> users) {
        for (User user : users)
            getInstance().htUser.put(user.getId(), user);
    }

    public static void removeUser(String username) {
        getInstance().htUser.remove(username);
    }

    public static void clearUsers() {
        getInstance().htUser.clear();
    }

    public static FollowedUserCache getInstance() {
        if (followedUserCache == null)
            followedUserCache = new FollowedUserCache();
        return followedUserCache;
    }
}
