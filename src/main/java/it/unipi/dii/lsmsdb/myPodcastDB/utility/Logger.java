package it.unipi.dii.lsmsdb.myPodcastDB.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {
    public static void initialize() {
        try {
            FileWriter fw = new FileWriter("Log.txt");
            Date timestamp = new Date();
            fw.write("====== " + timestamp.toString() + " ======\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void info(String message) {
        String log = "[+] " + message;
        System.out.println(log);

        try {
            FileWriter fw = new FileWriter("Log.txt", true);
            fw.write(log + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void error(String message) {
        String log = "[-] " + message;
        System.err.println(log);

        try {
            FileWriter fw = new FileWriter("Log.txt", true);
            fw.write(log + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
