package it.unipi.dii.lsmsdb.myPodcastDB.persistence.mongo;

import it.unipi.dii.lsmsdb.myPodcastDB.model.User;

import java.util.List;

public class UserMongoTest {

    UserMongo userMongo;

    public UserMongoTest() {
        this.userMongo = new UserMongo();
    }

    boolean compare(User a, User b, boolean sameId) {
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

    public void findByIdTest() {
        User user = this.userMongo.findUserById("6275040d29c6dd2b02682bae");
        if (user.getUsername().equals("silverelephant716273"))
            System.out.println("findUserById works");
        else
            System.err.println("findUserById doesn't works");
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
            System.out.println("addUser works");
        else
            System.err.println("addUser doesn't works");
    }

    public void findsTest() {
        User user1 = this.userMongo.findUserById("6275040d29c6dd2b02682bae");
        User user2 = this.userMongo.findUserByUsername(user1.getUsername());
        User user3 = this.userMongo.findUserByEmail(user1.getEmail());

        if(compare(user1, user2, true))
            System.out.println("findUserByUsername works");
        else
            System.err.println("findUserByUsername doesn't works");

        if(compare(user1, user3, true))
            System.out.println("findUserByEmail works");
        else
            System.err.println("findUserByEmail doesn't works");

        List<User> users = this.userMongo.findUsersByCountry("Ireland", 3);
        boolean test = true;
        for (User user : users)
            if (user.getCountry() != "Ireland")
                test = false;

        if(test)
            System.out.println("findUserByCountry works");
        else
            System.err.println("findUserByCountry doesn't works");
    }

    public void updateTest() {
        User newUser = this.userMongo.findUserByUsername("MarioRossi123456");
        String id = newUser.getId();
        newUser.setAge(25);
        this.userMongo.updateUser(newUser);
        newUser = this.userMongo.findUserById(id);
        if (newUser.getAge() == 25)
            System.out.println("updateUser work");
        else
            System.err.println("updateUser doesn't work");
    }

    public void deleteTest() {
        User newUser = this.userMongo.findUserByUsername("MarioRossi123456");
        String id = newUser.getId();
        this.userMongo.deleteUserById(id);
        User testUser = this.userMongo.findUserById(id);
        if (testUser == null)
            System.out.println("deleteUserById work");
        else
            System.err.println("deleteUserById doesn't work");

        this.userMongo.addUser(newUser);
        id = newUser.getId();
        this.userMongo.deleteUserByUsername(newUser.getUsername());
        testUser = this.userMongo.findUserById(id);
        if (testUser == null)
            System.out.println("deleteUserByUsername work");
        else
            System.err.println("deleteUserByUsername doesn't work");
    }

    public static void main(String[] args) {
        MongoManager manager = MongoManager.getInstance();
        manager.openConnection();
        UserMongoTest test = new UserMongoTest();
        test.findByIdTest();
        test.addTest();
        test.findsTest();
        test.updateTest();
        test.deleteTest();
        manager.closeConnection();
    }
}
