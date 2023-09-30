package com.example.kangai.Application;

import android.app.Application;

public class Kangai extends Application {

    Kangai instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}
