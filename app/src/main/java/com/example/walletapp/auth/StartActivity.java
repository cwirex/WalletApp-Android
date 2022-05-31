package com.example.walletapp.auth;

import static android.content.ContentValues.TAG;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.walletapp.DAO;
import com.example.walletapp.expense.Expense;
import com.example.walletapp.MainActivity;
import com.example.walletapp.R;
import com.example.walletapp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class StartActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        progressBar = findViewById(R.id.progressStart);

        animator = ObjectAnimator.ofInt(progressBar, "progress", 10000);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final Handler handler = new Handler(Looper.getMainLooper());

        if (user == null) {
            Intent intent;
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (!User.UID.equals(user.getUid())) {
                handler.postDelayed(() -> {
                    animator.setDuration(2000);
                    animator.setInterpolator(new DecelerateInterpolator(1.5f));
                    animator.start();
                }, 300);
                handler.postDelayed(() -> {
                    Intent intent;
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }, 2300);
                User.UID = user.getUid();
                getUserData(user.getUid());
                getUserExpenses(user.getUid());
            } else {
                Intent intent;
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void getUserData(String UID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(DAO.Users)
                .document(UID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            try {
                                User.email = document.getString(DAO.USERS.email);
                                User.name = document.getString(DAO.USERS.name);
                                User.phone = document.getString(DAO.USERS.phone);
                                User.bank = document.getString(DAO.USERS.bank);
                            } catch (RuntimeException exception) {
                                Log.e(TAG, "getUserProfile() caused: " + exception.getCause().toString());
                            }
                        }
                    }
                });
    }

    private void getUserExpenses(String UID) {
        User.expenses.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(DAO.Users)
                .document(UID)
                .collection(DAO.Expenses)
                .orderBy(DAO.EXPENSES.title)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        User.expenses.add(new Expense(doc.getData(), doc.getReference().getId()));
                    }
                });
    }
}