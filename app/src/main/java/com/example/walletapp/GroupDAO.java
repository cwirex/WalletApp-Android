package com.example.walletapp;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupDAO {
    private static GroupDAO instance;
    public final MutableLiveData<ArrayList<Group>> groups = new MutableLiveData<>();
    private final FirebaseFirestore firestore;

    private GroupDAO() {
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
                                        ArrayList<String> usersIds = new ArrayList<>();
                                        if (data.containsKey(DBS.GROUPS.users)) {
                                            HashMap<String, Object> temp = (HashMap<String, Object>) data.get(DBS.GROUPS.users);
                                            for (String key : temp.keySet()) {
                                                usersIds.add(String.valueOf(temp.get(key)));
                                            }
                                        }
                                        arrayList.add(new Group(gid, owner, usersIds, isNewGroup));
                                    } catch (ClassCastException castException) {
                                        Log.e(TAG, "!! DAO: cast exception");
                                    }
                                });
                                groups.setValue(arrayList);
                            }
                        }
                );
    }

    public synchronized static GroupDAO getInstance() {
        if (instance == null) {
            instance = new GroupDAO();
        }
        return instance;
    }

    public ArrayList<Group> getCurrentUserGroupsOwned() {
        String uid = FirebaseAuth.getInstance().getUid();
        ArrayList<Group> result = new ArrayList<>();
        if(this.groups.getValue() != null){
            for (Group g : this.groups.getValue()) {
                if (g.owner.equals(uid))
                    result.add(g);
            }
        }
        return result;
    }
}
