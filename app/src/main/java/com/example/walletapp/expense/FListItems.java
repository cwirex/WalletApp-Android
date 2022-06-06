package com.example.walletapp.expense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.walletapp.DBS;
import com.example.walletapp.R;
import com.example.walletapp.UserData;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.Comparator;


public class FListItems extends Fragment {

    private static final String ARG = "param";
    static boolean sortAscending;
    static String sortBy = "";
    private final Comparator<Expense> compareByName = Comparator.comparing((Expense o) -> o.title);
    private final Comparator<Expense> compareByPrice = Comparator.comparing((Expense o) -> Double.valueOf(o.cost));
    private final Comparator<Expense> compareByDate = Comparator.comparing((Expense o) -> o.dateTime);
    ExpensesAdapter adapter;
    ListView listView;
    MaterialButtonToggleGroup toggleGroup;
    ImageView ic_toggle_up, ic_toggle_down;
    private String deletedExpenseId = "";

    public FListItems() {
        // Required empty public constructor
    }

    public static FListItems newInstance(String deletedItemId) {
        FListItems fragment = new FListItems();
        Bundle args = new Bundle();
        args.putString(ARG, deletedItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deletedExpenseId = getArguments().getString(ARG);
            UserData.expenses.removeIf(expense -> expense.id.equals(deletedExpenseId));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(DBS.Users)
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("expenses")
                    .document(deletedExpenseId)
                    .delete()
                    .addOnSuccessListener(l -> {
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    });
        }
        if (sortBy.isEmpty())
            sortBy = DBS.EXPENSES.title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_f_list_items, container, false);

        ic_toggle_up = v.findViewById(R.id.ic_toggle_rising);
        ic_toggle_down = v.findViewById(R.id.ic_toggle_falling);
        toggleGroup = v.findViewById(R.id.toggleGroup);
        updateChecked();

        adapter = new ExpensesAdapter(getContext(), UserData.expenses);
        listView = v.findViewById(R.id.listExpenses);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Expense expense = (Expense) parent.getItemAtPosition(pos);
                FItem fragment = FItem.newInstance(expense.title, expense.cost, expense.description, expense.date, expense.category, expense.id);
                FragmentTransaction FT = getParentFragmentManager().beginTransaction()
                        .replace(R.id.frameExpenses, fragment);
                FT.commit();
            }
        });

        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.toggle_name) {
                        sortBy = DBS.EXPENSES.title;
                    } else if (checkedId == R.id.toggle_price) {
                        sortBy = DBS.EXPENSES.cost;
                    } else if (checkedId == R.id.toggle_date) {
                        sortBy = DBS.EXPENSES.datetime;
                    }
                    sortItems();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        ic_toggle_up.setOnClickListener(l -> {
            if (sortAscending) {
                ic_toggle_up.setVisibility(View.GONE);
                ic_toggle_down.setVisibility(View.VISIBLE);
                Collections.reverse(UserData.expenses);
                adapter.notifyDataSetChanged();
                sortAscending = false;
            }
        });

        ic_toggle_down.setOnClickListener(l -> {
            if (!sortAscending) {
                ic_toggle_down.setVisibility(View.GONE);
                ic_toggle_up.setVisibility(View.VISIBLE);
                Collections.reverse(UserData.expenses);
                adapter.notifyDataSetChanged();
                sortAscending = true;
            }
        });

        return v;
    }

    private void updateChecked() {
        sortItems();
        switch (sortBy) {
            case DBS.EXPENSES.title:
                toggleGroup.check(R.id.toggle_name);
                break;
            case DBS.EXPENSES.cost:
                toggleGroup.check(R.id.toggle_price);
                break;
            case DBS.EXPENSES.datetime:
                toggleGroup.check(R.id.toggle_date);
                break;
        }
        if (sortAscending)
            ic_toggle_up.setVisibility(View.VISIBLE);
        else
            ic_toggle_down.setVisibility(View.VISIBLE);
    }

    public void sortItems() {
        if (sortAscending) {
            switch (sortBy) {
                case DBS.EXPENSES.title:
                    UserData.expenses.sort(compareByName);
                    break;
                case DBS.EXPENSES.cost:
                    UserData.expenses.sort(compareByPrice);
                    break;
                case DBS.EXPENSES.datetime:
                    UserData.expenses.sort(compareByDate);
                    break;
            }
        } else {
            switch (sortBy) {
                case DBS.EXPENSES.title:
                    UserData.expenses.sort(compareByName.reversed());
                    break;
                case DBS.EXPENSES.cost:
                    UserData.expenses.sort(compareByPrice.reversed());
                    break;
                case DBS.EXPENSES.datetime:
                    UserData.expenses.sort(compareByDate.reversed());
                    break;
            }
        }
    }
}