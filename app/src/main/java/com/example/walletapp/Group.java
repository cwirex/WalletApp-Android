package com.example.walletapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group {
    public boolean new_group;
    public String owner;
    private String id;
    public ArrayList<String> users;

    public Group() {

    }

    public Group(String id, String owner, ArrayList<String> users, boolean new_group) {
        this.id = id;
        this.new_group = new_group;
        this.owner = owner;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
