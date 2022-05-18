package com.example.walletapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //todo:
        // 1. create profile in DB
        // 2. fetch data
        // 3. update data

        btn_back = findViewById(R.id.btn_profileBack);
        btn_back.setOnClickListener(click -> finish());
        if(savedInstanceState == null){
            if(findViewById(R.id.frame1) != null){
                ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance("Gosia", R.id.frame1);
                FragmentTransaction FT = getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame1, displayFragment, null);
                FT.commit();
            }
            if(findViewById(R.id.frame2) != null){
                ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance("gosia02@gmail.com", R.id.frame2);
                FragmentTransaction FT = getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame2, displayFragment, null);
                FT.commit();
            }
            if(findViewById(R.id.frame3) != null){
                ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance("+48 667-921-114", R.id.frame3);
                FragmentTransaction FT = getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame3, displayFragment, null);
                FT.commit();
            }
            if(findViewById(R.id.frame4) != null){
                ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance("49 1020 2892 2276 3005 0000 0000", R.id.frame4);
                FragmentTransaction FT = getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame4, displayFragment, null);
                FT.commit();
            }
        }
    }
}