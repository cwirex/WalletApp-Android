package com.example.walletapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static final int REQ_ONE_TAP = 333, REQ_SIGN_IN = 222;  // Can be any integer unique to the Activity.
    private final boolean showOneTapUI = true;
    private Button btn_login, btn_register, btn_google;
    private TextInputEditText eEmail, ePass;
    private FirebaseAuth auth;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_gotoRegister);
        btn_google = findViewById(R.id.btn_google);
        eEmail = findViewById(R.id.et_emailLOGIN);
        ePass = findViewById(R.id.et_passwordLOGIN);
        auth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(click -> {
            String login = eEmail.getText().toString();
            String password = ePass.getText().toString();
            if (login.isEmpty() || password.isEmpty()) {
                if (login.isEmpty()) {
                    eEmail.setError("Field can't be empty");
                }
                if (password.isEmpty()) {
                    ePass.setError("Field can't be empty");
                }
            } else {
                auth.signInWithEmailAndPassword(login, password)
                        .addOnSuccessListener(result -> {
//                                Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(result -> Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_LONG).show());
            }
        });

        btn_register.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
//            finish();
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        btn_google.setOnClickListener(v -> {
            signInWithGoogle();
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(t -> {
                            if (t.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String email = user.getEmail();
                                HashMap<String, String> hashMap = new HashMap<>();
                                String name = email.substring(0, email.indexOf('@'));
                                name = name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
                                hashMap.put("email", email);
                                hashMap.put("name", name);

                                FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(auth.getUid())
                                        .get()
                                        .addOnSuccessListener(doc -> {
                                            if (!doc.exists()) {
                                                FirebaseFirestore.getInstance()
                                                        .collection("users")
                                                        .document(auth.getUid())
                                                        .set(hashMap);
                                                Toast.makeText(this, "Signed up!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                                            }
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(failure -> {
                                            Toast.makeText(getApplicationContext(), failure.getMessage(), Toast.LENGTH_LONG).show();
                                        });
                            }

                        });
            }
            if (requestCode == REQ_ONE_TAP) {
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken != null) {
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
//                                            Log.d(TAG, "signInWithCredential:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(LoginActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // If sign in fails, display a message to the user.
//                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                            Toast.makeText(LoginActivity.this, "SignIn with Google failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                } catch (ApiException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}