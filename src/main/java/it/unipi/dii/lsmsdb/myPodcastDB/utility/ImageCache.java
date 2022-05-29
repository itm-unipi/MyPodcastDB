package it.unipi.dii.lsmsdb.myPodcastDB.utility;

import javafx.scene.image.Image;

import java.util.Hashtable;

public class ImageCache {
    private static ImageCache imageCache;

    private Hashtable<String, Image> htImages;

    public ImageCache() {
        this.htImages = new Hashtable<>();
    }

    public static Image getImageFromURL(String url) {
        if (getInstance().htImages.containsKey(url)) {
            return getInstance().htImages.get(url);
        } else {
            Image image = new Image(url);
            getInstance().htImages.put(url, image);
            return image;
        }
    }

    public static Image getImageFromLocalPath(String url) {
        if (getInstance().htImages.containsKey(url)) {
            return getInstance().htImages.get(url);
        } else {
            Image image = new Image("Files:/src/main/resources" + url);
            getInstance().htImages.put(url, image);
            return image;
        }
    }

    public static ImageCache getInstance() {
        if (imageCache == null)
            imageCache = new ImageCache();
        return imageCache;
    }
}
