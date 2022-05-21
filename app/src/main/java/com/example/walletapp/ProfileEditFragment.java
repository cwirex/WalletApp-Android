package com.example.walletapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;


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
            if(!s1.equals(strValue)){ // update profile and DB
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                int id = containerViewId;
                if (id == R.id.frameName) {
                    Profile.name = s1;
                    db.collection("profiles")
                            .document("example")
                            .update("name", s1)
                            .addOnFailureListener(l -> Toast.makeText(getContext(), "Update failed.", Toast.LENGTH_SHORT).show());
                } else if (id == R.id.frameEmail) {
                    Profile.email = s1;
                    db.collection("profiles")
                            .document("example")
                            .update("email", s1)
                            .addOnFailureListener(l -> Toast.makeText(getContext(), "Update failed.", Toast.LENGTH_SHORT).show());
                } else if (id == R.id.framePhone) {
                    Profile.phone = s1;
                    db.collection("profiles")
                            .document("example")
                            .update("phone", s1);
                } else if (id == R.id.frameBank) {
                    Profile.bank = s1;
                    db.collection("profiles")
                            .document("example")
                            .update("bank", s1);
                }
            }
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