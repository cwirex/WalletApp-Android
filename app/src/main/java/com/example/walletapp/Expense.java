package com.example.walletapp;

import java.util.Map;

public class Expense {
    public String title;
    public String cost;
    public String category;
    public String description;
    public String date;
    public String id;

    public Expense(Map<String, Object> data, String id) {
        this.title = (String) data.getOrDefault("title", "");
        this.cost = (String) data.getOrDefault("cost", "");
        this.category = (String) data.getOrDefault("category", "");
        this.description = (String) data.getOrDefault("desc", "");
        this.date = (String) data.getOrDefault("date", "");
        this.id = id;
    }
}
