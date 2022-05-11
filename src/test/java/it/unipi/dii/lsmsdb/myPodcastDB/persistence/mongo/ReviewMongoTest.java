package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;

import java.util.Date;

public class ReviewMongoTest {

    ReviewMongo rm;

    public ReviewMongoTest() {
        this.rm = new ReviewMongo();
    }

    public void deleteReviewByIdTest() {
        // Review newReview = addReviewForTest();

        if(rm.deleteReviewById("000000000000000000000002"))
            System.out.println("[+] deleteReviewById");
        else
            System.err.println("[-] deleteReviewById");
    }

    public void deleteReviewsByPodcastIdTest() {
        // Review newReview = addReviewForTest();
        int deletedCount = rm.deleteReviewsByPodcastId("7d7281ba3edb77da6a9fe95c");

        if (deletedCount > 0)
            System.out.println("[+] deleteReviewsByPodcastId - Deleted " + deletedCount);
        else
            System.err.println("[-] deleteReviewsByPodcastId");
    }

    public void deleteReviewsByAuthorUsernameTest() {
        // Review newReview = addReviewForTest();
        int deletedCount = rm.deleteReviewsByAuthorUsername("smallmeercat795128");

        if (deletedCount > 0)
            System.out.println("[+] deleteReviewsByAuthorUsername - Deleted " + deletedCount);
        else
            System.err.println("[-] deleteReviewsByAuthorUsername");
    }

    public void updateReviewTest() {
        // Review newReview = addReviewForTest();
        Date dateReview = new Date();
        Review newReview = new Review("000000000000000000000003", "b09ab608dd57d787d28980e9", "organicmouse599943", "titleReview", "We buonasera", 5, dateReview);

        if (rm.updateReview(newReview))
            System.out.println("[+] updateReview");
        else
            System.err.println("[-] updateReview");
    }

    public static void main(String[] args) {
        System.out.println("Test Review");

        MongoManager mongoManager = MongoManager.getInstance();
        mongoManager.openConnection();
        ReviewMongoTest test = new ReviewMongoTest();

        test.deleteReviewByIdTest();
        test.deleteReviewsByPodcastIdTest();
        test.deleteReviewsByAuthorUsernameTest();
        test.updateReviewTest();

        mongoManager.closeConnection();
    }
}
