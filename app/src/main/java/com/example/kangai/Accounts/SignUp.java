package com.example.kangai.Accounts;

import static com.example.kangai.Helpers.LocalStorageHelper.setAccountCreatedFlag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangai.Dashboard.Dashboard;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    TextInputLayout usernameField, passwordField, confirmPassField, phoneNumberField;
    Button signUpButton;
    TextView signInInstead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_signup);

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        confirmPassField = findViewById(R.id.confirmPassfield);
        phoneNumberField = findViewById(R.id.phoneNumberField);
        signUpButton = findViewById(R.id.signUpButton);
        signInInstead = findViewById(R.id.sign_in_instead);

        signUpButton.setOnClickListener(view -> SignUpButton());
        signInInstead.setOnClickListener(view -> SignInInstead());
    }

    private void SignUpButton(){
        String username = Objects.requireNonNull(usernameField.getEditText()).getText().toString().trim();
        String password1 = Objects.requireNonNull(passwordField.getEditText()).getText().toString().trim();
        String password2 = Objects.requireNonNull(confirmPassField.getEditText()).getText().toString().trim();
        String number = Objects.requireNonNull(phoneNumberField.getEditText()).getText().toString().trim();

        usernameField.setError("");
        passwordField.setError("");
        confirmPassField.setError("");

        if (username.equals("") || password1.equals("") || password2.equals("")){
            Toast.makeText(this, "Input all required fields.", Toast.LENGTH_SHORT).show();
            if (username.equals("")) usernameField.setError("This is required.");
            if (password1.equals("")) passwordField.setError("This is required.");
            if (password2.equals("")) confirmPassField.setError("This is required.");
            return;
        }
        if (!password1.equals(password2)){
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            confirmPassField.setError("Passwords do not match.");
            return;
        }
        FirebaseData fd = new FirebaseData();
        fd.retrieveData(this, "ExistingUsernames/", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                boolean valid = true;
                for (DataSnapshot usernames : dataSnapshot.getChildren()) {
                    if (usernames.getValue().equals(username)){
                        valid = false;
                        usernameField.setError("Username Already Exists.");
                    }
                }
                if (valid){
                    Long id = System.currentTimeMillis();
                    fd.addValue("ExistingUsernames/"+id, username);

                    setAccountCreatedFlag(SignUp.this,true, String.valueOf(id));

                    Map<String, Object> acctCred = new HashMap<>();
                    acctCred.put("Username", username);
                    acctCred.put("Password", password1);
                    acctCred.put("Image", "NULL");
                    acctCred.put("Phone", number);
                    Map<String, Object> notifications = new HashMap<>();
                    notifications.put("sms", true);
                    notifications.put("pushNotif", true);
                    Map<String, Object> logs = new HashMap<>();
                    logs.put(String.valueOf(System.currentTimeMillis()), "Account Created!");

                    fd.addValues("Users/"+id+"/AcctCredentials/", acctCred);
                    fd.addValues("Users/"+id+"/Settings/Notifications/", notifications);
                    fd.addValues("Users/"+id+"/Settings/Logs", logs);

                    startActivity(new Intent(SignUp.this, Dashboard.class));
                    finish();
                }
            }
        });
    }

    private void SignInInstead(){
        startActivity(new Intent(this, SignIn.class));
        finish();
    }

}