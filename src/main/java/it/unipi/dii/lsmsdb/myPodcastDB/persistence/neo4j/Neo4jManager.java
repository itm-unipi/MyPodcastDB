package it.unipi.dii.lsmsdb.myPodcastDB.persistence.neo4j;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class Neo4jManager implements AutoCloseable {
    static Neo4jManager neo4jManager;

    private Driver driver;

    public Neo4jManager() {
        this.driver = null;
    }

    public boolean openConnection() {
        String uri = "neo4j://localhost:7687";                      // ConfigManager.GetNeo4JConnectorString();
        String user = "neo4j";                                      // ConfigManager.GetNeo4JUser();
        String password = "ciambella";                               // ConfigManager.GetNeo4JPassword();

        try {
            this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (this.driver == null) {
            System.err.println("Connection to Neo4J database failed");
            return false;
        }

        return true;
    }

    public boolean closeConnection() {
        if (this.driver != null) {
            try {
                this.driver.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public void write(final String query, final Value parameters) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx ->
            {
                tx.run(query, parameters).consume();
                return null;
            });
        }
    }

    public void write(String query) {
        write(query, parameters());
    }

    public List<Record> read(final String query, final Value parameters) {
        List<Record> recordsList;
        try (Session session = driver.session()) {
            recordsList = session.readTransaction(tx -> {
                Result result = tx.run( query, parameters );
                List<Record> records = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    records.add(r);
                }
                return records;
            });
        }
        return recordsList;
    }

    public List<Record> read(String query) {
        return read(query, parameters());
    }

    public static Neo4jManager getInstance() {
        if (neo4jManager == null)
            neo4jManager = new Neo4jManager();

        return neo4jManager;
    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
}
