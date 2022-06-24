package it.unipi.dii.lsmsdb.myPodcastDB.utility;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigManager {

    public static XMLconfig config;

    public ConfigManager() {
    }

    private static boolean XMLValidation(String xml, String xsd) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document d = db.parse(new File(xml));
            Schema s = sf.newSchema(new StreamSource(xsd));
            s.newValidator().validate(new DOMSource(d));

        } catch(Exception e) {
            if(e instanceof SAXException)
                System.err.println("XML validation error: " + e.getMessage());
            else
                System.err.println(e.getMessage());

            return false;
        }

        return true;
    }

    public static boolean importConfig(String xmlPath, String xsdPath) {
        Logger.info("Loading configuration...");
        if (!XMLValidation(xmlPath, xsdPath)) {
            Logger.error("Validation failed");
            return false;
        }

        XStream xs = new XStream();
        xs.processAnnotations(XMLconfig.class);
        xs.addPermission(AnyTypePermission.ANY);
        String importedXML;

        try {
            importedXML = new String(Files.readAllBytes(Paths.get(xmlPath)));
        } catch(Exception e) {
            Logger.error("Loading of configuration failed");
            Logger.error(e.getMessage());
            return false;
        }
        XMLconfig newConfig = (XMLconfig)xs.fromXML(importedXML);
        Logger.success("Configuration loaded");
        config = newConfig;
        Logger.setLogMode(config.getLogMode());
        return true;
    }

    public static void printConfig() {
        Logger.info(config.toString());
    }

    public static String getMongoDBConnectorString() {
        if (config.getMongoDBConfigType().equals("local")) {
            String stringConnector = "mongodb://" +
                    config.getMongoDBLocalConfig().getMongoDBUser() + ":" +
                    config.getMongoDBLocalConfig().getMongoDBPassword() + "@" +
                    config.getMongoDBLocalConfig().getMongoDBIp() + ":" +
                    config.getMongoDBLocalConfig().getMongoDBPort();
            return stringConnector;
        } else {
            String stringConnector = "mongodb://" +
                    //config.getMongoDBClusterConfig().getMongoDBUser() + ":" +
                    //config.getMongoDBClusterConfig().getMongoDBPassword() + "@" +
                    config.getMongoDBClusterConfig().getMongoDBIp1() + ":" +
                    config.getMongoDBClusterConfig().getMongoDBPort1() + "," +
                    config.getMongoDBClusterConfig().getMongoDBIp2() + ":" +
                    config.getMongoDBClusterConfig().getMongoDBPort2() + "," +
                    config.getMongoDBClusterConfig().getMongoDBIp3() + ":" +
                    config.getMongoDBClusterConfig().getMongoDBPort3() + "/?retryWrites=true&w=" +
                    config.getMongoDBClusterConfig().getWriteConcern() + "&readPreference=" +
                    config.getMongoDBClusterConfig().getReadPreferences() + "&wtimeout=5000&ssl=false";
            return stringConnector;
        }
    }

    public static String getMongoDBName() {
        return config.getMongoDBLocalConfig().getMongoDBName();
    }

    public static String getNeo4JConnectorString() {
        String stringConnector = "neo4j://" +
                // config.getNeo4JConfig().getNeo4JUser() + ":" +
                // config.getNeo4JConfig().getNeo4JPassword() + "@" +
                config.getNeo4JConfig().getNeo4JIp() + ":" +
                config.getNeo4JConfig().getNeo4JPort() ;
        return stringConnector;
    }

    public static boolean isInitialized(){
        if(config == null)
            return false;
        else return true;
    }

    public static String getNeo4JName() { return config.getNeo4JConfig().getNeo4JName(); }

    public static String getNeo4JUser() { return config.getNeo4JConfig().getNeo4JUser(); }

    public static String getNeo4JPassword() { return config.getNeo4JConfig().getNeo4JPassword(); }

    public static String getLogMode() { return config.getLogMode(); }

    public static int getImageCacheSize() { return config.getImageCacheSize(); }
}
