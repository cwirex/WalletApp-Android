package com.example.walletapp;

public class User {
    public String uid;
    public UserDTO userData;

    public User(String uid, UserDTO userData) {
        this.uid = uid;
        this.userData = userData;
    }
}
