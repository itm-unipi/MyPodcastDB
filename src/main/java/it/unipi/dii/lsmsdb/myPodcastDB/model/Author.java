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
    private List<Podcast> ownPodcasts;
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

    public void copy(Author author) {
        this.id = author.id;
        this.name = author.name;
        this.password = author.password;
        this.email = author.email;
        this.ownPodcasts = new ArrayList<>(author.ownPodcasts);
        this.picturePath = author.picturePath;
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

    public List<Podcast> getOwnPodcasts() { return this.ownPodcasts; }

    public void setOwnPodcasts(List<Podcast> ownPodcasts) { this.ownPodcasts = ownPodcasts; }

    public void addPodcast(Podcast podcast) { this.ownPodcasts.add(podcast); }

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
