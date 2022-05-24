package com.example.walletapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class CreateExpenseActivity extends AppCompatActivity {

    static final ArrayList<String> categories = new ArrayList<>(Arrays.asList("Bill", "Food", "Gas", "Holidays"));
    DatePickerDialog datePickerDialog;
    private Button btn_create, btn_back, btn_date;
    private TextInputEditText eTitle, eCost, eDesc;
    private AutoCompleteTextView spinnerCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);
        getSupportActionBar().hide();

        btn_create = findViewById(R.id.btn_addCREATE);
        btn_back = findViewById(R.id.btn_goBackCREATE);
        btn_date = findViewById(R.id.btn_datePicker);
        eTitle = findViewById(R.id.et_titleCREATE);
        eCost = findViewById(R.id.et_costCREATE);
        eDesc = findViewById(R.id.et_descCREATE);
        spinnerCat = findViewById(R.id.spinner_category);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_category, categories);
        spinnerCat.setText(categories.get(0));
        spinnerCat.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btn_date.setText(getTodaysDate());
        initDatePicker();
        btn_date.setOnClickListener(this::openDatePicker);

        btn_back.setOnClickListener(l -> {
            Intent intent = new Intent(this, ExpensesActivity.class);
            startActivity(intent);
            finish();
        });

        btn_create.setOnClickListener(l -> {
            String title = eTitle.getText().toString();
            String cost = eCost.getText().toString();
            String desc = eDesc.getText().toString();
            String cat = spinnerCat.getText().toString();
            String date = btn_date.getText().toString();
            boolean emptyFields = false;
            if (title.isEmpty()) {
                eTitle.setError("Fill Title first!");
                emptyFields = true;
            }
            if (cost.isEmpty()) {
                eCost.setError("Fill Cost first!");
                emptyFields = true;
            } else{
                if(cost.contains(",")){
                    StringBuilder newCost = new StringBuilder(cost);
                    newCost.setCharAt(cost.indexOf(","), '.');
                    cost = newCost.toString();
                }
                try{
                    cost = String.format(Locale.getDefault(),"%.2f", Double.valueOf(cost));
                } catch (Exception e){
                    eCost.setError("Invalid value!");
                    emptyFields = true;
                }
            }
            if (!emptyFields) {
                eTitle.setText("");
                eCost.setText("");
                eDesc.setText("");
                btn_date.setText(getTodaysDate());

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("title", title);
                hashMap.put("cost", cost);
                hashMap.put("category", cat);
                hashMap.put("desc", desc);
                hashMap.put("date", date);

                String UID = FirebaseAuth.getInstance().getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("users")
                        .document(UID)
                        .collection("expenses")
                        .add(hashMap)
                        .addOnSuccessListener(s -> {
                            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
//                            addExpenseFromDB(s.getId());
                            User.expenses.add(new Expense(hashMap, s.getId()));
                            Intent intent = new Intent(this, ExpensesActivity.class);
                            startActivity(intent);
                            finish();
                        });
            }
        });
    }

    private void addExpenseFromDB(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("expenses")
                .document(id)
                .get()
                .addOnSuccessListener(doc -> User.expenses.add(new Expense(doc.getData(), doc.getReference().getId())));
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY) {
                String date = makeDateString(YEAR, MONTH + 1, DAY);
                btn_date.setText(date);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, onDateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());        // MAX_DATE
    }

    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(year, month + 1, day);
    }

    private String makeDateString(int year, int month, int day) {
        return Month.of(month).toString().substring(0, 3) + " " + day + " " + year;
    }

    private void openDatePicker(View view) {
        datePickerDialog.show();
    }
}