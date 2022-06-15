package it.unipi.dii.lsmsdb.myPodcastDB.utility;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("xmlConfig")
public class XMLconfig implements Serializable {
    private String mongoDBConfigType;
    private MongoDBLocalConfig mongoDBLocalConfig;
    private MongoDBClusterConfig mongoDBClusterConfig;
    private Neo4JConfig neo4JConfig;
    private String logMode;
    private int imageCacheSize;

    public XMLconfig() {
    }

    public MongoDBLocalConfig getMongoDBLocalConfig() { return this.mongoDBLocalConfig; }

    public void setMongoDBLocalConfig(MongoDBLocalConfig mongoDBConfig) {
        this.mongoDBLocalConfig = mongoDBConfig;
    }

    public String getMongoDBConfigType() {
        return mongoDBConfigType;
    }

    public void setMongoDBConfigType(String mongoDBConfigType) {
        this.mongoDBConfigType = mongoDBConfigType;
    }

    public MongoDBClusterConfig getMongoDBClusterConfig() {
        return mongoDBClusterConfig;
    }

    public void setMongoDBClusterConfig(MongoDBClusterConfig mongoDBClusterConfig) {
        this.mongoDBClusterConfig = mongoDBClusterConfig;
    }

    public Neo4JConfig getNeo4JConfig() {
        return neo4JConfig;
    }

    public void setNeo4JConfig(Neo4JConfig neo4JConfig) {
        this.neo4JConfig = neo4JConfig;
    }

    public String getLogMode() {
        return logMode;
    }

    public void setLogMode(String logMode) {
        this.logMode = logMode;
    }

    public int getImageCacheSize() {
        return imageCacheSize;
    }

    public void setImageCacheSize(int imageCacheSize) {
        this.imageCacheSize = imageCacheSize;
    }

    @Override
    public String toString() {
        return "XMLconfig{" +
                "mongoDBConfigType='" + mongoDBConfigType + '\'' +
                ", mongoDBLocalConfig=" + mongoDBLocalConfig +
                ", mongoDBClusterConfig=" + mongoDBClusterConfig +
                ", neo4JConfig=" + neo4JConfig +
                ", logMode='" + logMode + '\'' +
                ", imageCacheSize=" + imageCacheSize +
                '}';
    }
}

class MongoDBLocalConfig implements Serializable {
    private String mongoDBIp;
    private String mongoDBPort;
    private String mongoDBName;
    private String mongoDBUser;
    private String mongoDBPassword;

    public MongoDBLocalConfig() {
    }

    public String getMongoDBIp() {
        return mongoDBIp;
    }

    public void setMongoDBIp(String mongoDBIp) {
        this.mongoDBIp = mongoDBIp;
    }

    public String getMongoDBPort() {
        return mongoDBPort;
    }

    public void setMongoDBPort(String mongoDBPort) {
        this.mongoDBPort = mongoDBPort;
    }

    public String getMongoDBName() {
        return mongoDBName;
    }

    public void setMongoDBName(String mongoDBName) {
        this.mongoDBName = mongoDBName;
    }

    public String getMongoDBUser() {
        return mongoDBUser;
    }

    public void setMongoDBUser(String mongoDBUser) {
        this.mongoDBUser = mongoDBUser;
    }

    public String getMongoDBPassword() {
        return mongoDBPassword;
    }

    public void setMongoDBPassword(String mongoDBPassword) {
        this.mongoDBPassword = mongoDBPassword;
    }

    @Override
    public String toString() {
        return "MongoDBLocalConfig{" +
                "mongoDBIp='" + mongoDBIp + '\'' +
                ", mongoDBPort='" + mongoDBPort + '\'' +
                ", mongoDBName='" + mongoDBName + '\'' +
                ", mongoDBUser='" + mongoDBUser + '\'' +
                ", mongoDBPassword='" + mongoDBPassword + '\'' +
                '}';
    }
}

class MongoDBClusterConfig implements Serializable {
    private String mongoDBIp1;
    private String mongoDBPort1;
    private String mongoDBIp2;
    private String mongoDBPort2;
    private String mongoDBIp3;
    private String mongoDBPort3;
    private int writeConcern;
    private String readPreferences;
    private String mongoDBName;
    private String mongoDBUser;
    private String mongoDBPassword;

    public MongoDBClusterConfig() {
    }

    public String getMongoDBIp1() {
        return mongoDBIp1;
    }

    public void setMongoDBIp1(String mongoDBIp1) {
        this.mongoDBIp1 = mongoDBIp1;
    }

    public String getMongoDBPort1() {
        return mongoDBPort1;
    }

    public void setMongoDBPort1(String mongoDBPort1) {
        this.mongoDBPort1 = mongoDBPort1;
    }

    public String getMongoDBIp2() {
        return mongoDBIp2;
    }

    public void setMongoDBIp2(String mongoDBIp2) {
        this.mongoDBIp2 = mongoDBIp2;
    }

    public String getMongoDBPort2() {
        return mongoDBPort2;
    }

    public void setMongoDBPort2(String mongoDBPort2) {
        this.mongoDBPort2 = mongoDBPort2;
    }

    public String getMongoDBIp3() {
        return mongoDBIp3;
    }

    public void setMongoDBIp3(String mongoDBIp3) {
        this.mongoDBIp3 = mongoDBIp3;
    }

    public String getMongoDBPort3() {
        return mongoDBPort3;
    }

    public void setMongoDBPort3(String mongoDBPort3) {
        this.mongoDBPort3 = mongoDBPort3;
    }

    public int getWriteConcern() {
        return writeConcern;
    }

    public void setWriteConcern(int writeConcern) {
        this.writeConcern = writeConcern;
    }

    public String getReadPreferences() {
        return readPreferences;
    }

    public void setReadPreferences(String readPreferences) {
        this.readPreferences = readPreferences;
    }

    public String getMongoDBName() {
        return mongoDBName;
    }

    public void setMongoDBName(String mongoDBName) {
        this.mongoDBName = mongoDBName;
    }

    public String getMongoDBUser() {
        return mongoDBUser;
    }

    public void setMongoDBUser(String mongoDBUser) {
        this.mongoDBUser = mongoDBUser;
    }

    public String getMongoDBPassword() {
        return mongoDBPassword;
    }

    public void setMongoDBPassword(String mongoDBPassword) {
        this.mongoDBPassword = mongoDBPassword;
    }

    @Override
    public String toString() {
        return "MongoDBClusterConfig{" +
                "mongoDBIp1='" + mongoDBIp1 + '\'' +
                ", mongoDBPort1='" + mongoDBPort1 + '\'' +
                ", mongoDBIp2='" + mongoDBIp2 + '\'' +
                ", mongoDBPort2='" + mongoDBPort2 + '\'' +
                ", mongoDBIp3='" + mongoDBIp3 + '\'' +
                ", mongoDBPort3='" + mongoDBPort3 + '\'' +
                ", writeConcern=" + writeConcern +
                ", readPreferences='" + readPreferences + '\'' +
                ", mongoDBName='" + mongoDBName + '\'' +
                ", mongoDBUser='" + mongoDBUser + '\'' +
                ", mongoDBPassword='" + mongoDBPassword + '\'' +
                '}';
    }
}

class Neo4JConfig implements Serializable {
    private String neo4JIp;
    private String neo4JPort;
    private String neo4JName;
    private String neo4JUser;
    private String neo4JPassword;

    public Neo4JConfig() {
    }

    public String getNeo4JIp() {
        return neo4JIp;
    }

    public void setNeo4JIp(String neo4JIp) {
        this.neo4JIp = neo4JIp;
    }

    public String getNeo4JPort() {
        return neo4JPort;
    }

    public void setNeo4JPort(String neo4JPort) {
        this.neo4JPort = neo4JPort;
    }

    public String getNeo4JName() {
        return neo4JName;
    }

    public void setNeo4JName(String neo4JName) {
        this.neo4JName = neo4JName;
    }

    public String getNeo4JUser() {
        return neo4JUser;
    }

    public void setNeo4JUser(String neo4JUser) {
        this.neo4JUser = neo4JUser;
    }

    public String getNeo4JPassword() {
        return neo4JPassword;
    }

    public void setNeo4JPassword(String neo4JPassword) {
        this.neo4JPassword = neo4JPassword;
    }

    @Override
    public String toString() {
        return "Neo4JConfig{" +
                "neo4JIp='" + neo4JIp + '\'' +
                ", neo4JPort='" + neo4JPort + '\'' +
                ", neo4JName='" + neo4JName + '\'' +
                ", neo4JUser='" + neo4JUser + '\'' +
                ", neo4JPassword='" + neo4JPassword + '\'' +
                '}';
    }
}
