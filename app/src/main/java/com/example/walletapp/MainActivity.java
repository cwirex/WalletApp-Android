package com.example.walletapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btn_logout, btn_profile;
    private ProgressBar pgBar;
    private TextView pgBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_logout = findViewById(R.id.btn_logout);
        btn_profile = findViewById(R.id.btn_profile);
        pgBar = findViewById(R.id.pgMAIN);
        pgBackground = findViewById(R.id.pgBackgroundMAIN);
        auth = FirebaseAuth.getInstance();

        btn_logout.setOnClickListener(click -> {
            auth.signOut();
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            finish();
        });

        btn_profile.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        });

        FirebaseUser user = auth.getCurrentUser();
        String UID = user.getUid();
        if(!Objects.equals(Profile.UID, UID)) {
            Profile.UID = UID;
            getUserProfile("example"); //todo: UID
        }
        FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();

        //read from realtime db:
        firebaseDb.getReference()
                .child("Last user")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String value = snapshot.getValue(String.class);
//                        Toast.makeText(MainActivity.this, "UID: " + value, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        //save to realtime db:
//        firebaseDb.getReference()
//                .child("Last user")
//                .setValue(UID);

    }
    private void getUserProfile(String UID) {
        pgBar.setVisibility(View.VISIBLE);
        pgBackground.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("profiles")
                .document(UID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Profile.email = document.getString("email");
                            Profile.name = document.getString("name");
                            Profile.phone = document.getString("phone");
                            Profile.bank = document.getString("bank");
                        }
                    }
                    pgBar.setVisibility(View.GONE);
                    pgBackground.setVisibility(View.GONE);
                });
    }
}