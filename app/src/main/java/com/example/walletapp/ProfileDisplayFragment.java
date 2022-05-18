package com.example.walletapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileDisplayFragment extends Fragment {

    // Factory method parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String strValue;
    private Integer containerViewId;

    private ImageView btn_edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_display, container, false);

        btn_edit = view.findViewById(R.id.ic_edit_profile);
        btn_edit.setOnClickListener(click -> {          // switch to editFragment when clicked
            ProfileEditFragment editFragment = ProfileEditFragment.newInstance(strValue, containerViewId);
            FragmentTransaction FT = getParentFragmentManager().beginTransaction()
                    .replace(containerViewId, editFragment);
            FT.commit();
        });
        TextView textView = view.findViewById(R.id.tv_profile);
        textView.setText(strValue);
        return view;
    }

    public ProfileDisplayFragment() {
        // Required empty public constructor
    }

    public static ProfileDisplayFragment newInstance(String param1, Integer param2) {
        ProfileDisplayFragment fragment = new ProfileDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strValue = getArguments().getString(ARG_PARAM1);
            containerViewId = getArguments().getInt(ARG_PARAM2);
        }
    }
}