package com.example.kangai.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.Menu;

import com.example.kangai.Application.Kangai;
import com.example.kangai.Dashboard.Adapter.DevicesAdapter;
import com.example.kangai.Objects.Device;
import com.example.kangai.R;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    TextView reservoir;
    RecyclerView devices;
    CardView noDevices;
    Kangai kangai;

    private enum deviceAmount{
        hasDevice, noDevice
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        reservoir = findViewById(R.id.reservoir);
        devices = findViewById(R.id.devices);
        noDevices = findViewById(R.id.no_devices);

        kangai = Kangai.getInstance();
        List<Device> deviceList = kangai.getDevices();
        if (deviceList.size()==0) toggleDevices(deviceAmount.noDevice);
        else toggleDevices(deviceAmount.hasDevice);
        noDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, AddDevice.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_header, menu);
        return true;
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
        devices.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        devices.setAdapter(new DevicesAdapter(new ArrayList<>(kangai.getDevices())));
    }

}