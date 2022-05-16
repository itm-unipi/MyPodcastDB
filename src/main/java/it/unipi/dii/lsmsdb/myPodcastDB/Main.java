package it.unipi.dii.lsmsdb.myPodcastDB;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.initialize();
        Logger.info("Test info");
        Logger.error("Error info");
    }
}
