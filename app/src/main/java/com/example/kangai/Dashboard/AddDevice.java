package com.example.kangai.Dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.kangai.Accounts.EditAccount;
import com.example.kangai.Application.Kangai;
import com.example.kangai.Helpers.ThemedColor;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.Helpers.ToolbarMenu;
import com.example.kangai.MainActivity;
import com.example.kangai.Objects.Device;
import com.example.kangai.Objects.Plants;
import com.example.kangai.R;
import com.example.kangai.Settings.Settings;
import com.google.firebase.database.DataSnapshot;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Objects;

public class AddDevice extends AppCompatActivity {

    CodeScannerView scannerView;
    CodeScanner codeScanner;
    TextView scannerLabel;

    LinearLayout home;

    Kangai kangai;

    Integer CAMERA_PERMISSION_REQUEST_CODE = 1;
    Boolean scannerState = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_add_device);

        Toolbar toolbar = findViewById(R.id.toolbar);
        home = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddDevice.this, Dashboard.class));
                finish();
            }
        });
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        scannerView = findViewById(R.id.qr_scanner);
        scannerLabel = findViewById(R.id.qr_results);
        kangai = Kangai.getInstance();

        scannerLabel.setText("Scanning...");
        scannerLabel.setTextColor(ThemedColor.getColorStateList(this, R.attr.text));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else setupScanner();
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

    private void setupScanner(){
        scannerState = true;
        codeScanner = new CodeScanner(this, scannerView);
        scannerView.setOnClickListener(view -> codeScanner.startPreview());
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseData fd = new FirebaseData();
                        fd.retrieveData(AddDevice.this, "Devices/", new FirebaseData.FirebaseDataCallback() {
                            @Override
                            public void onDataReceived(DataSnapshot dataSnapshot) {
                                boolean exists = false;
                                String id = "";
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String childKey = childSnapshot.getKey();
                                    if (Objects.equals(childKey, result.toString())) {
                                        exists = true;
                                        id = childKey;
                                        break;
                                    }
                                }
                                Handler handler = new Handler();
                                if (exists){
                                    for (Device device : kangai.getDevices()) {
                                        if (device.getId().equals(id)){
                                            scannerLabel.setText("Valid Scan: Device already in account!");
                                            scannerLabel.setTextColor(ThemedColor.getColorStateList(AddDevice.this, R.attr.confirm));
                                            handler.postDelayed(() -> onBackPressed(), 3000);
                                        }
                                    }
                                    scannerLabel.setText("Valid Scan: Device Added!");
                                    scannerLabel.setTextColor(ThemedColor.getColorStateList(AddDevice.this, R.attr.confirm));
                                    fd.updateValue("Devices/"+id+"/Manager/", kangai.getUserID());
                                    fd.addValue("Users/"+kangai.getUserID()+"/Devices/"+id+"/", "");

                                    String finalId1 = id;
                                    fd.retrieveData(AddDevice.this, "Users/"+kangai.getUserID()+"/Devices/"+finalId1+"/", new FirebaseData.FirebaseDataCallback() {
                                        @Override
                                        public void onDataReceived(DataSnapshot dataSnapshot) {
                                            Object name = dataSnapshot.child("Name").getValue();
                                            Object manager = dataSnapshot.child("Manager").getValue();
                                            Object reservoir = dataSnapshot.child("Reservoir").getValue();
                                            Object lastUpdate = dataSnapshot.child("LastUpdate").getValue();


                                            DataSnapshot Slot1 = dataSnapshot.child("Plants/Slot1");
                                            Plants plant1 = new Plants(null, null, null, null, null);
                                            if (Slot1.getValue() != null) {
                                                plant1 = new Plants(1,
                                                        Slot1.child("Name").getValue().toString(),
                                                        Slot1.child("Status").getValue().toString(),
                                                        Slot1.child("Value").getValue().toString(),
                                                        Slot1.child("LastWatered").getValue().toString());
                                            }
                                            DataSnapshot Slot2 = dataSnapshot.child("Plants/Slot2");
                                            Plants plant2 = new Plants(null, null, null, null, null);
                                            if (Slot2.getValue() != null) {
                                                plant2 = new Plants(2,
                                                        Slot2.child("Name").getValue().toString(),
                                                        Slot2.child("Status").getValue().toString(),
                                                        Slot2.child("Value").getValue().toString(),
                                                        Slot2.child("LastWatered").getValue().toString());
                                            }
                                            DataSnapshot Slot3 = dataSnapshot.child("Plants/Slot3");
                                            Plants plant3 = new Plants(null, null, null, null, null);
                                            if (Slot3.getValue() != null) {
                                                plant3 = new Plants(3,
                                                        Slot3.child("Name").getValue().toString(),
                                                        Slot3.child("Status").getValue().toString(),
                                                        Slot3.child("Value").getValue().toString(),
                                                        Slot3.child("LastWatered").getValue().toString());
                                            }
                                            DataSnapshot Slot4 = dataSnapshot.child("Plants/Slot4");
                                            Plants plant4 = new Plants(null, null, null, null, null);
                                            if (Slot4.getValue() != null) {
                                                plant4 = new Plants(4,
                                                        Slot4.child("Name").getValue().toString(),
                                                        Slot4.child("Status").getValue().toString(),
                                                        Slot4.child("Value").getValue().toString(),
                                                        Slot4.child("LastWatered").getValue().toString());
                                            }
                                            Plants finalPlant1 = plant1;
                                            Plants finalPlant2 = plant2;
                                            Plants finalPlant3 = plant3;
                                            Plants finalPlant4 = plant4;
                                            Device device = new Device(finalId1,
                                                    manager != null ? manager.toString() : null,
                                                    name != null ? name.toString() : null,
                                                    new ArrayList<Plants>(){{add(finalPlant1); add(finalPlant2); add(finalPlant3); add(finalPlant4);}},
                                                    Long.valueOf(reservoir != null ? reservoir.toString() : "0"),
                                                    Long.valueOf(lastUpdate != null ? lastUpdate.toString() : "0"));
                                            kangai.addDevice(device);
                                            kangai.setNewDevice(device);
                                        }
                                    });
                                    handler.postDelayed(() -> {
                                        Intent returnIntent = new Intent(AddDevice.this, Dashboard.class);
                                        setResult(Activity.RESULT_OK, returnIntent);
                                        finish();
                                    }, 3000);
                                } else {
                                    scannerLabel.setText("Invalid Scan: Device Not Found");
                                    scannerLabel.setTextColor(ThemedColor.getColorStateList(AddDevice.this, R.attr.error));
                                    handler.postDelayed(() -> {
                                        codeScanner.startPreview();
                                        scannerLabel.setText("Try again.");
                                    }, 3000);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE & resultCode == RESULT_OK){
            setupScanner();
        } else {
            Toast.makeText(this, "Camera permission is required to access this feature.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (scannerState) codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if (scannerState) codeScanner.releaseResources();
        super.onPause();
    }
}