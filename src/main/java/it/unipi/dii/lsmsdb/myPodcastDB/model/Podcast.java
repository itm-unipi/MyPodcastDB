package it.unipi.dii.lsmsdb.myPodcastDB.model;

import java.util.*;
import java.util.Map.Entry;

public class Podcast {
    private String id;
    private String name;
    private String authorName;
    private String artworkUrl600;
    private String contentAdvisoryRating;
    private String country;
    private String primaryCategory;
    private List<String> categories;
    private Date releaseDate;
    private List<Episode> episodes;
    private List<ReducedReview> reviews;
    private List<Review> preloadedReviews;

    static final private int numberOfPreloadedReviews = 10;     // TODO: metterlo nel config manager

    public Podcast() {
    }

    public Podcast(String podcastId, String podcastName, String artworkUrl600) {
        this.id = podcastId;
        this.name= podcastName;
        this.artworkUrl600 = artworkUrl600;
    }

    public Podcast(String podcastId, String podcastName, Date releaseDate, String artworkUrl600, String category) {
        this.id = podcastId;
        this.name= podcastName;
        this.releaseDate = releaseDate;
        this.artworkUrl600 = artworkUrl600;
        this.primaryCategory = category;
    }

    public Podcast(String id, String name, String authorName, String artworkUrl600, String contentAdvisoryRating, String country, String primaryCategory, List<String> categories, Date releaseDate) {
        this.id = id;
        this.name = name;
        this.authorName = authorName;
        this.artworkUrl600 = artworkUrl600;
        this.contentAdvisoryRating = contentAdvisoryRating;
        this.country = country;
        this.primaryCategory = primaryCategory;
        this.categories = categories;
        this.releaseDate = releaseDate;
        this.episodes = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.preloadedReviews = new ArrayList<>();
    }

    public Podcast(Podcast podcast) {
        this.id = podcast.id;
        this.name = podcast.name;
        this.authorName = podcast.authorName;
        this.artworkUrl600 = podcast.artworkUrl600;
        this.contentAdvisoryRating = podcast.contentAdvisoryRating;
        this.country = podcast.country;
        this.primaryCategory = podcast.primaryCategory;
        this.categories = podcast.categories;
        this.releaseDate = podcast.releaseDate;
        this.episodes = new ArrayList<>(podcast.episodes);
        this.reviews = new ArrayList<>(podcast.reviews);
        this.preloadedReviews = new ArrayList<>(podcast.preloadedReviews);
    }

    public void copy(Podcast podcast) {
        this.id = podcast.id;
        this.name = podcast.name;
        this.authorName = podcast.authorName;
        this.artworkUrl600 = podcast.artworkUrl600;
        this.contentAdvisoryRating = podcast.contentAdvisoryRating;
        this.country = podcast.country;
        this.primaryCategory = podcast.primaryCategory;
        this.categories = podcast.categories;
        this.releaseDate = podcast.releaseDate;
        this.episodes = new ArrayList<>(podcast.episodes);
        this.reviews = new ArrayList<>(podcast.reviews);
        this.preloadedReviews = new ArrayList<>(podcast.preloadedReviews);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() { return this.authorName; }

    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getArtworkUrl600() {
        return artworkUrl600;
    }

    public void setArtworkUrl600(String artworkUrl600) {
        this.artworkUrl600 = artworkUrl600;
    }

    public String getContentAdvisoryRating() {
        return contentAdvisoryRating;
    }

    public void setContentAdvisoryRating(String contentAdvisoryRating) {
        this.contentAdvisoryRating = contentAdvisoryRating;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    protected List<ReducedReview> getReducedReviews() {
        return reviews;
    }

    public List<Entry<String, Integer>> getReviews() {

        List<Entry<String, Integer>> reducedReviews = new ArrayList<>();
        for(ReducedReview review : reviews){
            Entry<String, Integer> newReview = new AbstractMap.SimpleEntry<>(review.getId(), review.getRating()) ;
            reducedReviews.add(newReview);
        }
        return reducedReviews;
    }

    public void setReviews(List<Entry<String, Integer>> newReviews) {

        List<ReducedReview> reviews = new ArrayList<>();
        for(Entry<String, Integer> entry : newReviews){
            ReducedReview newReview = new ReducedReview(entry.getKey(), entry.getValue());
            reviews.add(newReview);
        }
        this.reviews = reviews;
    }

    public void addEpisode(String name, String description, Date releaseDate, int timeMillis) {
        Episode episode = new Episode(name, description, releaseDate, timeMillis);
        this.episodes.add(episode);
    }

    public void addEpisode(Episode newEpisode) {
        this.episodes.add(newEpisode);
    }

    public void deleteEpisode(Episode episode) {
        this.episodes.remove(episode);
    }

    public void addReview(String id, int rating) {
        ReducedReview review = new ReducedReview(id, rating);
        this.reviews.add(review);
    }

    public void deleteReview(Review review) {
        ReducedReview toRemove = new ReducedReview(review.getId(), review.getRating());
        this.reviews.remove(toRemove);
    }

    public float getRating() {
        float average = 0.0f;

        for (ReducedReview review : this.reviews)
            average += review.getRating();

        return average / this.reviews.size();
    }

    public List<Review> getPreloadedReviews() {
        return preloadedReviews;
    }

    public void setPreloadedReviews(List<Review> preloadedReviews) {
        this.preloadedReviews = preloadedReviews;
    }

    public void addPreloadedReview(Review review) {
        this.preloadedReviews.add(review);

        // if there are more than numberOfPreloadedReviews, remove the oldest created
        if (this.preloadedReviews.size() > numberOfPreloadedReviews)
            this.preloadedReviews.remove(0);
    }

    public void addInHeadPreloadedReview(Review review) {
        this.preloadedReviews.add(0, review);
    }

    public void deletePreloadedReview(Review review) {
        this.preloadedReviews.remove(review);
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", authorName='" + authorName + '\'' +
                ", artworkUrl600='" + artworkUrl600 + '\'' +
                ", contentAdvisoryRating='" + contentAdvisoryRating + '\'' +
                ", country='" + country + '\'' +
                ", primaryCategory='" + primaryCategory + '\'' +
                ", categories=" + categories +
                ", releaseDate=" + releaseDate +
                ", episodes=" + episodes +
                ", reviews=" + reviews +
                ", preloadedReviews=" + preloadedReviews +
                '}';
    }
}

class ReducedReview {
    private String id;
    private int rating;

    public ReducedReview() {
    }

    public ReducedReview(String id, int rating) {
        this.id = id;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ReducedReview{" +
                "id='" + id + '\'' +
                ", rating=" + rating +
                '}';
    }
}
