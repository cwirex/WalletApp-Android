package com.example.walletapp.groups;

import java.util.ArrayList;

public class Group {
    String id;
    ArrayList<GroupUser> users;

    public Group(String id) {
        this.id = id;
        users = new ArrayList<>();
    }

    public void addUser(GroupUser user){
        if(!users.contains(user))
            users.add(user);
    }
}
