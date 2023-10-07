package com.example.kangai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.kangai.Accounts.SignIn;
import com.example.kangai.Accounts.SignUp;
import com.example.kangai.Application.Kangai;
import com.example.kangai.CustomViews.LeafLoadingBar;
import com.example.kangai.Dashboard.Dashboard;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.Helpers.TimeHelper;
import com.example.kangai.Objects.BooleanReference;
import com.example.kangai.Objects.Device;
import com.example.kangai.Objects.Logs;
import com.example.kangai.Objects.Plants;
import com.example.kangai.localStorage.LocalStorageHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    /*
     * This MainActivity will be used for the loading screen.
     * */

    LeafLoadingBar display;
    TextView label;
    FirebaseData fd;
    Kangai kangai;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.loading_display);
        label = findViewById(R.id.loading_label);

        fd = new FirebaseData();

        final String Process1 = "Retrieving Resources";
        final String Process2 = "Processing";
        final String Process3 = "Another Sample Loading";
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
                        Process1();
                        break;
                    case Process2:
                        Process2();
                        break;
                    case Process3:
                        Process3();
                        break;
                }
            }
            if (LocalStorageHelper.isAccountCreated(this)) {
                // User has created an account, redirect to Dashboard
                kangai.setUserID(LocalStorageHelper.getAccount(this));
                startActivity(new Intent(MainActivity.this, Dashboard.class));
            } else {
                // User has not created an account, redirect to Sign Up
                startActivity(new Intent(MainActivity.this, SignIn.class));
            }

            // Finish the current activity to prevent going back to it
            finish();
        }).start();
    }

    private void Process1(){
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
                            Object lastUpdate = dataSnapshot.child("LastUpdate").getValue();

                            DataSnapshot Slot1 = dataSnapshot.child("Plants/Slot1");
                            Plants plant1 = new Plants(1,
                                    Slot1.child("Name").getValue().toString(),
                                    Slot1.child("Status").getValue().toString(),
                                    Slot1.child("Value").getValue().toString(),
                                    Slot1.child("LastWatered").getValue().toString());

                            DataSnapshot Slot2 = dataSnapshot.child("Plants/Slot2");
                            Plants plant2 = new Plants(1,
                                    Slot2.child("Name").getValue().toString(),
                                    Slot2.child("Status").getValue().toString(),
                                    Slot2.child("Value").getValue().toString(),
                                    Slot2.child("LastWatered").getValue().toString());

                            DataSnapshot Slot3 = dataSnapshot.child("Plants/Slot3");
                            Plants plant3 = new Plants(1,
                                    Slot3.child("Name").getValue().toString(),
                                    Slot3.child("Status").getValue().toString(),
                                    Slot3.child("Value").getValue().toString(),
                                    Slot3.child("LastWatered").getValue().toString());

                            DataSnapshot Slot4 = dataSnapshot.child("Plants/Slot4");
                            Plants plant4 = new Plants(1,
                                    Slot4.child("Name").getValue().toString(),
                                    Slot4.child("Status").getValue().toString(),
                                    Slot4.child("Value").getValue().toString(),
                                    Slot4.child("LastWatered").getValue().toString());

                            Device device = new Device(key,
                                    manager != null ? manager.toString() : null,
                                    name != null ? name.toString() : null,
                                    new ArrayList<Plants>(){{add(plant1); add(plant2); add(plant3); add(plant4);}},
                                    Long.getLong(reservoir != null ? reservoir.toString() : "0"),
                                    Long.getLong(lastUpdate != null ? lastUpdate.toString() : "0"));

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
                    kangai.addLogs(new Logs(TimeHelper.millisToReadable(
                            Long.valueOf(timestamp!=null?timestamp:"0")), log));
                }
                lock2.unlock();
            }
        });
        while (System.currentTimeMillis()-timeStarted<1000 && lock.isLocked() && lock2.isLocked());
    }
    private void Process2(){
        long timeStarted = System.currentTimeMillis();
        //PROCESS 2
        while (System.currentTimeMillis()-timeStarted<1000);
    }
    private void Process3(){
        long timeStarted = System.currentTimeMillis();
        //PROCESS 3
        while (System.currentTimeMillis()-timeStarted<1000);
    }
}