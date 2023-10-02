package com.example.kangai.Application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.Objects.Device;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Kangai extends Application {

    public static Kangai instance;

    private FirebaseData fd;
    private List<Device> devices;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        fd = new FirebaseData();
        devices = new ArrayList<>();
    }

    public static Kangai getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public List<Device> getDevices(){
        return devices;
    }

    public void setDevices(List<Device> devices){
        this.devices = devices;
    }

    public void addDevice(Device device){
        this.devices.add(device);
    }

}
