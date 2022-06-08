package com.example.walletapp.groups;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletapp.DAO;
import com.example.walletapp.Group;
import com.example.walletapp.R;
import com.example.walletapp.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity implements GroupActionFragment.OnFragmentActionListener, GroupUserAdapter.ItemClickListener {

    private final ArrayList<GroupExpenseDTO> expensesList = new ArrayList<>();
    Button btn_addUser, btn_newGroup, btn_createExpense, btn_summary;
    FrameLayout frame;
    ArrayList<Group> groupsList;
    GroupAdapter groupsAdapter;
    AutoCompleteTextView atv_groupsSpinner;
    ArrayList<GroupUser> usersList;
    GroupUserAdapter usersAdapter;
    RecyclerView usersRecyclerView;
    Observer<ArrayList<String>> groupObserver;
    Observer<ArrayList<GroupExpenseDTO>> expensesObserver;
    private Group currentGroup;
    private GroupExpenseDTOAdapter expensesAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        getSupportActionBar().hide();

        btn_addUser = findViewById(R.id.btn_adduserGROUPS);
        btn_newGroup = findViewById(R.id.btn_addgroupGROUPS);
        btn_createExpense = findViewById(R.id.btn_createGroupExpense);
        btn_summary = findViewById(R.id.grExp_btn_showSummary);
        frame = findViewById(R.id.frameGroups);

        // groups
        atv_groupsSpinner = findViewById(R.id.spinner_groups);
        groupsList = DAO.getInstance().getUserGroups(FirebaseAuth.getInstance().getUid());
        groupsAdapter = new GroupAdapter(this, groupsList);
        atv_groupsSpinner.setAdapter(groupsAdapter);
        groupObserver = update -> updateUsersList();
        expensesObserver = update -> updateExpensesList();

        // users
        usersRecyclerView = findViewById(R.id.users_recycler);
        usersList = new ArrayList<>();
        setupUserAdapter();

        //expenses
        listView = findViewById(R.id.grExp_listView);
        expensesAdapter = new GroupExpenseDTOAdapter(this, expensesList);
        listView.setAdapter(expensesAdapter);

        btn_newGroup.setOnClickListener(l -> {
            GroupActionFragment fragment = GroupActionFragment.newInstance("group", "Group Name (unique)");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameGroups, fragment);
            ft.commit();
        });

        btn_createExpense.setOnClickListener(l -> {
            if (currentGroup != null) {
                String uid = FirebaseAuth.getInstance().getUid();
                String gid = currentGroup.getId();
                CreateGroupExpenseFragment fragment = CreateGroupExpenseFragment.newInstance(uid, gid);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameGroups, fragment);
                ft.commit();
            } else
                Toast.makeText(this, "Select group first!", Toast.LENGTH_SHORT).show();
        });

        btn_addUser.setOnClickListener(l -> {
            if (currentGroup != null) {
                GroupActionFragment fragment = GroupActionFragment.newInstance("user", "User Email (existing)");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameGroups, fragment);
                ft.commit();
            } else
                Toast.makeText(this, "Select group first!", Toast.LENGTH_SHORT).show();

        });

        btn_summary.setOnClickListener(l -> {
            if (currentGroup != null) {
                GroupSummaryFragment fragment = GroupSummaryFragment.newInstance(currentGroup.getId());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameGroups, fragment);
                ft.commit();
            } else
                Toast.makeText(this, "Select group first!", Toast.LENGTH_SHORT).show();
        });

        DAO.getInstance().groups.observe(this, updatedGroups -> {
            groupsList.clear();
            groupsList.addAll(DAO.getInstance().getUserGroups(FirebaseAuth.getInstance().getUid()));
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
        switchCurrentGroup(position);
        // update ui
    }

    private void switchCurrentGroup(int position) {
        if (currentGroup == null) {                               // if current group wasn't set yet
            currentGroup = groupsAdapter.getItem(position);
            currentGroup.users.observe(this, groupObserver);
            currentGroup.expenses.observe(this, expensesObserver);
        } else if (!currentGroup.getId().equals(groupsAdapter.getItem(position).getId())) {  // switch only to another group
            currentGroup.users.removeObserver(groupObserver);
            currentGroup.expenses.removeObserver(expensesObserver);
            currentGroup = groupsAdapter.getItem(position);
            currentGroup.users.observe(this, groupObserver);
            currentGroup.expenses.observe(this, expensesObserver);
        }
    }


    private void setupUserAdapter() {
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        usersRecyclerView.setLayoutManager(horizontalLayoutManager);
        usersAdapter = new GroupUserAdapter(this, usersList);
        usersAdapter.setClickListener(this);
        usersRecyclerView.setAdapter(usersAdapter);
    }

    private void updateExpensesList() {
        expensesList.clear();
        expensesList.addAll(currentGroup.expenses.getValue());
        expensesAdapter.notifyDataSetChanged();
    }

    private void updateUsersList() {
        usersList.clear();
        for (String uid : currentGroup.users.getValue()) {
            User user = DAO.getInstance().getUserFromId(uid);
            if (user != null) {
                usersList.add(new GroupUser(uid, user.userData.name));
            }
        }
        usersAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyGroupAdded(String groupName) {
        String uid = FirebaseAuth.getInstance().getUid();
        DAO dao = DAO.getInstance();
        dao.addGroupToUser(groupName, uid);
        dao.addUserToGroup(uid, groupName);

        atv_groupsSpinner.setText(groupName, false);
        for (int i = 0; i < groupsAdapter.getCount(); i++) {
            if (groupsAdapter.getItem(i).getId().equals(groupName)) {
                switchCurrentGroup(i);
                break;
            }
        }
    }

    @Override
    public void notifyUserFound(GroupUser groupUser) {
        DAO dao = DAO.getInstance();
        dao.addUserToGroup(groupUser.uid, currentGroup.getId());
        dao.addGroupToUser(currentGroup.getId(), groupUser.uid);
    }

    @Override
    public void onUserItemClick(View view, int position) {
        String uid = usersAdapter.getItem(position).uid;
        if (uid != null) {
            UserProfileFragment fragment = UserProfileFragment.newInstance(uid, currentGroup.getId());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameGroups, fragment);
            ft.commit();
        } // remove when group changes?
//        Toast.makeText(this, usersAdapter.getItem(position).name, Toast.LENGTH_SHORT).show();
    }
}