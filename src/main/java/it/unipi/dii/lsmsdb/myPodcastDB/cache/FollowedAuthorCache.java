package it.unipi.dii.lsmsdb.myPodcastDB.cache;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class FollowedAuthorCache {
    private static FollowedAuthorCache followedAuthorCache;
    private Hashtable<String, Author> htAuthor;

    public FollowedAuthorCache() { this.htAuthor = new Hashtable<>(); }

    public static List<Author> getAllFollowedAuthors() {
        return new ArrayList<>(getInstance().htAuthor.values());
    }

    public static Author getAuthor(String authorName) {
        return  getInstance().htAuthor.get(authorName);
    }

    public static void addAuthor(Author author) {
        getInstance().htAuthor.put(author.getName(), author);
    }

    public static void addAuthorList(List<Author> authors) {
        for (Author author : authors)
            getInstance().htAuthor.put(author.getName(), author);
    }

    public static void removeAuthor(String authorName) {
        getInstance().htAuthor.remove(authorName);
    }

    public static void clearAuthors() {
        getInstance().htAuthor.clear();
    }

    public static FollowedAuthorCache getInstance() {
        if (followedAuthorCache == null)
            followedAuthorCache = new FollowedAuthorCache();
        return followedAuthorCache;
    }
}
