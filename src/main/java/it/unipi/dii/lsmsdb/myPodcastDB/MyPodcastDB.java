package it.unipi.dii.lsmsdb.myPodcastDB;

import it.unipi.dii.lsmsdb.myPodcastDB.utility.ConfigManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

public class MyPodcastDB {

    public MyPodcastDB() {
        Logger.initialize();
        ConfigManager.importConfig("config.xml", "src/main/java/it/unipi/dii/lsmsdb/myPodcastDB/utility/schema.xsd");
    }

    public void run() {
        Logger.info(ConfigManager.getMongoDBConnectorString());
        Logger.info(ConfigManager.getNeo4JConnectorString());
        ConfigManager.printConfig();
    }
}
