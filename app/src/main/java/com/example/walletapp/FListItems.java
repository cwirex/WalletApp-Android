package com.example.walletapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class FListItems extends Fragment {


    ExpensesAdapter adapter;
    ListView listView;


    public FListItems() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                Toast.makeText(getContext(), expense.title, Toast.LENGTH_SHORT).show();
            }
        });
//// SAVE:
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                db.collection("users")
//                        .document(UID)
//                        .collection("expenses")
//                        .add(e)
//                        .addOnSuccessListener(l -> Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show());
        return v;
    }
}