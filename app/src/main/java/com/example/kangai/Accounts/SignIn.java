package com.example.kangai.Accounts;

import static com.example.kangai.Helpers.LocalStorageHelper.setAccountCreatedFlag;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangai.Application.Kangai;
import com.example.kangai.Dashboard.Dashboard;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    TextInputLayout usernameField, passwordField;
    Button signInButton;
    TextView text2;
    TextView forgotPassword;

    Kangai kangai;

    FirebaseData fd = new FirebaseData();
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_signin);

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        signInButton = findViewById(R.id.signInButton);
        text2 = findViewById(R.id.text2);
        forgotPassword = findViewById(R.id.forgotPass);
        kangai = new Kangai();

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDialog();
            }
        });


        signInButton.setOnClickListener(view -> SignInButton());
        text2.setOnClickListener(view -> LogInstead());
    }

    private void showForgotPasswordDialog(){
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");

        // Inflate the dialog's layout
        View view = getLayoutInflater().inflate(R.layout.forgot_password_dialog, null);
        builder.setView(view);

        // Get references to views in the dialog
        TextInputLayout passText = view.findViewById(R.id.passField);
        EditText PasswordText =passText.getEditText();
        Button sendButton = view.findViewById(R.id.sendButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        // Create and show the dialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Configure the "Send" button click
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle sending the password reset email
                String password = PasswordText.getText().toString();
                dialog.dismiss();
            }
        });

        // Configure the "Cancel" button click
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
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


        fd.retrieveData(this, "ExistingUsernames/", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                String key = null;
                String name = null;
                for (DataSnapshot devices : dataSnapshot.getChildren()) {
                    name = devices.getValue().toString();
                    if (username.equals(name)) {
                        key = devices.getKey();
                        break;
                    }
                }
                if (key != null){
                    id = key;
                    setAccountCreatedFlag(SignIn.this,true, id, name);
                    fd.retrieveData(SignIn.this, "Users/" + key + "/AcctCredentials", new FirebaseData.FirebaseDataCallback(){
                        @Override
                        public void onDataReceived(DataSnapshot dataSnapshot) {
                            String pass = dataSnapshot.child("Password").getValue().toString();
                            if (pass.equals(password)){
                                startActivity(new Intent(SignIn.this, Dashboard.class));
//                                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//                                DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("Users/"+kangai.getUserID()+"/");
//                                scoresRef.keepSynced(true);
                                finish();
                            }else{
                                Toast.makeText(SignIn.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignIn.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void LogInstead(){
        startActivity(new Intent(this, SignUp.class));
        finish();
    }
}