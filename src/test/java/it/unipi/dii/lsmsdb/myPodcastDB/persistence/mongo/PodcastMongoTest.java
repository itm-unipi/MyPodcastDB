package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

public class PodcastMongoTest {

    PodcastMongo podcastMongo;

    public PodcastMongoTest() {
        this.podcastMongo = new PodcastMongo();
    }

    void deleteAllEpisodesTest() {
        this.podcastMongo.deleteAllEpisodesOfPodcast("9aaae9ac725c3a586701abf4");
    }

    void deleteReviewTest() {
        this.podcastMongo.deleteReviewOfPodcast("34e734b09246d17dc5d56f63", "000000000000000000080116");
    }

    void deleteAllReviewsTest() {
        this.podcastMongo.deleteAllReviewsOfPodcast("9aaae9ac725c3a586701abf4");
    }

    public static void main(String[] args) {
        MongoManager manager = MongoManager.getInstance();
        manager.openConnection();
        PodcastMongoTest test = new PodcastMongoTest();

        test.deleteAllEpisodesTest();
        test.deleteReviewTest();
        test.deleteAllReviewsTest();

        manager.closeConnection();
    }
}
