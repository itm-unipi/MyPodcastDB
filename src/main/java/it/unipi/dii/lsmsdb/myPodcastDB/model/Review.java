package it.unipi.dii.lsmsdb.myPodcastDB.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Review {
    private String id;
    private String podcastId;
    private String authorUsername;
    private String title;
    private String content;
    private int rating;
    private Date createdAt;

    public Review() {
    }

    public Review(String id, int rating) {
        this.id = id;
        this.rating = rating;
    }

    public Review(String id, String podcastId) {
        this.id = id;
        this.podcastId = podcastId;
    }

    public Review(String id, String podcastId, String authorUsername, String title, String content, int rating, Date createdAt) {
        this.id = id;
        this.podcastId = podcastId;
        this.authorUsername = authorUsername;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public void copy(Review review) {
        this.id = review.id;
        this.podcastId = review.podcastId;
        this.authorUsername = review.authorUsername;
        this.title = review.title;
        this.content = review.content;
        this.rating = review.rating;
        this.createdAt = review.createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(String podcastId) {
        this.podcastId = podcastId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", podcastId='" + podcastId + '\'' +
                ", authorUsername='" + authorUsername + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return rating == review.rating && Objects.equals(id, review.id) && Objects.equals(podcastId, review.podcastId) && Objects.equals(authorUsername, review.authorUsername) && Objects.equals(title, review.title) && Objects.equals(content, review.content) && Objects.equals(createdAt, review.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, podcastId, authorUsername, title, content, rating, createdAt);
    }
}
