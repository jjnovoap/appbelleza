package com.example.appbella.Model;

public class User {

    private String fbid,name,lastname,address,userPhone,numberid;

    public User() {
    }


    public User(String fbid, String name, String lastname, String address, String userPhone, String numberid) {
        this.fbid = fbid;
        this.name = name;
        this.lastname = lastname;
        this.address = address;
        this.userPhone = userPhone;
        this.numberid = numberid;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getNumberid() {
        return numberid;
    }

    public void setNumberid(String numberid) {
        this.numberid = numberid;
    }

}
