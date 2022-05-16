package it.unipi.dii.lsmsdb.myPodcastDB.utility;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("xmlConfig")
public class XMLconfig implements Serializable {
    private MongoDBConfig mongoDBConfig;
    private Neo4JConfig neo4JConfig;
    private String logMode;

    public XMLconfig() {
    }

    public MongoDBConfig getMongoDBConfig() {
        return mongoDBConfig;
    }

    public void setMongoDBConfig(MongoDBConfig mongoDBConfig) {
        this.mongoDBConfig = mongoDBConfig;
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

    @Override
    public String toString() {
        return "XMLconfig{" +
                "mongoDBConfig=" + mongoDBConfig +
                ", neo4JConfig=" + neo4JConfig +
                ", logMode='" + logMode + '\'' +
                '}';
    }
}

class MongoDBConfig implements Serializable {
    private String mongoDBIp;
    private String mongoDBPort;
    private String mongoDBName;
    private String mongoDBUser;
    private String mongoDBPassword;

    public MongoDBConfig() {
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
        return "MongoDBConfig{" +
                "mongoDBIp='" + mongoDBIp + '\'' +
                ", mongoDBPort='" + mongoDBPort + '\'' +
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
