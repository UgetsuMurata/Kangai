package com.example.kangai.Accounts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.kangai.Dashboard.AddDevice;
import com.example.kangai.Dashboard.Dashboard;
import com.example.kangai.Helpers.ToolbarMenu;
import com.example.kangai.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

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