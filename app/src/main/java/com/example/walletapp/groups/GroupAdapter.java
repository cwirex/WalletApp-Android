package com.example.walletapp.groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.walletapp.R;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter<Group> {
    public GroupAdapter(Context context, ArrayList<Group> list) {
        super(context, 0, list);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Group group = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_category, parent, false);
        }
        TextView tv = convertView.findViewById(R.id.tv_categoryName);
        String name = group.id;
        tv.setText(name);
        return convertView;
    }
}
