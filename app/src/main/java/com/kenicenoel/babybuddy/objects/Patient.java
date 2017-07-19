package com.kenicenoel.babybuddy.objects;



public class Patient
{
    private int _id;
    private String givenName;
    private String familyName;
    private String dateOfBirth;
    private String sex;
    private String address1;
    private String address2;
    private String city;
    private String stateParish;
    private String country;



    public Patient(int _id, String givenName, String familyName, String dateOfBirth, String sex, String address1, String address2, String city, String stateParish, String country) {
        this._id = _id;
        this.givenName = givenName;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.stateParish = stateParish;
        this.country = country;
    }

    public Patient(String givenName, String familyName, String dateOfBirth, String sex, String address1, String city, String stateParish, String country)
    {
        this.givenName = givenName;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.address1 = address1;
        this.city = city;
        this.stateParish = stateParish;
        this.country = country;
    }

    public Patient(String givenName, String familyName, String dateOfBirth, String sex, String address1, String address2, String city, String stateParish, String country)
    {
        this.givenName = givenName;
        this.familyName = familyName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.stateParish = stateParish;
        this.country = country;
    }



    public Patient()
    {

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateParish() {
        return stateParish;
    }

    public void setStateParish(String stateParish) {
        this.stateParish = stateParish;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGivenName()
    {
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }
}
