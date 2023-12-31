package com.example.kangai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kangai.Accounts.SignIn;
import com.example.kangai.Application.Kangai;
import com.example.kangai.CustomViews.LeafLoadingBar;
import com.example.kangai.DEVELOPER.FIREBASE_;
import com.example.kangai.Dashboard.Dashboard;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.Helpers.LocalStorageHelper;
import com.example.kangai.Helpers.TimeHelper;
import com.example.kangai.Objects.BooleanReference;
import com.example.kangai.Objects.Device;
import com.example.kangai.Objects.Logs;
import com.example.kangai.Objects.Plants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    /*
     * This MainActivity will be used for the loading screen.
     * */

    LeafLoadingBar display;
    TextView label;
    FirebaseData fd;
    Kangai kangai;
    Boolean signedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.loading_display);
        label = findViewById(R.id.loading_label);

        fd = new FirebaseData();

        final String Process1 = "Setting up Account";
        final String Process2 = "Retrieving Resources";
        final String Process3 = "Connecting to Firebase";
        kangai = Kangai.getInstance();

        ArrayList<String> loadingProcesses = new ArrayList<>(Arrays.asList(
                Process1,
                Process2,
                Process3
        ));
        display.setMax(loadingProcesses.size());
        new Thread(() -> {
            AtomicInteger process_number = new AtomicInteger(0);
            for (String process : loadingProcesses) {
                process_number.getAndIncrement();
                runOnUiThread(() -> {
                    label.setText(String.format("%s... %.0f%%", process, ((float) process_number.get() / loadingProcesses.size())*100));
                    display.setProgress(process_number.get());
                });
                switch (process){
                    case Process1:
                        Process1(((float) process_number.get() / loadingProcesses.size())*100);
                        break;
                    case Process2:
                        Process2();
                        break;
                    case Process3:
                        if (signedIn) Process3();
                        break;
                }
            }
            if (LocalStorageHelper.isAccountCreated(this)) {
                // User has created an account, redirect to Dashboard
                ArrayList<String> accountValues = LocalStorageHelper.getAccount(this);
                kangai.setUserID(accountValues.get(0));
                kangai.setUsername(accountValues.get(1));
                startActivity(new Intent(MainActivity.this, Dashboard.class));
            } else {
                // User has not created an account, redirect to Sign Up
                startActivity(new Intent(MainActivity.this, SignIn.class));
            }

            // Finish the current activity to prevent going back to it
            finish();
        }).start();
    }

    private void Process1(Float value){
        long timeStarted = System.currentTimeMillis();
        signedIn = LocalStorageHelper.isAccountCreated(this);
        while (System.currentTimeMillis()-timeStarted<500);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (signedIn) {
                    ArrayList<String> accountValues = LocalStorageHelper.getAccount(MainActivity.this);
                    kangai.setUserID(accountValues.get(0));
                    kangai.setUsername(accountValues.get(1));
                    label.setText(String.format("%s... %.0f%%", "Signed in", value));
                } else label.setText(String.format("%s... %.0f%%", "No accounts found", value));
            }
        });
        while (System.currentTimeMillis()-timeStarted<1000);
    }
    private void Process2(){
        long timeStarted = System.currentTimeMillis();
        //GET ACCT
        String usernameID = kangai.userID;
        BooleanReference lock = new BooleanReference(false);
        BooleanReference lock2 = new BooleanReference(false);
        //GET DEVICES
        lock.lock();
        fd.retrieveData(this, "Users/"+usernameID+"/Devices", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                for (DataSnapshot devices : dataSnapshot.getChildren()) {
                    String key = devices.getKey();
                    fd.retrieveData(MainActivity.this, "Devices/" + key + "/", new FirebaseData.FirebaseDataCallback() {
                        @Override
                        public void onDataReceived(DataSnapshot dataSnapshot) {
                            Object name = dataSnapshot.child("Name").getValue();
                            Object manager = dataSnapshot.child("Manager").getValue();
                            Object reservoir = dataSnapshot.child("Reservoir").getValue();

                            DataSnapshot Slot1 = dataSnapshot.child("Plants/Slot1");
                            Plants plant1 = new Plants(null, null, null);
                            if (Slot1.getValue() != null) {
                                try {
                                    plant1 = new Plants(1,
                                            Slot1.child("Name").getValue().toString(),
                                            Slot1.child("Status").getValue().toString());
                                } catch (NullPointerException ignore) {
                                    fd.removeData(String.format("Devices/%s/Plants/Slot1", key));
                                }
                            }
                            DataSnapshot Slot2 = dataSnapshot.child("Plants/Slot2");
                            Plants plant2 = new Plants(null, null, null);
                            if (Slot2.getValue() != null) {
                                try {
                                    plant2 = new Plants(2,
                                            Slot2.child("Name").getValue().toString(),
                                            Slot2.child("Status").getValue().toString());
                                } catch (NullPointerException ignore){
                                    fd.removeData(String.format("Devices/%s/Plants/Slot2", key));
                                }

                            }
                            Plants finalPlant1 = plant1;
                            Plants finalPlant2 = plant2;
                            Device device = new Device(key,
                                    manager != null ? manager.toString() : null,
                                    name != null ? name.toString() : null,
                                    new ArrayList<Plants>(){{add(finalPlant1); add(finalPlant2);}},
                                    Long.valueOf(reservoir != null ? reservoir.toString() : "0"));
                            kangai.addDevice(device);
                        }
                    });
                }
                lock.unlock();
            }
        });
        lock2.lock();
        fd.retrieveData(this, "Users/"+usernameID+"/Settings/Logs/", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                for (DataSnapshot logDS : dataSnapshot.getChildren()) {
                    String timestamp = logDS.getKey();
                    String log = String.valueOf(logDS.getValue()!=null?logDS.getValue():"");
                    kangai.addLogs(new Logs(Long.valueOf(timestamp!=null?timestamp:"0"), log));
                }
                lock2.unlock();
            }
        });
        while (System.currentTimeMillis()-timeStarted<1000 && lock.isLocked() && lock2.isLocked());
    }
    private void Process3(){
        long timeStarted = System.currentTimeMillis();
        while (System.currentTimeMillis()-timeStarted<1000);
    }
}