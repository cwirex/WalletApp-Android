package com.example.walletapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextView vaz, vAZ, v09, vlen, ic_email, ic_match, ic_complexity;    // validation TVs & icons
    LinearLayout complexityList;                                        // Layout with validation TextViews
    TextInputEditText eEmail, ePass, ePassConf;                         // EditText
    TextInputLayout inputPass;
    Button submit;                                                      // register button
    private int c_incorrect, c_correct, c_ic_enabled, c_ic_disabled;    // my colors
    private boolean validEmail, complexPassword, matchesPassword;       // validation booleans
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        linkViews();
        auth = FirebaseAuth.getInstance();

        ePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = ePass.getText().toString();
                String confirm = ePassConf.getText().toString();
                if (password.length() >= 6) {
                    inputPass.setCounterEnabled(false);
                }
                complexPassword = validateComplexity(password);
                matchesPassword = validateMatch(password, confirm);
                submit.setEnabled(validEmail && complexPassword && matchesPassword);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ePass.getText().toString().length() < 6) {
                    inputPass.setCounterEnabled(true);
                }
            }
        });
        ePassConf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = ePass.getText().toString();
                String confirm = ePassConf.getText().toString();
                matchesPassword = validateMatch(password, confirm);
                submit.setEnabled(validEmail && complexPassword && matchesPassword);
            }
        });
        eEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = eEmail.getText().toString();
                validEmail = validateEmail(email);

                submit.setEnabled(validEmail && complexPassword && matchesPassword);
            }
        });
        submit.setOnClickListener(click -> {        // Create account
            String password = ePass.getText().toString();
            String email = eEmail.getText().toString();

            HashMap<String, String> hashMap = new HashMap<>();
            String name = email.substring(0, email.indexOf('@'));
            name = name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
            hashMap.put("email", email);
            hashMap.put("name", name);

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(success -> {
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        firestore.collection("users")
                                .document(auth.getUid())
                                .set(hashMap);

//                        User.UID = auth.getUid();
//                        User.email = hashMap.get("email");
//                        User.name = hashMap.get("name");
                        Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(failure -> {
                        Toast.makeText(getApplicationContext(), failure.getMessage(), Toast.LENGTH_LONG).show();
                    });
//            String password = ePass.getText().toString();
//            SharedPreferences settings = getSharedPreferences("PREFS", 0);
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putString("password", password);
//            editor.apply();

        });
        ePass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                inputPass.setCounterEnabled(focused && ePass.getText().toString().length() < 6);
            }
        });
    }

    private boolean validateEmail(String email) {
        boolean valid = true;
        if (!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            ic_email.setBackgroundTintList(ColorStateList.valueOf(c_ic_disabled));
            valid = false;
        } else
            ic_email.setBackgroundTintList(ColorStateList.valueOf(c_ic_enabled));
        return valid;
    }

    private boolean validateMatch(String s1, String s2) {
        if (!complexPassword)
            return false;
        boolean valid = true;
        if (!s1.equals(s2)) {
            ic_match.setBackgroundTintList(ColorStateList.valueOf(c_ic_disabled));
            valid = false;
        } else
            ic_match.setBackgroundTintList(ColorStateList.valueOf(c_ic_enabled));
        return valid;
    }

    private boolean validateComplexity(String s1) {
        Pattern lowerCase = Pattern.compile("[a-z]");
        Pattern upperCase = Pattern.compile("[A-Z]");
        Pattern digitalCase = Pattern.compile("[0-9]");
        boolean valid = true;
        if (!lowerCase.matcher(s1).find()) {
            vaz.setTextColor(c_incorrect);
            valid = false;
        } else
            vaz.setTextColor(c_correct);
        if (!upperCase.matcher(s1).find()) {
            vAZ.setTextColor(c_incorrect);
            valid = false;
        } else
            vAZ.setTextColor(c_correct);
        if (!digitalCase.matcher(s1).find()) {
            v09.setTextColor(c_incorrect);
            valid = false;
        } else
            v09.setTextColor(c_correct);
        if (s1.length() < 6) {
            vlen.setTextColor(c_incorrect);
            valid = false;
        } else
            vlen.setTextColor(c_correct);

        if (!valid) {     // Change states
            ic_complexity.setBackgroundTintList(ColorStateList.valueOf(c_ic_disabled));
            complexityList.setVisibility(View.VISIBLE);
        } else {
            ic_complexity.setBackgroundTintList(ColorStateList.valueOf(c_ic_enabled));
            complexityList.setVisibility(View.GONE);
        }

        return valid;
    }

    private void linkViews() {
        eEmail = findViewById(R.id.et_emailREGISTER);
        ePass = findViewById(R.id.et_passwordREGISTER);
        ePassConf = findViewById(R.id.et_passwordConfirmREGISTER);
        submit = findViewById(R.id.btn_register);
        ic_email = findViewById(R.id.icon_check_correctEmail);
        ic_match = findViewById(R.id.icon_check_passwordsMatch);
        ic_complexity = findViewById(R.id.icon_check_passwordComplexity);
        vaz = findViewById(R.id.TVaz);
        vAZ = findViewById(R.id.TVAZ);
        v09 = findViewById(R.id.TV09);
        vlen = findViewById(R.id.TVlen);
        complexityList = findViewById(R.id.password_complexity_list);
        inputPass = findViewById(R.id.text_input_password);
        // link colors:
        c_incorrect = getResources().getColor(R.color.incorrect);
        c_correct = getResources().getColor(R.color.correct);
        c_ic_enabled = getResources().getColor(R.color.ic_enabled);
        c_ic_disabled = getResources().getColor(R.color.ic_disabled);
    }
}