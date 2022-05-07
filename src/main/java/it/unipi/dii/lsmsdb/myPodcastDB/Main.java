package it.unipi.dii.lsmsdb.myPodcastDB;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;

public class Main {
    public static void main(String[] args) {

        System.out.println("Test");

        // serie di operazioni da usare nei controller
        MongoManager manager = MongoManager.getInstance();
        manager.openConnection();

        PodcastMongo pm = new PodcastMongo();
        Podcast podcast = pm.findPodcastById("54eb342567c94dacfb2a3e50");
        System.out.println(podcast);
        System.out.println(podcast.getReleaseDateAsString());

        manager.closeConnection();
    }
}
