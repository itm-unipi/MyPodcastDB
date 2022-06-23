package it.unipi.dii.lsmsdb.myPodcastDB.cache;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class WatchlistCache {
    private static WatchlistCache watchlistCache;
    private Hashtable<String, Podcast> htPodcast;
    private static int limit = 100;
    private static int size;

    public WatchlistCache() {
        this.htPodcast = new Hashtable<>();
        size = 0;
    }

    public static List<Podcast> getAllPodcastsInWatchlist() {
        return new ArrayList<>(getInstance().htPodcast.values());
    }

    public static Podcast getPodcast(String podcastId) {
        return  getInstance().htPodcast.get(podcastId);
    }

    public static boolean addPodcast(Podcast podcast) {
        if(size >= limit)
            return false;
        else
            size++;
        getInstance().htPodcast.put(podcast.getId(), podcast);
        return true;
    }

    public static void addPodcastList(List<Podcast> podcasts) {
        for (Podcast podcast : podcasts)
            if(getInstance().htPodcast.put(podcast.getId(), podcast) == null)
                size++;
    }

    public static void removePodcast(String podcastId) {
        if(getInstance().htPodcast.remove(podcastId) != null)
            size--;
    }

    public static void clearPodcasts() {
        getInstance().htPodcast.clear();
        size = 0;
    }

    public static WatchlistCache getInstance() {
        if (watchlistCache == null)
            watchlistCache = new WatchlistCache();
        return watchlistCache;
    }
}
