package com.example.walletapp.profile;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.walletapp.DBS;
import com.example.walletapp.R;
import com.example.walletapp.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileEditFragment extends Fragment {

    // Factory method parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private String strValue, strContext;
    private Integer containerViewId;
    private boolean editable;

    private ImageView btn_save;
    private EditText editText;

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    public static ProfileEditFragment newInstance(String param1, Integer param2, String param3, Boolean param4) {
        ProfileEditFragment fragment = new ProfileEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putBoolean(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        btn_save = view.findViewById(R.id.ic_save_profile);
        editText = view.findViewById(R.id.et_profile);
        editText.setText(strValue);

        btn_save.setOnClickListener(click -> {          // switch to displayFragment when clicked
            String s1 = editText.getText().toString();
            if (!s1.equals(strValue)) { // update profile and DB
                updateProfile(s1, strContext);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String UID = FirebaseAuth.getInstance().getUid();
                assert UID != null;
                db.collection(DBS.Users)
                        .document(UID)
                        .update(strContext, s1)
                        .addOnFailureListener(l -> Log.w(TAG, "DB update failed"))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful())
                                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                            ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance(s1, containerViewId, strContext, editable);
                            FragmentTransaction FT = getParentFragmentManager().beginTransaction()
                                    .replace(containerViewId, displayFragment);
                            FT.commit();
                        })
                        .addOnFailureListener(f -> Toast.makeText(getContext(), f.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                ProfileDisplayFragment displayFragment = ProfileDisplayFragment.newInstance(s1, containerViewId, strContext, editable);
                FragmentTransaction FT = getParentFragmentManager().beginTransaction()
                        .replace(containerViewId, displayFragment);
                FT.commit();
            }
        });

        return view;
    }

    private void updateProfile(String newValue, String strContext) {
        switch (strContext) {
            case DBS.USERS.name:
                UserData.name = newValue;
                break;
            case DBS.USERS.email:
                UserData.email = newValue;
                break;
            case DBS.USERS.phone:
                UserData.phone = newValue;
                break;
            case DBS.USERS.bank:
                UserData.bank = newValue;
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strValue = getArguments().getString(ARG_PARAM1);
            containerViewId = getArguments().getInt(ARG_PARAM2);
            strContext = getArguments().getString(ARG_PARAM3);
            editable = getArguments().getBoolean(ARG_PARAM4);
        }
    }
}