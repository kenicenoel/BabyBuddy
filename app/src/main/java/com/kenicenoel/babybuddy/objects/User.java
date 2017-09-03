package com.kenicenoel.babybuddy.objects;

/**
 * Created by Software Developer on 6/30/2017.
 */

public class User
{
    private String givenName;
    private String familyName;
    private String emailAddress;
    private String userName;
    private String password;
    private String sex;
    private String country;
    private String role;

    public User(String givenName, String familyName, String emailAddress, String userName, String password, String sex, String country, String role)
    {
        this.givenName = givenName;
        this.familyName = familyName;
        this.userName = userName;
        this.password = password;
        this.emailAddress = emailAddress;
        this.sex = sex;
        this.country = country;
        this.role = role;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

//    public void setPassword(String password)
//    {
//        this.password = password;
//    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
