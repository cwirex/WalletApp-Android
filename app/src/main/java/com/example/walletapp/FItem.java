package com.example.walletapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FItem extends Fragment {

    private static final String ARG_TITLE = "param_TITLE";
    private static final String ARG_COST = "param_COST";
    private static final String ARG_DESC = "param_DESC";
    private static final String ARG_DATE = "param_DATE";
    private static final String ARG_CATEGORY = "param_CATEGORY";
    private static final String ARG_EID = "param_EID";
    TextView tvdate, tvtitle, tvcategory, tvcost, tvdesc;
    ImageView icBack, icEdit;
    private String title, category, cost, desc, date, eid;

    public FItem() {
        // Required empty public constructor
    }

    public static FItem newInstance(String param_TITLE, String param_COST, String param_DESC, String param_DATE, String param_CATEGORY, String param_EID) {
        FItem fragment = new FItem();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, param_TITLE);
        args.putString(ARG_COST, param_COST);
        args.putString(ARG_DESC, param_DESC);
        args.putString(ARG_DATE, param_DATE);
        args.putString(ARG_CATEGORY, param_CATEGORY);
        args.putString(ARG_EID, param_EID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            cost = getArguments().getString(ARG_COST);
            desc = getArguments().getString(ARG_DESC);
            date = getArguments().getString(ARG_DATE);
            category = getArguments().getString(ARG_CATEGORY);
            eid = getArguments().getString(ARG_EID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_f_item, container, false);

        icBack = v.findViewById(R.id.fitem_back);
        icEdit = v.findViewById(R.id.fitem_edit);

        tvtitle = v.findViewById(R.id.fitem_title);
        tvcategory = v.findViewById(R.id.fitem_category);
        tvcost = v.findViewById(R.id.fitem_cost);
        tvdesc = v.findViewById(R.id.fitem_description);
        tvdate = v.findViewById(R.id.fitem_date);

        tvtitle.setText(title);
        tvcategory.setText(category);
        tvcost.setText(cost);
        tvdesc.setText(desc);
        tvdate.setText(date);

        icBack.setOnClickListener(l -> {
            FListItems fragment = new FListItems();
            FragmentTransaction FT = getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameExpenses, fragment);
            FT.commit();
        });

        icEdit.setOnClickListener(l -> {
            //todo: edit (pass eid)
        });

        return v;
    }
}