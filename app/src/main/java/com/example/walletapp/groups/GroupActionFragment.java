package com.example.walletapp.groups;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.walletapp.DBS;
import com.example.walletapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;

public class GroupActionFragment extends Fragment {

    private static final String ARG_PARAM1 = "target";
    private static final String ARG_PARAM2 = "hint";
    TextInputLayout inputLayout;
    TextInputEditText editText;
    ImageView submit, cancel;
    private String target;
    private String hint;
    OnFragmentActionListener fragmentListener;

    public GroupActionFragment() {
        // Required empty public constructor
    }

    public interface OnFragmentActionListener{
        void notifyGroupAdded(String groupName);
        void notifyUserFound(GroupUser groupUser);
    }

    public static GroupActionFragment newInstance(String target, String hint) {
        GroupActionFragment fragment = new GroupActionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, target);
        args.putString(ARG_PARAM2, hint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            target = getArguments().getString(ARG_PARAM1);
            hint = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_action, container, false);

        submit = view.findViewById(R.id.btn_submitGROUP);
        cancel = view.findViewById(R.id.btn_cancelGROUP);
        editText = view.findViewById(R.id.et_inputGROUP);
        inputLayout = view.findViewById(R.id.et_inputGROUPparent);

        inputLayout.setHint(hint);

        //todo: use smaller icons
        if (target.equals("group")) {       // Fragment -> NewGroupFragment
            submit.setOnClickListener(l -> {
                String str = editText.getText().toString();
                if (str.isEmpty()) {
                    editText.setError("Enter name for a group");
                } else {                                         // group_name is valid
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(DBS.Groups)
                            .document(str)
                            .get()
                            .addOnCompleteListener(task -> {        // check if groups isn't already defined
                                if (task.getResult().exists()) {    // group name take, don't create it.
                                    editText.setError("Group name must be unique!");
                                    Toast.makeText(getContext(), "Name already taken", Toast.LENGTH_SHORT).show();
                                } else {                                // new group name, so can be created
                                    HashMap<String, Object> dataset = new HashMap<>();
                                    dataset.put(DBS.GROUPS.isnew, true);
                                    dataset.put(DBS.GROUPS.owner, FirebaseAuth.getInstance().getUid());
                                    db.collection(DBS.Groups)
                                            .document(str)
                                            .set(dataset)
                                            .addOnSuccessListener(t -> {            // Group created
                                                Toast.makeText(getContext(), "Created new group!", Toast.LENGTH_SHORT).show();
                                                fragmentListener.notifyGroupAdded(str);
                                                deleteFragment();
                                            })
                                            .addOnFailureListener(f -> {            // error
                                                Toast.makeText(getContext(), f.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
                            });
                }
            });
        } else if (target.equals("user")) {     // Fragment -> AddUserFragment
            submit.setOnClickListener(l -> {
                String strVal = editText.getText().toString();
                if (strVal.isEmpty()) {
                    editText.setError("Enter user email");
                } else {                                                // user_email is valid
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(DBS.Users)
                            .get()
                            .addOnCompleteListener(task -> {            // look for a user
                                boolean found = false;
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.getString(DBS.USERS.email).equals(strVal)) {
                                        found = true;
                                        String tUID = doc.getId();
                                        String tName = doc.getString(DBS.USERS.name);
                                        fragmentListener.notifyUserFound(new GroupUser(tUID, tName));    // notify parent: user found
                                        deleteFragment();
                                    }
                                }
                                if (!found) {               // notify:  user_email doesn't exist
                                    editText.setError("Enter existing user's email");
                                    Toast.makeText(getContext(), "UserData doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }

        cancel.setOnClickListener(c -> {
            deleteFragment();
        });

        return view;
    }

    private void deleteFragment() {
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try{
            fragmentListener = (OnFragmentActionListener) activity;
        } catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "... override first!");
        }
    }
}