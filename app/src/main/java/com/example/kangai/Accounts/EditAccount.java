package com.example.kangai.Accounts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kangai.Application.Kangai;
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
    TextView DeleteAccount;
    TextInputLayout usernameField,phoneNumberField;
    MaterialButton button,button2;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        home = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home.setOnClickListener(view -> startActivity(new Intent(EditAccount.this, Dashboard.class)));
        toolbar.setTitle("");
        toolbar.setSubtitle("");


        usernameField = findViewById(R.id.usernameField);
        phoneNumberField = findViewById(R.id.phoneNumberField);
        DeleteAccount = findViewById(R.id.DeleteAccount);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);

        key = Kangai.getInstance().getUserID();
        ShowUserData();
        setUpPhoneNumber();
        button2.setOnClickListener(view -> EditButton());

    }

    private void setUpPhoneNumber() {
        phoneNumberField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().contains("+63")) return;
                phoneNumberField.getEditText().setText(
                        charSequence.toString().replace("+63", ""));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void ShowUserData(){
        FirebaseData fd = new FirebaseData();
        fd.retrieveData(this, "Users/" + key + "/AcctCredentials", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("Username").getValue().toString();
                String phone = dataSnapshot.child("Phone").getValue().toString();

                usernameField.setPlaceholderText(username);
                phoneNumberField.setPlaceholderText(phone.replace("+63", ""));
            }
        });
    }

    private void EditButton(){
        String username = Objects.requireNonNull(usernameField.getEditText()).getText().toString().trim();
        String number = Objects.requireNonNull(phoneNumberField.getEditText()).getText().toString().trim();

        usernameField.setError("");
        phoneNumberField.setError("");

        FirebaseData fd = new FirebaseData();
        fd.retrieveData(this, "ExistingUsernames/", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                for (DataSnapshot usernames : dataSnapshot.getChildren()) {
                    if (usernames.getValue().equals(username)){
                        usernameField.setError("Username Already Exists.");
                        return;
                    }
                }
                fd.updateValue("ExistingUsernames/"+key, username);

                Map<String, Object> acctCred = new HashMap<>();

                acctCred.put("Username", username);
                acctCred.put("Phone", number);

                fd.updateValues("Users/"+key+"/AcctCredentials/", acctCred);
                Toast.makeText(EditAccount.this, "Edited Successfully!", Toast.LENGTH_SHORT).show();
                finish();
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