package com.example.walletapp;

import java.util.Map;

public class Expense {
    public String title;
    public String cost;
    public String category;
    public String description;
    public String date;
    public String id;

    public Expense(String title, String cost, String category, String description) {
        this.title = title;
        this.cost = cost;
        this.category = category;
        this.description = description;
    }

    public Expense(Map<String, Object> data, String id) {
        this.title = (String) data.getOrDefault("title", "");
        this.cost = (String) data.getOrDefault("cost", "");
        this.category = (String) data.getOrDefault("category", "");
        this.description = (String) data.getOrDefault("desc", "");
        this.date = (String) data.getOrDefault("date", "");
        this.id = id;
    }
}
