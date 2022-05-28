package it.unipi.dii.lsmsdb.myPodcastDB.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Author {
    private String id;
    private String name;
    private String password;
    private String email;
    private List<ReducedPodcast> ownPodcasts;
    private String picturePath;


    public Author() {
        this.ownPodcasts = new ArrayList<>();
    }

    public Author(String id, String name, String picturePath){
        this.id = id;
        this.name = name;
        this.picturePath = picturePath;
    }

    public Author(String id, String name, String password, String email, String picturePath) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.ownPodcasts = new ArrayList<>();
        this.picturePath = picturePath;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Podcast> getOwnPodcasts() {
        List<Podcast> podcasts = new ArrayList<>();

        for (ReducedPodcast rd: this.ownPodcasts) {
            podcasts.add(rd.getAsPodcast());
        }

        return podcasts;
    }

    public void setOwnPodcasts(List<Podcast> ownPodcasts) {
        if (this.ownPodcasts != null && !this.ownPodcasts.isEmpty())
            this.ownPodcasts.clear();

        for (Podcast podcast: ownPodcasts) {
            ReducedPodcast newRD = new ReducedPodcast(podcast);
            this.ownPodcasts.add(newRD);
        }
    }

    public void addPodcast(Podcast podcast) {
        ReducedPodcast reducedPodcast = new ReducedPodcast(podcast);
        this.ownPodcasts.add(reducedPodcast);
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", ownPodcasts=" + ownPodcasts +
                ", picturePath=" + picturePath +
                '}';
    }
}

class ReducedPodcast {
    private String id;
    private String name;
    private Date releaseDate;
    private String category;
    private String artworkUrl600;

    public ReducedPodcast() {
    }

    public ReducedPodcast(Podcast podcast) {
        this.id = podcast.getId();
        this.name = podcast.getName();
        this.releaseDate = podcast.getReleaseDate();
        this.category = podcast.getPrimaryCategory();
        this.artworkUrl600 = podcast.getArtworkUrl600();
    }

    public ReducedPodcast(String id, String name, Date releaseDate, String category, String artworkUrl600) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.category = category;
        this.artworkUrl600 = artworkUrl600;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArtworkUrl600() {
        return artworkUrl600;
    }

    public void setArtworkUrl600(String artworkUrl600) {
        this.artworkUrl600 = artworkUrl600;
    }

    public void setName(String name) {
        this.name = name;
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

    public Podcast getAsPodcast() {
        Podcast podcast = new Podcast(this.id, this.name, this.releaseDate, this.artworkUrl600, this.category);
        return podcast;
    }

    @Override
    public String toString() {
        return "ReducedPodcast{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", category='" + category + '\'' +
                ", artworkUrl600='" + artworkUrl600 + '\'' +
                '}';
    }
}
