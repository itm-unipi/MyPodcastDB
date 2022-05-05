package it.unipi.dii.lsmsdb.myPodcastDB.model;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private String id;
    private String name;
    private String password;
    private String email;
    private List<ReducedPodcast> ownPodcasts;

    public Author() {
    }

    public Author(String id, String name, String password, String email, List<ReducedPodcast> ownPodcasts) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.ownPodcasts = ownPodcasts;
    }

    public Author(Author other) {
        this.id = other.getId();
        this.name = other.getName();
        this.password = other.getPassword();
        this.email = other.getEmail();
        this.ownPodcasts = new ArrayList<>(other.getOwnPodcasts());
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

    public List<ReducedPodcast> getOwnPodcasts() {
        return ownPodcasts;
    }

    public void setOwnPodcasts(List<ReducedPodcast> ownPodcasts) {
        this.ownPodcasts = ownPodcasts;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", ownPodcasts=" + ownPodcasts +
                '}';
    }
}

class ReducedPodcast {
    private String id;
    private String name;
    private String releaseDate;

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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "ReducedPodcast{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
