package com.kenicenoel.babybuddy.objects;

/**
 * Created by Software Developer on 6/30/2017.
 */

public class User
{
    private String givenName;
    private String familyName;
    private String userName;
    private String password;
    private String sex;
    private String country;

    public User(String givenName, String familyName, String userName, String password, String sex, String country) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.userName = userName;
        this.password = password;
        this.sex = sex;
        this.country = country;
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
}
