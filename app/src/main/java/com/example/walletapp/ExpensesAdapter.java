package com.example.walletapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpensesAdapter extends ArrayAdapter<Expense> {
    public ExpensesAdapter(Context context, ArrayList<Expense> expenseArrayList) {
        super(context, 0, expenseArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Expense expense = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_items, parent, false);
        }
        TextView tvTitle = convertView.findViewById(R.id.text_title);
        TextView tvCost = convertView.findViewById(R.id.text_cost);
        String cost = expense.cost;

        tvTitle.setText(expense.title);
        tvCost.setText(cost);
//        if (expense.category.equals("income")) {
//            tvCost.setTextColor(getContext().getResources().getColor(R.color.cost_green));
//        }
        return convertView;
    }
}
