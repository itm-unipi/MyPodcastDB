package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;

import java.util.List;

public class ReviewMongoTest {

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

    public static void main(String[] args) {
        MongoManager manager = MongoManager.getInstance();
        manager.openConnection();
        ReviewMongoTest test = new ReviewMongoTest();

        test.findByIdTest();
        test.findTest();

        manager.closeConnection();
    }
}
