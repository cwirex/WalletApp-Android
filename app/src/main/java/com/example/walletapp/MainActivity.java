package com.example.walletapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_logout = findViewById(R.id.btn_logout);
        auth = FirebaseAuth.getInstance();

        btn_logout.setOnClickListener(click -> {
            auth.signOut();
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            finish();
        });

        FirebaseUser user = auth.getCurrentUser();
        if(user == null) {
            Toast.makeText(this, "Unexpected null user", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String UID = user.getUid();
        FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
        //read from realtime db:
        firebaseDb.getReference()
                .child("Last user")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);
                        Toast.makeText(MainActivity.this, "UID: " + value, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        //save to realtime db:
        firebaseDb.getReference()
                .child("Last user")
                .setValue(UID);

    }
}