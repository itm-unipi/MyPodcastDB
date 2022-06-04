package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AdminMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

public class AdminService {

    //---------------- GIANLUCA ---------------------
    private AdminMongo adminMongoManager;

    public AdminService(){
        adminMongoManager = new AdminMongo();
    }

    public int getAdminLogin(Admin admin){

        int res = -1;
        MongoManager.getInstance().openConnection();

        Admin newAdmin = adminMongoManager.findAdminByName(admin.getName());
        if(newAdmin == null || !admin.getPassword().equals(newAdmin.getPassword()))
            res = 1;
        else {
            admin.copy(newAdmin);
            res = 0;
        }

        MongoManager.getInstance().closeConnection();
        return res;
    }

    public int addAdmin(Admin admin){

        int res = -1;
        MongoManager.getInstance().openConnection();
        //check if an admin with the same name already exists
        if(adminMongoManager.findAdminByName(admin.getName()) != null)
            res = 1;
        //check failure mongo operation
        else if (!adminMongoManager.addAdmin(admin))
            res = 2;
        else
            res = 0;
        MongoManager.getInstance().closeConnection();
        return res;
    }
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
