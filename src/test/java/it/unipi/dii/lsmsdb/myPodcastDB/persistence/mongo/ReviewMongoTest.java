package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ConfigManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

import java.util.Date;
import java.util.List;

public class ReviewMongoTest {

    ReviewMongo reviewMongo;

    public ReviewMongoTest() {
        this.reviewMongo = new ReviewMongo();
    }

    static boolean compare(Review r1, Review r2) {
        if (!r1.getId().equals(r2.getId()))
            return false;
        if (!r1.getPodcastId().equals(r2.getPodcastId()))
            return false;
        if (!r1.getAuthorUsername().equals(r2.getAuthorUsername()))
            return false;
        if (!r1.getTitle().equals(r2.getTitle()))
            return false;
        if (!r1.getContent().equals(r2.getContent()))
            return false;
        if (r1.getRating() != r2.getRating())
            return false;
        if (!r1.getCreatedAtAsString().equals(r2.getCreatedAtAsString()))
            return false;
        return true;
    }

    public Review addReviewForTest() {
        Review newReview = new Review("100000000000000000000000", "000000000000000000000000", "testauthorusername", "TestTile", "TestContent", 5, new Date());
        reviewMongo.addReview(newReview);
        return newReview;
    }

    public void findByIdTest() {
        Review review = this.reviewMongo.findReviewById("000000000000000000000001");
        if (review.getPodcastId().equals("c9cd8c0d5a9f1752ee1982c7") && review.getTitle().equals("DJ RyB"))
            System.out.println("[+] findReviewById");
        else
            System.err.println("[-] findReviewById");
    }

    public void addTest() {
        String podcastId = "000000000000000000000000";
        String title = "Si";
        String content = "No";
        String authorUsername = "Mariorossi123456";
        int rating = 5;
        Date createdAt = new Date();

        Review newReview = new Review("", podcastId, authorUsername, title, content, rating, createdAt);
        this.reviewMongo.addReview(newReview);

        List<Review> reviews = this.reviewMongo.findReviewsByPodcastId(podcastId, 0, 0, "", true);
        boolean test = false;
        for (Review review : reviews) {
            if (compare(newReview, review)) {
                test = true;
                break;
            }
        }

        if (test)
            System.out.println("[+] addReview");
        else
            System.err.println("[-] addReview");
    }

    public void findSpecificTest() {
        Review review = this.reviewMongo.findSpecificReviewByAuthorName("000000000000000000000000", "Mariorossi123456");
        if (review != null)
            System.out.println("[+] findSpecificReviewByAuthorName");
        else
            System.err.println("[-] findSpecificReviewByAuthorName");
    }

    public void findByAuthorUsernameTest() {
        List<Review> reviews = this.reviewMongo.findReviewsByAuthorUsername("Mariorossi123456", 1, "", true);
        if (reviews.size() != 1)
            System.err.println("[-] findReviewsByAuthorUsername: limit doesn't work");
        boolean test = true;
        for (Review review : reviews) {
            if (!review.getAuthorUsername().equals("Mariorossi123456")) {
                System.err.println("[-] findReviewsByAuthorUsername");
                test = false;
            }
        }
        if (test)
            System.out.println("[+] findReviewsByAuthorUsername");
    }

    public void findByPodcastIdTest() {
        List<Review> reviews = this.reviewMongo.findReviewsByPodcastId("0005852e3f81889c1ff0f26c", 0, 4, "", true);
        if (reviews.size() != 4)
            System.err.println("[-] findReviewsByPodcastId: limit doesn't work");
        boolean test = true;
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
        Review newReview = addReviewForTest();
        String reviewId = newReview.getId();

        reviewMongo.deleteReviewById(newReview.getId());

        if(reviewMongo.findReviewById(reviewId) == null)
            System.out.println("[+] deleteReviewById");
        else
            System.err.println("[-] deleteReviewById");
    }

    public void deleteReviewsByPodcastIdTest() {
        Review newReview = addReviewForTest();
        int deletedCount = reviewMongo.deleteReviewsByPodcastId(newReview.getPodcastId());

        if (deletedCount > 0 && reviewMongo.findReviewById(newReview.getId()) == null)
            System.out.println("[+] deleteReviewsByPodcastId - Deleted " + deletedCount);
        else
            System.err.println("[-] deleteReviewsByPodcastId");
    }

    public void deleteReviewsByAuthorUsernameTest() {
        Review newReview = addReviewForTest();
        String authorUsername = newReview.getAuthorUsername();
        String reviewId = newReview.getId();

        int deletedCount = reviewMongo.deleteReviewsByAuthorUsername(authorUsername);

        if (deletedCount > 0 && reviewMongo.findReviewById(reviewId) == null)
            System.out.println("[+] deleteReviewsByAuthorUsername - Deleted " + deletedCount);
        else
            System.err.println("[-] deleteReviewsByAuthorUsername");
    }

    public void updateReviewTest() {
        Review newReview = addReviewForTest();
        String reviewId = newReview.getId();

        newReview.setContent("Updated Content");
        reviewMongo.updateReview(newReview);
        Review updatedReview = reviewMongo.findReviewById(reviewId);

        if (compare(newReview, updatedReview))
            System.out.println("[+] updateReview");
        else
            System.err.println("[-] updateReview");

        reviewMongo.deleteReviewById(reviewId);
    }

    public static void main(String[] args) {
        Logger.initialize();
        ConfigManager.importConfig("config.xml", "src/main/java/it/unipi/dii/lsmsdb/myPodcastDB/utility/schema.xsd");

        MongoManager manager = MongoManager.getInstance();
        manager.openConnection();
        ReviewMongoTest test = new ReviewMongoTest();

        test.addTest();
        test.findByIdTest();
        test.findSpecificTest();
        test.findByAuthorUsernameTest();
        test.findByPodcastIdTest();
        test.deleteReviewByIdTest();
        test.deleteReviewsByPodcastIdTest();
        test.deleteReviewsByAuthorUsernameTest();
        test.updateReviewTest();

        manager.closeConnection();
    }
}
