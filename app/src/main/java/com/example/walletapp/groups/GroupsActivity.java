package com.example.walletapp.groups;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletapp.Group;
import com.example.walletapp.GroupDAO;
import com.example.walletapp.MainActivity;
import com.example.walletapp.R;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity implements GroupActionFragment.OnFragmentActionListener, GroupUserAdapter.ItemClickListener {

    Button btn_addUser, btn_newGroup;
    FrameLayout frame;
    GroupAdapter groupAdapter;
    GroupUserAdapter userAdapter;
    AutoCompleteTextView spinner_group;
    ArrayList<Group> groups;
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

        groups = GroupDAO.getInstance().getCurrentUserGroupsOwned();
        groupAdapter = new GroupAdapter(this, groups);
        spinner_group.setAdapter(groupAdapter);

        recyclerView = findViewById(R.id.users_recycler);
        setupUserAdapter();

        btn_newGroup.setOnClickListener(l -> {
            GroupActionFragment fragment = GroupActionFragment.newInstance("group", "Group name (unique)");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameGroups, fragment);
            ft.commit();
        });

        btn_addUser.setOnClickListener(l -> {
            GroupActionFragment fragment = GroupActionFragment.newInstance("user", "UserData email (existing)");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameGroups, fragment);
            ft.commit();
        });

        GroupDAO.getInstance().groups.observe(this, updatedGroups -> {
            groups.clear();
            groups.addAll(GroupDAO.getInstance().getCurrentUserGroupsOwned());
            groupAdapter.notifyDataSetChanged();
        });

        spinner_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                groupSelected(item, position);
            }
        });
    }

    private void groupSelected(String gid, int position) {
        // todo - update ui
        Toast.makeText(GroupsActivity.this, gid, Toast.LENGTH_SHORT).show();
    }


    private void setupUserAdapter() {
//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(horizontalLayoutManager);
//        userAdapter = new GroupUserAdapter(this, currentGroup.users);
//        userAdapter.setClickListener(this);
//        recyclerView.setAdapter(userAdapter);
    }


    @Override
    public void notifyGroupAdded(String groupName) {
        // set it as current group?
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