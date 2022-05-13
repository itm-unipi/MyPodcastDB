package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class UserMongoTest {

    UserMongo userMongo;

    public UserMongoTest() {
        this.userMongo = new UserMongo();
    }

    static boolean compare(User a, User b, boolean sameId) {
        if (sameId && !a.getId().equals(b.getId()))
            return false;
        if (!a.getUsername().equals(b.getUsername()))
            return false;
        if (!a.getPassword().equals(b.getPassword()))
            return false;
        if (!a.getName().equals(b.getName()))
            return false;
        if (!a.getSurname().equals(b.getSurname()))
            return false;
        if (!a.getEmail().equals(b.getEmail()))
            return false;
        if (!a.getCountry().equals(b.getCountry()))
            return false;
        if (!a.getPictureSmall().equals(b.getPictureSmall()))
            return false;
        if (!a.getPictureMedium().equals(b.getPictureMedium()))
            return false;
        if (a.getAge() != b.getAge())
            return false;
        if (!a.getGender().equals(b.getGender()))
            return false;

        return true;
    }

    public void findByUsernameTest() {
        User user = this.userMongo.findUserByUsername("silverelephant716273");
        if (user.getUsername().equals("silverelephant716273"))
            System.out.println("[+] findUserByUsername");
        else
            System.err.println("[-] findUserByUsername");
    }

    public void addTest() {
        String username = "MarioRossi123456";
        String password = "password";
        String name = "Mario";
        String surname = "Rossi";
        String email = "mariorossi123456@example.com";
        String country = "Ireland";
        String pictureSmall = "https://randomuser.me/api/portraits/thumb/men/45.jpg";
        String pictureMedium = "https://randomuser.me/api/portraits/med/men/45.jpg";
        String favouriteGenre = "Documentary";
        int age = 23;
        String gender = "Male";

        User newUser = new User("", username, password, name, surname, email, country, pictureSmall, pictureMedium, favouriteGenre, age, gender);
        this.userMongo.addUser(newUser);

        User createdUser = this.userMongo.findUserById(newUser.getId());
        if(compare(newUser, createdUser, false))
            System.out.println("[+] addUser");
        else
            System.err.println("addUser doesn't works");
    }

    public void findsTest() {
        User user1 = this.userMongo.findUserByUsername("silverelephant716273");
        User user2 = this.userMongo.findUserById(user1.getId());
        User user3 = this.userMongo.findUserByEmail(user1.getEmail());

        if(user2 != null && compare(user1, user2, true))
            System.out.println("[+] findUserById");
        else
            System.err.println("[-] findUserById");

        if(user2 != null && compare(user1, user3, true))
            System.out.println("[+] findUserByEmail");
        else
            System.err.println("[-] findUserByEmail");

        List<User> users = this.userMongo.findUsersByCountry("Ireland", 3);
        boolean test = true;
        for (User user : users)
            if (user.getCountry() != "Ireland")
                test = false;

        if(test)
            System.out.println("[+] findUserByCountry");
        else
            System.err.println("[-] findUserByCountry");
    }

    public void updateTest() {
        User newUser = this.userMongo.findUserByUsername("MarioRossi123456");
        String id = newUser.getId();
        newUser.setAge(25);
        this.userMongo.updateUser(newUser);
        newUser = this.userMongo.findUserById(id);
        if (newUser.getAge() == 25)
            System.out.println("[+] updateUser");
        else
            System.err.println("[-] updateUser");
    }

    public void deleteTest() {
        User newUser = this.userMongo.findUserByUsername("MarioRossi123456");
        String id = newUser.getId();
        this.userMongo.deleteUserById(id);
        User testUser = this.userMongo.findUserById(id);
        if (testUser == null)
            System.out.println("[+] deleteUserById");
        else
            System.err.println("[-] deleteUserById");

        this.userMongo.addUser(newUser);
        id = newUser.getId();
        this.userMongo.deleteUserByUsername(newUser.getUsername());
        testUser = this.userMongo.findUserById(id);
        if (testUser == null)
            System.out.println("[+] deleteUserByUsername");
        else
            System.err.println("[-] deleteUserByUsername");
    }

    public void showAverageAgeOfUsersPerFavouriteCategoryTest() {
        List<Entry<String, Float>> list = new ArrayList<>();
        list = this.userMongo.showAverageAgeOfUsersPerFavouriteCategory(20);

        if (list != null) {
            System.out.println("[+] showAverageAgeOfUsersPerFavouriteCategory");
            for (Entry<String , Float> e: list) {
                //System.out.println(e.getKey() + " " + e.getValue());
            }
        } else {
            System.err.println("[-] showAverageAgeOfUsersPerFavouriteCategory");
        }
    }

    public void showAverageAgeOfUsersPerCountryTest() {
        List<Entry<String, Float>> list = new ArrayList<>();
        list = this.userMongo.showAverageAgeOfUsersPerCountry(20);

        if (list != null) {
            System.out.println("[+] showAverageAgeOfUsersPerCountry");
            for (Entry<String , Float> e: list) {
                //System.out.println(e.getKey() + " " + e.getValue());
            }
        } else {
            System.err.println("[-] showAverageAgeOfUsersPerCountry");
        }
    }

    public static void main(String[] args) {
        MongoManager manager = MongoManager.getInstance();
        manager.openConnection();
        UserMongoTest test = new UserMongoTest();

        System.out.println("############# MongoDB CRUD TEST #############");
        test.findByUsernameTest();
        test.addTest();
        test.findsTest();
        test.updateTest();
        test.deleteTest();

        System.out.println("############# MongoDB AGGREGATES TEST #############");
        test.showAverageAgeOfUsersPerFavouriteCategoryTest();
        test.showAverageAgeOfUsersPerCountryTest();

        manager.closeConnection();
    }
}
