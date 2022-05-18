module myPodcastDB {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires javatuples;
    requires org.neo4j.driver;
    requires java.xml;
    requires xstream;

    opens it.unipi.dii.lsmsdb.myPodcastDB.utility to xstream;
    opens it.unipi.dii.lsmsdb.myPodcastDB to javafx.fxml;
    exports it.unipi.dii.lsmsdb.myPodcastDB;
    exports it.unipi.dii.lsmsdb.myPodcastDB.controller;
}