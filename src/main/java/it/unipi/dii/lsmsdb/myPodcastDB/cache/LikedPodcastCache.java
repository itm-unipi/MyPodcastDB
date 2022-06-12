package it.unipi.dii.lsmsdb.myPodcastDB.cache;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class LikedPodcastCache {
    private static LikedPodcastCache likedPodcastCache;
    private Hashtable<String, Podcast> htPodcast;

    public LikedPodcastCache() { this.htPodcast = new Hashtable<>(); }

    public static List<Podcast> getAllLikedPodcasts() {
        return new ArrayList<>(getInstance().htPodcast.values());
    }

    public static Podcast getPodcast(String podcastId) {
        return  getInstance().htPodcast.get(podcastId);
    }

    public static void addPodcast(Podcast podcast) {
        getInstance().htPodcast.put(podcast.getId(), podcast);
    }

    public static void addPodcastList(List<Podcast> podcasts) {
        for (Podcast podcast : podcasts)
            getInstance().htPodcast.put(podcast.getId(), podcast);
    }

    public static void removePodcast(String podcastId) {
        getInstance().htPodcast.remove(podcastId);
    }

    public static void clearPodcasts() {
        getInstance().htPodcast.clear();
    }

    public static LikedPodcastCache getInstance() {
        if (likedPodcastCache == null)
            likedPodcastCache = new LikedPodcastCache();
        return likedPodcastCache;
    }
}
