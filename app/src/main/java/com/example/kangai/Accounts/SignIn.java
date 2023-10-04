package com.example.kangai.Accounts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangai.Dashboard.Dashboard;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.MainActivity;
import com.example.kangai.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    TextInputLayout usernameField, passwordField;
    Button signInButton;
    TextView text2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_signin);

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        signInButton = findViewById(R.id.signInButton);
        text2 = findViewById(R.id.text2);

        signInButton.setOnClickListener(view -> SignInButton());
        //text2.setOnClickListener(view -> SignInInstead());

    }

    private void SignInButton(){

        String username = Objects.requireNonNull(usernameField.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(passwordField.getEditText()).getText().toString().trim();

        usernameField.setError("");
        passwordField.setError("");

        if (username.equals("") || password.equals("")){
            Toast.makeText(this, "Input all required fields.", Toast.LENGTH_SHORT).show();
            if (username.equals("")) usernameField.setError("This is required.");
            if (password.equals("")) passwordField.setError("This is required.");
            return;
        }

        FirebaseData fd = new FirebaseData();

        fd.retrieveData(this, "ExistingUsernames/", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                for (DataSnapshot devices : dataSnapshot.getChildren()){
                    String key = devices.getKey();
                    fd.retrieveData(SignIn.this, "Users/" + key + "/AcctCredentials", new FirebaseData.FirebaseDataCallback(){
                        @Override
                        public void onDataReceived(DataSnapshot dataSnapshot) {
                            Object name = dataSnapshot.child("Username").getValue();
                            Object pass = dataSnapshot.child("Password").getValue();

                            if (name.equals(username) && pass.equals(password)){
                                startActivity(new Intent(SignIn.this, Dashboard.class));
                                finish();
                            }else{
                                Toast.makeText(SignIn.this, "Username or Password did not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}