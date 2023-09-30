package com.example.kangai.Application;

import android.app.Application;

public class Kangai extends Application {

    public static Kangai instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Kangai getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public Boolean isDeviceExisting(String deviceID){
        // Check in database.
        return true;
    }
}
