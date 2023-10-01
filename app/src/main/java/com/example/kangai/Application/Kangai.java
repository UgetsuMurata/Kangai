package com.example.kangai.Application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.kangai.Firebase.FirebaseData;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Kangai extends Application {

    public static Kangai instance;

    private FirebaseData fd;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        fd = new FirebaseData();
    }

    public static Kangai getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public Boolean isDeviceExisting(Context context, String deviceID) throws InterruptedException {
        // Check in database.

        CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean deviceKey = new AtomicBoolean(false);

        fd.retrieveData(context, "Devices/", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String childKey = childSnapshot.getKey();
                    if (childKey != null && childKey.equals(deviceID)) {
                        deviceKey.set(true);
                        break;
                    }
                }
                latch.countDown();
            }
        });

        latch.await();
        return deviceKey.get();
    }
}
