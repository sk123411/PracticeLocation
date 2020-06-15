package com.practicel.locationpractice;

import java.util.HashMap;

public class User {
    private String uid,email;
    private HashMap<String,User> accepList;


    public User(){

    }

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;
        accepList = new HashMap<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
