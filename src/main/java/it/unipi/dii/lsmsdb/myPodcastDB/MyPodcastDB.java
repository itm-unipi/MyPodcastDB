package it.unipi.dii.lsmsdb.myPodcastDB;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.ConfigManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

public class MyPodcastDB {
    private static MyPodcastDB myPodcastDB;

    private Object sessionActor;
    private String sessionType;

    public MyPodcastDB() {
        Logger.initialize();
        ConfigManager.importConfig("config.xml", "src/main/java/it/unipi/dii/lsmsdb/myPodcastDB/utility/schema.xsd");
    }

    public void run() {
        Logger.info(ConfigManager.getMongoDBConnectorString());
        Logger.info(ConfigManager.getNeo4JConnectorString());
        ConfigManager.printConfig();
    }

    public Object getSessionActor() {
        return sessionActor;
    }

    public void setSession(Object sessionActor, String type) {
        this.sessionActor = sessionActor;
        this.sessionType = type;                                // User, Author, Admin
    }

    public String getSessionType() {
        return sessionType;
    }

    public static MyPodcastDB getInstance() {
        if (myPodcastDB == null)
            myPodcastDB = new MyPodcastDB();

        return myPodcastDB;
    }
}
