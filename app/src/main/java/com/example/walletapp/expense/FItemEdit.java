package com.example.walletapp.expense;

import static com.example.walletapp.expense.CreateExpenseActivity.categories;
import static com.example.walletapp.expense.CreateExpenseActivity.myDateToLocalDateTime;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.walletapp.DAO;
import com.example.walletapp.R;
import com.example.walletapp.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class FItemEdit extends Fragment {

    private static final String ARG = "param";
    DatePickerDialog datePickerDialog;
    private String expenseId = "";
    private Expense expense;
    private TextInputEditText eTitle, eCost, eDesc;
    private AutoCompleteTextView spinnerCat;
    private Button btn_date;
    private ImageView btn_back, btn_save;

    public FItemEdit() {
        // Required empty public constructor
    }

    public static FItemEdit newInstance(String expenseId) {
        FItemEdit fragment = new FItemEdit();
        Bundle args = new Bundle();
        args.putString(ARG, expenseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            expenseId = getArguments().getString(ARG);
            for (Expense e : User.expenses)
                if (e.id.equals(expenseId))
                    expense = e;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_f_item_edit, container, false);

        eTitle = v.findViewById(R.id.fitemedit_et_titleEDIT);
        eCost = v.findViewById(R.id.fitemedit_et_costEDIT);
        eDesc = v.findViewById(R.id.fitemedit_et_descEDIT);
        spinnerCat = v.findViewById(R.id.fitemedit_spinner_categoryEDIT);
        btn_date = v.findViewById(R.id.fitemedit_btn_datePickerEDIT);
        btn_back = v.findViewById(R.id.fitemedit_back);
        btn_save = v.findViewById(R.id.fitemedit_save);

        eTitle.setText(expense.title);
        eCost.setText(expense.cost);
        eDesc.setText(expense.description);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_category, categories);
        spinnerCat.setText(expense.category);
        spinnerCat.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        btn_date.setText(expense.date);
        initDatePicker(myDateToLocalDateTime(expense.date));
        btn_date.setOnClickListener(this::openDatePicker);

        btn_back.setOnClickListener(l -> {
            FItem fragment = FItem.newInstance(expense.title, expense.cost, expense.description, expense.date, expense.category, expense.id);
            FragmentTransaction FT = getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameExpenses, fragment);
            FT.commit();
        });

        btn_save.setOnClickListener(l -> {
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
            } else {
                try {
                    cost = String.format(Locale.US,"%.2f", Double.parseDouble(cost.replace(',','.')));
                } catch (Exception e) {
                    eCost.setError("Invalid value!");
                    emptyFields = true;
                }
            }
            if (!emptyFields) {
                LocalDateTime dateTime = myDateToLocalDateTime(date);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(DAO.EXPENSES.title, title);
                hashMap.put(DAO.EXPENSES.cost, cost);
                hashMap.put(DAO.EXPENSES.category, cat);
                hashMap.put(DAO.EXPENSES.description, desc);
                hashMap.put(DAO.EXPENSES.date_str, date);
                hashMap.put(DAO.EXPENSES.datetime, dateTime);

                User.expenses.remove(expense);
                expense = new Expense(hashMap, expenseId);
                User.expenses.add(expense);

                String UID = FirebaseAuth.getInstance().getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection(DAO.Users)
                        .document(UID)
                        .collection(DAO.Expenses)
                        .document(expenseId)
                        .update(hashMap)
                        .addOnSuccessListener(s -> {
                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();

                            FItem fragment = FItem.newInstance(expense.title, expense.cost, expense.description, expense.date, expense.category, expense.id);
                            FragmentTransaction FT = getParentFragmentManager().beginTransaction()
                                    .replace(R.id.frameExpenses, fragment);
                            FT.commit();
                        });
            }
        });
        return v;
    }


    private void initDatePicker(LocalDateTime localDateTime) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY) {
                String date = makeDateString(YEAR, MONTH + 1, DAY);
                btn_date.setText(date);
            }
        };
        int year, month, day;
        if (localDateTime.equals(LocalDateTime.MIN)) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            year = localDateTime.getYear();
            month = localDateTime.getMonthValue() - 1;
            day = localDateTime.getDayOfMonth();
        }

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(), style, onDateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());        // MAX_DATE
    }

    private String makeDateString(int year, int month, int day) {
        return Month.of(month).toString().substring(0, 3) + " " + day + " " + year;
    }

    private void openDatePicker(View view) {
        datePickerDialog.show();
    }
}