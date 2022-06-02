package com.example.walletapp.groups;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletapp.DAO;
import com.example.walletapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupsActivity extends AppCompatActivity implements GroupActionFragment.OnFragmentActionListener, GroupUserAdapter.ItemClickListener {

    Button btn_addUser, btn_newGroup;
    FrameLayout frame;
    GroupAdapter groupAdapter;
    GroupUserAdapter userAdapter;
    ArrayList<Group> groups;
    AutoCompleteTextView spinner_group;
    private Group currentGroup;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        btn_addUser = findViewById(R.id.btn_adduserGROUPS);
        btn_newGroup = findViewById(R.id.btn_addgroupGROUPS);
        frame = findViewById(R.id.frameGroups);
        spinner_group = findViewById(R.id.spinner_groups);
        recyclerView = findViewById(R.id.users_recycler);

        // initialize groups and users
        groups = new ArrayList<>();
        currentGroup = null;

        //todo: load user groups in main

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(DAO.Groups)
                .document("User.groups.forEach()")
                .collection(DAO.GROUPS.users)
                .get()

        //todo: load data from db, and:
        if(!groups.isEmpty())
            setupGroupAdapter();
        if(currentGroup != null)
            setupUserAdapter();

        btn_newGroup.setOnClickListener(l -> {
            GroupActionFragment fragment = GroupActionFragment.newInstance("group", "Group name (unique)");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameGroups, fragment);
            ft.commit();
        });

        btn_addUser.setOnClickListener(l -> {
            GroupActionFragment fragment = GroupActionFragment.newInstance("user", "User email (existing)");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameGroups, fragment);
            ft.commit();
        });
    }

    private void setupGroupAdapter() {
        groupAdapter = new GroupAdapter(this, groups);
        spinner_group.setAdapter(groupAdapter);
    }

    private void setupUserAdapter() {
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        userAdapter = new GroupUserAdapter(this, currentGroup.users);
        userAdapter.setClickListener(this);
        recyclerView.setAdapter(userAdapter);
    }


    @Override
    public void notifyGroupAdded(Group group) {
        //todo: add group to the list, update adapter
    }

    @Override
    public void notifyUserFound(GroupUser groupUser) {
        //todo: add user to current group
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "Clicked " + userAdapter.getItem(position).name + " on item position " + position, Toast.LENGTH_SHORT).show();
    }
}