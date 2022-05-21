package it.unipi.dii.lsmsdb.myPodcastDB.model;

public class PodcastPreview {
    private String podcastId;
    private String podcastName;
    private String artworkUrl600;

    public PodcastPreview() {
    }

    public PodcastPreview(String podcastId, String podcastName, String artworkUrl600) {
        this.podcastId = podcastId;
        this.podcastName = podcastName;
        this.artworkUrl600 = artworkUrl600;
    }

    public String getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(String podcastId) {
        this.podcastId = podcastId;
    }

    public String getPodcastName() {
        return podcastName;
    }

    public void setPodcastName(String podcastName) {
        this.podcastName = podcastName;
    }

    public String getArtworkUrl600() {
        return artworkUrl600;
    }

    public void setArtworkUrl600(String artworkUrl600) {
        this.artworkUrl600 = artworkUrl600;
    }

    @Override
    public String toString() {
        return "PodcastPreview{" +
                "podcastId='" + podcastId + '\'' +
                ", podcastName='" + podcastName + '\'' +
                ", artworkUrl600='" + artworkUrl600 + '\'' +
                '}';
    }
}
