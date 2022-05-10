package it.unipi.dii.lsmsdb.myPodcastDB.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

public class Podcast {
    private String id;
    private String name;
    private ReducedAuthor author;
    private String artworkUrl60;
    private String artworkUrl600;
    private String contentAdvisoryRating;
    private String country;
    private String primaryCategory;
    private List<String> categories;
    private Date releaseDate;
    private List<Episode> episodes;
    private List<ReducedReview> reviews;

    public Podcast() {
    }

    public Podcast(String id, String name, String authorId, String authorName, String artworkUrl60, String artworkUrl600, String contentAdvisoryRating, String country, String primaryCategory, List<String> categories, Date releaseDate) {
        this.id = id;
        this.name = name;
        this.author = new ReducedAuthor(authorId, authorName);
        this.artworkUrl60 = artworkUrl60;
        this.artworkUrl600 = artworkUrl600;
        this.contentAdvisoryRating = contentAdvisoryRating;
        this.country = country;
        this.primaryCategory = primaryCategory;
        this.categories = categories;
        this.releaseDate = releaseDate;
        this.episodes = new ArrayList<>();
        this.reviews = new ArrayList<>();
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

    public String getAuthorId() { return author.getId(); }

    public String getAuthorName() { return author.getName(); }

    public void setAuthor(String authorId, String authorName) {
        ReducedAuthor newAuthor = new ReducedAuthor(authorId, authorName);
        this.author = newAuthor;
    }

    public String getArtworkUrl60() {
        return artworkUrl60;
    }

    public void setArtworkUrl60(String artworkUrl60) {
        this.artworkUrl60 = artworkUrl60;
    }

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

    public String getReleaseDateAsString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(this.releaseDate).replace(" ", "T") + "Z";
        return date;
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

    public List<Entry<String, Integer>> getReviews() {

        List<Entry<String, Integer>> reducedReviews = new ArrayList<>();
        for(ReducedReview review : reviews){
            Entry<String, Integer> newReview = new AbstractMap.SimpleEntry<>(review.getId(), review.getRating()) ;
            reducedReviews.add(newReview);
        }
        return reducedReviews;
    }

    public void setReviews(List<ReducedReview> reviews) {
        this.reviews = reviews;
    }

    public void addEpisode(String name, String description, Date releaseDate, int timeMillis) {
        Episode episode = new Episode(name, description, releaseDate, timeMillis);
        this.episodes.add(episode);
    }

    public void addReview(String id, int rating) {
        ReducedReview review = new ReducedReview(id, rating);
        this.reviews.add(review);
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", author=" + author +
                ", artworkUrl60='" + artworkUrl60 + '\'' +
                ", artworkUrl600='" + artworkUrl600 + '\'' +
                ", contentAdvisoryRating='" + contentAdvisoryRating + '\'' +
                ", country='" + country + '\'' +
                ", primaryCategory='" + primaryCategory + '\'' +
                ", categories=" + categories +
                ", releaseDate=" + releaseDate +
                ", episodes=" + episodes +
                ", reviews=" + reviews +
                '}';
    }
}

class ReducedAuthor {
    private String id;
    private String name;

    public ReducedAuthor() {
    }

    public ReducedAuthor(String id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "ReducedAuthor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
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
