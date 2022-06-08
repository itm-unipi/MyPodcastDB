package it.unipi.dii.lsmsdb.myPodcastDB.service;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.AdminMongo;
import it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo.MongoManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

public class AdminDashboardService {

    //---------------- GIANLUCA ---------------------
    private AdminMongo adminMongoManager;

    public AdminDashboardService(){
        adminMongoManager = new AdminMongo();
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

    public int updateAdmin(Admin oldAdmin, Admin newAdmin){

        int res = -1;

        //check if there is something to update
        if( oldAdmin.getName().equals(newAdmin.getName()) &&
            oldAdmin.getEmail().equals(newAdmin.getEmail()) &&
            oldAdmin.getPassword().equals(newAdmin.getPassword())
        )
            return 1;

        MongoManager.getInstance().openConnection();
        //check if oldAdmin exists
        if(adminMongoManager.findAdminByName(oldAdmin.getName()) == null)
            res = 2;
        //check if an admin with the same name already exists
        else if(!oldAdmin.getName().equals(newAdmin.getName()) && adminMongoManager.findAdminByName(newAdmin.getName()) != null)
            res = 3;
        //check failure mongo operation
        else if (!adminMongoManager.updateAdmin(newAdmin))
            res = 4;
        else
            res = 0;
        MongoManager.getInstance().closeConnection();
        if(res == 0)
            oldAdmin.copy(newAdmin);
        return res;
    }

    public int deleteAdmin(Admin admin){
        int res = -1;
        MongoManager.getInstance().openConnection();
        //check if admin exists
        if(adminMongoManager.findAdminByName(admin.getName()) == null)
            res = 1;
        //check failure mongo operation
        else if (!adminMongoManager.deleteAdminById(admin.getId()))
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
