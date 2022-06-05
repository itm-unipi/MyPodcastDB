package it.unipi.dii.lsmsdb.myPodcastDB.utility;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {
    static private String fileName;
    static private String logMode;

    public static void initialize() {

        fileName = "log.txt";
        logMode = "verbose";

        try {
            FileWriter fw = new FileWriter(fileName);
            Date timestamp = new Date();
            fw.write("====== " + timestamp.toString() + " ======\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void info(String message) {
        if (!logMode.equals("verbose"))
            return;

        String page = "INIT";
        if(ConfigManager.isInitialized())
            page = MyPodcastDB.getInstance().getSessionPage();

        String log = "[!] {" + page + "} " + message;

        System.out.println(log);

        try {
            FileWriter fw = new FileWriter(fileName, true);
            fw.write(log + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void success(String message) {
        if (logMode.equals("deploy"))
            return;

        String page = "INIT";
        if(ConfigManager.isInitialized())
            page = MyPodcastDB.getInstance().getSessionPage();

        String log = "[+] {" + page + "} " + message;
        System.out.println(log);

        try {
            FileWriter fw = new FileWriter(fileName, true);
            fw.write(log + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void error(String message) {
        String page = "INIT";
        if(ConfigManager.isInitialized())
            page = MyPodcastDB.getInstance().getSessionPage();

        String log = "[-] {" + page + "} " + message;
        System.err.println(log);

        try {
            FileWriter fw = new FileWriter(fileName, true);
            fw.write(log + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLogMode(String newLogMode) {
        logMode = newLogMode;
    }
}
