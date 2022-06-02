package it.unipi.dii.lsmsdb.myPodcastDB.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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

    public Podcast(Podcast podcast) {
        this.id = podcast.id;
        this.name = podcast.name;
        this.author = new ReducedAuthor(podcast.author);
        this.artworkUrl60 = podcast.artworkUrl60;
        this.artworkUrl600 = podcast.artworkUrl600;
        this.contentAdvisoryRating = podcast.contentAdvisoryRating;
        this.country = podcast.country;
        this.primaryCategory = podcast.primaryCategory;
        this.categories = podcast.categories;
        this.releaseDate = podcast.releaseDate;
        this.episodes = new ArrayList<>(podcast.episodes);
        this.reviews = new ArrayList<>(podcast.reviews);
    }

    public void copy(Podcast podcast) {
        this.id = podcast.id;
        this.name = podcast.name;
        this.author = new ReducedAuthor(podcast.author);
        this.artworkUrl60 = podcast.artworkUrl60;
        this.artworkUrl600 = podcast.artworkUrl600;
        this.contentAdvisoryRating = podcast.contentAdvisoryRating;
        this.country = podcast.country;
        this.primaryCategory = podcast.primaryCategory;
        this.categories = podcast.categories;
        this.releaseDate = podcast.releaseDate;
        this.episodes = new ArrayList<>(podcast.episodes);
        this.reviews = new ArrayList<>(podcast.reviews);
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

    protected ReducedAuthor getAuthor() {
        return author;
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

    public void addReview(String id, int rating) {
        ReducedReview review = new ReducedReview(id, rating);
        this.reviews.add(review);
    }

    public float getRating() {
        float average = 0.0f;

        for (ReducedReview review : this.reviews)
            average += review.getRating();

        return average / this.reviews.size();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Podcast)) return false;
        Podcast podcast = (Podcast) o;
        return Objects.equals(getName(), podcast.getName()) && Objects.equals(getAuthor(), podcast.getAuthor()) && Objects.equals(getArtworkUrl60(), podcast.getArtworkUrl60()) && Objects.equals(getArtworkUrl600(), podcast.getArtworkUrl600()) && Objects.equals(getContentAdvisoryRating(), podcast.getContentAdvisoryRating()) && Objects.equals(getCountry(), podcast.getCountry()) && Objects.equals(getPrimaryCategory(), podcast.getPrimaryCategory()) && Objects.equals(getCategories(), podcast.getCategories()) && Objects.equals(getReleaseDate(), podcast.getReleaseDate()) && Objects.equals(getEpisodes(), podcast.getEpisodes()) && Objects.equals(getReviews(), podcast.getReviews());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAuthor(), getArtworkUrl60(), getArtworkUrl600(), getContentAdvisoryRating(), getCountry(), getPrimaryCategory(), getCategories(), getReleaseDate(), getEpisodes(), getReviews());
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

    public ReducedAuthor(ReducedAuthor author) {
        this.id = author.id;
        this.name = author.name;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReducedAuthor)) return false;
        ReducedAuthor that = (ReducedAuthor) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReducedReview)) return false;
        ReducedReview that = (ReducedReview) o;
        return getRating() == that.getRating() && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRating());
    }
}
