package com.example.walletapp;

import com.example.walletapp.expense.Expense;

import java.util.ArrayList;

public abstract class UserData {
    public static String email = "";
    public static String name = "";
    public static String phone = "";
    public static String bank = "";
    public static String UID = "";
    public static ArrayList<Expense> expenses = new ArrayList<>();
}
