package com.example.kangai.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kangai.Application.Kangai;
import com.example.kangai.Dashboard.Adapter.DevicesAdapter;
import com.example.kangai.Dashboard.Adapter.LogsAdapter;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.Helpers.ToolbarMenu;
import com.example.kangai.Objects.Device;
import com.example.kangai.Objects.Logs;
import com.example.kangai.R;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    RecyclerView devices, logs;
    CardView noDevices;
    Kangai kangai;
    LinearLayout home;

    Integer ADD_DEVICE = 1;
    TextView username;

    private final Handler handler = new Handler();
    private Runnable updateRecyclerviews;

    private enum deviceAmount{
        hasDevice, noDevice
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        home = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        devices = findViewById(R.id.devices);
        logs = findViewById(R.id.logs);
        noDevices = findViewById(R.id.no_devices);
        username = findViewById(R.id.username);

        kangai = Kangai.getInstance();

        List<Device> deviceList = kangai.getDevices();
        if (deviceList.size()==0) toggleDevices(deviceAmount.noDevice);
        else toggleDevices(deviceAmount.hasDevice);
        setUpUsername();
        noDevices.setOnClickListener(view -> startActivityForResult(new Intent(Dashboard.this, AddDevice.class), ADD_DEVICE));

        updateRecyclerviews = new Runnable() {
            @Override
            public void run() {
                UpdateRecyclerviews();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updateRecyclerviews);
    }

    private void UpdateRecyclerviews() {
        if (kangai.isDevicesHasChanges()) displayDevices();
        if (kangai.isLogsHasChanges()) displayLogs();
    }

    private void setUpUsername() {
        FirebaseData fd = new FirebaseData();
        fd.retrieveData(this, String.format("ExistingUsernames/%s", kangai.getUserID()), new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                Object username_value = dataSnapshot.getValue();
                if (username_value != null) {
                    username.setText(username_value.toString());
                } else {
                    username.setText(R.string.default_username);
                }
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear(); // Clear the existing menu items
        getMenuInflater().inflate(R.menu.menu_header_with_add_button, menu);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.add) {
            // Handle the "Add" button click
            startActivityForResult(new Intent(Dashboard.this, AddDevice.class), ADD_DEVICE);
            return true;
        } else if (ToolbarMenu.ToolbarOption(this, item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void toggleDevices(deviceAmount amount){
        if (amount == deviceAmount.hasDevice){
            devices.setVisibility(View.VISIBLE);
            noDevices.setVisibility(View.GONE);
            setUpRecyclerView();
        } else {
            devices.setVisibility(View.GONE);
            noDevices.setVisibility(View.VISIBLE);
        }
    }

    private void setUpRecyclerView(){
        displayDevices();
        displayLogs();
    }

    private void displayLogs(){
        logs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<Logs> logsList = new ArrayList<>(kangai.getLogs());
        Collections.reverse(logsList);
        logs.setAdapter(new LogsAdapter(new ArrayList<>(logsList)));
    }
    private void displayDevices(){
        devices.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        devices.setAdapter(new DevicesAdapter(this, new ArrayList<>(kangai.getDevices())));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_DEVICE){
            if (resultCode == RESULT_OK){
                displayDevices();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateRecyclerviews);
    }
}