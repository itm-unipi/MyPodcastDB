package it.unipi.dii.lsmsdb.myPodcastDB.model;

public class UserPreview {
    String username;
    String pictureMedium;

    public UserPreview() {
    }

    public UserPreview(String username, String pictureMedium) {
        this.username = username;
        this.pictureMedium = pictureMedium;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPictureMedium() {
        return pictureMedium;
    }

    public void setPictureMedium(String pictureMedium) {
        this.pictureMedium = pictureMedium;
    }

    @Override
    public String toString() {
        return "UserPreview{" +
                "username='" + username + '\'' +
                ", pictureMedium='" + pictureMedium + '\'' +
                '}';
    }
}
