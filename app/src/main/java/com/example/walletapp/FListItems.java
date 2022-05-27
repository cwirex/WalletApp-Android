package com.example.walletapp;

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

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class FListItems extends Fragment {

    private static final String ARG = "param";
    private String deletedExpenseId = "";
    ExpensesAdapter adapter;
    ListView listView;
    MaterialButtonToggleGroup toggleGroup;
    ImageView ic_toggle_up, ic_toggle_down;
    private boolean sortAscending;

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
            User.expenses.removeIf(expense -> expense.id.equals(deletedExpenseId));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("expenses")
                    .document(deletedExpenseId)
                    .delete()
                    .addOnSuccessListener(l -> {
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    });
        }
        User.expenses.sort(Comparator.comparing(expense -> expense.title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_f_list_items, container, false);

        ic_toggle_up = v.findViewById(R.id.ic_toggle_rising);
        ic_toggle_down = v.findViewById(R.id.ic_toggle_falling);
        toggleGroup = v.findViewById(R.id.toggleGroup);

        adapter = new ExpensesAdapter(getContext(), User.expenses);
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
                if(isChecked){
                    if(checkedId == R.id.toggle_name){
                        sortItems("name");
                    } else if(checkedId == R.id.toggle_price){
                        sortItems("price");
                    } else if(checkedId == R.id.toggle_date){
                        sortItems("date");
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        ic_toggle_up.setOnClickListener(l -> {
            if(sortAscending) {
                ic_toggle_up.setVisibility(View.GONE);
                ic_toggle_down.setVisibility(View.VISIBLE);
                Collections.reverse(User.expenses);
                adapter.notifyDataSetChanged();
                sortAscending = false;
            }
        });

        ic_toggle_down.setOnClickListener(l -> {
            if(!sortAscending){
                ic_toggle_down.setVisibility(View.GONE);
                ic_toggle_up.setVisibility(View.VISIBLE);
                Collections.reverse(User.expenses);
                adapter.notifyDataSetChanged();
                sortAscending = true;
            }
        });

        return v;
    }

    public void sortItems(String sortBy){
        if(sortAscending){
            switch (sortBy) {
                case "name":
                    User.expenses.sort(compareByName);
                    break;
                case "price":
                    User.expenses.sort(compareByPrice);
                    break;
                case "date":
                    User.expenses.sort(compareByDate);
                    break;
            }
        } else{
            switch (sortBy) {
                case "name":
                    User.expenses.sort(compareByName.reversed());
                    break;
                case "price":
                    User.expenses.sort(compareByPrice.reversed());
                    break;
                case "date":
                    User.expenses.sort(compareByDate.reversed());
                    break;
            }
        }
    }

    private final Comparator<Expense> compareByName = Comparator.comparing((Expense o) -> o.title);
    private final Comparator<Expense> compareByPrice = Comparator.comparing((Expense o) -> Double.valueOf(o.cost));
    private final Comparator<Expense> compareByDate = Comparator.comparing((Expense o) -> o.title); //todo
}