package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AdminService;
import it.unipi.dii.lsmsdb.myPodcastDB.service.AuthorService;
import it.unipi.dii.lsmsdb.myPodcastDB.service.UserService;
import it.unipi.dii.lsmsdb.myPodcastDB.view.DialogManager;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.javatuples.Triplet;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label loginUsernameLabel;
    @FXML
    private RadioButton loginUserRadioButton;

    @FXML
    private RadioButton loginAdminRadioButton;

    @FXML
    private RadioButton loginAuthorRadioButton;

    @FXML
    private Label loginUnUserButton;

    @FXML
    private Button loginLoginButton;

    @FXML
    private TextField loginPasswordTextField;


    @FXML
    private Button loginSignUpButton;

    @FXML
    private TextField loginUsernameTextField;

    @FXML
    private AnchorPane loginAnchorPane;

    @FXML
    public void initialize(){

        ToggleGroup tg = new ToggleGroup();
        loginUserRadioButton.setToggleGroup(tg);
        loginAuthorRadioButton.setToggleGroup(tg);
        loginAdminRadioButton.setToggleGroup(tg);
        loginUserRadioButton.setSelected(true);
    }

    @FXML
    void loginLoginButtonClick(MouseEvent event) throws IOException {
        if(loginUsernameTextField.getText().isEmpty() || loginPasswordTextField.getText().isEmpty()) {

            String msg = "invalid values";
            Logger.error("Login clicked: " + msg);
            DialogManager.getInstance().createErrorAlert(loginAnchorPane, msg);
            return;
        }

        login();
    }

    @FXML
    void loginLoginButtonIn(MouseEvent event){
        Logger.info("login button pressed");
        loginLoginButton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #4CAF50;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: black;"
        );
    }

    @FXML
    void loginLoginButtonOut(MouseEvent event){
        Logger.info("login buttod released");
        loginLoginButton.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-border-color:  transparent;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: white;"
        );
    }

    @FXML
    void loginSignUpButtonIn(MouseEvent event){
        Logger.info("sugnup butto pressed");
        loginSignUpButton.setStyle(
                "-fx-background-color:  white;" +
                        "-fx-border-color:  #f4511e;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: black;"
        );
    }

    @FXML
    void loginSgnUpButtonOut(MouseEvent event){
        Logger.info("login button released");
        loginSignUpButton.setStyle(
                "-fx-background-color:  #f4511e;" +
                        "-fx-border-color:  transparent;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: white;"
        );
    }


    @FXML
    void loginSignUpButtonClick(MouseEvent event) throws IOException {
        Logger.info("SignUp clicked");
        StageManager.showPage(ViewNavigator.SIGNUP.getPage());

    }

    @FXML
    void userRadioButtonClick(){

        loginUsernameLabel.setText("Username");
    }

    @FXML
    void authorRadioButtonClick(){
        loginUsernameLabel.setText("Author Name");
    }

    @FXML
    void adminRadioButtonClick(){
        loginUsernameLabel.setText("Admin Name");
    }

    @FXML
    void unUserButtonClick() throws IOException {
        Logger.info("Sign in as unregistered user");
        MyPodcastDB.getInstance().setSession("","Unregistered");
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }

    @FXML
    void unUserButtonIn(){
        loginUnUserButton.setStyle("-fx-background-color: white; -fx-text-fill: grey; -fx-background-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void unUserButtonOut(){
        loginUnUserButton.setStyle("-fx-background-color: grey; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;");
    }

    @FXML
    void onKeyPressed(KeyEvent event) throws IOException {
        if(!event.getCode().equals(KeyCode.ENTER))
            return;
        if(loginUsernameTextField.getText().isEmpty() || loginPasswordTextField.getText().isEmpty())
            return;

        Logger.info("enter pressed");
        login();



    }

    void login() throws IOException {
        String actorName = loginUsernameTextField.getText();
        String password = loginPasswordTextField.getText();

        if(loginUserRadioButton.isSelected()) {
            Logger.info("User actor selected");
            String actorType = "User";
            User user = new User();
            user.setUsername(actorName);
            user.setPassword(password);
            //user = (User)simActorService(actorName, password, actorType);
            UserService service = new UserService();
            if(service.getUserLogin(user))
                MyPodcastDB.getInstance().setSession(user, actorType);
            else{
                String msg = "user not registered";
                Logger.error(msg);
                DialogManager.getInstance().createErrorAlert(loginAnchorPane, msg);
                return;
            }
        }
        else if(loginAuthorRadioButton.isSelected()) {
            Logger.info("Author actor selected");
            String actorType = "Author";
            Author author = new Author();
            author.setName(actorName);
            author.setPassword(password);
            //author = (Author)simActorService(actorName, password, actorType);
            AuthorService service = new AuthorService();
            if(service.getAuthorLogin(author)){
                MyPodcastDB.getInstance().setSession(author, actorType);
            }else{
                Logger.error("author not registered");
                DialogManager.getInstance().createErrorAlert(loginAnchorPane,"author not registered");
                return;
            }
        }
        else if(loginAdminRadioButton.isSelected()){
            Logger.info("Admin actor selected");
            String actorType = "Admin";
            Admin admin = new Admin();
            admin.setName(actorName);
            admin.setPassword(password);
            //admin = (Admin)simActorService(actorName, password, actorType);
            AdminService service = new AdminService();
            if(service.getAdminLogin(admin))
                MyPodcastDB.getInstance().setSession(admin, actorType);
            else{
                Logger.error("admin not registered");
                DialogManager.getInstance().createErrorAlert(loginAnchorPane, "admin not registered");
                return;
            }
        }
        else {
            Logger.error("No actor selected");
            return;
        }

        String log = "Login clicked: (" + actorName + ", " + password +")";
        Logger.info(log);
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }

    Object simActorService(String actorname, String password, String actorType){

        if(actorType.equals("User")){
            User user = new User("af679", actorname, password, "userTestName", "userTestSurname", "userTest@example.com", "Italy", "/img/account_50px.png", "Horror", 34, "Male");
            return user;
        }
        else if(actorType.equals("Author")){
            Author author = new Author("1234",actorname, password, "authorTest@example.com", "/img/authorAnonymousPicture.png");
            return author;
        }
        else if(actorType.equals("Admin")){
            Admin admin = new Admin("sacr23", actorname, password, "adminTest@example.com");
            return admin;
        }
        else{
            Logger.error("Uncorrected actorType");
            return null;
        }

    }

    @FXML
    void testAddAdminClick(MouseEvent event) throws IOException {

        loginAnchorPane.setEffect(new BoxBlur(3, 3, 3));
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("AddAdmin.fxml"));

        DialogPane addAdminDialog = new DialogPane();
        addAdminDialog.setContent(fxmlLoader.load());

        Dialog<Triplet<String, String, String>> dialog = new Dialog<>();
        dialog.setDialogPane(addAdminDialog);
        dialog.setTitle("Add new Admin");
        dialog.initOwner(loginAnchorPane.getScene().getWindow());

        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageCache.getImageFromLocalPath("/img/browse_podcasts_64px.png"));
        dialog.showAndWait();
        loginAnchorPane.setEffect(null);

    }
}