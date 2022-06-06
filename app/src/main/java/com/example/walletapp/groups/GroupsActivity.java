package com.example.walletapp.groups;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletapp.Group;
import com.example.walletapp.DAO;
import com.example.walletapp.R;
import com.example.walletapp.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity implements GroupActionFragment.OnFragmentActionListener, GroupUserAdapter.ItemClickListener {

    Button btn_addUser, btn_newGroup;
    FrameLayout frame;

    ArrayList<Group> groupsList;
    GroupAdapter groupsAdapter;
    AutoCompleteTextView atv_groupsSpinner;
    private Group currentGroup;

    ArrayList<GroupUser> usersList;
    GroupUserAdapter usersAdapter;
    RecyclerView usersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        btn_addUser = findViewById(R.id.btn_adduserGROUPS);
        btn_newGroup = findViewById(R.id.btn_addgroupGROUPS);
        frame = findViewById(R.id.frameGroups);

        // groups
        atv_groupsSpinner = findViewById(R.id.spinner_groups);
        groupsList = DAO.getInstance().getCurrentUserGroupsOwned();
        groupsAdapter = new GroupAdapter(this, groupsList);
        atv_groupsSpinner.setAdapter(groupsAdapter);

        // users
        usersRecyclerView = findViewById(R.id.users_recycler);
        usersList = new ArrayList<>();
        setupUserAdapter();

        btn_newGroup.setOnClickListener(l -> {
            GroupActionFragment fragment = GroupActionFragment.newInstance("group", "Group Name (unique)");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameGroups, fragment);
            ft.commit();
        });

        btn_addUser.setOnClickListener(l -> {
            GroupActionFragment fragment = GroupActionFragment.newInstance("user", "User Email (existing)");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameGroups, fragment);
            ft.commit();
        });

        DAO.getInstance().groups.observe(this, updatedGroups -> {
            groupsList.clear();
            groupsList.addAll(DAO.getInstance().getCurrentUserGroupsOwned());
            groupsAdapter.notifyDataSetChanged();
        });

        atv_groupsSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                groupSelected(item, position);
            }
        });
    }

    private void groupSelected(String gid, int position) {
        Toast.makeText(GroupsActivity.this, gid, Toast.LENGTH_SHORT).show();
        currentGroup = groupsAdapter.getItem(position);
        updateUsersList();
        // todo - update ui
    }


    private void setupUserAdapter() {
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        usersRecyclerView.setLayoutManager(horizontalLayoutManager);
        usersAdapter = new GroupUserAdapter(this, usersList);
        usersAdapter.setClickListener(this);
        usersRecyclerView.setAdapter(usersAdapter);
    }


    @Override
    public void notifyGroupAdded(String groupName) {
        String uid = FirebaseAuth.getInstance().getUid();
        DAO dao = DAO.getInstance();
        dao.addGroupToUser(groupName, uid);
        dao.addUserToGroup(uid, groupName);

        atv_groupsSpinner.setText(groupName, false);
        for(int i = 0; i < groupsAdapter.getCount(); i++){
            if(groupsAdapter.getItem(i).getId().equals(groupName)){
                currentGroup = groupsAdapter.getItem(i);
                updateUsersList();
                break;
            }
        }
    }

    private void updateUsersList() {
        usersList.clear();
        for(String uid : currentGroup.users){
            User user = DAO.getInstance().getUserFromId(uid);
            if(user != null){
                usersList.add(new GroupUser(uid, user.userData.name));
            }
        }
        usersAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyUserFound(GroupUser groupUser) {
        DAO.getInstance().addUserToGroup(groupUser.uid, currentGroup.getId());
        usersList.add(0, groupUser);
        usersAdapter.notifyItemInserted(0);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, usersAdapter.getItem(position).name, Toast.LENGTH_SHORT).show();
    }
}