package com.example.walletapp;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserDAO {
    private static UserDAO instance;
    private final FirebaseFirestore firestore;
    private final MutableLiveData<ArrayList<UserDTO>> usersData = new MutableLiveData<>();
    private final MutableLiveData<UserDTO> currentUser = new MutableLiveData<>();

    private UserDAO() {
        firestore = FirebaseFirestore.getInstance();
        listenFirestoreUsers();
        firestore.collection("users")
                .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen Failed", error);
                            } else if (value != null) {
                                ArrayList<UserDTO> lUsers = new ArrayList<>();
                                value.forEach(v -> {
                                    UserDTO tempUser = v.toObject(UserDTO.class);
                                    lUsers.add(tempUser);
                                    if(FirebaseAuth.getInstance().getCurrentUser() != null)
                                        if (v.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                            currentUser.setValue(tempUser);
                                        }
                                });
                                usersData.setValue(lUsers);
                            }
                        }
                );
    }

    public synchronized static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    private void listenFirestoreUsers() {

    }

    public MutableLiveData<UserDTO> getCurrentUser() {
        return currentUser;
    }

    public MutableLiveData<ArrayList<UserDTO>> getAllUsers() {
        return usersData;
    }
}
