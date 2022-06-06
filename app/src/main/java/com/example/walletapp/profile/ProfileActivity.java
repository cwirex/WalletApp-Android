package com.example.walletapp.profile;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.walletapp.DBS;
import com.example.walletapp.UserData;
import com.example.walletapp.R;

public class ProfileActivity extends AppCompatActivity {

    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF018786")));

        btn_back = findViewById(R.id.btn_profileBack);
        btn_back.setOnClickListener(click -> finish());

        if (savedInstanceState == null) {
            loadFrames();
        }
    }

    private void loadFrames() {
        Integer[] f_ids = {R.id.frameEmail, R.id.frameName, R.id.framePhone, R.id.frameBank};
        String[] f_names = {DBS.USERS.email, DBS.USERS.name, DBS.USERS.phone, DBS.USERS.bank};
//        UserDTO currentUser = UserDAO.getInstance().getCurrentUser().getValue();      // new approach
//        String[] f_vals = {currentUser.email, currentUser.name, currentUser.phone, currentUser.bank};
        String[] f_vals = {UserData.email, UserData.name, UserData.phone, UserData.bank};
        Boolean[] f_bool = {false, true, true, true};

        for (int i = 0; i < f_ids.length; i++) {
            if (findViewById(f_ids[i]) != null) {
                ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance(f_vals[i], f_ids[i], f_names[i], f_bool[i]);
                FragmentTransaction FT = getSupportFragmentManager()
                        .beginTransaction()
                        .add(f_ids[i], displayFragment, null);
                FT.commit();
            }
        }
    }
}
