package com.example.walletapp;

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

        btn_add = findViewById(R.id.btn_newExpenseEXPENSES);
        btn_back = findViewById(R.id.btn_goBackEXPENSES);

        btn_add.setOnClickListener(l -> {
            //add expense
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