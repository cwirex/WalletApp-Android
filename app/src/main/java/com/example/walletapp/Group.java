package com.example.walletapp;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.walletapp.groups.GroupExpenseDTO;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Map;

public class Group {
    public boolean new_group;
    public String owner;
    private String id;
    public final MutableLiveData<ArrayList<String>> users = new MutableLiveData<>();
    public final MutableLiveData<ArrayList<GroupExpenseDTO>> expenses = new MutableLiveData<>();

    public Group() {}

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
        reference.collection(DBS.GROUPS.Expenses)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen Failed", error);
                    } else if (value != null) {
                        ArrayList<GroupExpenseDTO> arrayList = new ArrayList<>();
                        value.forEach(v -> {
                            try {
                                GroupExpenseDTO grExp = v.toObject(GroupExpenseDTO.class);
                                arrayList.add(grExp);
                            } catch (Exception e) {
                                Log.e(TAG, "!! DAO: cast | read exception");
                            }
                        });
                        expenses.setValue(arrayList);
                    }
                });
    }
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
