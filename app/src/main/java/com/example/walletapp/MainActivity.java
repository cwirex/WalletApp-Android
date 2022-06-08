package com.example.walletapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walletapp.auth.LoginActivity;
import com.example.walletapp.auth.UserData;
import com.example.walletapp.expense.ExpensesActivity;
import com.example.walletapp.groups.GroupsActivity;
import com.example.walletapp.profile.ProfileActivity;
import com.example.walletapp.summary.SummaryActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btn_logout, btn_profile, btn_expenses, btn_summary, btn_groups;
    private ProgressBar pgBar;
    private TextView pgBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        btn_logout = findViewById(R.id.btn_logout);
        btn_profile = findViewById(R.id.btn_profile);
        btn_expenses = findViewById(R.id.btn_expense);
        btn_summary = findViewById(R.id.btn_summary);
        btn_groups = findViewById(R.id.btn_groups);

        pgBar = findViewById(R.id.pgMAIN);
        pgBackground = findViewById(R.id.pgBackgroundMAIN);
        auth = FirebaseAuth.getInstance();

        btn_logout.setOnClickListener(click -> {
            auth.signOut();
            UserData.UID = "";
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btn_profile.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        });

        btn_expenses.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), ExpensesActivity.class);
            startActivity(intent);
        });

        btn_summary.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
            startActivity(intent);
        });

        btn_groups.setOnClickListener(click ->
                startActivity(new Intent(getApplicationContext(), GroupsActivity.class)));

        /*
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String UID = user.getUid();
        if (!UserData.UID.equals(UID)) {
            UserData.UID = UID;
            getUserData(user.getUid());
        }
        */
    }

/*
    private void getUserData(String UID) {
        pgBar.setVisibility(View.VISIBLE);
        pgBackground.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(DBS.Users)
                .document(UID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            try {
                                UserData.email = document.getString(DBS.USERS.email);
                                UserData.name = document.getString(DBS.USERS.name);
                                UserData.phone = document.getString(DBS.USERS.phone);
                                UserData.bank = document.getString(DBS.USERS.bank);
                            } catch (RuntimeException exception) {
                                Log.e(TAG, "getUserProfile() caused: " + exception.getCause().toString());
                            }
                        }
                    }
                    getUserExpenses(UID);
                });
    }

    private void getUserExpenses(String UID) {
        UserData.expenses.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(DBS.Users)
                .document(UID)
                .collection(DBS.Expenses)
                .orderBy(DBS.EXPENSES.title)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        UserData.expenses.add(new Expense(doc.getData(), doc.getReference().getId()));
                    }
                    pgBar.setVisibility(View.GONE);
                    pgBackground.setVisibility(View.GONE);
                });
    }
    */
}