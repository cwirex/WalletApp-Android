package com.example.walletapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class ProfileActivity extends AppCompatActivity {

    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //todo:
        // 3. update data

        btn_back = findViewById(R.id.btn_profileBack);
        btn_back.setOnClickListener(click -> finish());
        if (savedInstanceState == null) {
            loadFrames();
        }
    }

    private void loadFrames() {
        if (findViewById(R.id.frameName) != null) {
            ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance(Profile.name, R.id.frameName);
            FragmentTransaction FT = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameName, displayFragment, null);
            FT.commit();
        }
        if (findViewById(R.id.frameEmail) != null) {
            ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance(Profile.email, R.id.frameEmail);
            FragmentTransaction FT = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameEmail, displayFragment, null);
            FT.commit();
        }
        if (findViewById(R.id.framePhone) != null) {
            ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance(Profile.phone, R.id.framePhone);
            FragmentTransaction FT = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.framePhone, displayFragment, null);
            FT.commit();
        }
        if (findViewById(R.id.frameBank) != null) {
            ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance(Profile.bank, R.id.frameBank);
            FragmentTransaction FT = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameBank, displayFragment, null);
            FT.commit();
        }
    }
}
