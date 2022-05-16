package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ConfigManager;
import org.bson.Document;

public class MongoManager implements AutoCloseable {
    static MongoManager mongoManager;

    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoManager() {
        this.mongoClient = null;
        this.database = null;
    }

    public boolean openConnection() {
        try {
            ConnectionString uri = new ConnectionString(ConfigManager.getMongoDBConnectorString());
            this.mongoClient = MongoClients.create(uri);
            this.database = mongoClient.getDatabase(ConfigManager.getMongoDBName());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean closeConnection() {
        if (this.mongoClient != null) {
            try {
                this.mongoClient.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public MongoCollection<Document> getCollection(String collection) {
        return this.database.getCollection(collection);
    }

    public static MongoManager getInstance() {
        if (mongoManager == null)
            mongoManager = new MongoManager();

        return mongoManager;
    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
}
