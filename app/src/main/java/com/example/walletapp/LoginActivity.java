package com.example.walletapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login, btn_register;
    private TextInputEditText eEmail, ePass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_gotoRegister);
        eEmail = findViewById(R.id.et_emailLOGIN);
        ePass = findViewById(R.id.et_passwordLOGIN);
        auth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(click -> {
            String login = eEmail.getText().toString();
            String password = ePass.getText().toString();
            if(login.isEmpty() || password.isEmpty()){
                if(login.isEmpty()){
                    eEmail.setError("Field can't be empty");
                }
                if(password.isEmpty()){
                    ePass.setError("Field can't be empty");
                }
            } else {
                //TODO: LOG IN
                auth.signInWithEmailAndPassword(login, password)
                        .addOnSuccessListener(result -> {
//                                Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                        })
                        .addOnFailureListener(result -> Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_LONG).show());
            }
        });

        btn_register.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
//            finish();
        });
    }
}