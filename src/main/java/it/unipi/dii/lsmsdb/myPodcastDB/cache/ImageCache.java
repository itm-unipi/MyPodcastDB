package it.unipi.dii.lsmsdb.myPodcastDB.cache;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import javafx.application.Application;
import javafx.scene.image.Image;

import java.util.Hashtable;
import java.util.LinkedList;

public class ImageCache {
    private static ImageCache imageCache;
    private static Application application;

    // cache structure
    private int loadedImages;
    private int cacheSize;                  // TODO: da mettere nel config manager
    private Hashtable<String, Image> htImages;
    private LinkedList<String> lruImages;

    public ImageCache() {
        this.loadedImages = 0;
        this.cacheSize = 100;               // TODO: da mettere nel config manager
        this.htImages  = new Hashtable<>();
        this.lruImages = new LinkedList<>();
    }

    public Image addImage(String url, boolean isLocal) {
        // if cache is full, remove the LRU image
        if (this.loadedImages == this.cacheSize) {
            String lruImage = this.lruImages.removeLast();
            this.htImages.remove(lruImage);
            Logger.info("Cache full");
        }

        // else update the LRU status
        else {
            this.loadedImages++;
        }

        // load new image
        Image image = isLocal ? new Image(application.getClass().getResourceAsStream(url)) : new Image(url);
        this.lruImages.addFirst(url);
        this.htImages.put(url, image);

        return image;
    }

    private void updateLRUStatus(String url) {
        this.lruImages.remove(url);
        this.lruImages.addFirst(url);
    }

    public static void initializeCache(Application app) {
        application = app;
    }

    public static Image getImageFromURL(String url) {
        Image image = getInstance().htImages.get(url);
        if (image == null)
            image = getInstance().addImage(url, false);
        else
            getInstance().updateLRUStatus(url);

        return image;
    }

    public static Image getImageFromLocalPath(String url) {
        Image image = getInstance().htImages.get(url);
        if (image == null)
            image = getInstance().addImage(url, true);
        else
            getInstance().updateLRUStatus(url);

        return image;
    }

    public static void clearImages() {
        getInstance().htImages.clear();
        getInstance().lruImages.clear();
        getInstance().loadedImages = 0;
    }

    public static ImageCache getInstance() {
        if (imageCache == null)
            imageCache = new ImageCache();
        return imageCache;
    }
}
