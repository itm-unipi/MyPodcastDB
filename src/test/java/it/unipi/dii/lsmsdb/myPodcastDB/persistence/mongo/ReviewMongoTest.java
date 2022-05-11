package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;

import java.util.Date;
import java.util.List;

public class ReviewMongoTest {

    // TODO: aggiungere test add e rendere i test consistenti (add - test - delete)
    ReviewMongo reviewMongo;

    public ReviewMongoTest() {
        this.reviewMongo = new ReviewMongo();
    }

    public void findByIdTest() {
        Review review = this.reviewMongo.findReviewById("000000000000000000000000");
        if (review.getPodcastId().equals("7d7281ba3edb77da6a9fe95c") && review.getTitle().equals("Best podcast like, Ever."))
            System.out.println("[+] findReviewById");
        else
            System.err.println("[-] findReviewById");
    }

    public void findTest() {
        List<Review> reviews = this.reviewMongo.findReviewsByAuthorUsername("angrybear104838", 2, "", true);
        if (reviews.size() != 2)
            System.err.println("[-] findReviewsByAuthorUsername: limit doesn't work");
        boolean test = true;
        for (Review review : reviews) {
            if (!review.getAuthorUsername().equals("angrybear104838")) {
                System.err.println("[-] findReviewsByAuthorUsername");
                test = false;
            }
        }
        if (test)
            System.out.println("[+] findReviewsByAuthorUsername");

        reviews = this.reviewMongo.findReviewsByPodcastId("0005852e3f81889c1ff0f26c", 4, "", true);
        if (reviews.size() != 4)
            System.err.println("[-] findReviewsByPodcastId: limit doesn't work");
        test = true;
        for (Review review : reviews) {
            if (!review.getPodcastId().equals("0005852e3f81889c1ff0f26c")) {
                System.err.println("[-] findReviewsByPodcastId");
                test = false;
            }
        }
        if (test)
            System.out.println("[+] findReviewsByPodcastId");
    }

    public void deleteReviewByIdTest() {
        // Review newReview = addReviewForTest();

        if(reviewMongo.deleteReviewById("000000000000000000000002"))
            System.out.println("[+] deleteReviewById");
        else
            System.err.println("[-] deleteReviewById");
    }

    public void deleteReviewsByPodcastIdTest() {
        // Review newReview = addReviewForTest();
        int deletedCount = reviewMongo.deleteReviewsByPodcastId("7d7281ba3edb77da6a9fe95c");

        if (deletedCount > 0)
            System.out.println("[+] deleteReviewsByPodcastId - Deleted " + deletedCount);
        else
            System.err.println("[-] deleteReviewsByPodcastId");
    }

    public void deleteReviewsByAuthorUsernameTest() {
        // Review newReview = addReviewForTest();
        int deletedCount = reviewMongo.deleteReviewsByAuthorUsername("smallmeercat795128");

        if (deletedCount > 0)
            System.out.println("[+] deleteReviewsByAuthorUsername - Deleted " + deletedCount);
        else
            System.err.println("[-] deleteReviewsByAuthorUsername");
    }

    public void updateReviewTest() {
        // Review newReview = addReviewForTest();
        Date dateReview = new Date();
        Review newReview = new Review("000000000000000000000003", "b09ab608dd57d787d28980e9", "organicmouse599943", "titleReview", "We buonasera", 5, dateReview);

        if (reviewMongo.updateReview(newReview))
            System.out.println("[+] updateReview");
        else
            System.err.println("[-] updateReview");
    }
    
    public static void main(String[] args) {
        MongoManager manager = MongoManager.getInstance();
        manager.openConnection();
        ReviewMongoTest test = new ReviewMongoTest();

        test.findByIdTest();
        test.findTest();
        test.deleteReviewByIdTest();
        test.deleteReviewsByPodcastIdTest();
        test.deleteReviewsByAuthorUsernameTest();
        test.updateReviewTest();

        manager.closeConnection();
    }
}
