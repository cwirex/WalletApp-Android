package com.example.walletapp;

import java.util.HashMap;
import java.util.Map;

public class UserDTO {
    public String bank = "";
    public String email = "";
    public String name = "";
    public String phone = "";
    public Map<String, Object> groups = new HashMap<>();
    public Map<String, Object> expenses = new HashMap<>();
}
