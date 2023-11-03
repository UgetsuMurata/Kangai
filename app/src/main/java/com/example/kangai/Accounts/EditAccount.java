package com.example.kangai.Accounts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kangai.Dashboard.AddDevice;
import com.example.kangai.Dashboard.Dashboard;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.Helpers.ToolbarMenu;
import com.example.kangai.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditAccount extends AppCompatActivity {

    LinearLayout home;
    TextView idChangeProfile, DeleteAccount;
    TextInputLayout usernameField, passwordField,phoneNumberField;
    MaterialButton button,button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_edit);

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        phoneNumberField = findViewById(R.id.phoneNumberField);
        idChangeProfile = findViewById(R.id.idChangeProfile);
        DeleteAccount = findViewById(R.id.DeleteAccount);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        home = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditAccount.this, Dashboard.class));
            }
        });
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        button2.setOnClickListener(view -> EditButton());

    }

    private void ShowUserData(){

        FirebaseData fd = new FirebaseData();


    }


    private void EditButton(){
        String username = Objects.requireNonNull(usernameField.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(passwordField.getEditText()).getText().toString().trim();
        String number = Objects.requireNonNull(phoneNumberField.getEditText()).getText().toString().trim();

        usernameField.setError("");
        passwordField.setError("");
        phoneNumberField.setError("");

        FirebaseData fd = new FirebaseData();
        fd.retrieveData(this, "ExistingUsernames/", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                boolean valid = true;
                String key = null;
                for (DataSnapshot usernames : dataSnapshot.getChildren()) {
                    if (usernames.getValue().equals(username)){
                        valid = false;
                        usernameField.setError("Username Already Exists.");
                    }else{
                        key = usernames.getKey();
                        break;
                    }
                }

                if (valid){
                    fd.updateValue("ExistingUsernames/"+key, username);

                    Map<String, Object> acctCred = new HashMap<>();

                    acctCred.put("Username", username);
                    acctCred.put("Password", password);
                    acctCred.put("Image", "NULL");
                    acctCred.put("Phone", number);

                    fd.updateValues("Users/"+key+"/AcctCredentials/", acctCred);
                    Toast.makeText(EditAccount.this, "Edit Succesfully", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_header, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ToolbarMenu.ToolbarOption(this, item)) return true;
        else return super.onOptionsItemSelected(item);
    }
}