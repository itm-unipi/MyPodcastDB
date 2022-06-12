package it.unipi.dii.lsmsdb.myPodcastDB.cache;

import javafx.application.Application;
import javafx.scene.image.Image;

import java.util.Hashtable;

public class ImageCache {
    private static ImageCache imageCache;
    private static Application application;
    private Hashtable<String, Image> htImages;

    public ImageCache() {
        this.htImages = new Hashtable<>();
    }

    public static void initializeCache(Application app) {
        application = app;
    }

    public static Image getImageFromURL(String url) {
        Image image = getInstance().htImages.get(url);
        if (image == null) {
            image = new Image(url);
            getInstance().htImages.put(url, image);
        }

        return image;
    }

    public static Image getImageFromLocalPath(String url) {
        Image image = getInstance().htImages.get(url);
        if (image == null) {
            image = new Image(application.getClass().getResourceAsStream(url));
            getInstance().htImages.put(url, image);
        }

        return image;
    }

    public static ImageCache getInstance() {
        if (imageCache == null)
            imageCache = new ImageCache();
        return imageCache;
    }
}
