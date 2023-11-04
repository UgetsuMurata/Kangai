package com.example.kangai.Objects;

import android.util.Log;

import androidx.annotation.NonNull;

public class Logs {
    String timestamp, log;

    public Logs(String timestamp, String log) {
        this.timestamp = timestamp;
        this.log = log;
    }
    public Logs(Long timestamp, String log) {
        this.timestamp = String.valueOf(timestamp);
        this.log = log;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLog() {
        return this.log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @NonNull
    @Override
    public String toString() {
        return this.timestamp+" - "+this.log;
    }
}
