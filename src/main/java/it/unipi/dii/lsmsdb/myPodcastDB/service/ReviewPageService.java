package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.ReviewMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;

import java.util.*;

public class ReviewPageService {

    private PodcastMongo podcastMongo;
    private ReviewMongo reviewMongo;

    public ReviewPageService() {
        this.podcastMongo = new PodcastMongo();
        this.reviewMongo  = new ReviewMongo();
    }

    public Boolean loadReviewPageForUser(Podcast podcast, String username, Review ownReview) {
        MongoManager.getInstance().openConnection();
        Boolean result = true;

        // load own review if exists
        Review own = this.reviewMongo.findSpecificReviewByAuthorName(podcast.getId(), username);
        if (own != null) {
            ownReview.copy(own);
        } else {
            ownReview.setTitle(null);
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public Boolean loadReviewPageForNotUser(Podcast podcast, List<Review> reviews, int limit, String attributeToOrder, Boolean ascending) {
        MongoManager.getInstance().openConnection();
        Boolean result = true;

        // search the podcast
        Podcast foundPodcast = this.podcastMongo.findPodcastById(podcast.getId());
        if (foundPodcast == null) {
            Logger.error("Podcast requested not found");
            result = false;
        } else {
            podcast.copy(foundPodcast);
        }

        // load the podcast's reviews
        Logger.info(this.reviewMongo.findReviewsByPodcastId(podcast.getId(), 0, limit, attributeToOrder, ascending).toString());
        reviews.addAll(this.reviewMongo.findReviewsByPodcastId(podcast.getId(), 0, limit, attributeToOrder, ascending));

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public boolean loadOtherReview(Podcast podcast, Review own, List<Review> reviews, int skip, int limit, String attributeToOrder, Boolean ascending) {
        MongoManager.getInstance().openConnection();
        Boolean result = true;

        // get the list of Review ID
        List<Map.Entry<String, Integer>> listOfReviews = podcast.getReviews();

        // sort them
        if (attributeToOrder.equals("rating")) {
            if (ascending) {
                listOfReviews.sort(new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        return o1.getValue().compareTo(o2.getValue());
                    }
                });
            } else {
                listOfReviews.sort(new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
            }
        } else if (attributeToOrder.equals("createdAt") && !ascending) {
            Collections.reverse(listOfReviews);
        }

        // get the reviews from collection
        List<Review> requestedReviews = new ArrayList<>();
        int k = 0;
        for (int i = skip; i < skip + limit && i < listOfReviews.size(); i++) {
            k++;
            Review requestedReview = this.reviewMongo.findReviewById(listOfReviews.get(i).getKey());
            if (requestedReview != null) {
                requestedReviews.add(requestedReview);
            } else {
                result = false;
                break;
            }
        }

        // check the result
        if (result && !requestedReviews.isEmpty())
            reviews.addAll(requestedReviews);

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int addNewReview(Review review) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // add new review to MongoDB
        boolean addRes = this.reviewMongo.addReview(review);

        // check the result
        if (!addRes) {
            Logger.error("Failed to add review to MongoDB");
            result = -1;
        } else {
            // create a copy of podcast and update it
            Podcast podcast = new Podcast();
            podcast.copy((Podcast)StageManager.getObjectIdentifier());

            // add to reduced reviews and to preloaded reviews
            podcast.addReview(review.getId(), review.getRating());
            podcast.addPreloadedReview(review);

            // update the podcast on mongo
            boolean addEmb = this.podcastMongo.updateReviewsOfPodcast(podcast);

            // check the result
            if (!addEmb) {
                Logger.error("Failed to update review in podcast");
                result = -2;

                // rollback
                this.reviewMongo.deleteReviewById(review.getId());
            } else {
                StageManager.setObjectIdentifier(podcast);
                Logger.success("Review successfully added");
            }
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int deleteReview(Review review, boolean isPreloaded) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // remove from MongoDB
        boolean resRem = this.reviewMongo.deleteReviewById(review.getId());

        // check the result
        if (!resRem) {
            Logger.error("Failed to remove review from MongoDB");
            result = -1;
        } else {
            // create a copy of podcast and update it
            Podcast podcast = new Podcast();
            podcast.copy((Podcast)StageManager.getObjectIdentifier());

            // remove from reduced reviews and from preloaded review
            podcast.deleteReview(review);
            podcast.removePreloadedReview(review);

            // if the podcast has more than 9 other reviews load another one as preloaded
            int totalReviews = podcast.getReviews().size();
            if (totalReviews > 9) {
                Review otherReview = this.reviewMongo.findReviewById(podcast.getReviews().get(totalReviews - 10).getKey());
                if (otherReview != null)
                    podcast.addInHeadPreloadedReview(review);
                else
                    result = -2;
            }

            // update the podcast
            boolean resPre = false;
            if (result != -2)
                resPre = this.podcastMongo.updateReviewsOfPodcast(podcast);

            // check te result
            if (result == -2) {
                Logger.error("Failed to find review to update podcast");
            } else if (!resPre) {
                Logger.error("Failed to update review in podcast");
                result = -3;
            } else {
                StageManager.setObjectIdentifier(podcast);
                Logger.success("Review successfully deleted");
            }
        }

        // check if is required to rollback the operation
        if (result == -2 || result == -3)
            this.reviewMongo.addReview(review);

        MongoManager.getInstance().closeConnection();
        return result;
    }
}
