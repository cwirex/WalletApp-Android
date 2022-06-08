package com.example.walletapp;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class DBS {

    public static final String Users = "users";
    public static final String Expenses = "expenses";
    public static final String Groups = "groups";

    public abstract static class USERS {
        public static final String email = "email";
        public static final String name = "name";
        public static final String bank = "bank";
        public static final String phone = "phone";
        public static final String groupid = "groupId";
    }

    public abstract static class EXPENSES {
        public static final String category = "category";
        public static final String cost = "cost";
        public static final String date_str = "date";
        public static final String datetime = "datetime";
        public static final String description = "desc";
        public static final String title = "title";
    }

    public abstract static class CATEGORIES {
        public static final String bill = "Bill";
        public static final String food = "Food";
        public static final String gas = "Gas";
        public static final String holidays = "Holidays";
    }

    public abstract static class GROUPS {
        public static final String users = "users";
        public static final String isnew = "new_group";
        public static final String owner = "owner";
        public static final String userid = "userId";
        public static final String Expenses = "expenses";
    }

    public static final ArrayList<String> categoriesList = new ArrayList<>(Arrays.asList("Bill", "Food", "Gas", "Holidays"));
    public static final ArrayList<String> splitMethodList = new ArrayList<>(Arrays.asList("Equal", "Other"));

}
