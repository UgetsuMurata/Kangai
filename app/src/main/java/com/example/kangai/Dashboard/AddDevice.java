package com.example.kangai.Dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import com.example.kangai.R;
import com.example.kangai.Settings.Settings;
import com.google.firebase.database.DataSnapshot;
import com.google.zxing.Result;

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
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String childKey = childSnapshot.getKey();
                                    if (Objects.equals(childKey, result.toString())) {
                                        exists = true;
                                        break;
                                    }
                                }
                                Handler handler = new Handler();
                                if (exists){
                                    scannerLabel.setText("Valid Scan: Device Added!");
                                    scannerLabel.setTextColor(ThemedColor.getColorStateList(AddDevice.this, R.attr.confirm));

                                    handler.postDelayed(() -> onBackPressed(), 3000);
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