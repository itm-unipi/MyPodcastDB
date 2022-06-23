package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Review;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.PodcastMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.ReviewMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.UserMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;

import java.util.*;

public class ReviewPageService {

    private PodcastMongo podcastMongo;
    private ReviewMongo reviewMongo;
    private UserMongo userMongo;

    public ReviewPageService() {
        this.podcastMongo = new PodcastMongo();
        this.reviewMongo  = new ReviewMongo();
        this.userMongo = new UserMongo();
    }

    public Boolean loadReviewOfUser(Review ownReview) {
        Logger.info("Load Review of User Service");

        MongoManager.getInstance().openConnection();
        Boolean result = true;

        // load own review if exists
        Review own = this.reviewMongo.findReviewById(ownReview.getId());
        if (own != null) {
            ownReview.copy(own);
            Logger.error("Successfully Loaded Review of User");
        } else {
            result = false;
            Logger.error("Failed to Load Review of User");
        }

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public boolean loadOtherReview(Podcast podcast, Review own, List<Review> reviews, int skip, int limit, String attributeToOrder, Boolean ascending) {
        Logger.info("Load Reviews service");

        MongoManager.getInstance().openConnection();
        Boolean result = true;

        // if the skip isn't a multiple of 10 reduce of 1 the skip otherwise one review isn't loaded
        if (skip % 10 == 1) {
            skip--;
            limit--;            // the next loadOtherReview will have the correct value
        }

        // get the list of Review ID
        List<Review> listOfReviews = podcast.getReviews();

        // sort them
        if (attributeToOrder.equals("rating")) {
            if (ascending) {
                listOfReviews.sort(new Comparator<Review>() {
                    @Override
                    public int compare(Review o1, Review o2) {
                        Integer r1 = o1.getRating();
                        Integer r2 = o2.getRating();

                        if (r1 < r2)
                            return -1;
                        else if (r1 == r2)
                            return 0;
                        else
                            return 1;
                    }
                });
            } else {
                listOfReviews.sort(new Comparator<Review>() {
                    @Override
                    public int compare(Review o1, Review o2) {
                        Integer r1 = o1.getRating();
                        Integer r2 = o2.getRating();

                        if (r2 < r1)
                            return -1;
                        else if (r1 == r2)
                            return 0;
                        else
                            return 1;
                    }
                });
            }
        } else if (attributeToOrder.equals("createdAt") && !ascending) {
            Collections.reverse(listOfReviews);
        }

        // get the reviews from collection
        List<Review> requestedReviews = new ArrayList<>();
        for (int i = skip; i < skip + limit && i < listOfReviews.size(); i++) {
            // get the id of next review
            String id = listOfReviews.get(i).getId();

            // if is user and this is the id of review he written don't ask again it to MongoDB
            if (MyPodcastDB.getInstance().getSessionType().equals("User") && id.equals(own.getId())) {
                limit++;                                                            // ask always for 10 reviews
                continue;
            }

            Review requestedReview = this.reviewMongo.findReviewById(id);
            if (requestedReview != null) {
                requestedReviews.add(requestedReview);
            } else {
                result = false;
                break;
            }
        }

        // check the result
        if (result && !requestedReviews.isEmpty()) {
            // update the local list
            reviews.addAll(requestedReviews);
        }

        if (result)
            Logger.success("Successfully Loaded the reviews");
        else
            Logger.error("Failed to Load Reviews");

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int addNewReview(Review review) {
        Logger.info("Add Review Service");

        MongoManager.getInstance().openConnection();
        int result = 0;

        // add new review to MongoDB
        boolean addRes = this.reviewMongo.addReview(review);

        // check the result
        if (!addRes) {
            Logger.error("Failed to add review to MongoDB");
            result = -1;
        } else {
            // create the updated user and update it
            User user = new User();
            user.copy((User)MyPodcastDB.getInstance().getSessionActor());

            // add to embedded review and update it
            user.addReview(review);
            boolean addRev = this.userMongo.updateReviewsOfUser(user);

            // check the result
            if (!addRev) {
                Logger.error("Failed to update review in user");
                result = -2;
            } else {
                // create a copy of podcast and update it
                Podcast podcast = new Podcast();
                podcast.copy((Podcast) StageManager.getObjectIdentifier());

                // add to reduced reviews and to preloaded reviews
                podcast.addReview(review.getId(), review.getRating());
                podcast.addPreloadedReview(review);

                // update the podcast on mongo
                boolean addEmb = this.podcastMongo.updateReviewsOfPodcast(podcast);

                // check the result
                if (!addEmb) {
                    Logger.error("Failed to update review in podcast");
                    result = -3;
                } else {
                    Logger.success("Review successfully added");

                    // update session actor and object identifier
                    ((User)MyPodcastDB.getInstance().getSessionActor()).copy(user);
                    StageManager.setObjectIdentifier(podcast);
                }
            }
        }

        // rollback if process failed
        if (result != 0)
            rollbackAddNewReview(result, review);

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int deleteReviewAsUser(Review review) {
        Logger.info("Delete Review as User Service");

        MongoManager.getInstance().openConnection();
        int result = 0;

        // remove from MongoDB
        boolean resRem = this.reviewMongo.deleteReviewById(review.getId());

        // check the result
        if (!resRem) {
            Logger.error("Failed to remove review from MongoDB");
            result = -1;
        } else {
            // create the updated user and update it
            User user = new User();
            user.copy((User)MyPodcastDB.getInstance().getSessionActor());

            // remove from embedded review and update it
            user.removeReview(review);
            boolean resEmb = this.userMongo.updateReviewsOfUser(user);

            if (!resEmb) {
                Logger.error("Failed to remove review embedded in user");
                result = -2;
            } else {
                // create a copy of podcast and update it
                Podcast podcast = new Podcast();
                podcast.copy((Podcast) StageManager.getObjectIdentifier());

                // remove from reduced reviews and from preloaded review
                podcast.deleteReview(review);
                podcast.deletePreloadedReview(review);

                // if the podcast has more than 9 other reviews load another one as preloaded
                int totalReviews = podcast.getReviews().size();
                if (totalReviews > 9) {
                    Review otherReview = this.reviewMongo.findReviewById(podcast.getReviews().get(totalReviews - 10).getId());
                    if (otherReview != null)
                        podcast.addInHeadPreloadedReview(otherReview);
                    else
                        result = -3;
                }

                // update the podcast
                boolean resPre = false;
                if (result != -3)
                    resPre = this.podcastMongo.updateReviewsOfPodcast(podcast);

                // check te result
                if (result == -3) {
                    Logger.error("Failed to find review to update podcast");
                } else if (!resPre) {
                    Logger.error("Failed to update review in podcast");
                    result = -4;
                } else {
                    Logger.success("Review successfully deleted");

                    // update session actor and object identifier
                    ((User)MyPodcastDB.getInstance().getSessionActor()).copy(user);
                    Logger.info("" + (User)MyPodcastDB.getInstance().getSessionActor());
                    StageManager.setObjectIdentifier(podcast);
                }
            }
        }

        // rollback if process failed
        if (result != 0)
            rollbackDeleteReview(result, review, null);

        MongoManager.getInstance().closeConnection();
        return result;
    }

    public int deleteReviewAsAdmin(Review review) {
        Logger.info("Delete Review as Admin Service");

        MongoManager.getInstance().openConnection();
        int result = 0;
        User user = null;                                   // user for an eventual rollback

        // remove from MongoDB
        boolean resRem = this.reviewMongo.deleteReviewById(review.getId());

        // check the result
        if (!resRem) {
            Logger.error("Failed to remove review from MongoDB");
            result = -1;
        } else {
            // get the user to update if isn't removed account
            boolean resEmb = true;
            if (!review.getAuthorUsername().equals("Removed account")) {
                user = this.userMongo.findUserByUsername(review.getAuthorUsername());

                // remove from embedded review and update it
                if (user != null) {
                    user.removeReview(review);
                    resEmb = this.userMongo.updateReviewsOfUser(user);
                } else {
                    resEmb = false;
                }
            }

            // check the result
            if (!resEmb) {
                Logger.error("Failed to remove review embedded in user");
                result = -2;
            } else {
                // create a copy of podcast and update it
                Podcast podcast = new Podcast();
                podcast.copy((Podcast) StageManager.getObjectIdentifier());

                // remove from reduced reviews and from preloaded review
                podcast.deleteReview(review);
                podcast.deletePreloadedReview(review);

                // if the podcast has more than 9 other reviews load another one as preloaded
                int totalReviews = podcast.getReviews().size();
                if (totalReviews > 9) {
                    Review otherReview = this.reviewMongo.findReviewById(podcast.getReviews().get(totalReviews - 10).getId());
                    if (otherReview != null)
                        podcast.addInHeadPreloadedReview(otherReview);
                    else
                        result = -3;
                }

                // update the podcast
                boolean resPre = false;
                if (result != -3)
                    resPre = this.podcastMongo.updateReviewsOfPodcast(podcast);

                // check te result
                if (result == -3) {
                    Logger.error("Failed to find review to update podcast");
                } else if (!resPre) {
                    Logger.error("Failed to update review in podcast");
                    result = -4;
                } else {
                    Logger.success("Review successfully deleted");

                    // update object identifier
                    StageManager.setObjectIdentifier(podcast);
                }
            }
        }

        // rollback if process failed
        if (result != 0)
            rollbackDeleteReview(result, review, user);

        MongoManager.getInstance().closeConnection();
        return result;
    }

    private void rollbackAddNewReview(int result, Review review) {
        // failed to update review in podcast
        if (result == -3) {
            // rollback the user
            User user = (User)MyPodcastDB.getInstance().getSessionActor();
            this.userMongo.updateReviewsOfUser(user);
        }

        // failed to update review in user
        if (result <= -2) {
            // delete the review
            this.reviewMongo.deleteReviewById(review.getId());
        }
    }

    private void rollbackDeleteReview(int result, Review review, User user) {
        // failed to update the podcast
        if (result == -3) {
            // rollback the user
            if (MyPodcastDB.getInstance().getSessionType().equals("User")) {
                // if user rollback to the user saved in session
                User oldUser = (User) MyPodcastDB.getInstance().getSessionActor();
                this.userMongo.updateReviewsOfUser(oldUser);
            } else {
                // if admin add the review and update
                user.addReview(review);
                this.userMongo.updateReviewsOfUser(user);
            }
        }

        // failed to remove review embedded in user
        if (result <= -2) {
            // recreate the review
            this.reviewMongo.addReview(review);
        }
    }
}
