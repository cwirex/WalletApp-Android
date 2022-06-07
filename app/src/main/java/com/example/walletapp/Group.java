package com.example.walletapp;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group {
    public boolean new_group;
    public String owner;
    private String id;
    public MutableLiveData<ArrayList<String>> users = new MutableLiveData<>();

    public Group() {

    }

    public Group(String id, String owner, boolean new_group, DocumentReference reference) {
        this.new_group = new_group;
        this.owner = owner;
        this.id = id;
        reference.collection(DBS.GROUPS.users)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen Failed", error);
                    } else if (value != null) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        value.forEach(v -> {
                            try {
                                Map<String, Object> data = v.getData();
                                String strId = String.valueOf(data.getOrDefault(DBS.GROUPS.userid, ""));
                                if (!strId.isEmpty())
                                    arrayList.add(strId);
                            } catch (Exception e) {
                                Log.e(TAG, "!! DAO: cast | read exception");
                            }
                        });
                        users.setValue(arrayList);
                    }
                });
    }

    public Group(String id, String owner, boolean new_group, ArrayList<String> users) {
        this.id = id;
        this.new_group = new_group;
        this.owner = owner;
        this.users.setValue(users);
    }
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
