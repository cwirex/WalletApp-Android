package com.example.walletapp.groups;

public class GroupUser {
    String uid;
    String name;

    public GroupUser(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
