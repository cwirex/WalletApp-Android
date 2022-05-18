package com.example.walletapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


public class ProfileEditFragment extends Fragment {

    // Factory method parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String strValue;
    private Integer containerViewId;

    private ImageView btn_save;
    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        btn_save = view.findViewById(R.id.ic_save_profile);
        editText = view.findViewById(R.id.et_profile);
        editText.setText(strValue);

        btn_save.setOnClickListener(click -> {          // switch to displayFragment when clicked
            String s1 = editText.getText().toString();
            // Todo: Save changes to db
            ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance(s1, containerViewId);
            FragmentTransaction FT = getParentFragmentManager().beginTransaction()
                    .replace(containerViewId, displayFragment);
            FT.commit();
        });

        return view;
    }

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    public static ProfileEditFragment newInstance(String param1, Integer param2) {
        ProfileEditFragment fragment = new ProfileEditFragment();
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