package com.example.walletapp.groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.walletapp.R;

import java.util.ArrayList;

public class GroupExpenseDTOAdapter extends ArrayAdapter<GroupExpenseDTO> {
    public GroupExpenseDTOAdapter(Context context, ArrayList<GroupExpenseDTO> expenseArrayList) {
        super(context, 0, expenseArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        GroupExpenseDTO expense = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_row_items, parent, false);
        }
        TextView tvTitle = convertView.findViewById(R.id.text_title);
        TextView tvCost = convertView.findViewById(R.id.text_cost);

        tvTitle.setText(expense.title);
        tvCost.setText(expense.cost);

        return convertView;
    }
}
