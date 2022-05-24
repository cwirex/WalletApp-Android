package com.example.walletapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class FListItems extends Fragment {

    private static final String ARG = "param";
    ExpensesAdapter adapter;
    ListView listView;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_f_list_items, container, false);
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
        return v;
    }
}