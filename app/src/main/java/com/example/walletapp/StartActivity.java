package com.example.walletapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        Intent intent;
        if(user == null)   // user not signed in
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        else
            intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
        finish();
    }
}