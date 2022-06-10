package it.unipi.dii.lsmsdb.myPodcastDB.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class User {
    private String id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String country;
    private String picturePath;
    private String favouriteGenre;
    private Date dateOfBirth;
    private String gender;

    public User() {
    }

    public User(String username, String picturePath){
        this.username = username;
        this.picturePath = picturePath;
    }

    public User(String id, String username, String password, String name, String surname, String email, String country, String picturePath, String favouriteGenre, Date dateOfBirth, String gender) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.country = country;
        this.picturePath = picturePath;
        this.favouriteGenre = favouriteGenre;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public void copy(User user) {
        this.id = user.id;
        this.username = user.username;
        this.password = user.password;
        this.name = user.name;
        this.surname = user.surname;
        this.email = user.email;
        this.country = user.country;
        this.picturePath = user.picturePath;
        this.favouriteGenre = user.favouriteGenre;
        this.dateOfBirth = user.dateOfBirth;
        this.gender = user.gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getFavouriteGenre() {
        return favouriteGenre;
    }

    public void setFavouriteGenre(String favouriteGenre) {
        this.favouriteGenre = favouriteGenre;
    }

    public int getAge() {
        LocalDate birth = this.dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birth, LocalDate.now()).getYears();
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", favouriteGenre='" + favouriteGenre + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                '}';
    }
}
