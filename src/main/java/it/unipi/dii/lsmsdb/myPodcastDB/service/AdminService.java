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

    public boolean getAdminLogin(Admin admin){

        boolean res;
        MongoManager.getInstance().openConnection();

        Admin newAdmin = adminMongoManager.findAdminByName(admin.getName());
        if(newAdmin == null || !admin.getPassword().equals(newAdmin.getPassword()))
            res = false;
        else {
            admin.copy(newAdmin);
            res = true;
        }

        MongoManager.getInstance().closeConnection();
        return res;
    }
    //-----------------------------------------------

    //----------------- BIAGIO ----------------------
    //-----------------------------------------------

    //----------------- MATTEO ----------------------
    //-----------------------------------------------
}
