package com.example.walletapp.groups;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.walletapp.DAO;
import com.example.walletapp.DBS;
import com.example.walletapp.R;
import com.example.walletapp.User;

public class UserProfileFragment extends Fragment {

    ImageView btn_back;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String uid;
    private String gid;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(String UserId, String GroupId) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, UserId);
        args.putString(ARG_PARAM2, GroupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(ARG_PARAM1);
            gid = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        btn_back = view.findViewById(R.id.btn_backGroupUserProfile);
        btn_back.setOnClickListener(click -> finish());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        User user = DAO.getInstance().getUserFromId(uid);
        Integer[] f_ids = {R.id.frameNameGroupUserProfile, R.id.frameEmailGroupUserProfile, R.id.framePhoneGroupUserProfile, R.id.frameBankGroupUserProfile};
        String[] f_names = {DBS.USERS.name, DBS.USERS.email, DBS.USERS.phone, DBS.USERS.bank};
        String[] f_vals = {user.userData.name, user.userData.email, user.userData.phone, user.userData.bank};
        Boolean[] f_bool = {false, true, true, true};

        for (int i = 0; i < f_ids.length; i++) {
            if (view.findViewById(f_ids[i]) != null) {
                UserProfileSingleLineFragment fragment = UserProfileSingleLineFragment.newInstance(f_vals[i], f_bool[i]);
                FragmentTransaction FT = getChildFragmentManager()
                        .beginTransaction()
                        .replace(f_ids[i], fragment);
                FT.commit();
            }
        }
    }

    private void finish() {
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }
}
