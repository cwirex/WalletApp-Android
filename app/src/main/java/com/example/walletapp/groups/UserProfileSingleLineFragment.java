package com.example.walletapp.groups;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walletapp.R;

public class UserProfileSingleLineFragment extends Fragment {

    // Factory method parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param4";
    private String strValue;
    private boolean canCopy;

    private ImageView btn_copy;
    private TextView textView;


    public UserProfileSingleLineFragment() {
        // Required empty public constructor
    }

    public static UserProfileSingleLineFragment newInstance(String param1, Boolean param2) {
        UserProfileSingleLineFragment fragment = new UserProfileSingleLineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strValue = getArguments().getString(ARG_PARAM1);
            canCopy = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_single_line, container, false);

        btn_copy = view.findViewById(R.id.ic_groupUserIcon);
        textView = view.findViewById(R.id.tv_groupUserItem);
        textView.setText(strValue);

        if(!canCopy || strValue.isEmpty())
            btn_copy.setVisibility(View.INVISIBLE);

        btn_copy.setOnClickListener(c -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied", strValue);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Copied " + strValue, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}