package com.example.walletapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class ExpensesActivity extends AppCompatActivity {

    Button btn_add, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF6200EE")));

        btn_add = findViewById(R.id.btn_newExpenseEXPENSES);
        btn_back = findViewById(R.id.btn_goBackEXPENSES);

        btn_add.setOnClickListener(l -> {
            Intent intent = new Intent(this, CreateExpenseActivity.class);
            startActivity(intent);
            finish();
        });

        btn_back.setOnClickListener(l -> finish());

        if (savedInstanceState == null) {
            FListItems listItems = new FListItems();
            FragmentTransaction FT = getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameExpenses, listItems, null);
            FT.commit();
        }
    }
}