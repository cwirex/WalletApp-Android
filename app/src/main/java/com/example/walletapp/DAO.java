package com.example.walletapp;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DAO {
    private static DAO instance;
    public final MutableLiveData<ArrayList<Group>> groups = new MutableLiveData<>();
    public final MutableLiveData<ArrayList<User>> users = new MutableLiveData<>();
    private final FirebaseFirestore firestore;

    private DAO() {
        //todo: listeners for each sub-collection
        firestore = FirebaseFirestore.getInstance();
        firestore.collection(DBS.Groups)
                .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen Failed", error);
                            } else if (value != null) {
                                ArrayList<Group> arrayList = new ArrayList<>();
                                value.forEach(v -> {
                                    try {
                                        String gid = v.getId();
                                        Map<String, Object> data = v.getData();
                                        String owner = String.valueOf(data.getOrDefault(DBS.GROUPS.owner, ""));
                                        boolean isNewGroup = (boolean) data.getOrDefault(DBS.GROUPS.isnew, false);
                                        v.getReference().collection(DBS.GROUPS.users)
                                                .get()
                                                .addOnCompleteListener(task -> {
                                                    ArrayList<String> usersIds = new ArrayList<>();
                                                    if (task.isSuccessful()) {
                                                        for (DocumentSnapshot doc : task.getResult()) {
                                                            Map<String, Object> temp = doc.getData();
                                                            usersIds.add(String.valueOf(temp.get(DBS.GROUPS.USERS.id)));
                                                        }
                                                    }
                                                    arrayList.add(new Group(gid, owner, usersIds, isNewGroup));
                                                });
                                    } catch (ClassCastException castException) {
                                        Log.e(TAG, "!! DAO: cast exception");
                                    }
                                });
                                groups.setValue(arrayList);
                            }
                        }
                );

        firestore.collection(DBS.Users)
                .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen Failed", error);
                            } else if (value != null) {
                                ArrayList<User> arrayList = new ArrayList<>();
                                value.forEach(v -> {
                                    UserDTO tempUser = v.toObject(UserDTO.class);
                                    arrayList.add(new User(v.getId(), tempUser));
                                });
                                users.setValue(arrayList);
                            }
                        }
                );
    }

    public synchronized static DAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }
        return instance;
    }

    public void addGroupToUser(String groupName, String uid) {
        if (uid != null && !groupName.isEmpty()) {
            HashMap<String, Object> entry = new HashMap<>();
            entry.put("groupId", groupName);
            entry.put("joinedAt", LocalDateTime.now());
            FirebaseFirestore.getInstance()
                    .collection(DBS.Users)
                    .document(uid)
                    .collection(DBS.Groups)
                    .document(groupName)
                    .set(entry)
                    .addOnFailureListener(e -> Log.w(TAG, "! Append group to user failed " + e.getMessage()));
        }
    }

    public void addUserToGroup(String uid, String groupName) {
        if (uid != null && !groupName.isEmpty()) {
            HashMap<String, Object> entry = new HashMap<>();
            entry.put("userId", uid);
            entry.put("joinedAt", LocalDateTime.now());
            FirebaseFirestore.getInstance()
                    .collection(DBS.Groups)
                    .document(groupName)
                    .collection(DBS.Users)
                    .document(uid)
                    .set(entry)
                    .addOnFailureListener(e -> Log.w(TAG, "! Append user to group failed " + e.getMessage()));
        }
    }

    public ArrayList<Group> getCurrentUserGroupsOwned() {
        String uid = FirebaseAuth.getInstance().getUid();
        ArrayList<Group> result = new ArrayList<>();
        if (this.groups.getValue() != null) {
            for (Group g : this.groups.getValue()) {
                if (g.owner.equals(uid))
                    result.add(g);
            }
        }
        return result;
    }

    public User getUserFromId(String uid) {
        for (User u : users.getValue()) {
            if (uid.equals(u.uid))
                return u;
        }
        return null;
    }
}
