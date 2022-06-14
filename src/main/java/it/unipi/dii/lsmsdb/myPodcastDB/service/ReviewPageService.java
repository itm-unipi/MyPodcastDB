package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.ReviewMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

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

        // check if the user has already written a review
        Review reviewCheck = this.reviewMongo.findSpecificReviewByAuthorName(review.getPodcastId(), review.getAuthorUsername());
        if (reviewCheck != null) {
            Logger.error("User has already written a review");
            result = -1;
        } else {
            // add new review to MongoDB
            Boolean addRes = this.reviewMongo.addReview(review);

            // check the result
            if (!addRes) {
                Logger.error("Failed to add review to MongoDB");
                result = -2;
            } else {
                // add the rating embedded in podcast
                Boolean addEmb = this.podcastMongo.addReviewToPodcast(review.getPodcastId(), review.getId(), review.getRating());

                // check the result
                if (!addEmb) {
                    Logger.error("Failed to add rating embedded to podcast");
                    result = -3;

                    // rollback
                    this.reviewMongo.deleteReviewById(review.getId());
                } else {
                    Logger.success("Review successfully added");
                }
            }
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int deleteReview(Review review) {
        MongoManager.getInstance().openConnection();
        int result = 0;

        // check if exists
        Review foundReview = this.reviewMongo.findReviewById(review.getId());
        if (foundReview == null) {
            Logger.error("Review not found");
            result = -1;
        } else {
            // remove from MongoDB
            boolean resRem = this.reviewMongo.deleteReviewById(review.getId());

            // check the result
            if (!resRem) {
                Logger.error("Failed to remove review from MongoDB");
                result = -2;
            } else {
                // remove from podcast
                Boolean resEmb = this.podcastMongo.deleteReviewOfPodcast(review.getPodcastId(), review.getId());

                // check the result
                if (!resEmb) {
                    Logger.error("Failed to remove review from podcast");
                    result = -3;

                    // rollback
                    this.reviewMongo.addReview(review);
                } else {
                    Logger.success("Review successfully removed");
                }
            }
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }
}
