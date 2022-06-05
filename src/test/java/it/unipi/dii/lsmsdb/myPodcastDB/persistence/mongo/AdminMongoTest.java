package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ConfigManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;

import java.util.List;

public class AdminMongoTest {

    AdminMongo adminMongo;

    public AdminMongoTest() {
        this.adminMongo = new AdminMongo();
    }

    static boolean compare(Admin a, Admin b, boolean sameId) {
        if (sameId && !a.getId().equals(b.getId()))
            return false;
        if (!a.getName().equals(b.getName()))
            return false;
        if (!a.getPassword().equals(b.getPassword()))
            return false;
        if (!a.getEmail().equals(b.getEmail()))
            return false;

        return true;
    }

    public void findByNameTest() {
        Admin admin = this.adminMongo.findAdminByName("admin");
        if (admin.getName().equals("admin"))
            System.out.println("[+] findAdminByName");
        else
            System.err.println("[-] findAdminByName");
    }

    public void addTest() {
        String name = "adminTest";
        String password = "password";
        String email = "admintest@example.com";

        Admin newAdmin = new Admin("", name, password, email);
        this.adminMongo.addAdmin(newAdmin);

        Admin createdAdmin = this.adminMongo.findAdminByName("adminTest");
        if(compare(newAdmin, createdAdmin, false))
            System.out.println("[+] addAdmin");
        else
            System.err.println("[-] addAdmin");
    }

    public void findsTest() {
        Admin admin1 = this.adminMongo.findAdminByName("admin");
        Admin admin2 = this.adminMongo.findAdminById(admin1.getId());
        Admin admin3 = this.adminMongo.findAdminByEmail(admin1.getEmail());

        if(admin2 != null && compare(admin1, admin2, true))
            System.out.println("[+] findAdminById");
        else
            System.err.println("[-] findAdminById");

        if(admin2 != null && compare(admin1, admin3, true))
            System.out.println("[+] findAdminByEmail");
        else
            System.err.println("[-] findAdminByEmail");
    }

    public void updateTest() {
        Admin admin = this.adminMongo.findAdminByName("adminTest");
        String id = admin.getId();
        admin.setName("adminTestUpdated");
        admin.setPassword("passwordUpdated");
        admin.setEmail("admintestupdated@example.com");
        this.adminMongo.updateAdmin(admin);
        admin = this.adminMongo.findAdminById(id);
        if (admin.getName().equals("adminTestUpdated") && admin.getPassword().equals("passwordUpdated") && admin.getEmail().equals("admintestupdated@example.com"))
            System.out.println("[+] updateAdmin");
        else
            System.err.println("[-] updateAdmin");
    }

    public void deleteTest() {
        Admin admin1 = this.adminMongo.findAdminByName("adminTestUpdated");
        Admin admin2 = this.adminMongo.findAdminByName("adminTestUpdated");
        admin2.setName("adminTest2");
        this.adminMongo.addAdmin(admin2);
        Admin admin3 = this.adminMongo.findAdminByName("adminTestUpdated");
        admin3.setName("adminTest3");
        admin3.setEmail("admintest3@example.com");
        this.adminMongo.addAdmin(admin3);

        String id = admin1.getId();
        this.adminMongo.deleteAdminById(id);
        Admin testAdmin = this.adminMongo.findAdminById(id);
        if (testAdmin == null)
            System.out.println("[+] deleteAdminById");
        else
            System.err.println("[-] deleteAdminById");

        String name = admin2.getName();
        this.adminMongo.deleteAdminByName(name);
        testAdmin = this.adminMongo.findAdminByName(name);
        if (testAdmin == null)
            System.out.println("[+] deleteAdminByName");
        else
            System.err.println("[-] deleteAdminByName");

        String email = admin3.getEmail();
        this.adminMongo.deleteAdminByEmail(email);
        testAdmin = this.adminMongo.findAdminByEmail(email);
        if (testAdmin == null)
            System.out.println("[+] deleteAdminByEmail");
        else
            System.err.println("[-] deleteAdminByEmail");
    }

    public static void main(String[] args) {
        Logger.initialize();
        ConfigManager.importConfig("config.xml", "src/main/java/it/unipi/dii/lsmsdb/myPodcastDB/utility/schema.xsd");

        MongoManager manager = MongoManager.getInstance();
        manager.openConnection();
        AdminMongoTest test = new AdminMongoTest();

        test.findByNameTest();
        test.addTest();
        test.findsTest();
        test.updateTest();
        test.deleteTest();

        manager.closeConnection();
    }
}
