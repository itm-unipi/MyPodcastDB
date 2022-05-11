package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;

import java.util.Date;

public class ReviewMongoTest {
    public static void main(String[] args) {
        System.out.println("Test Review");

        MongoManager mongoManager = MongoManager.getInstance();
        mongoManager.openConnection();
        ReviewMongo rm = new ReviewMongo();

        int deletedCount = rm.deleteReviewsByPodcastId("7d7281ba3edb77da6a9fe95c");
        System.out.println(deletedCount);

        if(rm.deleteReviewById("000000000000000000000002"))
            System.out.println("[+] OK");
        else
            System.err.println("[-] FAIL");

        deletedCount = rm.deleteReviewsByAuthorUsername("ticklishcat734546");
        System.out.println(deletedCount);

        Date dateReview = new Date();
        Review newReview = new Review("000000000000000000000003", "b09ab608dd57d787d28980e9", "organicmouse599943", "titleReview", "We buonasera", 5, dateReview);

        if(rm.updateReview(newReview))
            System.out.println("[+] OK");
        else
            System.err.println("[-] FAIL");

        mongoManager.closeConnection();
    }
}
