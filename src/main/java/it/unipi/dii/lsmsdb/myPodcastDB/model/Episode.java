package it.unipi.dii.lsmsdb.myPodcastDB.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Episode {
    private String name;
    private String description;
    private Date releaseDate;
    private int timeMillis;

    public Episode() {
    }

    public Episode(String name, String description, Date releaseDate, int timeMillis) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.timeMillis = timeMillis;
    }

    public void copy(Episode episode) {
        this.name = episode.name;
        this.description = episode.description;
        this.releaseDate = episode.releaseDate;
        this.timeMillis = episode.timeMillis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(int timeMillis) {
        this.timeMillis = timeMillis;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", timeMillis=" + timeMillis +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Episode)) return false;
        Episode episode = (Episode) o;
        return getTimeMillis() == episode.getTimeMillis() && Objects.equals(getName(), episode.getName()) && Objects.equals(getDescription(), episode.getDescription()) && Objects.equals(getReleaseDate(), episode.getReleaseDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getReleaseDate(), getTimeMillis());
    }
}
